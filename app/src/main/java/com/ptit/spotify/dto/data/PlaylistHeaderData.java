package com.ptit.spotify.dto.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistHeaderData {
    private String playlistImageUrl;
    private String playlistDescription;
    private String userCreatedImageUrl;
    private String userCreatedName;
    private long numberOfLikes;
    private long totalLength;
    private boolean liked;
    private boolean downloaded;
}
