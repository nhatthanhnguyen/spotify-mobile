package com.ptit.spotify.dto.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Interaction {
    private int user_id;
    private int song_id;
    private int liked;
}