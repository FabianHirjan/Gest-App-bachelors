package hirjanfabian.bachelors.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/text")
public class TextQueryController {

    private static final String XAI_ENDPOINT = "https://api.x.ai/v1/chat/completions";

    @Value("${xai.api-key}")
    private String xaiApiKey;

    @PostMapping("/query")
    public ResponseEntity<Map<String, Object>> queryGrok(@Valid @RequestBody TextQueryRequest request) {
        try {
            // Prepare headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + xaiApiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Build request body for xAI API
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "grok-3-latest");
            requestBody.put("messages", Collections.singletonList(
                new HashMap<String, String>() {{
                    put("role", "user");
                    put("content", request.getText());
                }}
            ));
            requestBody.put("stream", false);
            requestBody.put("temperature", 0.7); // Moderate creativity for general responses

            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);
            RestTemplate restTemplate = new RestTemplate();

            // Call xAI Grok API
            ResponseEntity<Map> response = restTemplate.postForEntity(XAI_ENDPOINT, httpEntity, Map.class);
            Map<String, Object> responseBody = response.getBody();

            // Validate response
            if (responseBody == null || !responseBody.containsKey("choices")) {
                throw new RuntimeException("Invalid response from xAI: 'choices' key missing");
            }

            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
            if (choices.isEmpty()) {
                throw new RuntimeException("Invalid response from xAI: 'choices' list is empty");
            }

            Map<String, Object> choice = choices.get(0);
            if (!choice.containsKey("message") || !((Map) choice.get("message")).containsKey("content")) {
                throw new RuntimeException("Invalid response from xAI: 'message' or 'content' key missing");
            }

            String grokResponse = (String) ((Map) choice.get("message")).get("content");

            // Build response
            Map<String, Object> output = new HashMap<>();
            output.put("query", request.getText());
            output.put("response", grokResponse);

            return ResponseEntity.ok(output);
        } catch (HttpClientErrorException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to query xAI API: " + e.getStatusCode() + " - " + e.getMessage());
            return ResponseEntity.status(e.getStatusCode().value()).body(error);
        } catch (RestClientException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to connect to xAI API: " + e.getMessage());
            return ResponseEntity.status(503).body(error);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error processing request: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    // DTO for request validation
    public static class TextQueryRequest {
        @NotBlank(message = "Text query cannot be blank")
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}