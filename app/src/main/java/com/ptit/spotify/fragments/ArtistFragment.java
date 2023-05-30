package com.ptit.spotify.fragments;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ptit.spotify.R;
import com.ptit.spotify.adapters.artist.ArtistAdapter;
import com.ptit.spotify.dto.data.ArtistCaptionData;
import com.ptit.spotify.dto.data.ArtistDescriptionData;
import com.ptit.spotify.dto.data.ArtistHeaderData;
import com.ptit.spotify.dto.data.ArtistSettingHeaderData;
import com.ptit.spotify.dto.data.ArtistSongData;
import com.ptit.spotify.dto.data.SettingOptionData;
import com.ptit.spotify.dto.model.Artist;
import com.ptit.spotify.dto.model.Song;
import com.ptit.spotify.itemdecorations.VerticalViewItemDecoration;
import com.ptit.spotify.utils.Constants;
import com.ptit.spotify.utils.OnItemArtistClickedListener;
import com.ptit.spotify.utils.OnItemSettingClickedListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ArtistFragment extends Fragment implements OnItemArtistClickedListener {
    private static final String ARTIST_DATA = "Artist";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String artistId = "";
        if (bundle != null) {
            artistId = String.valueOf(bundle.getInt(ARTIST_DATA, 0));
        }
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        int spacing = getContext().getResources().getDimensionPixelSize(R.dimen.spacing_16);
        List<Object> artistItems = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String finalArtistId = artistId;
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new VerticalViewItemDecoration(spacing));
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                Constants.getArtistByIdEndpoint(artistId),
                null,
                artistResponse -> {
                    Log.i("LOG_RESPONSE", String.valueOf(artistResponse));
                    Gson gson = new Gson();
                    JSONArray artists = artistResponse.optJSONArray("artists");
                    if (artists == null) return;
                    Artist artist = null;
                    try {
                        artist = gson.fromJson(artists.get(0).toString(), Artist.class);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    artistItems.add(new ArtistHeaderData(
                            artist.getName(),
                            artist.getCoverImg(),
                            1000,
                            false
                    ));
                    artistItems.add(new ArtistCaptionData("Popular"));
                    Artist finalArtist = artist;
                    JsonObjectRequest jsonObjectSongRequest = new JsonObjectRequest(
                            Request.Method.GET,
                            Constants.getSongByArtistIdEndpoint(finalArtistId),
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
                                    artistItems.add(new ArtistSongData(
                                            String.valueOf(i + 1),
                                            song.getName(),
                                            "",
                                            String.valueOf(123)
                                    ));
                                }
                                artistItems.add(new ArtistCaptionData("Description"));
                                artistItems.add(new ArtistDescriptionData(1_139_683, finalArtist.getDescription(), finalArtist.getCoverImg()));
                                ArtistAdapter artistAdapter = new ArtistAdapter(artistItems, this);
                                recyclerView.setAdapter(artistAdapter);
                            }, error -> Log.e("LOG_RESPONSE", error.toString()));
                    requestQueue.add(jsonObjectSongRequest);
                },
                errorArtist -> Log.e("LOG_RESPONSE", errorArtist.toString())
        );
        requestQueue.add(jsonObjectRequest);
        return view;
    }

    private void addData(List<Object> albumItems) {
        // LẤY THÔNG TIN CỦA NGHỆ SĨ VÀ BÀI HÁT CỦA NGHỆ SĨ ĐÓ
        albumItems.add(new ArtistHeaderData("Emanuel Fremont", "https://i.scdn.co/image/ab6761610000e5eb4de66b2170a9e8049a828d5f", 1_139_683, false));
        albumItems.add(new ArtistCaptionData("Popular"));
        albumItems.add(new ArtistSongData("1", "Saying Things", "https://i.scdn.co/image/ab67616d00001e02b94f78cf2a6ac9c700ee2812", "30780389"));
        albumItems.add(new ArtistSongData("2", "Thinking About Jane", "https://i.scdn.co/image/ab67616d00001e0274711e144bfd76c62f044872", "7707204"));
        albumItems.add(new ArtistCaptionData("Description"));
        albumItems.add(new ArtistDescriptionData(1_139_683, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", "https://i.scdn.co/image/ab6761610000e5eb4de66b2170a9e8049a828d5f"));
    }

    @Override
    public void onBackButtonClickedListener() {
        getParentFragmentManager().popBackStack();
    }

    @Override
    public void onArtistSettingClickedListener(ArtistHeaderData data) {
        List<Object> items = new ArrayList<>();
        items.add(new ArtistSettingHeaderData(data.getArtistImageUrl(), data.getArtistName()));
        items.add(new SettingOptionData(R.drawable.ic_artist_follow, "Follow", SETTING_LIKE));
        SettingFragment settingFragment = new SettingFragment(items, new OnItemSettingClickedListener() {
            @Override
            public void onSettingItemClickedListener(Object data) {
                Toast.makeText(getContext(), data.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        settingFragment.show(getParentFragmentManager(), settingFragment.getTag());
    }
}
