package com.ptit.spotify.fragments;

import static com.ptit.spotify.utils.ItemType.SETTING_DESTINATION_ADD_TO_PLAYLIST;
import static com.ptit.spotify.utils.ItemType.SETTING_DESTINATION_ALBUM;
import static com.ptit.spotify.utils.ItemType.SETTING_DESTINATION_ARTIST;
import static com.ptit.spotify.utils.ItemType.SETTING_LIKE;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ptit.spotify.R;
import com.ptit.spotify.adapters.playlist.PlaylistAdapter;
import com.ptit.spotify.dto.data.PlaylistHeaderData;
import com.ptit.spotify.dto.data.PlaylistSettingHeaderData;
import com.ptit.spotify.dto.data.PlaylistSongData;
import com.ptit.spotify.dto.data.SettingOptionData;
import com.ptit.spotify.dto.data.SongSettingHeaderData;
import com.ptit.spotify.dto.model.Account;
import com.ptit.spotify.dto.model.Album;
import com.ptit.spotify.dto.model.Artist;
import com.ptit.spotify.dto.model.CountResponse;
import com.ptit.spotify.dto.model.PlayList;
import com.ptit.spotify.dto.model.Song;
import com.ptit.spotify.dto.model.TotalTimeResponse;
import com.ptit.spotify.helper.SessionManager;
import com.ptit.spotify.itemdecorations.VerticalViewItemDecoration;
import com.ptit.spotify.utils.Constants;
import com.ptit.spotify.utils.OnItemPlaylistClickedListener;
import com.ptit.spotify.utils.OnItemSettingClickedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PlaylistFragment extends Fragment implements OnItemPlaylistClickedListener {
    private static final String PLAYLIST_DATA = "Playlist";

    public PlaylistFragment() {
    }

    private String username;
    private String userId;
    private PlaylistAdapter playlistAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String playlistId = "";
        if (bundle != null) {
            playlistId = String.valueOf(bundle.getInt(PLAYLIST_DATA, 0));
        }
        SessionManager session = new SessionManager(getContext());
        username = session.getUsername();
        userId = String.valueOf(session.getUserId());
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        int spacing = getContext().getResources().getDimensionPixelSize(R.dimen.spacing_16);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new VerticalViewItemDecoration(spacing));
        List<Object> playlistItems = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        // TODO DONE: LẤY THÔNG TIN CỦA PLAYLIST
        String finalPlaylistId = playlistId;
        JsonObjectRequest jsonObjectPlaylistRequest = new JsonObjectRequest(
                Request.Method.GET,
                Constants.getPlaylistByIdEndpoint(playlistId), null,
                playlistResponse -> {
                    Log.i("LOG_RESPONSE", String.valueOf(playlistResponse));
                    Gson gson = new Gson();
                    JSONArray items = playlistResponse.optJSONArray("play_lists");
                    if (items == null) return;
                    PlayList playList;
                    try {
                        playList = gson.fromJson(items.get(0).toString(), PlayList.class);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    JSONObject jsonBody = new JSONObject();
                    try {
                        jsonBody.put("user_id", playList.getUser_id());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    String requestBody = jsonBody.toString();
                    JsonObjectRequest jsonObjectRequestUser = new JsonObjectRequest(
                            Request.Method.POST,
                            Constants.getAccountInfoEndpoint(),
                            new JSONObject(),
                            accountInfo -> {
                                Account account;
                                try {
                                    account = gson.fromJson(accountInfo.getString("account"), Account.class);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                JsonObjectRequest jsonNumberLikes = new JsonObjectRequest(
                                        Request.Method.GET,
                                        Constants.getCountLiked(1, playList.getPlay_list_id()),
                                        null,
                                        likes -> {
                                            JsonObjectRequest jsonTotalLengthRequest = new JsonObjectRequest(
                                                    Request.Method.GET,
                                                    Constants.getTotalTimePlaylist(playList.getPlay_list_id()),
                                                    null,
                                                    totalLengthResponse -> {
                                                        TotalTimeResponse totalTimeResponse = gson.fromJson(totalLengthResponse.toString(), TotalTimeResponse.class);
                                                        CountResponse countResponse = gson.fromJson(likes.toString(), CountResponse.class);
                                                        JsonObjectRequest jsonPlaylistLikeRequest = new JsonObjectRequest(
                                                                Request.Method.GET,
                                                                Constants.getPlaylistInteractionEndpoint(userId),
                                                                null,
                                                                playlistLikeResponse -> {
                                                                    boolean playlistLiked = true;
                                                                    JSONArray playlistLikes = playlistLikeResponse.optJSONArray("play_lists");
                                                                    if (playlistLikes == null)
                                                                        playlistLiked = false;
                                                                    PlaylistHeaderData data = new PlaylistHeaderData(
                                                                            String.valueOf(playList.getPlay_list_id()),
                                                                            playList.getName(),
                                                                            playList.getCover_img(),
                                                                            "",
                                                                            "",
                                                                            account.getUsername(),
                                                                            countResponse.getCount(),
                                                                            totalTimeResponse.getTotal_time(),
                                                                            playlistLiked);
                                                                    playlistItems.add(data);
                                                                    JsonObjectRequest jsonObjectSongRequest = new JsonObjectRequest(
                                                                            Request.Method.GET,
                                                                            Constants.getSongByPlaylistIdEndpoint(finalPlaylistId),
                                                                            null,
                                                                            songsResponse -> {
                                                                                Log.i("LOG_RESPONSE", String.valueOf(songsResponse));
                                                                                JSONArray songItems = songsResponse.optJSONArray("songs");
                                                                                if (songItems == null)
                                                                                    return;
                                                                                if (songItems.length() == 0) {
                                                                                    PlaylistAdapter adapter = new PlaylistAdapter(playlistItems, this);
                                                                                    recyclerView.setAdapter(adapter);
                                                                                    return;
                                                                                }
                                                                                for (int i = 0; i < songItems.length(); i++) {
                                                                                    Song song = null;
                                                                                    try {
                                                                                        song = gson.fromJson(songItems.get(i).toString(), Song.class);
                                                                                    } catch (
                                                                                            JSONException e) {
                                                                                        throw new RuntimeException(e);
                                                                                    }
                                                                                    Song finalSong = song;
                                                                                    JsonObjectRequest jsonSongArtistRequest = new JsonObjectRequest(
                                                                                            Request.Method.GET,
                                                                                            Constants.getArtistByIdEndpoint(String.valueOf(song.getArtist_id())),
                                                                                            null,
                                                                                            artistResponse -> {
                                                                                                Log.i("LOG_RESPONSE", String.valueOf(artistResponse));
                                                                                                JSONArray artistItems = artistResponse.optJSONArray("artists");
                                                                                                if (artistItems == null)
                                                                                                    return;
                                                                                                Artist artistSong = null;
                                                                                                try {
                                                                                                    artistSong = gson.fromJson(artistItems.get(0).toString(), Artist.class);
                                                                                                } catch (
                                                                                                        JSONException e) {
                                                                                                    throw new RuntimeException(e);
                                                                                                }
                                                                                                Artist finalArtistSong = artistSong;
                                                                                                JsonObjectRequest jsonAlbumSongRequest = new JsonObjectRequest(
                                                                                                        Request.Method.GET,
                                                                                                        Constants.getAlbumsByIdEndpoint(String.valueOf(finalSong.getAlbum_id())),
                                                                                                        null,
                                                                                                        albumResponse -> {
                                                                                                            Log.i("LOG RESPONSE", String.valueOf(albumResponse));
                                                                                                            JSONArray albumItems = albumResponse.optJSONArray("albums");
                                                                                                            if (albumItems == null)
                                                                                                                return;
                                                                                                            Album albumSong = null;
                                                                                                            try {
                                                                                                                albumSong = gson.fromJson(albumItems.get(0).toString(), Album.class);
                                                                                                            } catch (
                                                                                                                    JSONException e) {
                                                                                                                throw new RuntimeException(e);
                                                                                                            }
                                                                                                            Album finalAlbumSong = albumSong;
                                                                                                            JsonObjectRequest jsonSongLikedRequest = new JsonObjectRequest(
                                                                                                                    Request.Method.GET,
                                                                                                                    Constants.getSongInteractionEndpoint(userId, String.valueOf(finalSong.getSong_id())),
                                                                                                                    null,
                                                                                                                    songLikesResponse -> {
                                                                                                                        boolean songLiked = true;
                                                                                                                        Log.i("LOG RESPONSE", songLikesResponse.toString());
                                                                                                                        JSONArray songLikes = songLikesResponse.optJSONArray("songs");
                                                                                                                        if (songLikes == null) songLiked = false;
                                                                                                                        PlaylistSongData songData = new PlaylistSongData(
                                                                                                                                finalSong.getSong_id(),
                                                                                                                                finalSong.getCover_img(),
                                                                                                                                finalSong.getName(),
                                                                                                                                finalSong.getArtist_id(),
                                                                                                                                finalArtistSong.getName(),
                                                                                                                                finalSong.getAlbum_id(),
                                                                                                                                finalAlbumSong.getName(),
                                                                                                                                finalSong.getLength(),
                                                                                                                                songLiked);
                                                                                                                        playlistItems.add(songData);
                                                                                                                        playlistAdapter = new PlaylistAdapter(playlistItems, this);
                                                                                                                        recyclerView.setAdapter(playlistAdapter);
                                                                                                                    },
                                                                                                                    error -> Log.e("LOG RESPONSE", error.toString())
                                                                                                            );
                                                                                                            requestQueue.add(jsonSongLikedRequest);
                                                                                                        },
                                                                                                        error -> Log.e("LOG RESPONSE", error.toString())
                                                                                                );
                                                                                                requestQueue.add(jsonAlbumSongRequest);
                                                                                            },
                                                                                            error -> Log.e("LOG RESPONSE", error.toString())
                                                                                    );
                                                                                    requestQueue.add(jsonSongArtistRequest);
                                                                                }
                                                                            }, error -> Log.e("LOG_RESPONSE", error.toString()));
                                                                    requestQueue.add(jsonObjectSongRequest);
                                                                },
                                                                error -> Log.e("LOG RESPONSE", error.toString())
                                                        );
                                                        requestQueue.add(jsonPlaylistLikeRequest);
                                                    },
                                                    error -> Log.e("LOG RESPONSE", error.toString())
                                            );
                                            requestQueue.add(jsonTotalLengthRequest);
                                        },
                                        error -> Log.e("LOG RESPONSE", error.toString())
                                );
                                requestQueue.add(jsonNumberLikes);
                            },
                            error -> Log.e("LOG_RESPONSE", error.toString())
                    ) {
                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }

                        @Override
                        public byte[] getBody() {
                            return requestBody.getBytes(StandardCharsets.UTF_8);
                        }
                    };
                    requestQueue.add(jsonObjectRequestUser);
                }, error -> Log.e("LOG_RESPONSE", error.toString()));
        requestQueue.add(jsonObjectPlaylistRequest);
        return view;
    }

    private void addItems(List<Object> playlistItems, String playlistId) {
//        playlistItems.add(new PlaylistHeaderData(
//                "Peaceful Piano",
//                "https://i.scdn.co/image/ab67706f00000002ca5a7517156021292e5663a6",
//                "Peaceful piano to help you slow down, breathe, and relax.",
//                "https://i.scdn.co/image/ab67757000003b8255c25988a6ac314394d3fbf5",
//                "Spotify",
//                7035021,
//                10000,
//                true,
//                false));
//        // TODO DONE: LẤY TOÀN BỘ BÀI HÁT CỦA PLAYLIST
//        playlistItems.add(new PlaylistSongData("https://i.scdn.co/image/ab67616d0000485128ccaf8cb23d857cb9361ec4",
//                "Tjärnheden",
//                "Farsjön",
//                "Fjäderlätt",
//                false,
//                false));
//        playlistItems.add(new PlaylistSongData("https://i.scdn.co/image/ab67616d00004851ba1332de8185cce3a9490e74",
//                "Quand vous souriez",
//                "Libor Kolman",
//                "Quand vous souriez",
//                false,
//                false));
//        playlistItems.add(new PlaylistSongData("https://i.scdn.co/image/ab67616d0000485147b70771cb7375cd30ceec54",
//                "Allena",
//                "M. Ljungström",
//                "Nostalgia",
//                true,
//                true));
    }

    @Override
    public void onPlaylistSettingClickedListener(PlaylistHeaderData data) {
        List<Object> items = new ArrayList<>();
        items.add(new PlaylistSettingHeaderData(data.getPlaylistImageUrl(), data.getPlaylistName(),
                data.getUserCreatedName()));
        items.add(new SettingOptionData(
                R.drawable.ic_like_outlined,
                "Like",
                SETTING_LIKE));
        items.add(new SettingOptionData(
                R.drawable.ic_add_to_playlist,
                "Add to another playlist",
                SETTING_DESTINATION_ADD_TO_PLAYLIST));
        SettingFragment settingFragment = new SettingFragment(items, data, new OnItemSettingClickedListener() {
            @Override
            public void onSettingItemClickedListener(Object headerData, Object data) {
                Toast.makeText(getContext(), data.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        settingFragment.show(getParentFragmentManager(), settingFragment.getTag());
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onLikeButtonClickedListener(PlaylistHeaderData data) {
        JSONObject request = new JSONObject();
        try {
            request.put("user_id", Integer.parseInt(userId));
            request.put("object_id", Integer.parseInt(data.getId()));
            request.put("object_type", 1);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String requestBody = request.toString();
        if (data.isLiked()) {
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            JsonObjectRequest jsonDeleteInteraction = new JsonObjectRequest(
                    Request.Method.POST,
                    Constants.postDeleteInteraction(),
                    new JSONObject(),
                    response -> {
                        Log.i("LOG RESPONSE", response.toString());
                        data.setLiked(false);
                        playlistAdapter.notifyDataSetChanged();
                    },
                    error -> Log.e("LOG RESPONSE", error.toString())
            ) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() {
                    return requestBody.getBytes(StandardCharsets.UTF_8);
                }
            };
            requestQueue.add(jsonDeleteInteraction);
        } else {
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            JsonObjectRequest jsonAddInteraction = new JsonObjectRequest(
                    Request.Method.POST,
                    Constants.postAddInteraction(),
                    new JSONObject(),
                    response -> {
                        Log.i("LOG RESPONSE", response.toString());
                        data.setLiked(true);
                        playlistAdapter.notifyDataSetChanged();
                    },
                    error -> Log.e("LOG RESPONSE", error.toString())
            ) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() {
                    return requestBody.getBytes(StandardCharsets.UTF_8);
                }
            };
            requestQueue.add(jsonAddInteraction);
        }
    }

    @Override
    public void onBackButtonClickedListener() {
        getParentFragmentManager().popBackStack();
    }

    @Override
    public void onSearchClickedListener() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        PlaylistSearchFragment playlistSearchFragment = new PlaylistSearchFragment();
        transaction.replace(R.id.fragment_container, playlistSearchFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onSongClickedListener(PlaylistSongData data) {

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
                SettingOptionData settingOptionData = (SettingOptionData) data;

            }
        });
        settingFragment.show(getParentFragmentManager(), settingFragment.getTag());
    }
}