package com.ptit.spotify.dto.data;

import com.ptit.spotify.utils.TypeUserSettingsItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSettingsOptionData {
    private String title;
    private String description;
    private TypeUserSettingsItem type;
}
