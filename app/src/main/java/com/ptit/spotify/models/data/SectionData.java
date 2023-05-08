package com.ptit.spotify.models.data;

import java.util.List;

public class SectionData {
    private String title;
    private List<CardData> cards;

    public SectionData(String title, List<CardData> cards) {
        this.title = title;
        this.cards = cards;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<CardData> getCards() {
        return cards;
    }

    public void setCards(List<CardData> cards) {
        this.cards = cards;
    }
}
