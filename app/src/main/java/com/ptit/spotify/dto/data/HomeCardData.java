package com.ptit.spotify.dto.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
}
