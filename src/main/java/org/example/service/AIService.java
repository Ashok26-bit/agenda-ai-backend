package org.example.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;

@Service
public class AIService {

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> getPrediction(String absoluteFilePath) {
        // Laptop IP address thaan use pannanum, localhost venaam
        String aiUrl = "http://172.16.83.137:5000/predict";
        //http://localhost:5000/predict

        // JSON Header set pandrom (Flask-la request.is_json work aaga idhu mukkiyam)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // JSON Body: {"image": "C:/path/to/image.jpg"}
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("image", absoluteFilePath);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        try {
            // Flask-ku POST request anupuroam
            ResponseEntity<Map> response = restTemplate.postForEntity(aiUrl, request, Map.class);

            // Response-ah safe-ah vaanguroam
            return (Map<String, Object>) response.getBody();

        } catch (Exception e) {
            // Error handling
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("category", "AI Connectivity Error");
            errorMap.put("description", "Spring Boot cannot reach Flask: " + e.getMessage());
            return errorMap;
        }
    }
}
