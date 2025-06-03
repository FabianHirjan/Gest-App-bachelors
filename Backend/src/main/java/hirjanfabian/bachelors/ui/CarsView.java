package hirjanfabian.bachelors.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import hirjanfabian.bachelors.dto.*;
import hirjanfabian.bachelors.ui.base.BaseProtectedView;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * View de administrare a mașinilor cu caching pentru prețuri estimate și selecție de mărci/modele.
 */
@Route("cars")
public class CarsView extends BaseProtectedView {

    private static final Logger logger = LoggerFactory.getLogger(CarsView.class);

    /*──────────── câmpuri UI + date ────────────*/
    private final Grid<CarDTO> grid = new Grid<>(CarDTO.class, false);
    private final HorizontalLayout statsLayout = new HorizontalLayout();
    private List<CarDTO> cars = Collections.emptyList();
    // Cache în memorie pentru prețurile estimate (ID mașină -> preț EUR)
    private final Map<Long, Double> priceCache = new ConcurrentHashMap<>();
    private RestTemplate restTemplate = new RestTemplate();

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

    /*──────────── constructor ────────────*/
    public CarsView() {
        super("/api/cars");
        this.restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(HttpClients.createDefault()));
        configureGrid();

        Button addCar = new Button("Adaugă mașină", e -> openAddDialog());
        addCar.addClassName("add-car");

        Button refreshPrices = new Button("Reîmprospătează prețurile", e -> refreshAllPrices());
        refreshPrices.addClassName("refresh-prices");

        statsLayout.setWidthFull();
        statsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        HorizontalLayout buttonLayout = new HorizontalLayout(addCar, refreshPrices);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        setMainContent(buttonLayout, grid, statsLayout);

        loadCars();
    }

    /*────────────────────────────────────────────
     * Grid
     *───────────────────────────────────────────*/
    private void configureGrid() {
        grid.addClassName("cars-grid");
        grid.setHeight("400px");

        grid.addColumn(CarDTO::getLicensePlate).setHeader("Nr.");
        grid.addColumn(CarDTO::getYear).setHeader("An");
        grid.addColumn(CarDTO::getMileage).setHeader("Km");
        grid.addColumn(dto -> Optional.ofNullable(priceCache.get(dto.getId()))
                .map(p -> String.format("%.2f", p))
                .orElse("N/A")).setHeader("Preț estimat (EUR)");
        grid.addColumn(dto -> dto.getCarMake().getMake()).setHeader("Marcă");
        grid.addColumn(dto -> dto.getCarModel().getModel()).setHeader("Model");
        grid.addColumn(dto -> Optional.ofNullable(dto.getDriver())
                        .map(UserDTO::getUsername).orElse(""))
                .setHeader("Șofer");

        grid.addComponentColumn(dto -> {
            Button btn = new Button("Mentenanță");
            styleMaintBtn(dto, btn);
            btn.addClickListener(e -> openMaintenanceDialog(dto));
            return btn;
        }).setHeader("Mentenanță");

        grid.addComponentColumn(this::actionLayout)
                .setHeader("Acțiuni").setWidth("260px");
    }

    private HorizontalLayout actionLayout(CarDTO dto) {
        Button edit = new Button("Edit", e -> openEditDialog(dto));
        Button assign = dto.getDriver() == null
                ? new Button("Assign", e -> openAssignDialog(dto))
                : new Button("Unassign", e -> unassignCar(dto));
        Button del = new Button("Delete", e -> deleteCar(dto));

        assign.addClassNames("action-button",
                dto.getDriver() == null ? "assign-button" : "unassign-button");
        edit.addClassName("action-button");
        del.addClassName("action-button");

        HorizontalLayout hl = new HorizontalLayout(edit, assign, del);
        hl.addClassName("actions-layout");
        return hl;
    }

    private void styleMaintBtn(CarDTO dto, Button b) {
        b.removeClassNames("success", "warning", "error");
        if (dto.isAnyOverdue()) {
            b.addClassName("error");
        } else if (dto.getInsuranceDueSoon() || dto.getInspectionDueSoon()
                || dto.getOilDueSoon() || dto.getTireDueSoon()) {
            b.addClassName("warning");
        } else {
            b.addClassName("success");
        }
    }

    /*────────────────────────────────────────────
     * Dialoguri
     *───────────────────────────────────────────*/
    private void openMaintenanceDialog(CarDTO d) {
        Dialog dlg = new Dialog();
        dlg.setHeaderTitle("Mentenanță – " + d.getLicensePlate());

        VerticalLayout list = new VerticalLayout();
        list.setSpacing(true);

        list.add(makeBar("Asigurare",
                parse(d.getInsuranceExpiration()), 30));
        list.add(makeBar("ITP",
                addYears(parse(d.getLastInspection()), 1), 30));
        list.add(makeBar("Schimb ulei",
                addMonths(parse(d.getLastOilChange()), 6), 30));
        list.add(makeBar("Anvelope",
                addYears(parse(d.getLastTireChange()), 1), 30));

        list.add(new Span("Preț estimat: " +
                Optional.ofNullable(priceCache.get(d.getId()))
                        .map(p -> String.format("%.2f EUR", p))
                        .orElse("N/A")));

        dlg.add(list);
        dlg.open();
    }

    private Component makeBar(String label, LocalDateTime due, int warnDays) {
        HorizontalLayout row = new HorizontalLayout();
        row.setAlignItems(FlexComponent.Alignment.CENTER);

        Span l = new Span(label + ": ");
        Span daysSpan = new Span("N/A");

        Div bar = new Div();
        bar.getStyle().set("height", "12px")
                .set("border-radius", "4px");

        if (due != null) {
            long days = Duration.between(LocalDateTime.now(), due).toDays();
            daysSpan.setText(days + " zile");

            int width = (int) Math.min(Math.max(days, 0), 60) * 2; // max 120px
            bar.getStyle().set("width", width + "px");

            String col = (days < 0) ? "#dc3545"  // roșu
                    : (days <= warnDays) ? "#ffc107"  // galben
                    : "#28a745"; // verde
            bar.getStyle().set("background", col);
        } else {
            bar.getStyle().set("background", "#6c757d") // gri
                    .set("width", "60px");
        }

        row.add(l, bar, daysSpan);
        return row;
    }

    /* edit */
    private void openEditDialog(CarDTO d) {
        Dialog dlg = new Dialog();
        dlg.setHeaderTitle("Edit – " + d.getLicensePlate());

        TextField plate = new TextField("Nr. înmatriculare", d.getLicensePlate());
        Button ok = new Button("Save",
                e -> {
                    updateCar(d.getId(), plate.getValue());
                    dlg.close();
                });

        dlg.add(new VerticalLayout(plate, ok));
        dlg.open();
    }

    /* assign */
    private void openAssignDialog(CarDTO d) {
        Dialog dlg = new Dialog();
        dlg.setHeaderTitle("Assign – " + d.getLicensePlate());

        ComboBox<UserDTO> users = new ComboBox<>("Șofer");
        users.setItemLabelGenerator(UserDTO::getUsername);
        users.setItems(loadUsers());

        Button ok = new Button("OK", e -> {
            if (users.isEmpty()) Notification.show("Selectează șofer");
            else {
                assignCar(d, users.getValue());
                dlg.close();
            }
        });

        dlg.add(new VerticalLayout(users, ok));
        dlg.open();
    }

    /* add */
    private void openAddDialog() {
        Dialog dlg = new Dialog();
        dlg.setHeaderTitle("Add car");

        TextField plate = new TextField("Număr");
        NumberField year = new NumberField("An fabricație");
        ComboBox<CarMakeDTO> make = new ComboBox<>("Marcă");
        ComboBox<CarModelDTO> model = new ComboBox<>("Model");
        NumberField mileage = new NumberField("Kilometri");

        // Configurează ComboBox pentru mărci
        make.setItems(loadCarMakes());
        make.setItemLabelGenerator(CarMakeDTO::getMake);
        make.setPlaceholder("Selectează marca");

        // Configurează ComboBox pentru modele
        model.setItemLabelGenerator(CarModelDTO::getModel);
        model.setPlaceholder("Selectează modelul");
        model.setEnabled(false); // Dezactivat până la selectarea mărcii

        // Actualizează modelele când se schimbă marca
        make.addValueChangeListener(event -> {
            if (event.getValue() == null) {
                model.setEnabled(false);
                model.clear();
            } else {
                model.setEnabled(true);
                List<CarModelDTO> models = loadCarModels(event.getValue().getId());
                model.setItems(models);
            }
        });

        Button ok = new Button("Add", e -> {
            if (plate.isEmpty() || year.isEmpty() || make.isEmpty() || model.isEmpty() || mileage.isEmpty()) {
                Notification.show("Completați toate câmpurile obligatorii!");
                return;
            }
            CarDTO c = new CarDTO();
            c.setLicensePlate(plate.getValue());
            c.setYear(year.getValue().intValue());
            c.setMileage(mileage.getValue().intValue());
            c.setCarMake(make.getValue());
            c.setCarModel(model.getValue());
            addCar(c);
            dlg.close();
        });

        VerticalLayout layout = new VerticalLayout(plate, year, make, model, mileage, ok);
        layout.setSpacing(true);
        dlg.add(layout);
        dlg.open();
    }

    /*────────────────────────────────────────────
     * Helper date
     *───────────────────────────────────────────*/
    private LocalDateTime parse(String s) {
        try {
            return s != null ? LocalDateTime.parse(s, DATE_FMT) : null;
        } catch (Exception e) {
            return null;
        }
    }

    private LocalDateTime addMonths(LocalDateTime d, int m) {
        return d != null ? d.plusMonths(m) : null;
    }

    private LocalDateTime addYears(LocalDateTime d, int y) {
        return d != null ? d.plusYears(y) : null;
    }

    /*────────────────────────────────────────────
     * HTTP helpers (REST calls)
     *───────────────────────────────────────────*/
    private <T> ResponseEntity<T> req(String path, HttpMethod method, Object body, Class<T> respType) {
        HttpHeaders h = new HttpHeaders();
        h.setBearerAuth(jwt());
        h.setContentType(MediaType.APPLICATION_JSON);
        logger.info("REST request: {} {}", method, path);
        try {
            return restTemplate.exchange("http://localhost:8080" + path, method, new HttpEntity<>(body, h), respType);
        } catch (Exception e) {
            logger.error("REST error for {} {}: {}", method, path, e.getMessage(), e);
            throw e;
        }
    }

    /*────────────────────────────────────────────
     * CRUD actions
     *───────────────────────────────────────────*/
    private void loadCars() {
        try {
            ResponseEntity<CarDTO[]> r = req("/api/cars", HttpMethod.GET, null, CarDTO[].class);
            if (r.getStatusCode().is2xxSuccessful() && r.getBody() != null) {
                cars = Arrays.asList(r.getBody());
                grid.setItems(cars);
                updateStats();
                loadPricesForCars(cars);
            }
        } catch (Exception ex) {
            Notification.show("Eroare la încărcarea mașinilor: " + ex.getMessage());
            logger.error("Error loading cars: {}", ex.getMessage(), ex);
        }
    }

    private void addCar(CarDTO c) {
        try {
            ResponseEntity<CarDTO> response = req("/api/cars", HttpMethod.POST, c, CarDTO.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                loadCars();
                estimatePriceForCar(response.getBody());
            }
        } catch (Exception ex) {
            Notification.show("Eroare la adăugarea mașinii: " + ex.getMessage());
            logger.error("Error adding car: {}", ex.getMessage(), ex);
        }
    }

    private void deleteCar(CarDTO c) {
        req("/api/cars/" + c.getId(), HttpMethod.DELETE, null, Void.class);
        priceCache.remove(c.getId());
        loadCars();
    }

    private void updateCar(Long id, String plate) {
        CarDTO d = new CarDTO();
        d.setLicensePlate(plate);
        req("/api/cars/" + id, HttpMethod.PATCH, d, Void.class);
        loadCars();
    }

    private void assignCar(CarDTO car, UserDTO u) {
        req("/api/users/assignCar", HttpMethod.POST,
                Map.of("carId", car.getId(), "userId", u.getId()),
                String.class);
        loadCars();
    }

    private void unassignCar(CarDTO car) {
        req("/api/users/unassignCar", HttpMethod.POST,
                Map.of("id", car.getId()), String.class);
        loadCars();
    }

    private List<UserDTO> loadUsers() {
        try {
            ResponseEntity<UserDTO[]> r = req("/api/users", HttpMethod.GET, null, UserDTO[].class);
            return r.getStatusCode().is2xxSuccessful() && r.getBody() != null
                    ? Arrays.asList(r.getBody())
                    : Collections.emptyList();
        } catch (Exception e) {
            Notification.show("Eroare la încărcarea utilizatorilor: " + e.getMessage());
            logger.error("Error loading users: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    /*────────────────────────────────────────────
     * Încărcare mărci și modele
     *───────────────────────────────────────────*/
    private List<CarMakeDTO> loadCarMakes() {
        try {
            ResponseEntity<CarMakeDTO[]> r = req("/api/cars/makes", HttpMethod.GET, null, CarMakeDTO[].class);
            logger.info("Loaded {} car makes", r.getBody() != null ? r.getBody().length : 0);
            return r.getStatusCode().is2xxSuccessful() && r.getBody() != null
                    ? Arrays.asList(r.getBody())
                    : Collections.emptyList();
        } catch (Exception e) {
            Notification.show("Eroare la încărcarea mărcilor: " + e.getMessage());
            logger.error("Error loading car makes: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    private List<CarModelDTO> loadCarModels(Long makeId) {
        try {
            String path = "/api/cars/models" + (makeId != null ? "?makeId=" + makeId : "");
            ResponseEntity<CarModelDTO[]> r = req(path, HttpMethod.GET, null, CarModelDTO[].class);
            logger.info("Loaded {} car models for makeId={}", r.getBody() != null ? r.getBody().length : 0, makeId);
            return r.getStatusCode().is2xxSuccessful() && r.getBody() != null
                    ? Arrays.asList(r.getBody())
                    : Collections.emptyList();
        } catch (Exception e) {
            Notification.show("Eroare la încărcarea modelelor: " + e.getMessage());
            logger.error("Error loading car models for makeId={}: {}", makeId, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    /*────────────────────────────────────────────
     * Gestionare prețuri estimate
     *───────────────────────────────────────────*/
    private void loadPricesForCars(List<CarDTO> cars) {
        for (CarDTO car : cars) {
            if (!priceCache.containsKey(car.getId())) {
                estimatePriceForCar(car);
            }
        }
        grid.getDataProvider().refreshAll();
    }

    private void refreshAllPrices() {
        priceCache.clear();
        loadPricesForCars(cars);
        Notification.show("Prețurile au fost reîmprospătate!");
    }

    private void estimatePriceForCar(CarDTO car) {
        try {
            String url = String.format("http://localhost:8080/api/price/estimate?make=%s&model=%s&year=%d&kilometers=%d",
                    car.getCarMake().getMake(), car.getCarModel().getModel(), car.getYear(), car.getMileage());
            logger.info("Estimating price for {}: {}", car.getLicensePlate(), url);
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                Double price = (Double) body.get("estimatedPrice");
                if (price != null) {
                    priceCache.put(car.getId(), price);
                    CarDTO updatedCar = new CarDTO();
                    updatedCar.setId(car.getId());
                    updatedCar.setEstimatedPrice(price);
                    req("/api/cars/" + car.getId(), HttpMethod.PATCH, updatedCar, Void.class);
                    logger.info("Updated price for {}: {} EUR", car.getLicensePlate(), price);
                }
            }
        } catch (Exception ex) {
            Notification.show("Eroare la estimarea prețului pentru " + car.getLicensePlate() + ": " + ex.getMessage());
            logger.error("Error estimating price for {}: {}", car.getLicensePlate(), ex.getMessage(), ex);
        }
    }

    /*────────────────────────────────────────────
     * Statistici
     *───────────────────────────────────────────*/
    private void updateStats() {
        statsLayout.removeAll();
        statsLayout.add(avgAgeChart(), avgMileageChart(), avgPriceChart());
    }

    private Component avgAgeChart() {
        double age = cars.stream()
                .filter(c -> c.getYear() != null)
                .mapToInt(c -> LocalDate.now().getYear() - c.getYear())
                .average().orElse(0);
        return bar("Vârstă medie", age, " ani", "#28a745");
    }

    private Component avgMileageChart() {
        double km = cars.stream()
                .mapToLong(CarDTO::getMileage)
                .average().orElse(0) / 1000d;
        return bar("Km medii", km, " mii km", "#007bff");
    }

    private Component avgPriceChart() {
        double avgPrice = priceCache.values().stream()
                .mapToDouble(Double::doubleValue)
                .average().orElse(0);
        return bar("Preț mediu", avgPrice / 1000, " mii EUR", "#ff5733");
    }

    private Component bar(String title, double value, String suffix, String color) {
        VerticalLayout box = new VerticalLayout();
        Span t = new Span(title);
        t.getStyle().set("font-weight", "bold");

        Div bar = new Div();
        bar.getStyle()
                .set("background", color)
                .set("height", "100px")
                .set("width", Math.min(value * 10, 200) + "px")
                .set("border-radius", "4px");

        box.add(t, bar, new Span(String.format("%.1f%s", value, suffix)));
        box.setWidth("20%");
        return box;
    }

    /* helper null-safe string */
    private static String nvl(String s) {
        return s != null ? s : "N/A";
    }
}