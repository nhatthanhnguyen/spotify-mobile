package com.ptit.spotify.dto.data;

import com.ptit.spotify.utils.TypeDestination;
import com.ptit.spotify.utils.TypeOption;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumSettingsOptionNavigationData {
    private String imageResourceOption;
    private String textOption;
    private TypeDestination typeDestination;
    private TypeOption typeOption = TypeOption.NAVIGATE;

    public AlbumSettingsOptionNavigationData(String imageResourceOption, String textOption, TypeDestination typeDestination) {
        this.imageResourceOption = imageResourceOption;
        this.textOption = textOption;
        this.typeDestination = typeDestination;
    }
}
