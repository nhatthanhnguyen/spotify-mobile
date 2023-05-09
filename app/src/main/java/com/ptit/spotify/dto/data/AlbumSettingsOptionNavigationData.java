package com.ptit.spotify.dto.data;

import com.ptit.spotify.utils.TypeDestination;
import com.ptit.spotify.utils.TypeOption;

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

    public AlbumSettingsOptionNavigationData(String imageResourceOption, String textOption, TypeDestination typeDestination, TypeOption typeOption) {
        this.imageResourceOption = imageResourceOption;
        this.textOption = textOption;
        this.typeDestination = typeDestination;
        this.typeOption = typeOption;
    }

    public String getImageResourceOption() {
        return imageResourceOption;
    }

    public void setImageResourceOption(String imageResourceOption) {
        this.imageResourceOption = imageResourceOption;
    }

    public String getTextOption() {
        return textOption;
    }

    public void setTextOption(String textOption) {
        this.textOption = textOption;
    }

    public TypeDestination getTypeDestination() {
        return typeDestination;
    }

    public void setTypeDestination(TypeDestination typeDestination) {
        this.typeDestination = typeDestination;
    }

    public TypeOption getTypeOption() {
        return typeOption;
    }

    public void setTypeOption(TypeOption typeOption) {
        this.typeOption = typeOption;
    }
}
