package com.ptit.spotify.fragments;

import static com.ptit.spotify.utils.ItemType.ADD_ARTIST;
import static com.ptit.spotify.utils.ItemType.ALBUM;
import static com.ptit.spotify.utils.ItemType.ALL_RESULT;
import static com.ptit.spotify.utils.ItemType.ARTIST;
import static com.ptit.spotify.utils.ItemType.PLAYLIST;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;
import com.ptit.spotify.adapters.library.LibraryAdapter;
import com.ptit.spotify.adapters.search.ButtonFilterAdapter;
import com.ptit.spotify.dto.data.ButtonFilterData;
import com.ptit.spotify.dto.data.LibraryItemData;
import com.ptit.spotify.dto.data.SearchItemResultData;
import com.ptit.spotify.itemdecorations.HorizontalViewItemDecoration;
import com.ptit.spotify.itemdecorations.VerticalViewItemDecoration;
import com.ptit.spotify.utils.ItemType;
import com.ptit.spotify.utils.OnItemButtonFilterClickedListener;
import com.ptit.spotify.utils.OnItemSearchResultClickedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.hdodenhof.circleimageview.CircleImageView;

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

    public LibraryFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        CircleImageView imageViewAccount = view.findViewById(R.id.imageViewAccount);
        recyclerViewFilter = view.findViewById(R.id.recyclerViewFilter);
        recyclerViewResult = view.findViewById(R.id.recyclerViewResult);
        recyclerViewResult.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewFilter.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,
                false));
        int spacing = getContext().getResources().getDimensionPixelSize(R.dimen.spacing_16);
        recyclerViewFilter.addItemDecoration(new HorizontalViewItemDecoration(spacing));
        recyclerViewResult.addItemDecoration(new VerticalViewItemDecoration(spacing));
        addData();
        resultItemsAll = new ArrayList<>(resultItems);
        filterItems = new ArrayList<>();
        filterItems.add(new ButtonFilterData(ALL_RESULT, true));
        filterItems.add(new ButtonFilterData(PLAYLIST, false));
        filterItems.add(new ButtonFilterData(ARTIST, false));
        filterItems.add(new ButtonFilterData(ALBUM, false));
        filterAdapter = new ButtonFilterAdapter(filterItems, this);
        resultAdapter = new LibraryAdapter(resultItems, false, this);
        recyclerViewFilter.setAdapter(filterAdapter);
        recyclerViewResult.setAdapter(resultAdapter);
        return view;
    }

    void addData() {
        // TODO: LẤY NHỮNG PLAYLIST, ALBUM, NGHỆ SĨ ĐÃ THÍCH, ADD_ARTIST GIỮ NGUYÊN
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
        if (data.getType() == ARTIST) {
            ArtistFragment fragment = new ArtistFragment();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
            return;
        }
        if (data.getType() == ALBUM) {
            AlbumFragment fragment = new AlbumFragment();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
            return;
        }
        if (data.getType() == PLAYLIST) {
            PlaylistFragment fragment = new PlaylistFragment();
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