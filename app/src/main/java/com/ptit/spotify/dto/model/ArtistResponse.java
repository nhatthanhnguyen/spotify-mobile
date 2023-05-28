package com.ptit.spotify.dto.model;
import java.util.List;

public class ArtistResponse {
    private List<Artist> artists;
    private StatusError statusError;

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public StatusError getStatusError() {
        return statusError;
    }

    public void setStatusError(StatusError statusError) {
        this.statusError = statusError;
    }
}