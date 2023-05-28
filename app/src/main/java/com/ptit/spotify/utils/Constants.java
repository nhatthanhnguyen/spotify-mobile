package com.ptit.spotify.utils;

public class Constants {
    public static final String PLAYLIST = "Playlist";
    public static final String ARTIST = "Artist";
    public static final String ALBUM = "Album.java";
    public static final String SONG = "Song";
    public static final String ApiURL = "http://103.142.26.18:8080/api/v1";
    public static final String ApiFindSongURL = "http://103.142.26.18:5000/find_song";
    public static final String AuthenURL = "/authen";
    public static final String AccountURL = "/accounts";
    public static final String PlaylistURL = "/playlists";
    public static final String SongURL = "/songs";
    public static final String InteractionURL = "/interaction";
    public static final String ArtistURL = "/artists";
    public static final String AlbumsURL = "/albums";

    //Authen
    public static String getLoginEndpoint() {
        return ApiURL + AuthenURL + "/login";
    }
    public static String getVerifyEndpoint() {
        return ApiURL + AuthenURL + "/verify";
    }
    public static String getLogoutEndpoint() {
        return ApiURL + AuthenURL + "/logout";
    }
    public static String getRegisterEndpoint() {
        return ApiURL + AccountURL + "/register";
    }
    public static String getChangePasswordEndpoint() {
        return ApiURL + AccountURL + "/change-password";
    }
    public static String getAllPlaylistEndpoint() {
        return ApiURL + PlaylistURL;
    }
    public static String getPlaylistByNameEndpoint(String name) {
        return ApiURL + PlaylistURL + "/name/" + name;
    }
    public static String getPlaylistByIdEndpoint(String id) {
        return ApiURL + PlaylistURL + "id/" + id;
    }
    public static String getPlaylistAllSongEndpoint() {
        return ApiURL + PlaylistURL;
    }
    public static String getAddPlaylistEndpoint() {
        return ApiURL + PlaylistURL + "/add";
    }
    public static String getUpdatePlaylistEndpoint() {
        return ApiURL + PlaylistURL + "update";
    }
    public static String getAllSongEndpoint() {
        return ApiURL + SongURL;
    }
    public static String getSongByIdEndpoint(String id) {
        return ApiURL + SongURL + "/id/" + id;
    }
    public static String getSongByNameEndpoint(String name) {
        return ApiURL + SongURL + "/name/" + name;
    }
    public static String getSongByPlaylistIdEndpoint(String id) {
        return ApiURL + SongURL + "/play_list/" + id;
    }
    public static String getSongInteractionEndpoint(String id) {
        return ApiURL + SongURL + "/interactions/" + id;
    }
    public static String getSongByAlbumIdEndpoint(String id) {
        return ApiURL + SongURL + "/album/" + id;
    }
    public static String getSongByArtistIdEndpoint(String id) {
        return ApiURL + SongURL + "/songs/" + id;
    }
    public static String getAddSongEndpoint() {
        return ApiURL + SongURL + "/add";
    }
    public static String getAddSongToPlaylistEndpoint() {
        return ApiURL + SongURL + "/add_playlist";
    }
    public static String getRemoveSongToPlaylistEndpoint() {
        return ApiURL + SongURL + "/remove_playlist";
    }
    public static String getAddInteractionEndpoint(String userId, String songId) {
        return ApiURL + InteractionURL + "/add/" + userId + "/"+songId;
    }
    public static String getDeleteInteractionEndpoint(String userId, String songId) {
        return ApiURL + InteractionURL + "/add/" + userId + "/"+songId;
    }
    public static String getAddAlbumsEndpoint() {
        return ApiURL + AlbumsURL + "/add";
    }
    public static String getAllAlbumsEndpoint() {
        return ApiURL + AlbumsURL;
    }
    public static String getAlbumsByIdEndpoint(String id) {
        return ApiURL + AlbumsURL + "/id/"+id;
    }
    public static String getUpdateAlbumsEndpoint() {
        return ApiURL + AlbumsURL + "/update";
    }
    public static String getAddArtistEndpoint() {
        return ApiURL + ArtistURL + "/add";
    }
    public static String getArtistByIdEndpoint(String id) {
        return ApiURL + AlbumsURL + "/id/"+id;
    }
    public static String getUpdateArtistEndpoint() {
        return ApiURL + ArtistURL + "/update";
    }
    public static String getFindSongEndpoint() {
        return ApiFindSongURL;
    }
}
