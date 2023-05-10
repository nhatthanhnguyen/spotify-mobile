package com.ptit.spotify.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;
import com.ptit.spotify.adapters.AlbumSettingsAdapter;
import com.ptit.spotify.dto.data.AlbumSettingsHeaderData;
import com.ptit.spotify.dto.data.AlbumSettingsOptionNavigationData;
import com.ptit.spotify.dto.data.AlbumSettingsOptionToggleData;
import com.ptit.spotify.itemdecorations.VerticalViewItemDecoration;
import com.ptit.spotify.utils.TypeDestination;
import com.ptit.spotify.utils.TypeEntity;

import java.util.ArrayList;
import java.util.List;

public class AlbumSettingsActivity extends AppCompatActivity implements AlbumSettingsAdapter.OnItemClickedListener {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_settings);
        int spacing = getResources().getDimensionPixelSize(R.dimen.spacing_32);
        List<Object> albumSettingsItems = new ArrayList<>();
        addItems(albumSettingsItems);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new VerticalViewItemDecoration(spacing));
        AlbumSettingsAdapter albumSettingsAdapter = new AlbumSettingsAdapter(albumSettingsItems, this);
        recyclerView.setAdapter(albumSettingsAdapter);
    }

    void addItems(List<Object> albumSettingsItems) {
        albumSettingsItems.add(new AlbumSettingsHeaderData(
                "https://i.scdn.co/image/ab67616d0000b273b94f78cf2a6ac9c700ee2812",
                "Saying Things",
                "Emanuel Fremont"
        ));
        albumSettingsItems.add(new AlbumSettingsOptionToggleData("ic_like", "Liked", "Like", true));
        albumSettingsItems.add(new AlbumSettingsOptionToggleData("ic_like", "Liked all songs", "Like all songs", true));
        albumSettingsItems.add(new AlbumSettingsOptionNavigationData("ic_view_artist", "View Artist", TypeDestination.ARTIST));
    }

    @Override
    public void onItemClickedNavigationOptionListener(TypeDestination type) {
        Intent intent = new Intent();
        intent.putExtra("nav", TypeEntity.ARTIST.toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}