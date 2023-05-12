package com.ptit.spotify.dto.data;

public class UserInfoHeaderData {
    private String imageUrl;
    private String name;
    private int numberFollowing;

    public UserInfoHeaderData(String imageUrl, String name, int numberFollowing) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.numberFollowing = numberFollowing;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberFollowing() {
        return numberFollowing;
    }

    public void setNumberFollowing(int numberFollowing) {
        this.numberFollowing = numberFollowing;
    }
}
