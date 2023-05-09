package com.ptit.spotify.dto.data;

public class ArtistSongData {
    private String orderNumber;
    private String songName;
    private String songImageUrl;
    private String numberOfLikes;

    public ArtistSongData(String orderNumber, String songName, String songImageUrl, String numberOfLikes) {
        this.orderNumber = orderNumber;
        this.songName = songName;
        this.songImageUrl = songImageUrl;
        this.numberOfLikes = numberOfLikes;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongImageUrl() {
        return songImageUrl;
    }

    public void setSongImageUrl(String songImageUrl) {
        this.songImageUrl = songImageUrl;
    }

    public String getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(String numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }
}
