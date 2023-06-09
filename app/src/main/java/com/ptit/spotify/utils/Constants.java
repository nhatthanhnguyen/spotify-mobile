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
    public static final String ArtistURL = "/artist";
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

    public static String getAccountInfoEndpoint() {
        return ApiURL + AccountURL + "/get";
    }

    public static String getAllPlaylistEndpoint() {
        return ApiURL + PlaylistURL;
    }

    public static String getPlaylistByNameEndpoint(String name) {
        return ApiURL + PlaylistURL + "/name/" + name;
    }

    public static String getPlaylistByIdEndpoint(String id) {
        return ApiURL + PlaylistURL + "/id/" + id;
    }

    public static String getPlaylistByUserIdEndpoint(String id) {
        return ApiURL + PlaylistURL + "/user/" + id;
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

    public static String getSongInteractionEndpoint(String userId, String id) {
        return ApiURL + SongURL + "/interactions/" + userId + "/" + id;
    }

    public static String getSongByAlbumIdEndpoint(String id) {
        return ApiURL + SongURL + "/album/" + id;
    }

    public static String getSongByArtistIdEndpoint(String id) {
        return ApiURL + SongURL + "/artist/" + id;
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

    public static String postAddInteractionEndpoint(String userId, String songId) {
        return ApiURL + InteractionURL + "/add/" + userId + "/" + songId;
    }

    public static String postDeleteInteractionEndpoint(String userId, String songId) {
        return ApiURL + InteractionURL + "/remove/" + userId + "/" + songId;
    }

    public static String getAddAlbumsEndpoint() {
        return ApiURL + AlbumsURL + "/add";
    }

    public static String getAllAlbumsEndpoint() {
        return ApiURL + AlbumsURL;
    }

    public static String getAlbumsByIdEndpoint(String id) {
        return ApiURL + AlbumsURL + "/id/" + id;
    }

    public static String getUpdateAlbumsEndpoint() {
        return ApiURL + AlbumsURL + "/update";
    }

    public static String getAddArtistEndpoint() {
        return ApiURL + ArtistURL + "/add";
    }

    public static String getArtistByIdEndpoint(String id) {
        return ApiURL + ArtistURL + "/id/" + id;
    }

    public static String getUpdateArtistEndpoint() {
        return ApiURL + ArtistURL + "/update";
    }

    public static String getAllArtistEndpoint() {
        return ApiURL + ArtistURL;
    }

    public static String getHistorySongEndpoint(String userId) {
        return ApiURL + SongURL + "/history/" + userId;
    }

    public static String getFindSongEndpoint() {
        return ApiFindSongURL;
    }

    public static String getPlaylistInteractionEndpoint(String userId) {
        return ApiURL + PlaylistURL + "/interactions/" + userId;
    }

    public static String getAlbumInteractionEndpoint(String userId) {
        return ApiURL + AlbumsURL + "/interactions/" + userId;
    }

    public static String getArtistInteractionEndpoint(String userId) {
        return ApiURL + ArtistURL + "/interactions/" + userId;
    }

    public static String getCountLiked(int type, int id) {
        return ApiURL + InteractionURL + "/count/" + type + "/" + id;
    }

    public static String getTotalTimePlaylist(int id) {
        return ApiURL + PlaylistURL + "/total_time/" + id;
    }

    public static String postDeleteInteraction() {
        return ApiURL + InteractionURL + "/remove_in/";
    }

    public static String postAddInteraction() {
        return ApiURL + InteractionURL + "/add_new";
    }
}
