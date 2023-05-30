package com.ptit.spotify.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Album {
    private int album_id;
    private String name;
    private int artist_id;
    private String cover_img;
    private String created_at;
}