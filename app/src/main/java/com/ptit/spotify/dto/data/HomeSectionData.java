package com.ptit.spotify.dto.data;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeSectionData {
    private Object header;
    private List<HomeCardData> cards;
}
