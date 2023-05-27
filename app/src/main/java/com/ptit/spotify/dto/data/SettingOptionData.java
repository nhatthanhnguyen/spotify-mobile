package com.ptit.spotify.dto.data;

import com.ptit.spotify.utils.ItemType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettingOptionData {
    private int iconId;
    private String description;
    private ItemType itemType;
}
