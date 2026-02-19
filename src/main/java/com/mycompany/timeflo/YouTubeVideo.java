/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.timeflo;

/**
 *
 * @author theri
 */
public class YouTubeVideo {
    private final String title;
    private final String videoId;
    
    public YouTubeVideo(String title, String videoId){
        this.title = title;
        this.videoId = videoId;
    }
    public String getUrl(){
        return "https://www.youtube.com/watch?v=" + videoId;
    }
    @Override
    public String toString(){
        return title;
    }
}
