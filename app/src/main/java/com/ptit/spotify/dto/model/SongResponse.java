package com.ptit.spotify.dto.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongResponse {
    private List<Song> songs;
    private StatusError status_error;
}