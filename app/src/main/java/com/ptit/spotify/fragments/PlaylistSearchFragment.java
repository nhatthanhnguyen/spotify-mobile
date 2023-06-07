package com.ptit.spotify.fragments;

import static com.ptit.spotify.utils.ItemType.SETTING_DESTINATION_ADD_TO_PLAYLIST;
import static com.ptit.spotify.utils.ItemType.SETTING_DESTINATION_ALBUM;
import static com.ptit.spotify.utils.ItemType.SETTING_DESTINATION_ARTIST;
import static com.ptit.spotify.utils.ItemType.SETTING_LIKE;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ptit.spotify.R;
import com.ptit.spotify.adapters.playlist.PlaylistSearchAdapter;
import com.ptit.spotify.dto.data.PlaylistSongData;
import com.ptit.spotify.dto.data.SettingOptionData;
import com.ptit.spotify.dto.data.SongSettingHeaderData;
import com.ptit.spotify.dto.model.Album;
import com.ptit.spotify.dto.model.Artist;
import com.ptit.spotify.dto.model.Song;
import com.ptit.spotify.helper.SessionManager;
import com.ptit.spotify.itemdecorations.VerticalViewItemDecoration;
import com.ptit.spotify.utils.Constants;
import com.ptit.spotify.utils.HttpUtils;
import com.ptit.spotify.utils.OnItemPlaylistSearchClickedListener;
import com.ptit.spotify.utils.OnItemSettingClickedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

public class PlaylistSearchFragment extends Fragment implements OnItemPlaylistSearchClickedListener {
    public PlaylistSearchFragment() {
        // Required empty public constructor
    }
    private String userId;
    private String username;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist_search, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        SessionManager sessionManager = new SessionManager(getContext());
        userId = String.valueOf(sessionManager.getUserId());
        username = sessionManager.getUsername();
        ImageButton buttonBack = view.findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> {
            onBackButtonClickedListener();
        });
        int spacing = getContext().getResources().getDimensionPixelSize(R.dimen.spacing_16);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new VerticalViewItemDecoration(spacing));
        List<PlaylistSongData> items = new ArrayList<>();
        //TODO: truyền id cho playlist
        String playlistId = "";
        getPlaylistSongs(items, playlistId);
        PlaylistSearchAdapter playlistAdapter = new PlaylistSearchAdapter(items, this);
        recyclerView.setAdapter(playlistAdapter);
        return view;
    }

    private void getPlaylistSongs(List<PlaylistSongData> playlistSongDataList, String id) {
        // TODO DONE: LẤY TOÀN BỘ BÀI HÁT CỦA PLAYLIST
        JSONObject jsonBody = new JSONObject();
        final String mRequestBody = jsonBody.toString();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                Constants.getSongByPlaylistIdEndpoint(id),
                null,
                songsResponse -> {
                    Log.i("LOG_RESPONSE", String.valueOf(songsResponse));
                    Gson gson = new Gson();
                    JSONArray songs = songsResponse.optJSONArray("songs");
                    if (songs != null) {
                        for (int i = 0; i < songs.length(); i++) {
                            Song song = null;
                            try {
                                song = gson.fromJson(songs.get(i).toString(), Song.class);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            Song finalSong = song;
                            JsonObjectRequest jsonObjectAlbumRequest = new JsonObjectRequest(
                                    Request.Method.GET,
                                    Constants.getAlbumsByIdEndpoint(String.valueOf(song.getAlbum_id())),
                                    null,
                                    albumSongResponse -> {
                                        Log.i("LOG_RESPONSE", String.valueOf(albumSongResponse));
                                        JSONArray albumSongs = albumSongResponse.optJSONArray("albums");
                                        Album album = null;
                                        try {
                                            album = gson.fromJson(albumSongs.get(0).toString(), Album.class);
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                        Album finalAlbum = album;
                                        JsonObjectRequest jsonObjectArtistRequest = new JsonObjectRequest(
                                                Request.Method.GET,
                                                Constants.getArtistByIdEndpoint(String.valueOf(finalSong.getArtist_id())),
                                                null,
                                                artistSongResponse -> {
                                                    Log.i("LOG_RESPONSE", String.valueOf(artistSongResponse));
                                                    JSONArray artistSongs = artistSongResponse.optJSONArray("artists");
                                                    Artist artist = null;
                                                    try {
                                                        artist = gson.fromJson(artistSongs.get(0).toString(), Artist.class);
                                                    } catch (JSONException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                    Artist finalArtist = artist;
                                                    JsonObjectRequest jsonObjectLikeRequest = new JsonObjectRequest(
                                                            Request.Method.GET,
                                                            Constants.getSongInteractionEndpoint(userId, String.valueOf(finalSong.getSong_id())),
                                                            null,
                                                            response1 -> {
                                                                boolean liked = true;
                                                                Log.i("LOG_RESPONSE", String.valueOf(response1));
                                                                JSONArray songsLike = response1.optJSONArray("songs");
                                                                if (songsLike == null) liked = false;
                                                                PlaylistSongData data = new PlaylistSongData(
                                                                        finalSong.getSong_id(),
                                                                        finalSong.getUrl(),
                                                                        finalSong.getName(),
                                                                        finalSong.getArtist_id(),
                                                                        finalArtist.getName(),
                                                                        finalSong.getAlbum_id(),
                                                                        finalAlbum.getName(),
                                                                        finalSong.getLength(),
                                                                        liked);
                                                                playlistSongDataList.add(data);
                                                            }, error -> Log.e("LOG_RESPONSE", error.toString()));
                                                    HttpUtils.getInstance(getContext()).getRequestQueue().add(jsonObjectLikeRequest);
                                                }, error -> Log.e("LOG_RESPONSE", error.toString()));
                                        HttpUtils.getInstance(getContext()).getRequestQueue().add(jsonObjectArtistRequest);
                                    }, error -> Log.e("LOG_RESPONSE", error.toString()));
                            HttpUtils.getInstance(getContext()).getRequestQueue().add(jsonObjectAlbumRequest);
                        }
                    }
                }, error -> Log.e("LOG_RESPONSE", error.toString()));
        HttpUtils.getInstance(getContext()).getRequestQueue().add(jsonObjectRequest);
//        items.add(new PlaylistSongData("https://i.scdn.co/image/ab67616d0000485128ccaf8cb23d857cb9361ec4",
//                "Tjärnheden",
//                "Farsjön",
//                "Fjäderlätt",
//                false,
//                false));
//        items.add(new PlaylistSongData("https://i.scdn.co/image/ab67616d00004851ba1332de8185cce3a9490e74",
//                "Quand vous souriez",
//                "Libor Kolman",
//                "Quand vous souriez",
//                false,
//                false));
//        items.add(new PlaylistSongData("https://i.scdn.co/image/ab67616d0000485147b70771cb7375cd30ceec54",
//                "Allena",
//                "M. Ljungström",
//                "Nostalgia",
//                true,
//                true));
    }

    @Override
    public void onBackButtonClickedListener() {
        getParentFragmentManager().popBackStack();
    }

    @Override
    public void onSongSettingClickedListener(PlaylistSongData data) {
        List<Object> items = new ArrayList<>();
        items.add(new SongSettingHeaderData(
                data.getSongUrl(),
                data.getSongName(),
                data.getArtistName(),
                data.getAlbumName()));
        items.add(new SettingOptionData(
                R.drawable.ic_like_outlined,
                "Like",
                SETTING_LIKE
        ));
        items.add(new SettingOptionData(
                R.drawable.ic_add_to_playlist,
                "Add to playlist",
                SETTING_DESTINATION_ADD_TO_PLAYLIST
        ));
        items.add(new SettingOptionData(
                R.drawable.ic_view_album,
                "View album",
                SETTING_DESTINATION_ALBUM
        ));
        items.add(new SettingOptionData(
                R.drawable.ic_view_artist,
                "View artist",
                SETTING_DESTINATION_ARTIST
        ));
        SettingFragment settingFragment = new SettingFragment(items, data, new OnItemSettingClickedListener() {
            @Override
            public void onSettingItemClickedListener(Object headerData, Object data) {
                Toast.makeText(getContext(), data.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        settingFragment.show(getParentFragmentManager(), settingFragment.getTag());
    }
}