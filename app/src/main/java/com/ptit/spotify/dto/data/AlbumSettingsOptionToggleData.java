package com.ptit.spotify.dto.data;

import com.ptit.spotify.utils.TypeOption;

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

    public AlbumSettingsOptionToggleData(String imageResourceOption, String textOptionOn, String textOptionOff, boolean isOn, TypeOption typeOption) {
        this.imageResourceOption = imageResourceOption;
        this.textOptionOn = textOptionOn;
        this.textOptionOff = textOptionOff;
        this.isOn = isOn;
        this.typeOption = typeOption;
    }

    public String getImageResourceOption() {
        return imageResourceOption;
    }

    public void setImageResourceOption(String imageResourceOption) {
        this.imageResourceOption = imageResourceOption;
    }

    public String getTextOptionOn() {
        return textOptionOn;
    }

    public void setTextOptionOn(String textOptionOn) {
        this.textOptionOn = textOptionOn;
    }

    public String getTextOptionOff() {
        return textOptionOff;
    }

    public void setTextOptionOff(String textOptionOff) {
        this.textOptionOff = textOptionOff;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public TypeOption getTypeOption() {
        return typeOption;
    }

    public void setTypeOption(TypeOption typeOption) {
        this.typeOption = typeOption;
    }
}
