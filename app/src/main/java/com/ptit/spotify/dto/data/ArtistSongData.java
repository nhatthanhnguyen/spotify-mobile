package com.ptit.spotify.dto.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtistSongData {
    private String orderNumber;
    private String songName;
    private String songImageUrl;
    private String numberOfLikes;
}
