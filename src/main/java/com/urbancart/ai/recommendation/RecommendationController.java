package com.urbancart.ai.recommendation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping("/recommend")
    public ResponseEntity<RecommendationResponse> recommend(@Valid @RequestBody RecommendationRequest request) {
        return ResponseEntity.ok(recommendationService.recommend(request.query()));
    }
}
