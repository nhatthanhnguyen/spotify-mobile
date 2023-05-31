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
import com.ptit.spotify.dto.model.PlayList;
import com.ptit.spotify.dto.model.Song;
import com.ptit.spotify.itemdecorations.VerticalViewItemDecoration;
import com.ptit.spotify.utils.Constants;
import com.ptit.spotify.utils.OnItemPlaylistClickedListener;
import com.ptit.spotify.utils.OnItemSettingClickedListener;
import com.ptit.spotify.utils.Utils;

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
                        jsonBody.put("username", playList.getUser_id());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    String requestBody = jsonBody.toString();
                    JsonObjectRequest jsonObjectRequestUser = new JsonObjectRequest(
                            Request.Method.GET,
                            Constants.getAccountInfoEndpoint(),
                            new JSONObject(),
                            accountInfo -> {
                                Account account;
                                try {
                                    account = gson.fromJson(accountInfo.getString("account"), Account.class);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                PlaylistHeaderData data = new PlaylistHeaderData(
                                        playList.getName(),
                                        playList.getCover_img(),
                                        "",
                                        playList.getCover_img(),
                                        account.getUsername(),
                                        Utils.generateRandomNumberInRange(1000),
                                        items.length(),
                                        false);
                                playlistItems.add(data);
                                JsonObjectRequest jsonObjectSongRequest = new JsonObjectRequest(
                                        Request.Method.GET,
                                        Constants.getSongByPlaylistIdEndpoint(finalPlaylistId),
                                        null,
                                        response -> {
                                            Log.i("LOG_RESPONSE", String.valueOf(response));
                                            JSONArray songItems = response.optJSONArray("songs");
                                            if (songItems != null) {
                                                for (int i = 0; i < songItems.length(); i++) {
                                                    Song song = null;
                                                    try {
                                                        song = gson.fromJson(songItems.get(i).toString(), Song.class);
                                                    } catch (JSONException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                    final Artist[] artist = new Artist[1];
                                                    JsonObjectRequest jsonObjectArtistRequest = new JsonObjectRequest(
                                                            Request.Method.GET,
                                                            Constants.getArtistByIdEndpoint(String.valueOf(song.getArtist_id())),
                                                            null,
                                                            response1 -> {
                                                                Log.i("LOG_RESPONSE", String.valueOf(response1));
                                                                JSONArray artists = response1.optJSONArray("artists");
                                                                artist[0] = gson.fromJson(artists.opt(0).toString(), Artist.class);
                                                            }, error -> Log.e("LOG_RESPONSE", error.toString()));
                                                    requestQueue.add(jsonObjectArtistRequest);
                                                    final Album[] album = new Album[1];
                                                    JsonObjectRequest jsonObjectAlbumRequest = new JsonObjectRequest(
                                                            Request.Method.GET,
                                                            Constants.getAlbumsByIdEndpoint(String.valueOf(song.getAlbum_id())),
                                                            null,
                                                            response12 -> {
                                                                Log.i("LOG_RESPONSE", String.valueOf(response12));
                                                                JSONArray albumItems = response12.optJSONArray("albums");
                                                                album[0] = gson.fromJson(albumItems.opt(0).toString(), Album.class);
                                                            }, error -> Log.e("LOG_RESPONSE", error.toString()));
                                                    requestQueue.add(jsonObjectAlbumRequest);
                                                    PlaylistSongData songData = new PlaylistSongData(
                                                            song.getSong_id(),
                                                            song.getUrl(),
                                                            song.getName(),
                                                            song.getArtist_id(),
                                                            artist[0].getName(),
                                                            song.getAlbum_id(),
                                                            album[0].getName(),
                                                            song.getLength(),
                                                            false);
                                                    playlistItems.add(songData);
                                                }
                                                PlaylistAdapter adapter = new PlaylistAdapter(playlistItems, this);
                                                recyclerView.setAdapter(adapter);
                                            }
                                        }, error -> Log.e("LOG_RESPONSE", error.toString()));
                                requestQueue.add(jsonObjectSongRequest);
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
        SettingFragment settingFragment = new SettingFragment(items, new OnItemSettingClickedListener() {
            @Override
            public void onSettingItemClickedListener(Object data) {
                Toast.makeText(getContext(), data.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        settingFragment.show(getParentFragmentManager(), settingFragment.getTag());
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
        SettingFragment settingFragment = new SettingFragment(items, new OnItemSettingClickedListener() {
            @Override
            public void onSettingItemClickedListener(Object data) {
                SettingOptionData settingOptionData = (SettingOptionData) data;

            }
        });
        settingFragment.show(getParentFragmentManager(), settingFragment.getTag());
    }
}