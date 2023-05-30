package com.ptit.spotify.dto.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistSongData {
    private int songID;
    private String songUrl;
    private String songName;
    private int artistID;
    private String artistName;
    private int albumID;
    private String albumName;
    private int length;
    private boolean liked;
}
