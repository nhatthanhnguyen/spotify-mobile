package com.ptit.spotify.dto.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InteractionResponse {
    private List<Interaction> interactions;
    private StatusError status_error;
}