package com.ptit.spotify.fragments;

import static com.ptit.spotify.utils.ItemType.SETTING_DESTINATION_ADD_TO_PLAYLIST;
import static com.ptit.spotify.utils.ItemType.SETTING_DESTINATION_ARTIST;
import static com.ptit.spotify.utils.ItemType.SETTING_LIKE;
import static com.ptit.spotify.utils.ItemType.SETTING_LIKE_ALL_SONG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ptit.spotify.R;
import com.ptit.spotify.adapters.album.AlbumAdapter;
import com.ptit.spotify.dto.data.AlbumHeaderData;
import com.ptit.spotify.dto.data.AlbumSettingHeaderData;
import com.ptit.spotify.dto.data.AlbumSongData;
import com.ptit.spotify.dto.data.SettingOptionData;
import com.ptit.spotify.dto.model.Album;
import com.ptit.spotify.dto.model.Artist;
import com.ptit.spotify.dto.model.Song;
import com.ptit.spotify.itemdecorations.VerticalViewItemDecoration;
import com.ptit.spotify.utils.Constants;
import com.ptit.spotify.utils.HttpUtils;
import com.ptit.spotify.utils.OnItemAlbumClickedListener;
import com.ptit.spotify.utils.OnItemSettingClickedListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

public class AlbumFragment extends Fragment implements OnItemAlbumClickedListener {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int spacing = getContext().getResources().getDimensionPixelSize(R.dimen.spacing_16);
        List<AlbumHeaderData> albumItems = new ArrayList<>();
        List<AlbumSongData> songs = new ArrayList<>();
        //TODO: truyền vào albumsid
        String albumId = "";
        addData(albumItems, songs, albumId);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new VerticalViewItemDecoration(spacing));
        AlbumAdapter albumAdapter = new AlbumAdapter(albumItems, this);
        recyclerView.setAdapter(albumAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content, container, false);
    }

    private void addData(List<AlbumHeaderData> album, List<AlbumSongData> songInAlbums, String albumId) {
        // TODO DONE: LẤY INFO ALBUM
        JSONObject jsonBody = new JSONObject();
        final String mRequestBody = jsonBody.toString();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constants.getAlbumsByIdEndpoint(albumId), new JSONObject(), new Response.Listener<JSONObject>() {
            @SneakyThrows
            @Override
            public void onResponse(JSONObject response) {
                Log.i("LOG_RESPONSE", String.valueOf(response));
                Gson gson = new Gson();
                JSONArray items = response.optJSONArray("albums");
                if(items != null) {
                    for(int i = 0; i < items.length(); i++) {
                        Album ab = gson.fromJson(items.get(i).toString(), Album.class);
                        final String[] artistName = {""};
                        final String[] artistImg = {""};
                        JsonObjectRequest jsonObjectArtistRequest = new JsonObjectRequest(Constants.getArtistByIdEndpoint(String.valueOf(ab.getArtistID())), new JSONObject(), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("LOG_RESPONSE", String.valueOf(response));
                                Gson gson = new Gson();
                                Artist at = gson.fromJson(response.toString(), Artist.class);
                                artistName[0] = at.getName();
                                artistImg[0] = at.getCoverImg();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("LOG_RESPONSE", error.toString());
                            }
                        });
                        HttpUtils.getInstance(getContext()).getRequestQueue().add(jsonObjectArtistRequest);

                        AlbumHeaderData data = new AlbumHeaderData(ab.getCoverImg(), ab.getName(), artistName[0], artistImg[0], "20-12-2022");
                        album.add(data);
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
//        albumItems.add(new AlbumHeaderData(
//                "https://i.scdn.co/image/ab67616d0000b273b94f78cf2a6ac9c700ee2812",
//                "Saying Things",
//                "Emanuel Fremont",
//                "https://i.scdn.co/image/ab6761610000e5eb4de66b2170a9e8049a828d5f",
//                "2022",
//                true,
//                false,
//                false)
//        );
        // TODO DONE: LẤY SONG CỦA ALBUM
        JsonObjectRequest jsonObjectSongRequest = new JsonObjectRequest(Constants.getSongByAlbumIdEndpoint(albumId), new JSONObject(), new Response.Listener<JSONObject>() {
            @SneakyThrows
            @Override
            public void onResponse(JSONObject response) {
                Log.i("LOG_RESPONSE", String.valueOf(response));
                Gson gson = new Gson();
                JSONArray items = response.optJSONArray("songs");
                if(items != null) {
                    for(int i = 0; i < items.length(); i++) {
                        Song song = gson.fromJson(items.get(i).toString(), Song.class);
                        final String[] artistName = {""};
                        JsonObjectRequest jsonObjectArtistRequest = new JsonObjectRequest(Constants.getArtistByIdEndpoint(String.valueOf(song.getArtistID())), new JSONObject(), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("LOG_RESPONSE", String.valueOf(response));
                                Gson gson = new Gson();
                                Artist at = gson.fromJson(response.toString(), Artist.class);
                                artistName[0] = at.getName();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("LOG_RESPONSE", error.toString());
                            }
                        });
                        HttpUtils.getInstance(getContext()).getRequestQueue().add(jsonObjectArtistRequest);

                        AlbumSongData data = new AlbumSongData(String.valueOf(song.getSongID()), song.getName(), song.getUrl(), artistName[0]);
                        songInAlbums.add(data);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_RESPONSE", error.toString());
            }
        });
        HttpUtils.getInstance(getContext()).getRequestQueue().add(jsonObjectSongRequest);
//        albumItems.add(new AlbumSongData(
//                "song12",
//                "Saying Things",
//                "https://i.scdn.co/image/ab6761610000e5eb4de66b2170a9e8049a828d5f",
//                "Emanuel Fremont"));
    }

    @Override
    public void onBackButtonClickedListener() {
        getParentFragmentManager().popBackStack();
    }

    @Override
    public void onAlbumSettingClickedListener(AlbumHeaderData data) {
        List<Object> items = new ArrayList<>();
        items.add(new AlbumSettingHeaderData(data.getImageUrl(), data.getAlbumName(), data.getArtistName()));
        items.add(new SettingOptionData(R.drawable.ic_like_outlined, "Like", SETTING_LIKE));
        items.add(new SettingOptionData(R.drawable.ic_view_artist, "View Artist", SETTING_DESTINATION_ARTIST));
        items.add(new SettingOptionData(R.drawable.ic_add_to_playlist, "Add to playlist", SETTING_DESTINATION_ADD_TO_PLAYLIST));
        items.add(new SettingOptionData(R.drawable.ic_like_outlined, "Like all songs", SETTING_LIKE_ALL_SONG));
        SettingFragment settingFragment = new SettingFragment(items, new OnItemSettingClickedListener() {
            @Override
            public void onSettingItemClickedListener(Object data) {
                Toast.makeText(getContext(), data.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        settingFragment.show(getParentFragmentManager(), settingFragment.getTag());
    }
}
