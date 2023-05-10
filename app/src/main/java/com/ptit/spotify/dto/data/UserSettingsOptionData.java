package com.ptit.spotify.dto.data;

import com.ptit.spotify.utils.TypeUserSettingsItem;

public class UserSettingsOptionData {
    private String title;
    private String description;
    private TypeUserSettingsItem type;

    public UserSettingsOptionData(String title, String description, TypeUserSettingsItem type) {
        this.title = title;
        this.description = description;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeUserSettingsItem getType() {
        return type;
    }

    public void setType(TypeUserSettingsItem type) {
        this.type = type;
    }
}
