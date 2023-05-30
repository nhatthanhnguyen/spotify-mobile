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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
import com.ptit.spotify.utils.OnItemAlbumClickedListener;
import com.ptit.spotify.utils.OnItemSettingClickedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AlbumFragment extends Fragment implements OnItemAlbumClickedListener {
    private static final String ALBUM_DATA = "Album";
    private List<Object> albumItems;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        String albumId = "";
        if (bundle != null) {
            albumId = String.valueOf(bundle.getInt(ALBUM_DATA, 0));
        }
        int spacing = getContext().getResources().getDimensionPixelSize(R.dimen.spacing_16);
        albumItems = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new VerticalViewItemDecoration(spacing));
        //TODO: truyền vào albumsid
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        // TODO DONE: LẤY INFO ALBUM
        String finalAlbumId = albumId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                Constants.getAlbumsByIdEndpoint(albumId),
                null,
                albumResponse -> {
                    Log.i("LOG_RESPONSE", String.valueOf(albumResponse));
                    Gson gson = new Gson();
                    JSONArray items = albumResponse.optJSONArray("albums");
                    if (items != null) {
                        Album album = null;
                        try {
                            album = gson.fromJson(items.get(0).toString(), Album.class);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        Album finalAlbum = album;
                        JsonObjectRequest jsonObjectArtistRequest = new JsonObjectRequest(
                                Request.Method.GET,
                                Constants.getArtistByIdEndpoint(String.valueOf(album.getArtist_id())),
                                null,
                                artistResponse -> {
                                    Log.i("LOG_RESPONSE", String.valueOf(artistResponse));
                                    Gson gson1 = new Gson();
                                    JSONArray itemArtists = artistResponse.optJSONArray("artists");
                                    if (itemArtists == null) {
                                        return;
                                    }
                                    Artist artist = null;
                                    try {
                                        artist = gson1.fromJson(itemArtists.get(0).toString(), Artist.class);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                    AlbumHeaderData headerData = new AlbumHeaderData(
                                            finalAlbum.getCover_img(),
                                            finalAlbum.getName(),
                                            artist.getName(),
                                            artist.getCoverImg(),
                                            "20-12-2022");
                                    albumItems.add(headerData);
                                    JsonObjectRequest jsonObjectSongRequest = new JsonObjectRequest(
                                            Request.Method.GET,
                                            Constants.getSongByAlbumIdEndpoint(finalAlbumId),
                                            null,
                                            responseSong -> {
                                                Log.i("LOG_RESPONSE", String.valueOf(responseSong));
                                                Gson gson2 = new Gson();
                                                JSONArray itemSongs = responseSong.optJSONArray("songs");
                                                if (itemSongs == null) return;
                                                for (int i = 0; i < itemSongs.length(); i++) {
                                                    Song song = null;
                                                    try {
                                                        song = gson.fromJson(itemSongs.get(i).toString(), Song.class);
                                                    } catch (JSONException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                    Song finalSong = song;
                                                    JsonObjectRequest jsonObjectRequestArtistSong = new JsonObjectRequest(
                                                            Request.Method.GET,
                                                            Constants.getArtistByIdEndpoint(String.valueOf(song.getArtist_id())),
                                                            new JSONObject(),
                                                            response -> {
                                                                Log.i("LOG_RESPONSE", String.valueOf(response));
                                                                Gson gson3 = new Gson();
                                                                JSONArray itemArtistSongs = response.optJSONArray("artists");
                                                                if (itemArtistSongs == null) return;
                                                                Artist artistSong = null;
                                                                try {
                                                                    artistSong = gson3.fromJson(itemArtistSongs.get(0).toString(), Artist.class);
                                                                } catch (JSONException e) {
                                                                    throw new RuntimeException(e);
                                                                }
                                                                AlbumSongData albumSongData = new AlbumSongData(
                                                                        String.valueOf(finalSong.getSong_id()),
                                                                        finalSong.getName(),
                                                                        "",
                                                                        artistSong.getName());
                                                                albumItems.add(albumSongData);
                                                            }, error -> Log.e("LOG_RESPONSE", error.toString()));
                                                    requestQueue.add(jsonObjectRequestArtistSong);
                                                }
                                                AlbumAdapter albumAdapter = new AlbumAdapter(albumItems, this);
                                                recyclerView.setAdapter(albumAdapter);
                                            }, error -> Log.e("LOG_RESPONSE", error.toString()));
                                    requestQueue.add(jsonObjectSongRequest);
                                }, error -> Log.e("LOG_RESPONSE", error.toString()));
                        requestQueue.add(jsonObjectArtistRequest);
                    }
                }, error -> Log.e("LOG_RESPONSE", error.toString()));
        requestQueue.add(jsonObjectRequest);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content, container, false);
    }

    private void addData(String albumId) {
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
