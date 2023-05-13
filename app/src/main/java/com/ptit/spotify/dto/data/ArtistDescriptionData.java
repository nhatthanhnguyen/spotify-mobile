package com.ptit.spotify.dto.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtistDescriptionData {
    private long numberOfLikes;
    private String description;
    private String artistImageUrl;
}
