package com.mycompany.timeflo.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ClaudeAIService {

    private static final String API_URL     = "https://api.anthropic.com/v1/messages";
    private static final String API_VERSION = "2023-06-01";
    private static final String MODEL       = "claude-haiku-4-5-20251001";
    private String apiKey;

    public ClaudeAIService(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public boolean hasApiKey() {
        return apiKey != null && !apiKey.trim().isEmpty();
    }

    public String getDailyRecommendation(String scheduleText, String journalText) throws Exception {
        if (!hasApiKey()) {
            throw new IllegalStateException("Claude API key not set.");
        }

        String prompt =
            "You are a friendly daily planning assistant for a college student.\\n\\n" +
            "Here is their schedule for today:\\n" + escapeJson(scheduleText) + "\\n\\n" +
            "Here are their journal notes:\\n" + escapeJson(journalText) + "\\n\\n" +
            "Give a short, realistic daily recommendation in 3-5 sentences. " +
            "Be encouraging and specific. Focus on time management, energy, and priorities.";

        String body = "{"
            + "\"model\":\"" + MODEL + "\","
            + "\"max_tokens\":300,"
            + "\"messages\":[{\"role\":\"user\",\"content\":\"" + prompt + "\"}]"
            + "}";

        HttpURLConnection conn = (HttpURLConnection) new URL(API_URL).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type",      "application/json");
        conn.setRequestProperty("x-api-key",         apiKey.trim());
        conn.setRequestProperty("anthropic-version", API_VERSION);
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes(StandardCharsets.UTF_8));
        }

        int code = conn.getResponseCode();
        if (code != 200) {
            throw new RuntimeException("Claude API error HTTP " + code);
        }

        BufferedReader reader = new BufferedReader(
            new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8)
        );
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) sb.append(line);

        return parseTextFromResponse(sb.toString());
    }

    private String parseTextFromResponse(String json) {
        String marker = "\"text\":\"";
        int start = json.indexOf(marker);
        if (start == -1) return "Could not parse AI response.";
        start += marker.length();
        int end = json.indexOf("\"", start);
        if (end == -1) return "Could not parse AI response.";
        return json.substring(start, end)
                   .replace("\\n", "\n")
                   .replace("\\\"", "\"");
    }

    private String escapeJson(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
