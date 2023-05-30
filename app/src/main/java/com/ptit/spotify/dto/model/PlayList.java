package com.ptit.spotify.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayList {
    private int play_list_id;
    private String name;
    private String cover_img;
    private int user_id;
    private String created_at;
}