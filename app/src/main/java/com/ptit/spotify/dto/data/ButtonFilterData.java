package com.ptit.spotify.dto.data;

import com.ptit.spotify.utils.ItemType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ButtonFilterData {
    private ItemType type;
    private boolean selected;
}
