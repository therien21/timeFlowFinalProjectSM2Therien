/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.timeflo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author theri
 */
public class YouTubeClient {
    public List<YouTubeVideo> searchTopVideos(String apiKey,String query, int maxResults) throws Exception {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalStateException("YouTube API key not set.");
        }
        String urlStr =
                "https://www.googleapis.com/youtube/v3/search" +
                "?part=snippet&type=video&maxResults=" + maxResults +
                "&q=" + URLEncoder.encode(query, StandardCharsets.UTF_8) +
                "&key=" + apiKey.trim();
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestMethod("GET");
        int code = conn.getResponseCode();
        if (code != 200) {
            throw new RuntimeException("YouTube API error HTTP " + code);
        }
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8)
        );
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) sb.append(line);
        return parseVideoIdTitlePairs(sb.toString(), maxResults);
    }
    private List<YouTubeVideo> parseVideoIdTitlePairs(String json, int limit) {
        List<YouTubeVideo> out = new ArrayList<>();
        int idx = 0;
        while (out.size() < limit) {
            int videoIdIndex = json.indexOf("\"videoId\":", idx);
            if (videoIdIndex == -1) break;
            int videoIdStart = json.indexOf("\"", videoIdIndex + 10) + 1;
            int videoIdEnd = json.indexOf("\"", videoIdStart);
            String videoId = json.substring(videoIdStart, videoIdEnd);
            int titleIndex = json.indexOf("\"title\":", videoIdEnd);
            if (titleIndex == -1) break;
            int titleStart = json.indexOf("\"", titleIndex + 8) + 1;
            int titleEnd = json.indexOf("\"", titleStart);
            String title = json.substring(titleStart, titleEnd);
            title = title.replace("\\u0026", "&").replace("\\\"", "\"");
            out.add(new YouTubeVideo(title, videoId));
            idx = titleEnd;
        }
        return out;
    }   
}
