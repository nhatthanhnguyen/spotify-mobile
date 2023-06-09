package com.ptit.spotify.dto.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Song implements Serializable {
    private int song_id;
    private String name;
    private int album_id;
    private int artist_id;
    private String lyrics;
    private int length;
    private String url;
    private String youtube_link;
    private String song_cloud_id;
    private String cover_img;
}