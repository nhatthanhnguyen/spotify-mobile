package com.ptit.spotify.models.data;

import java.util.List;

public class SectionMoreLikeData {
    private String id;
    private String imageUrl;
    private String title;
    private String type;
    private List<CardData> cards;

    public SectionMoreLikeData(String id, String imageUrl, String title, String type, List<CardData> cards) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.type = type;
        this.cards = cards;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<CardData> getCards() {
        return cards;
    }

    public void setCards(List<CardData> cards) {
        this.cards = cards;
    }
}
