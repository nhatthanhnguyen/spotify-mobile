package com.ptit.spotify.models.data;

public class ArtistDescriptionData {
    private long numberOfLikes;
    private String description;
    private String artistImageUrl;

    public ArtistDescriptionData(long numberOfLikes, String description, String artistImageUrl) {
        this.numberOfLikes = numberOfLikes;
        this.description = description;
        this.artistImageUrl = artistImageUrl;
    }

    public long getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(long numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArtistImageUrl() {
        return artistImageUrl;
    }

    public void setArtistImageUrl(String artistImageUrl) {
        this.artistImageUrl = artistImageUrl;
    }
}
