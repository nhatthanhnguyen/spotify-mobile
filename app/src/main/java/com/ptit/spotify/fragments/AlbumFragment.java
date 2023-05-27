package com.ptit.spotify.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;
import com.ptit.spotify.adapters.album.AlbumAdapter;
import com.ptit.spotify.dto.data.AlbumHeaderData;
import com.ptit.spotify.dto.data.AlbumSongData;
import com.ptit.spotify.itemdecorations.VerticalViewItemDecoration;
import com.ptit.spotify.utils.OnItemAlbumClickedListener;

import java.util.ArrayList;
import java.util.List;

public class AlbumFragment extends Fragment implements OnItemAlbumClickedListener {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int spacing = getContext().getResources().getDimensionPixelSize(R.dimen.spacing_16);
        List<Object> albumItems = new ArrayList<>();
        addData(albumItems);
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

    private void addData(List<Object> albumItems) {
        albumItems.add(new AlbumHeaderData(
                "https://i.scdn.co/image/ab67616d0000b273b94f78cf2a6ac9c700ee2812",
                "Saying Things",
                "Emanuel Fremont",
                "https://i.scdn.co/image/ab6761610000e5eb4de66b2170a9e8049a828d5f",
                "2022",
                true,
                false,
                false)
        );
        albumItems.add(new AlbumSongData(
                "song12",
                "Saying Things",
                "https://i.scdn.co/image/ab6761610000e5eb4de66b2170a9e8049a828d5f",
                "Emanuel Fremont"));
    }

    @Override
    public void onItemClickedToFragmentListener() {
        getParentFragmentManager().popBackStack();
    }
}
