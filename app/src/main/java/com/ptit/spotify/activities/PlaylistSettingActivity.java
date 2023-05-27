package com.ptit.spotify.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;
import com.ptit.spotify.adapters.playlist.PlaylistSettingAdapter;
import com.ptit.spotify.dto.data.PlaylistSettingHeaderData;
import com.ptit.spotify.itemdecorations.VerticalViewItemDecoration;
import com.ptit.spotify.utils.OnItemPlaylistSettingClickedListener;

import java.util.ArrayList;
import java.util.List;

public class PlaylistSettingActivity extends AppCompatActivity implements OnItemPlaylistSettingClickedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_settings);
        int spacing = getResources().getDimensionPixelSize(R.dimen.spacing_32);
        List<Object> playlistSettingsItems = new ArrayList<>();
        addItems(playlistSettingsItems);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new VerticalViewItemDecoration(spacing));
        PlaylistSettingAdapter adapter = new PlaylistSettingAdapter(playlistSettingsItems, this);
        recyclerView.setAdapter(adapter);
    }

    private void addItems(List<Object> playlistSettingsItems) {
        playlistSettingsItems.add(new PlaylistSettingHeaderData("https://i.scdn.co/image/ab67706f00000002ca5a7517156021292e5663a6",
                "Peaceful Piano",
                "Spotify"));
    }

    @Override
    public void onBackButtonClickedListener() {
        finish();
    }

    @Override
    public void onOptionClickedListener() {
        finish();
    }
}