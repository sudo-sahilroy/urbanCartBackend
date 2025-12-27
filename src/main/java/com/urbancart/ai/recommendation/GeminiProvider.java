package com.urbancart.ai.recommendation;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GeminiProvider implements AIProvider {

    private static final Logger log = LoggerFactory.getLogger(GeminiProvider.class);

    private final RestTemplate restTemplate;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.model:gemini-2.0-flash}")
    private String model;

    private static final String GEMINI_URL_TEMPLATE = "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent";

    @Override
    public List<String> generateKeywords(String query) {
        if (!StringUtils.hasText(apiKey) || apiKey.contains("YOUR_")) {
            log.warn("Gemini API key is missing or placeholder; falling back to naive keywords");
            return List.of(query.split(" "));
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-goog-api-key", apiKey);
            Map<String, Object> payload = Map.of(
                    "contents", List.of(Map.of(
                            "parts", List.of(Map.of("text", """
                                    Extract concise fashion product search terms separated by commas from the text: '%s'.
                                    Return only single-word, singular tokens (no phrases) in a flat comma-separated list.
                                    Prefer catalog-friendly bases like 'women', 'men', 'dress', 'jacket', colors, fabrics, and style/occasion terms.
                                    """.formatted(query)))
                    ))
            );
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
            String url = GEMINI_URL_TEMPLATE.formatted(model);
            Map response = restTemplate.postForObject(url, entity, Map.class);
            return parseKeywords(response);
        } catch (HttpStatusCodeException e) {
            log.error("Gemini call failed with status {} and body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            return List.of(query.split(" "));
        } catch (Exception e) {
            log.error("Gemini call failed, falling back to naive keywords", e);
            return List.of(query.split(" "));
        }
    }

    @SuppressWarnings("unchecked")
    private List<String> parseKeywords(Map response) {
        if (response == null) return List.of();
        List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
        if (candidates == null || candidates.isEmpty()) return List.of();
        Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
        if (content == null) return List.of();
        List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
        if (parts == null || parts.isEmpty()) return List.of();
        String text = (String) parts.get(0).get("text");
        if (text == null) return List.of();
        String[] tokens = text.split(",");
        List<String> keywords = new ArrayList<>();
        for (String token : tokens) {
            String trimmed = token.trim();
            if (!trimmed.isEmpty()) keywords.add(trimmed);
        }
        return keywords;
    }
}