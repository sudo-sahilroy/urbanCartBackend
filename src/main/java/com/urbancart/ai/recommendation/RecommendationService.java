package com.urbancart.ai.recommendation;

import com.urbancart.ai.products.ProductDto;
import com.urbancart.ai.products.ProductEntity;
import com.urbancart.ai.products.ProductRepository;
import org.apache.commons.text.similarity.FuzzyScore;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private static final int MAX_RESULTS = 20;
    private static final int CANDIDATE_PAGE_SIZE = 30;
    private static final FuzzyScore FUZZY_SCORE = new FuzzyScore(Locale.ENGLISH);

    private final AIProvider aiProvider;
    private final ProductRepository productRepository;

    public RecommendationResponse recommend(String query) {
        String normalizedQuery = StringUtils.hasText(query) ? query.trim() : "";
        List<String> generatedKeywords = aiProvider.generateKeywords(normalizedQuery);
        List<String> expandedTerms = expandTerms(normalizedQuery, generatedKeywords);

        Map<Long, ProductScore> scoredProducts = new LinkedHashMap<>();
        PageRequest pageRequest = PageRequest.of(0, CANDIDATE_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "rating"));

        if (expandedTerms.isEmpty() && StringUtils.hasText(normalizedQuery)) {
            fetchAndScoreProducts(normalizedQuery, normalizedQuery, scoredProducts, pageRequest);
        } else {
            expandedTerms.forEach(term -> fetchAndScoreProducts(normalizedQuery, term, scoredProducts, pageRequest));
        }

        List<ProductDto> products = scoredProducts.values().stream()
                .sorted(Comparator.comparing(ProductScore::score).reversed())
                .limit(MAX_RESULTS)
                .map(ps -> ProductDto.from(ps.product()))
                .toList();
        return new RecommendationResponse(products);
    }

    private List<String> expandTerms(String query, List<String> generatedKeywords) {
        Set<String> terms = new LinkedHashSet<>();
        if (StringUtils.hasText(query)) {
            terms.addAll(tokenize(query));
        }
        generatedKeywords.forEach(terms::add);

        Set<String> expanded = new LinkedHashSet<>();
        terms.stream()
                .map(term -> term.toLowerCase(Locale.ENGLISH).trim())
                .filter(StringUtils::hasText)
                .forEach(term -> expanded.addAll(normalizeVariants(term)));
        return new ArrayList<>(expanded);
    }

    private List<String> tokenize(String input) {
        String normalized = input.replaceAll("[^a-zA-Z0-9 ]", " ").toLowerCase(Locale.ENGLISH);
        String[] parts = normalized.split("\\s+");
        List<String> tokens = new ArrayList<>();
        for (String part : parts) {
            if (StringUtils.hasText(part)) {
                tokens.add(part);
            }
        }
        return tokens;
    }

    private Set<String> normalizeVariants(String term) {
        Set<String> variants = new LinkedHashSet<>();

        String cleaned = term.replaceAll("[^a-z0-9 ]", "").trim();
        if (!StringUtils.hasText(cleaned)) {
            return variants;
        }

        variants.add(cleaned);

        // Handle plural to singular (naive)
        if (cleaned.endsWith("ies") && cleaned.length() > 3) {
            variants.add(cleaned.substring(0, cleaned.length() - 3) + "y");
        } else if (cleaned.endsWith("s") && cleaned.length() > 3) {
            variants.add(cleaned.substring(0, cleaned.length() - 1));
        }

        // Canonicalize common gender/category terms to match catalog words
        switch (cleaned) {
            case "womens", "women", "woman", "womans", "ladies", "lady", "female" -> {
                variants.add("women");
                variants.add("female");
            }
            case "mens", "men", "man", "mans", "male", "gents" -> {
                variants.add("men");
                variants.add("male");
            }
            case "unisex" -> variants.add("unisex");
            default -> {
            }
        }

        return variants;
    }

    private void fetchAndScoreProducts(String originalQuery, String term, Map<Long, ProductScore> scoredProducts, PageRequest pageRequest) {
        productRepository.searchByTermAcrossFields(term, pageRequest)
                .forEach(product -> addOrUpdateScore(product, originalQuery, term, scoredProducts));
    }

    private void addOrUpdateScore(ProductEntity product, String originalQuery, String term, Map<Long, ProductScore> scoredProducts) {
        String searchableText = buildSearchableText(product);
        int termScore = FUZZY_SCORE.fuzzyScore(searchableText, term);
        int queryScore = StringUtils.hasText(originalQuery) ? FUZZY_SCORE.fuzzyScore(searchableText, originalQuery) : 0;
        double ratingScore = Optional.ofNullable(product.getRating()).orElse(0.0d);
        double combinedScore = (Math.max(termScore, queryScore) * 2.0) + ratingScore;

        ProductScore incomingScore = new ProductScore(product, combinedScore);
        scoredProducts.merge(
                product.getId(),
                incomingScore,
                (existing, incoming) -> incoming.score() > existing.score() ? incoming : existing
        );
    }

    private String buildSearchableText(ProductEntity product) {
        return java.util.stream.Stream.of(product.getTitle(), product.getDescription(), product.getCategory())
                .filter(StringUtils::hasText)
                .map(String::toLowerCase)
                .collect(Collectors.joining(" "));
    }

    private record ProductScore(ProductEntity product, double score) {
    }
}