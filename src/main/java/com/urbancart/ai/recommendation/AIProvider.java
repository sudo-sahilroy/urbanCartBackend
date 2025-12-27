package com.urbancart.ai.recommendation;

import java.util.List;

public interface AIProvider {
    List<String> generateKeywords(String query);
}
