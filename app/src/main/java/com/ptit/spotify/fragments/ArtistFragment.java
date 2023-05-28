package com.ptit.spotify.fragments;

import static com.ptit.spotify.utils.ItemType.SETTING_LIKE;

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
import com.ptit.spotify.adapters.artist.ArtistAdapter;
import com.ptit.spotify.dto.data.ArtistCaptionData;
import com.ptit.spotify.dto.data.ArtistDescriptionData;
import com.ptit.spotify.dto.data.ArtistHeaderData;
import com.ptit.spotify.dto.data.ArtistSettingHeaderData;
import com.ptit.spotify.dto.data.ArtistSongData;
import com.ptit.spotify.dto.data.SettingOptionData;
import com.ptit.spotify.itemdecorations.VerticalViewItemDecoration;
import com.ptit.spotify.utils.OnItemArtistClickedListener;
import com.ptit.spotify.utils.OnItemSettingClickedListener;

import java.util.ArrayList;
import java.util.List;

public class ArtistFragment extends Fragment implements OnItemArtistClickedListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        int spacing = getContext().getResources().getDimensionPixelSize(R.dimen.spacing_16);
        List<Object> artistItems = new ArrayList<>();
        addData(artistItems);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new VerticalViewItemDecoration(spacing));
        ArtistAdapter artistAdapter = new ArtistAdapter(artistItems, this);
        recyclerView.setAdapter(artistAdapter);
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
