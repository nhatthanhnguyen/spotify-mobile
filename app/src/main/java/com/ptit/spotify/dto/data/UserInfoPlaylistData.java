package com.ptit.spotify.dto.data;

public class UserInfoPlaylistData {
    private String playlistImageUrl;
    private String playlistName;

    public UserInfoPlaylistData(String playlistImageUrl, String playlistName) {
        this.playlistImageUrl = playlistImageUrl;
        this.playlistName = playlistName;
    }

    public String getPlaylistImageUrl() {
        return playlistImageUrl;
    }

    public void setPlaylistImageUrl(String playlistImageUrl) {
        this.playlistImageUrl = playlistImageUrl;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }
}
