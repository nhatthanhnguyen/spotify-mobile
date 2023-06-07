package com.ptit.spotify.fragments;

import static com.ptit.spotify.application.SpotifyApplication.ACTION_LIKE;
import static com.ptit.spotify.application.SpotifyApplication.ACTION_NEXT;
import static com.ptit.spotify.application.SpotifyApplication.ACTION_PAUSE;
import static com.ptit.spotify.application.SpotifyApplication.ACTION_PREV;
import static com.ptit.spotify.application.SpotifyApplication.ACTION_RESUME;
import static com.ptit.spotify.application.SpotifyApplication.ACTION_UNLIKE;
import static com.ptit.spotify.fragments.HomeFragment.ARTIST_DATA;
import static com.ptit.spotify.utils.ItemType.ALBUM;
import static com.ptit.spotify.utils.ItemType.SETTING_DESTINATION_ADD_TO_PLAYLIST;
import static com.ptit.spotify.utils.ItemType.SETTING_DESTINATION_ARTIST;
import static com.ptit.spotify.utils.ItemType.SETTING_LIKE;
import static com.ptit.spotify.utils.Utils.getYear;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.ptit.spotify.activities.ContentActivity;
import com.ptit.spotify.adapters.album.AlbumAdapter;
import com.ptit.spotify.dto.data.AlbumHeaderData;
import com.ptit.spotify.dto.data.AlbumSettingHeaderData;
import com.ptit.spotify.dto.data.AlbumSongData;
import com.ptit.spotify.dto.data.SettingOptionData;
import com.ptit.spotify.dto.model.Album;
import com.ptit.spotify.dto.model.Artist;
import com.ptit.spotify.dto.model.Song;
import com.ptit.spotify.helper.SessionManager;
import com.ptit.spotify.itemdecorations.VerticalViewItemDecoration;
import com.ptit.spotify.utils.Constants;
import com.ptit.spotify.utils.OnItemAlbumClickedListener;
import com.ptit.spotify.utils.OnItemSettingClickedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AlbumFragment extends Fragment implements OnItemAlbumClickedListener, OnItemSettingClickedListener {
    private static final String ALBUM_DATA = "Album";
    private List<Object> albumItems;
    private AlbumAdapter albumAdapter;
    private SettingFragment settingFragment;
    private String userId;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                updateData(action);
            }
        }
    };

    @SuppressLint("NotifyDataSetChanged")
    private void updateData(String action) {
        Log.d("ALBUM FRAGMENT", action);
        if (action.equals(ACTION_PAUSE)) {
            AlbumHeaderData headerData = (AlbumHeaderData) albumItems.get(0);
            headerData.setPlaying(false);
        }
        if (action.equals(ACTION_RESUME)) {
            AlbumHeaderData headerData = (AlbumHeaderData) albumItems.get(0);
            headerData.setPlaying(true);
        }
        if (action.equals(ACTION_NEXT)) {
            int pos = posOfSong();
            Log.d("POS", String.valueOf(pos));
            if (pos == -1) return;
            updateSong(pos);
        }

        if (action.equals(ACTION_PREV)) {
            int pos = posOfSong();
            if (pos == -1) return;
            Log.d("POS", String.valueOf(pos));
            updateSong(pos);
        }

        if (action.equals(ACTION_LIKE)) {
            int pos = posOfSong();
            if (pos == -1) return;
            AlbumSongData songData = (AlbumSongData) albumItems.get(pos);
            songData.setLiked(true);
        }

        if (action.equals(ACTION_UNLIKE)) {
            int pos = posOfSong();
            if (pos == -1) return;
            AlbumSongData songData = (AlbumSongData) albumItems.get(pos);
            songData.setLiked(false);
        }
        albumAdapter.notifyDataSetChanged();
    }

    private void updateSong(int pos) {
        for (int i = 1; i < albumItems.size(); ++i) {
            AlbumSongData songData = (AlbumSongData) albumItems.get(i);
            songData.setSelected(i == pos);
        }
    }

    private int posOfSong() {
        for (int i = 1; i < albumItems.size(); ++i) {
            AlbumSongData songData = (AlbumSongData) albumItems.get(i);
            if (songData.getId().equals(String.valueOf(ContentActivity.musicPlayerService.currentSong.getSong_id()))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        String albumId = "";
        if (bundle != null) {
            albumId = String.valueOf(bundle.getInt(ALBUM_DATA, 0));
        }
        SessionManager sessionManager = new SessionManager(getContext());
        userId = String.valueOf(sessionManager.getUserId());

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_PAUSE);
        intentFilter.addAction(ACTION_RESUME);
        intentFilter.addAction(ACTION_NEXT);
        intentFilter.addAction(ACTION_PREV);
        intentFilter.addAction(ACTION_LIKE);
        intentFilter.addAction(ACTION_UNLIKE);
        getActivity().registerReceiver(receiver, intentFilter);

        int spacing = getContext().getResources().getDimensionPixelSize(R.dimen.spacing_16);
        albumItems = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new VerticalViewItemDecoration(spacing));
        //TODO: truyền vào albumsid
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        // TODO DONE: LẤY INFO ALBUM
        String finalAlbumId = albumId;
        String finalAlbumId1 = albumId;
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
                                    boolean isPlayingAlbum = ContentActivity.musicPlayerService.type != null &&
                                            (ContentActivity.musicPlayerService.type == ALBUM &&
                                                    ContentActivity.musicPlayerService.id == Integer.parseInt(finalAlbumId) &&
                                                    ContentActivity.musicPlayerService.isPlaying
                                            );
                                    Artist finalArtist = artist;
                                    JsonObjectRequest jsonLikeAlbumRequest = new JsonObjectRequest(
                                            Request.Method.GET,
                                            Constants.getAlbumInteractionEndpoint(userId),
                                            null,
                                            likeAlbums -> {
                                                boolean isLiked = false;
                                                Log.i("LOG RESPONSE", likeAlbums.toString());
                                                JSONArray albums = likeAlbums.optJSONArray("albums");
                                                if (albums == null) {
                                                    isLiked = false;
                                                }
                                                for (int i = 0; i < albums.length(); ++i) {
                                                    Album album1 = null;
                                                    try {
                                                        album1 = gson.fromJson(albums.get(i).toString(), Album.class);
                                                    } catch (JSONException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                    if (album1.getAlbum_id() == Integer.parseInt(finalAlbumId1)) {
                                                        isLiked = true;
                                                        break;
                                                    }
                                                }
                                                AlbumHeaderData headerData = new AlbumHeaderData(
                                                        Integer.parseInt(finalAlbumId),
                                                        finalAlbum.getCover_img(),
                                                        finalAlbum.getName(),
                                                        finalArtist.getArtist_id(),
                                                        finalArtist.getName(),
                                                        finalArtist.getCoverImg(),
                                                        getYear(finalAlbum.getCreated_at()),
                                                        isLiked,
                                                        isPlayingAlbum);
                                                albumItems.add(headerData);
                                                JsonObjectRequest jsonObjectSongRequest = new JsonObjectRequest(
                                                        Request.Method.GET,
                                                        Constants.getSongByAlbumIdEndpoint(finalAlbumId),
                                                        null,
                                                        responseSong -> {
                                                            Log.i("LOG_RESPONSE", String.valueOf(responseSong));
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
                                                                            if (itemArtistSongs == null)
                                                                                return;
                                                                            Artist artistSong = null;
                                                                            try {
                                                                                artistSong = gson3.fromJson(itemArtistSongs.get(0).toString(), Artist.class);
                                                                            } catch (
                                                                                    JSONException e) {
                                                                                throw new RuntimeException(e);
                                                                            }
                                                                            boolean isSelected = ContentActivity.musicPlayerService.currentSong != null &&
                                                                                    ContentActivity.musicPlayerService.currentSong.getSong_id() == finalSong.getSong_id();
                                                                            AlbumSongData albumSongData = new AlbumSongData(
                                                                                    String.valueOf(finalSong.getSong_id()),
                                                                                    finalSong.getName(),
                                                                                    "",
                                                                                    artistSong.getName(),
                                                                                    false,
                                                                                    isSelected
                                                                            );
                                                                            albumItems.add(albumSongData);
                                                                            albumAdapter = new AlbumAdapter(albumItems, this);
                                                                            recyclerView.setAdapter(albumAdapter);
                                                                        }, error -> Log.e("LOG_RESPONSE", error.toString()));
                                                                requestQueue.add(jsonObjectRequestArtistSong);
                                                            }
                                                        }, error -> Log.e("LOG_RESPONSE", error.toString()));
                                                requestQueue.add(jsonObjectSongRequest);
                                            },
                                            error -> Log.e("LOG_RESPONSE", error.toString())
                                    );
                                    requestQueue.add(jsonLikeAlbumRequest);
                                }, error -> Log.e("LOG_RESPONSE", error.toString()));
                        requestQueue.add(jsonObjectArtistRequest);
                    }
                }, error -> Log.e("LOG_RESPONSE", error.toString()));
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
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
        boolean isAlbumLiked = ((AlbumHeaderData) albumItems.get(0)).isLiked();
        items.add(new AlbumSettingHeaderData(data.getImageUrl(), data.getAlbumName(), data.getArtistName()));
        items.add(new SettingOptionData(isAlbumLiked ? R.drawable.ic_like_filled : R.drawable.ic_like_outlined, "Like", SETTING_LIKE));
        items.add(new SettingOptionData(R.drawable.ic_view_artist, "View Artist", SETTING_DESTINATION_ARTIST));
        items.add(new SettingOptionData(R.drawable.ic_add_to_playlist, "Add to playlist", SETTING_DESTINATION_ADD_TO_PLAYLIST));
        settingFragment = new SettingFragment(items, this);
        settingFragment.show(getParentFragmentManager(), settingFragment.getTag());
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onLikeButtonClickedListener(AlbumHeaderData data) {
        JSONObject request = new JSONObject();
        try {
            request.put("user_id", Integer.parseInt(userId));
            request.put("object_id", data.getId());
            request.put("object_type", 2);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String requestBody = request.toString();
        if (data.isLiked()) {
            data.setLiked(false);
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            JsonObjectRequest jsonDeleteInteraction = new JsonObjectRequest(
                    Request.Method.POST,
                    Constants.postDeleteInteraction(),
                    new JSONObject(),
                    response -> {
                        Log.i("LOG RESPONSE", response.toString());
                        albumAdapter.notifyDataSetChanged();
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
            data.setLiked(true);
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            JsonObjectRequest jsonAddInteraction = new JsonObjectRequest(
                    Request.Method.POST,
                    Constants.postAddInteraction(),
                    new JSONObject(),
                    response -> {
                        Log.i("LOG RESPONSE", response.toString());
                        albumAdapter.notifyDataSetChanged();
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
    public void onSettingItemClickedListener(Object data) {
        if (data instanceof SettingOptionData) {
            SettingOptionData optionData = (SettingOptionData) data;
            if (optionData.getItemType() == SETTING_DESTINATION_ARTIST) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putInt(ARTIST_DATA, ((AlbumHeaderData) albumItems.get(0)).getArtistId());
                ArtistFragment fragment = new ArtistFragment();
                fragment.setArguments(bundle);
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }
        settingFragment.dismiss();
    }
}
