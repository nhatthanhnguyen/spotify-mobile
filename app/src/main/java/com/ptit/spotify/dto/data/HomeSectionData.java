package com.ptit.spotify.dto.data;

import java.util.List;

public class HomeSectionData {
    private Object header;
    private List<HomeCardData> cards;

    public HomeSectionData(Object header, List<HomeCardData> cards) {
        this.header = header;
        this.cards = cards;
    }

    public Object getHeader() {
        return header;
    }

    public void setHeader(Object header) {
        this.header = header;
    }

    public List<HomeCardData> getCards() {
        return cards;
    }

    public void setCards(List<HomeCardData> cards) {
        this.cards = cards;
    }
}
