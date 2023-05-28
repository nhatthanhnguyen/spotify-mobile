package com.ptit.spotify.dto.model;
import java.util.List;

public class SongResponse {
    private List<Song> songs;
    private StatusError statusError;

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public StatusError getStatusError() {
        return statusError;
    }

    public void setStatusError(StatusError statusError) {
        this.statusError = statusError;
    }
}