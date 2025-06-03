package hirjanfabian.bachelors.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/price")
public class PriceEstimateController {

    private static final String XAI_ENDPOINT = "https://api.x.ai/v1/chat/completions";

    @Value("${xai.api-key}")
    private String xaiApiKey;

    @GetMapping("/estimate")
    public ResponseEntity<Map<String, Object>> estimatePrice(
            @RequestParam String make,
            @RequestParam String model,
            @RequestParam int year,
            @RequestParam int kilometers) {
        try {
            // Construiește query-ul natural
            String query = String.format("Cât costă o %s %s %d cu %d km în România?",
                    make, model, year, kilometers);

            // Apelează xAI Grok API
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + xaiApiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "grok-3-latest");
            requestBody.put("messages", new Object[]{
                    new HashMap<String, String>() {{
                        put("role", "system");
                        put("content", "You are an assistant that estimates car prices in Romania based on make, model, year, and kilometers. Return only the estimated price in EUR as a number.");
                    }},
                    new HashMap<String, String>() {{
                        put("role", "user");
                        put("content", query);
                    }}
            });
            requestBody.put("stream", false);
            requestBody.put("temperature", 0);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(XAI_ENDPOINT, request, Map.class);

            // Extrage prețul estimat
            Map<String, Object> responseBody = response.getBody();
            if (responseBody == null || !responseBody.containsKey("choices")) {
                throw new RuntimeException("Răspuns invalid de la xAI: cheia 'choices' lipsește");
            }

            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
            if (choices.isEmpty()) {
                throw new RuntimeException("Răspuns invalid de la xAI: lista 'choices' este goală");
            }

            Map<String, Object> choice = choices.get(0);
            if (!choice.containsKey("message")) {
                throw new RuntimeException("Răspuns invalid de la xAI: cheia 'message' lipsește");
            }

            Map<String, Object> message = (Map<String, Object>) choice.get("message");
            if (!message.containsKey("content")) {
                throw new RuntimeException("Răspuns invalid de la xAI: cheia 'content' lipsește");
            }

            String content = (String) message.get("content");
            Double estimatedPrice = parsePriceFromContent(content);

            // Răspuns JSON
            Map<String, Object> output = new HashMap<>();
            output.put("query", query);
            output.put("make", make);
            output.put("model", model);
            output.put("year", year);
            output.put("kilometers", kilometers);
            output.put("estimatedPrice", estimatedPrice);
            output.put("currency", "EUR");

            return ResponseEntity.ok(output);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Eroare la estimarea prețului: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    private Double parsePriceFromContent(String content) {
        try {
            String trimmed = content.trim();
            if (!trimmed.matches("\\d+(\\.\\d+)?")) {
                throw new IllegalArgumentException("Răspunsul nu este un număr valid: " + trimmed);
            }
            return Double.parseDouble(trimmed);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la parsarea prețului: " + e.getMessage());
        }
    }
}