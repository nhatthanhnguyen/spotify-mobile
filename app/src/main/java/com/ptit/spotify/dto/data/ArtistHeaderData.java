package com.ptit.spotify.dto.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtistHeaderData {
    private String id;
    private String artistName;
    private String artistImageUrl;
    private long numberOfLikes;
    private boolean isFollow;
}
