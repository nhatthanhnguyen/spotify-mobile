package com.ptit.spotify.fragments;

import static com.ptit.spotify.utils.ItemType.ALBUM;
import static com.ptit.spotify.utils.ItemType.ARTIST;
import static com.ptit.spotify.utils.ItemType.PLAYLIST;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.ptit.spotify.adapters.home.HomeAdapter;
import com.ptit.spotify.dto.data.HomeCardData;
import com.ptit.spotify.dto.data.HomeGreetingData;
import com.ptit.spotify.dto.data.HomeSectionData;
import com.ptit.spotify.dto.model.Album;
import com.ptit.spotify.dto.model.Artist;
import com.ptit.spotify.dto.model.PlayList;
import com.ptit.spotify.helper.SessionManager;
import com.ptit.spotify.utils.Constants;
import com.ptit.spotify.utils.ItemType;
import com.ptit.spotify.utils.OnItemHomeClickedListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements OnItemHomeClickedListener {
    private static final String ALBUM_DATA = "Album";
    private static final String ARTIST_DATA = "Artist";
    private static final String PLAYLIST_DATA = "Playlist";
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private SessionManager session;
    private List<Object> dataItems;

    public HomeFragment() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dataItems.clear();
        dataItems = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        session = new SessionManager(getContext());
        dataItems = new ArrayList<>();
        String userId = String.valueOf(session.getUserId());
        dataItems.add(new HomeGreetingData());
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectAlbumRequest = new JsonObjectRequest(
                Request.Method.GET,
                Constants.getAllAlbumsEndpoint(),
                null,
                response -> {
                    Log.i("LOG_RESPONSE", String.valueOf(response));
                    Gson gson = new Gson();
                    JSONArray items = response.optJSONArray("albums");
                    if (items != null) {
                        List<HomeCardData> homeCardDataAlbum = new ArrayList<>();
                        for (int i = 0; i < items.length(); i++) {
                            Album at = null;
                            try {
                                at = gson.fromJson(items.get(i).toString(), Album.class);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            HomeCardData data = new HomeCardData(
                                    String.valueOf(at.getArtist_id()),
                                    at.getCover_img(),
                                    at.getName(),
                                    "",
                                    ALBUM
                            );
                            homeCardDataAlbum.add(data);
                        }
                        Log.d("LOG_ALBUMS", String.valueOf(homeCardDataAlbum.size()));

                        // Lấy dữ liệu Playlist
                        JsonObjectRequest jsonObjectPlaylistRequest = new JsonObjectRequest(
                                Request.Method.GET,
                                Constants.getAllPlaylistEndpoint(),
                                null,
                                response1 -> {
                                    Log.i("LOG_RESPONSE", String.valueOf(response1));
                                    JSONArray items1 = response1.optJSONArray("play_lists");
                                    if (items1 != null) {
                                        List<HomeCardData> homeCardDataPlaylist = new ArrayList<>();
                                        for (int i = 0; i < items1.length(); i++) {
                                            PlayList playlist = null;
                                            try {
                                                playlist = gson.fromJson(items1.get(i).toString(), PlayList.class);
                                            } catch (JSONException e) {
                                                throw new RuntimeException(e);
                                            }
                                            HomeCardData data = new HomeCardData(
                                                    String.valueOf(playlist.getPlay_list_id()),
                                                    playlist.getCover_img(),
                                                    playlist.getName(),
                                                    "",
                                                    PLAYLIST
                                            );
                                            homeCardDataPlaylist.add(data);
                                        }
                                        Log.d("LOG_PLAYLIST", String.valueOf(homeCardDataPlaylist.size()));

                                        // Lấy dữ liệu Artist
                                        JsonObjectRequest jsonObjectArtistRequest = new JsonObjectRequest(
                                                Request.Method.GET,
                                                Constants.getAllArtistEndpoint(),
                                                null,
                                                response2 -> {
                                                    Log.i("LOG_RESPONSE", String.valueOf(response2));
                                                    JSONArray items2 = response2.optJSONArray("artists");
                                                    if (items2 != null) {
                                                        List<HomeCardData> homeCardDataArtists = new ArrayList<>();
                                                        for (int i = 0; i < items2.length(); i++) {
                                                            Artist artist = null;
                                                            try {
                                                                artist = gson.fromJson(items2.get(i).toString(), Artist.class);
                                                            } catch (JSONException e) {
                                                                throw new RuntimeException(e);
                                                            }
                                                            HomeCardData data = new HomeCardData(
                                                                    String.valueOf(artist.getArtist_id()),
                                                                    artist.getCoverImg(),
                                                                    artist.getName(),
                                                                    artist.getDescription(),
                                                                    ARTIST
                                                            );
                                                            homeCardDataArtists.add(data);
                                                        }
                                                        Log.d("LOG_ARTIST", String.valueOf(homeCardDataArtists.size()));

                                                        // Sau khi có dữ liệu từ tất cả các yêu cầu mạng, thêm vào dataItems
                                                        dataItems.add(new HomeSectionData("Albums", homeCardDataAlbum));
                                                        dataItems.add(new HomeSectionData("Playlist", homeCardDataPlaylist));
                                                        dataItems.add(new HomeSectionData("Artist", homeCardDataArtists));
                                                        recyclerView = view.findViewById(R.id.recyclerView);
                                                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                                        homeAdapter = new HomeAdapter(dataItems, this);
                                                        recyclerView.setAdapter(homeAdapter);
                                                    }
                                                },
                                                error -> Log.e("LOG_RESPONSE", error.toString())
                                        );
                                        requestQueue.add(jsonObjectArtistRequest);
                                    }
                                },
                                error -> Log.e("LOG_RESPONSE", error.toString())
                        );
                        requestQueue.add(jsonObjectPlaylistRequest);
                    }
                },
                error -> Log.e("LOG_RESPONSE", error.toString())
        );
        requestQueue.add(jsonObjectAlbumRequest);
        return view;
    }

    void addData(List<Object> dataItems, String userId) {
        dataItems.add(new HomeGreetingData());
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        /*List<HomeCardData> homeCardDataAlbum = new ArrayList<>();
        // TODO DONE: LẤY TOÀN BỘ ALBUM
        JsonObjectRequest jsonObjectAlbumRequest = new JsonObjectRequest(
                Request.Method.GET,
                Constants.getAllAlbumsEndpoint(),
                null,
                response -> {
                    Log.i("LOG_RESPONSE", String.valueOf(response));
                    Gson gson = new Gson();
                    JSONArray items = response.optJSONArray("albums");
                    if (items != null) {
                        for (int i = 0; i < items.length(); i++) {
                            Album at = null;
                            try {
                                at = gson.fromJson(items.get(i).toString(), Album.class);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            HomeCardData data = new HomeCardData(
                                    String.valueOf(at.getArtist_id()),
                                    at.getCover_img(),
                                    at.getName(),
                                    "",
                                    ALBUM
                            );
                            homeCardDataAlbum.add(data);
                        }
                        Log.d("LOG_ALBUMS", String.valueOf(homeCardDataAlbum.size()));
                    }
                }, error -> Log.e("LOG_RESPONSE", error.toString()));
        requestQueue.add(jsonObjectAlbumRequest);
        dataItems.add(new HomeSectionData("Albums", homeCardDataAlbum));

        // TODO DONE: LẤY TOÀN BỘ PLAYLIST
        List<HomeCardData> homeCardDataPlaylist = new ArrayList<>();
        JsonObjectRequest jsonObjectPlaylistRequest = new JsonObjectRequest(Request.Method.GET, Constants.getAllPlaylistEndpoint(), null, response -> {
            Log.i("LOG_RESPONSE", String.valueOf(response));
            Gson gson = new Gson();
            JSONArray items = response.optJSONArray("play_lists");
            if (items != null) {
                for (int i = 0; i < items.length(); i++) {
                    PlayList playlist = null;
                    try {
                        playlist = gson.fromJson(items.get(i).toString(), PlayList.class);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    HomeCardData data = new HomeCardData(
                            String.valueOf(playlist.getPlay_list_id()),
                            playlist.getCover_img(),
                            playlist.getName(),
                            "",
                            PLAYLIST
                    );
                    homeCardDataPlaylist.add(data);
                }
                Log.d("LOG_PLAYLIST", String.valueOf(homeCardDataPlaylist.size()));
            }
        }, error -> Log.e("LOG_RESPONSE", error.toString()));
        requestQueue.add(jsonObjectPlaylistRequest);
        dataItems.add(new HomeSectionData("Playlist", homeCardDataPlaylist));

        // TODO DONE: LẤY TOÀN BỘ NGHỆ SĨ
        List<HomeCardData> homeCardDataArtists = new ArrayList<>();
        JsonObjectRequest jsonObjectArtistRequest = new JsonObjectRequest(
                Request.Method.GET,
                Constants.getAllArtistEndpoint(),
                null,
                response -> {
                    Log.i("LOG_RESPONSE", String.valueOf(response));
                    Gson gson = new Gson();
                    JSONArray items = response.optJSONArray("artists");
                    if (items != null) {
                        for (int i = 0; i < items.length(); i++) {
                            Artist artist = null;
                            try {
                                artist = gson.fromJson(items.get(i).toString(), Artist.class);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            HomeCardData data = new HomeCardData(
                                    String.valueOf(artist.getArtist_id()),
                                    artist.getCoverImg(),
                                    artist.getName(),
                                    artist.getDescription(),
                                    ARTIST
                            );
                            homeCardDataArtists.add(data);
                        }
                        Log.d("LOG_ARTIST", String.valueOf(homeCardDataArtists.size()));
                    }
                }, error -> Log.e("LOG_RESPONSE", error.toString()));
        requestQueue.add(jsonObjectArtistRequest);
        dataItems.add(new HomeSectionData("Artist", homeCardDataArtists));*/
        JsonObjectRequest jsonObjectAlbumRequest = new JsonObjectRequest(
                Request.Method.GET,
                Constants.getAllAlbumsEndpoint(),
                null,
                response -> {
                    Log.i("LOG_RESPONSE", String.valueOf(response));
                    Gson gson = new Gson();
                    JSONArray items = response.optJSONArray("albums");
                    if (items != null) {
                        List<HomeCardData> homeCardDataAlbum = new ArrayList<>();
                        for (int i = 0; i < items.length(); i++) {
                            Album at = null;
                            try {
                                at = gson.fromJson(items.get(i).toString(), Album.class);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            HomeCardData data = new HomeCardData(
                                    String.valueOf(at.getArtist_id()),
                                    at.getCover_img(),
                                    at.getName(),
                                    "",
                                    ALBUM
                            );
                            homeCardDataAlbum.add(data);
                        }
                        Log.d("LOG_ALBUMS", String.valueOf(homeCardDataAlbum.size()));

                        // Lấy dữ liệu Playlist
                        JsonObjectRequest jsonObjectPlaylistRequest = new JsonObjectRequest(
                                Request.Method.GET,
                                Constants.getAllPlaylistEndpoint(),
                                null,
                                response1 -> {
                                    Log.i("LOG_RESPONSE", String.valueOf(response1));
                                    JSONArray items1 = response1.optJSONArray("play_lists");
                                    if (items1 != null) {
                                        List<HomeCardData> homeCardDataPlaylist = new ArrayList<>();
                                        for (int i = 0; i < items1.length(); i++) {
                                            PlayList playlist = null;
                                            try {
                                                playlist = gson.fromJson(items1.get(i).toString(), PlayList.class);
                                            } catch (JSONException e) {
                                                throw new RuntimeException(e);
                                            }
                                            HomeCardData data = new HomeCardData(
                                                    String.valueOf(playlist.getPlay_list_id()),
                                                    playlist.getCover_img(),
                                                    playlist.getName(),
                                                    "",
                                                    PLAYLIST
                                            );
                                            homeCardDataPlaylist.add(data);
                                        }
                                        Log.d("LOG_PLAYLIST", String.valueOf(homeCardDataPlaylist.size()));

                                        // Lấy dữ liệu Artist
                                        JsonObjectRequest jsonObjectArtistRequest = new JsonObjectRequest(
                                                Request.Method.GET,
                                                Constants.getAllArtistEndpoint(),
                                                null,
                                                response2 -> {
                                                    Log.i("LOG_RESPONSE", String.valueOf(response2));
                                                    JSONArray items2 = response2.optJSONArray("artists");
                                                    if (items2 != null) {
                                                        List<HomeCardData> homeCardDataArtists = new ArrayList<>();
                                                        for (int i = 0; i < items2.length(); i++) {
                                                            Artist artist = null;
                                                            try {
                                                                artist = gson.fromJson(items2.get(i).toString(), Artist.class);
                                                            } catch (JSONException e) {
                                                                throw new RuntimeException(e);
                                                            }
                                                            HomeCardData data = new HomeCardData(
                                                                    String.valueOf(artist.getArtist_id()),
                                                                    artist.getCoverImg(),
                                                                    artist.getName(),
                                                                    artist.getDescription(),
                                                                    ARTIST
                                                            );
                                                            homeCardDataArtists.add(data);
                                                        }
                                                        Log.d("LOG_ARTIST", String.valueOf(homeCardDataArtists.size()));

                                                        // Sau khi có dữ liệu từ tất cả các yêu cầu mạng, thêm vào dataItems
                                                        dataItems.add(new HomeSectionData("Albums", homeCardDataAlbum));
                                                        dataItems.add(new HomeSectionData("Playlist", homeCardDataPlaylist));
                                                        dataItems.add(new HomeSectionData("Artist", homeCardDataArtists));
                                                    }
                                                },
                                                error -> Log.e("LOG_RESPONSE", error.toString())
                                        );
                                        requestQueue.add(jsonObjectArtistRequest);
                                    }
                                },
                                error -> Log.e("LOG_RESPONSE", error.toString())
                        );
                        requestQueue.add(jsonObjectPlaylistRequest);
                    }
                },
                error -> Log.e("LOG_RESPONSE", error.toString())
        );
        requestQueue.add(jsonObjectAlbumRequest);
    }

    @Override
    public void onCardClickedListener(ItemType type, int id) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Bundle bundle;
        switch (type) {
            case ALBUM:
                bundle = new Bundle();
                bundle.putInt(ALBUM_DATA, id);
                AlbumFragment albumFragment = new AlbumFragment();
                albumFragment.setArguments(bundle);
                transaction.replace(R.id.fragment_container, albumFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case ARTIST:
                bundle = new Bundle();
                bundle.putInt(ARTIST_DATA, id);
                ArtistFragment artistFragment = new ArtistFragment();
                artistFragment.setArguments(bundle);
                transaction.replace(R.id.fragment_container, artistFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case PLAYLIST:
                bundle = new Bundle();
                bundle.putInt(PLAYLIST_DATA, id);
                PlaylistFragment playlistFragment = new PlaylistFragment();
                playlistFragment.setArguments(bundle);
                transaction.replace(R.id.fragment_container, playlistFragment);
                transaction.addToBackStack(null);
                transaction.commit();
        }
    }

    @Override
    public void onUserSettingClickedListener() {
        UserSettingFragment fragment = new UserSettingFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}