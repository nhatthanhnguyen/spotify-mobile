package com.ptit.spotify.dto.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtistObject {
    private int id;
    private String name;
    private String description;
    private String coverImage;
}
