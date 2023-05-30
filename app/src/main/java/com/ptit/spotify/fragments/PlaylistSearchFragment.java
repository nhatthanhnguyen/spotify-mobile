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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ptit.spotify.R;
import com.ptit.spotify.adapters.playlist.PlaylistSearchAdapter;
import com.ptit.spotify.dto.data.PlaylistSongData;
import com.ptit.spotify.dto.data.SettingOptionData;
import com.ptit.spotify.dto.data.SongSettingHeaderData;
import com.ptit.spotify.dto.model.Song;
import com.ptit.spotify.itemdecorations.VerticalViewItemDecoration;
import com.ptit.spotify.utils.Constants;
import com.ptit.spotify.utils.HttpUtils;
import com.ptit.spotify.utils.OnItemPlaylistSearchClickedListener;
import com.ptit.spotify.utils.OnItemSettingClickedListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaylistSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaylistSearchFragment extends Fragment implements OnItemPlaylistSearchClickedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlaylistSearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlaylistSearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaylistSearchFragment newInstance(String param1, String param2) {
        PlaylistSearchFragment fragment = new PlaylistSearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist_search, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constants.getSongByPlaylistIdEndpoint(id), new JSONObject(), new Response.Listener<JSONObject>() {
            @SneakyThrows
            @Override
            public void onResponse(JSONObject response) {
                Log.i("LOG_RESPONSE", String.valueOf(response));
                Gson gson = new Gson();
                JSONArray items = response.optJSONArray("songs");
                if (items != null) {
                    for (int i = 0; i < items.length(); i++) {
                        Song song = gson.fromJson(items.get(i).toString(), Song.class);
                        boolean liked = false;
                        boolean downloaded = false;
                        final String[] albumName = {""};
                        final String[] artistName = {""};
                        JsonObjectRequest jsonObjectAlbumRequest = new JsonObjectRequest(Constants.getAlbumsByIdEndpoint(String.valueOf(song.getAlbum_id())), new JSONObject(), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("LOG_RESPONSE", String.valueOf(response));
                                Gson gson = new Gson();
                                albumName[0] = gson.fromJson(items.toString(), Song.class).getName();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("LOG_RESPONSE", error.toString());
                            }
                        });
                        HttpUtils.getInstance(getContext()).getRequestQueue().add(jsonObjectAlbumRequest);

                        JsonObjectRequest jsonObjectArtistRequest = new JsonObjectRequest(Constants.getArtistByIdEndpoint(String.valueOf(song.getArtist_id())), new JSONObject(), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("LOG_RESPONSE", String.valueOf(response));
                                Gson gson = new Gson();
                                artistName[0] = gson.fromJson(items.toString(), Song.class).getName();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("LOG_RESPONSE", error.toString());
                            }
                        });
                        HttpUtils.getInstance(getContext()).getRequestQueue().add(jsonObjectArtistRequest);

                        JsonObjectRequest jsonObjectLikeRequest = new JsonObjectRequest(Constants.getSongInteractionEndpoint(String.valueOf(song.getSong_id())), new JSONObject(), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("LOG_RESPONSE", String.valueOf(response));
                                Gson gson = new Gson();
                                artistName[0] = gson.fromJson(items.toString(), Song.class).getName();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("LOG_RESPONSE", error.toString());
                            }
                        });
                        HttpUtils.getInstance(getContext()).getRequestQueue().add(jsonObjectLikeRequest);

                        PlaylistSongData data = new PlaylistSongData(
                                song.getSong_id(),
                                song.getUrl(),
                                song.getName(),
                                song.getArtist_id(),
                                artistName[0],
                                song.getAlbum_id(),
                                artistName[0],
                                song.getLength(),
                                false);
                        playlistSongDataList.add(data);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_RESPONSE", error.toString());
            }
        });
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
        SettingFragment settingFragment = new SettingFragment(items, new OnItemSettingClickedListener() {
            @Override
            public void onSettingItemClickedListener(Object data) {
                Toast.makeText(getContext(), data.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        settingFragment.show(getParentFragmentManager(), settingFragment.getTag());
    }
}