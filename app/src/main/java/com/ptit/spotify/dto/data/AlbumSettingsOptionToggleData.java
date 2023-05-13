package com.ptit.spotify.dto.data;

import com.ptit.spotify.utils.TypeOption;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumSettingsOptionToggleData {
    private String imageResourceOption;
    private String textOptionOn;
    private String textOptionOff;
    private boolean isOn;
    private TypeOption typeOption = TypeOption.TOGGLE;

    public AlbumSettingsOptionToggleData(String imageResourceOption, String textOptionOn, String textOptionOff, boolean isOn) {
        this.imageResourceOption = imageResourceOption;
        this.textOptionOn = textOptionOn;
        this.textOptionOff = textOptionOff;
        this.isOn = isOn;
    }
}
