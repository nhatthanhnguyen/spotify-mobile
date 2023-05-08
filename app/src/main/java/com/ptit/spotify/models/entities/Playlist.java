package com.ptit.spotify.models.entities;

import com.ptit.spotify.utils.Constants;

import java.util.List;

public class Playlist {
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private List<String> genres;
    private String type = Constants.PLAYLIST;

    public Playlist(String id, String name, String description, String imageUrl, List<String> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.genres = genres;
    }

    public Playlist(String id, String name, String description, String imageUrl, List<String> genres, String type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.genres = genres;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
