package com.ptit.spotify.models.data;

public class ArtistHeaderData {
    private String artistName;
    private String artistImageUrl;
    private long numberOfLikes;
    private boolean isFollow;

    public ArtistHeaderData(String artistName, String artistImageUrl, long numberOfLikes, boolean isFollow) {
        this.artistName = artistName;
        this.artistImageUrl = artistImageUrl;
        this.numberOfLikes = numberOfLikes;
        this.isFollow = isFollow;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistImageUrl() {
        return artistImageUrl;
    }

    public void setArtistImageUrl(String artistImageUrl) {
        this.artistImageUrl = artistImageUrl;
    }

    public long getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(long numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }
}
