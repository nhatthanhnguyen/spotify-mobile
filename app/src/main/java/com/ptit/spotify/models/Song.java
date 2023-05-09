package com.ptit.spotify.models;

import com.ptit.spotify.utils.Constants;

import java.util.List;

public class Song {
    private String id;
    private String name;
    private Artist artist;
    private List<String> genres;
    private Album album;
    private String imageUrl;
    private long length;
    private String type = Constants.SONG;

    public Song(String id, String name, Artist artist, List<String> genres, Album album, String imageUrl, long length) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.genres = genres;
        this.album = album;
        this.imageUrl = imageUrl;
        this.length = length;
    }

    public Song(String id, String name, Artist artist, List<String> genres, Album album, String imageUrl, long length, String type) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.genres = genres;
        this.album = album;
        this.imageUrl = imageUrl;
        this.length = length;
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

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
