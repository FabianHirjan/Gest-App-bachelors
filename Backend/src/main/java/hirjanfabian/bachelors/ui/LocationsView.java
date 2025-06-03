package hirjanfabian.bachelors.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;
import hirjanfabian.bachelors.ui.base.BaseProtectedView;

/**
 * Pagina care afişează pe Google Maps locaţiile ultim raportate
 * de toţi utilizatorii (endpoint: /api/users/locations).
 *
 * Route: /locations
 */
@Route("locations")
public class LocationsView extends BaseProtectedView {

    private final Div map = new Div();
    private final Span errorMessage = new Span("Failed to load the map. Please try again later.");

    public LocationsView() {
        super("locations");

        map.setId("map");
        map.setWidthFull();
        map.setHeight("600px");

        // Style the error message (hidden by default)
        errorMessage.getStyle().set("color", "red").set("display", "none");
        map.add(errorMessage);

        setMainContent(map);

        initialiseGoogleMap();
    }

    /** Defineşte funcţia `initMap` şi încarcă biblioteca Google Maps într-un singur apel JS. */
    private void initialiseGoogleMap() {
        UI ui = UI.getCurrent();
        String token = jwt(); // helper din BaseProtectedView

        String script = "window.initMap = function() {" +
                "  try {" +
                "    const map = new google.maps.Map(document.getElementById('map'), {" +
                "      zoom: 6, center: {lat: 45.9432, lng: 24.9668}});" +
                "    fetch('/api/users/locations', {headers: {Authorization: 'Bearer " + token + "'}})" +
                "      .then(r => {" +
                "        if (!r.ok) throw new Error('Failed to fetch locations: ' + r.status);" +
                "        return r.json();" +
                "      })" +
                "      .then(list => list.forEach(u => new google.maps.Marker({" +
                "        position: {lat: u.latitude, lng: u.longitude}, map: map, title: u.username" +
                "      })))" +
                "      .catch(err => {" +
                "        console.error('Map fetch error:', err);" +
                "        document.getElementById('map').querySelector('span').style.display = 'block';" +
                "      });" +
                "  } catch (err) {" +
                "    console.error('Map initialization error:', err);" +
                "    document.getElementById('map').querySelector('span').style.display = 'block';" +
                "  }" +
                "};" +
                "const script = document.createElement('script');" +
                "script.src = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyAoN3dq6KaVeD7u1O8w6afKkpS4bdDWSRU&callback=initMap';" +
                "script.async = true;" +
                "script.onerror = () => {" +
                "  console.error('Failed to load Google Maps API');" +
                "  document.getElementById('map').querySelector('span').style.display = 'block';" +
                "};" +
                "document.head.appendChild(script);";

        ui.getPage().executeJs(script);
    }
}