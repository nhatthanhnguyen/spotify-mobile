package com.ptit.spotify.utils;

public class Constants {
    public static final String PLAYLIST = "Playlist";
    public static final String ARTIST = "Artist";
    public static final String ALBUM = "Album";
    public static final String SONG = "Song";
    public static final String ApiURL = "http://103.142.26.18:8080/api/v1";
    public static final String ApiLoginURL = "/authen/login";
    public static final String ApiRegisterURL = "/accounts/register";
    public static String getLoginEndpoint() {
        return ApiURL + ApiLoginURL;
    }
    public static String getRegisterEndpoint() {
        return ApiURL + ApiRegisterURL;
    }
}
