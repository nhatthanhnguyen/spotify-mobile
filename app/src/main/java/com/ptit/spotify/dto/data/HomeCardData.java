package com.ptit.spotify.dto.data;

public class HomeCardData {
    private String id;
    private String imageUrl;
    private String title;
    private String description;
    private String type;
    private boolean isSmallCard = false;
    private boolean isHiddenTitle = false;
    private boolean isHiddenDescription = false;

    public HomeCardData(String id, String imageUrl, String title, String description, String type) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
        this.type = type;
    }

    public HomeCardData(String id, String imageUrl, String title, String description, String type, boolean isSmallCard, boolean isHiddenTitle, boolean isHiddenDescription) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
        this.type = type;
        this.isSmallCard = isSmallCard;
        this.isHiddenTitle = isHiddenTitle;
        this.isHiddenDescription = isHiddenDescription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSmallCard() {
        return isSmallCard;
    }

    public void setSmallCard(boolean smallCard) {
        isSmallCard = smallCard;
    }

    public boolean isHiddenTitle() {
        return isHiddenTitle;
    }

    public void setHiddenTitle(boolean hiddenTitle) {
        isHiddenTitle = hiddenTitle;
    }

    public boolean isHiddenDescription() {
        return isHiddenDescription;
    }

    public void setHiddenDescription(boolean hiddenDescription) {
        isHiddenDescription = hiddenDescription;
    }
}
