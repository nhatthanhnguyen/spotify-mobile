package com.ptit.spotify.dto.model;

import java.util.List;

public class InteractionResponse {
    private List<Interaction> interactions;
    private StatusError statusError;

    public List<Interaction> getInteractions() {
        return interactions;
    }

    public void setInteractions(List<Interaction> interactions) {
        this.interactions = interactions;
    }

    public StatusError getStatusError() {
        return statusError;
    }

    public void setStatusError(StatusError statusError) {
        this.statusError = statusError;
    }
}