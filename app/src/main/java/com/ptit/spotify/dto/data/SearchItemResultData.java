package com.ptit.spotify.dto.data;

import com.ptit.spotify.utils.ItemType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchItemResultData {
    private String id;
    private String imageUrl;
    private String title;
    private String description;
    private String artistName;
    private String albumName;
    private ItemType type;
}
