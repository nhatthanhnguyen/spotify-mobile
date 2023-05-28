package com.ptit.spotify.fragments;

import static com.ptit.spotify.utils.ItemType.SETTING_DESTINATION_ADD_TO_PLAYLIST;
import static com.ptit.spotify.utils.ItemType.SETTING_DESTINATION_ARTIST;
import static com.ptit.spotify.utils.ItemType.SETTING_LIKE;
import static com.ptit.spotify.utils.ItemType.SETTING_LIKE_ALL_SONG;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;
import com.ptit.spotify.adapters.album.AlbumAdapter;
import com.ptit.spotify.dto.data.AlbumHeaderData;
import com.ptit.spotify.dto.data.AlbumSettingHeaderData;
import com.ptit.spotify.dto.data.AlbumSongData;
import com.ptit.spotify.dto.data.SettingOptionData;
import com.ptit.spotify.itemdecorations.VerticalViewItemDecoration;
import com.ptit.spotify.utils.OnItemAlbumClickedListener;
import com.ptit.spotify.utils.OnItemSettingClickedListener;

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
