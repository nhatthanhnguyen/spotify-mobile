package com.ptit.spotify.fragments;

import static com.ptit.spotify.utils.ItemType.ADD_ARTIST;
import static com.ptit.spotify.utils.ItemType.ALBUM;
import static com.ptit.spotify.utils.ItemType.ALL_RESULT;
import static com.ptit.spotify.utils.ItemType.ARTIST;
import static com.ptit.spotify.utils.ItemType.PLAYLIST;

import android.annotation.SuppressLint;
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
import com.ptit.spotify.adapters.library.LibraryAdapter;
import com.ptit.spotify.adapters.search.ButtonFilterAdapter;
import com.ptit.spotify.dto.data.ButtonFilterData;
import com.ptit.spotify.dto.data.LibraryItemData;
import com.ptit.spotify.dto.data.SearchItemResultData;
import com.ptit.spotify.dto.model.Account;
import com.ptit.spotify.dto.model.Album;
import com.ptit.spotify.dto.model.Artist;
import com.ptit.spotify.dto.model.PlayList;
import com.ptit.spotify.helper.SessionManager;
import com.ptit.spotify.itemdecorations.HorizontalViewItemDecoration;
import com.ptit.spotify.itemdecorations.VerticalViewItemDecoration;
import com.ptit.spotify.utils.Constants;
import com.ptit.spotify.utils.ItemType;
import com.ptit.spotify.utils.OnItemButtonFilterClickedListener;
import com.ptit.spotify.utils.OnItemSearchResultClickedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LibraryFragment extends Fragment implements
        OnItemSearchResultClickedListener,
        OnItemButtonFilterClickedListener {
    private List<LibraryItemData> resultItems;
    private List<LibraryItemData> resultItemsAll;
    private List<ButtonFilterData> filterItems;
    private RecyclerView recyclerViewFilter;
    private RecyclerView recyclerViewResult;
    private ButtonFilterAdapter filterAdapter;
    private LibraryAdapter resultAdapter;
    private SessionManager sessionManager;
    private List<Artist> artistTempList = new ArrayList<>();
    private List<Album> albumTempList = new ArrayList<>();
    private List<PlayList> playListTempList = new ArrayList<>();

    public LibraryFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sessionManager = new SessionManager(getContext());
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        recyclerViewFilter = view.findViewById(R.id.recyclerViewFilter);
        recyclerViewResult = view.findViewById(R.id.recyclerViewResult);
        recyclerViewResult.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewFilter.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,
                false));
        int spacing = getContext().getResources().getDimensionPixelSize(R.dimen.spacing_16);
        recyclerViewFilter.addItemDecoration(new HorizontalViewItemDecoration(spacing));
        recyclerViewResult.addItemDecoration(new VerticalViewItemDecoration(spacing));
        String userId = String.valueOf(sessionManager.getUserId());
        String username = sessionManager.getUsername();
        resultItems = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        Gson gson = new Gson();
        JsonObjectRequest jsonArtistRequest = new JsonObjectRequest(
                Request.Method.GET,
                Constants.getAllArtistEndpoint(),
                null,
                artistsResponse -> {
                    Log.i("LOG RESPONSE", artistsResponse.toString());
                    JSONArray artists = artistsResponse.optJSONArray("artists");
                    if (artists == null) return;
                    for (int i = 0; i < artists.length(); i++) {
                        Artist artist = null;
                        try {
                            artist = gson.fromJson(artists.get(i).toString(), Artist.class);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        artistTempList.add(artist);
                    }
                    JsonObjectRequest jsonAlbumRequest = new JsonObjectRequest(
                            Request.Method.GET,
                            Constants.getAllAlbumsEndpoint(),
                            null,
                            albumsResponse -> {
                                Log.i("LOG RESPONSE", albumsResponse.toString());
                                JSONArray albums = albumsResponse.optJSONArray("albums");
                                if (albums == null) return;
                                for (int j = 0; j < albums.length(); j++) {
                                    Album album = null;
                                    try {
                                        album = gson.fromJson(albums.get(j).toString(), Album.class);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                    albumTempList.add(album);
                                }
                                JsonObjectRequest jsonPlaylistRequest = new JsonObjectRequest(
                                        Request.Method.GET,
                                        Constants.getAllPlaylistEndpoint(),
                                        null,
                                        playlistsResponse -> {
                                            Log.i("LOG RESPONSE", playlistsResponse.toString());
                                            JSONArray playlists = playlistsResponse.optJSONArray("play_lists");
                                            if (playlists == null) return;
                                            for (int k = 0; k < playlists.length(); k++) {
                                                PlayList playList = null;
                                                try {
                                                    playList = gson.fromJson(playlists.get(k).toString(), PlayList.class);
                                                } catch (JSONException e) {
                                                    throw new RuntimeException(e);
                                                }
                                                playListTempList.add(playList);
                                            }
                                            getLibrary(gson, requestQueue, userId, username);
                                        },
                                        error -> Log.e("LOG RESPONSE", error.toString())
                                );
                                requestQueue.add(jsonPlaylistRequest);
                            },
                            error -> Log.e("LOG RESPONSE", error.toString())
                    );
                    requestQueue.add(jsonAlbumRequest);
                },
                error -> Log.e("LOG RESPONSE", error.toString())
        );
        requestQueue.add(jsonArtistRequest);
        return view;
    }

    private void getLibrary(Gson gson, RequestQueue requestQueue, String userId, String username) {
        JsonObjectRequest jsonAlbumLikedRequest = new JsonObjectRequest(
                Request.Method.GET,
                Constants.getAlbumsByIdEndpoint(userId),
                null,
                albumLikes -> {
                    Log.i("LOG RESPONSE", albumLikes.toString());
                    JSONArray albums = albumLikes.optJSONArray("albums");
                    if (albums != null) {
                        for (int i = 0; i < albums.length(); i++) {
                            Album album = null;
                            try {
                                album = gson.fromJson(albums.get(i).toString(), Album.class);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            resultItems.add(new LibraryItemData(
                                    String.valueOf(album.getAlbum_id()),
                                    album.getCover_img(),
                                    album.getName(),
                                    null,
                                    findArtist(album.getArtist_id()).getName(),
                                    null,
                                    ALBUM
                            ));
                        }
                    }
                    JsonObjectRequest jsonArtistLikesRequest = new JsonObjectRequest(
                            Request.Method.GET,
                            Constants.getArtistInteractionEndpoint(userId),
                            null,
                            artistLikes -> {
                                Log.i("LOG RESPONSE", artistLikes.toString());
                                JSONArray artists = artistLikes.optJSONArray("artists");
                                if (artists != null) {
                                    for (int j = 0; j < artists.length(); ++j) {
                                        Artist artist = null;
                                        try {
                                            artist = gson.fromJson(artists.get(j).toString(), Artist.class);
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                        resultItems.add(new LibraryItemData(
                                                String.valueOf(artist.getArtist_id()),
                                                artist.getCoverImg(),
                                                artist.getName(),
                                                artist.getDescription(),
                                                null,
                                                null,
                                                ARTIST
                                        ));
                                    }
                                }
                                JsonObjectRequest jsonPlaylistLikesRequest = new JsonObjectRequest(
                                        Request.Method.GET,
                                        Constants.getPlaylistInteractionEndpoint(userId),
                                        null,
                                        playlistLikes -> {
                                            Log.i("LOG RESPONSE", playlistLikes.toString());
                                            JSONArray playlists = playlistLikes.optJSONArray("play_lists");
                                            if (playlists != null) {
                                                for (int k = 0; k < playlists.length(); k++) {
                                                    PlayList playList = null;
                                                    try {
                                                        playList = gson.fromJson(playlists.get(k).toString(), PlayList.class);
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
                                                    PlayList finalPlayList = playList;
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
                                                                resultItems.add(new LibraryItemData(
                                                                        String.valueOf(finalPlayList.getPlay_list_id()),
                                                                        finalPlayList.getCover_img(),
                                                                        finalPlayList.getName(),
                                                                        null,
                                                                        account.getUsername(),
                                                                        null,
                                                                        PLAYLIST
                                                                ));
                                                                resultItemsAll = new ArrayList<>(resultItems);
                                                                filterItems = new ArrayList<>();
                                                                for (LibraryItemData item : resultItems) {
                                                                    if (!checkIsInList(item.getType())) {
                                                                        filterItems.add(new ButtonFilterData(item.getType(), false));
                                                                    }
                                                                }
                                                                filterItems.sort(Comparator.comparing(ButtonFilterData::getType));
                                                                filterItems.add(0, new ButtonFilterData(ALL_RESULT, true));
                                                                filterAdapter = new ButtonFilterAdapter(filterItems, this);
                                                                resultAdapter = new LibraryAdapter(resultItems, false, this);
                                                                recyclerViewFilter.setAdapter(filterAdapter);
                                                                recyclerViewResult.setAdapter(resultAdapter);
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
                                                    requestQueue.add(jsonObjectRequestUser);
                                                }
                                            } else {
                                                resultItemsAll = new ArrayList<>(resultItems);
                                                filterItems = new ArrayList<>();
                                                for (LibraryItemData item : resultItems) {
                                                    if (!checkIsInList(item.getType())) {
                                                        filterItems.add(new ButtonFilterData(item.getType(), false));
                                                    }
                                                }
                                                filterItems.sort(Comparator.comparing(ButtonFilterData::getType));
                                                filterItems.add(0, new ButtonFilterData(ALL_RESULT, true));
                                                filterAdapter = new ButtonFilterAdapter(filterItems, this);
                                                resultAdapter = new LibraryAdapter(resultItems, false, this);
                                                recyclerViewFilter.setAdapter(filterAdapter);
                                                recyclerViewResult.setAdapter(resultAdapter);
                                            }
                                        },
                                        error -> Log.e("LOG RESPONSE", error.toString())
                                );
                                requestQueue.add(jsonPlaylistLikesRequest);
                            },
                            error -> Log.e("LOG RESPONSE", error.toString())
                    );
                    requestQueue.add(jsonArtistLikesRequest);
                },
                error -> Log.e("LOG RESPONSE", error.toString())
        );
        requestQueue.add(jsonAlbumLikedRequest);
    }

    private Artist findArtist(int artistId) {
        for (Artist artist : artistTempList) {
            if (artist.getArtist_id() == artistId) {
                return artist;
            }
        }
        return null;
    }

    private boolean checkIsInList(ItemType type) {
        for (ButtonFilterData data : filterItems) {
            if (data.getType() == type) {
                return true;
            }
        }
        return false;
    }

    void addData(String userId) {
        resultItems = new ArrayList<>();
        resultItems.add(new LibraryItemData(
                "",
                "https://i.scdn.co/image/ab67706f00000002ca5a7517156021292e5663a6",
                "Peaceful Piano",
                "Relax and indulge with beautiful piano pieces",
                "Spotify",
                null,
                PLAYLIST)
        );
        resultItems.add(new LibraryItemData(
                "",
                "https://i.scdn.co/image/ab67616d00001e02b94f78cf2a6ac9c700ee2812",
                "Emanuel Fremont",
                null,
                null,
                null,
                ARTIST
        ));
        resultItems.add(new LibraryItemData(
                "",
                "https://i.scdn.co/image/ab67616d0000b273b94f78cf2a6ac9c700ee2812",
                "Saying Things",
                null,
                "Emanuel Fremont",
                null,
                ALBUM
        ));
        resultItems.add(new LibraryItemData(
                "",
                null,
                "Add artist",
                null,
                null,
                null,
                ADD_ARTIST
        ));

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void updateButtonFilter(int position) {
        if (position != 0) {
            ItemType type = filterItems.get(position).getType();
            List<LibraryItemData> filteredItems = resultItemsAll.stream()
                    .filter(item -> item.getType() == type)
                    .collect(Collectors.toList());
            resultAdapter = new LibraryAdapter(filteredItems, true, this);
            recyclerViewResult.setAdapter(resultAdapter);
        } else {
            resultAdapter = new LibraryAdapter(resultItemsAll, false, this);
            recyclerViewResult.setAdapter(resultAdapter);
        }
        filterItems = filterItems.stream()
                .peek(item -> item.setSelected(filterItems.get(position).getType() == item.getType()))
                .collect(Collectors.toList());
        filterAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSongSettingClickedListener(SearchItemResultData data) {
    }

    @Override
    public void onItemClickedListener(SearchItemResultData data) {
    }

    @Override
    public void onItemClickedListener(LibraryItemData data) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        if (data.getType() == ARTIST) {
            bundle.putInt("Artist", Integer.parseInt(data.getId()));
            ArtistFragment fragment = new ArtistFragment();
            fragment.setArguments(bundle);
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
            return;
        }
        if (data.getType() == ALBUM) {
            bundle.putInt("Album", Integer.parseInt(data.getId()));
            AlbumFragment fragment = new AlbumFragment();
            fragment.setArguments(bundle);
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
            return;
        }
        if (data.getType() == PLAYLIST) {
            bundle.putInt("Playlist", Integer.parseInt(data.getId()));
            PlaylistFragment fragment = new PlaylistFragment();
            fragment.setArguments(bundle);
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
            return;
        }
    }

    @Override
    public void onAddArtistClickedListener() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        ArtistAddFragment fragment = new ArtistAddFragment();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}