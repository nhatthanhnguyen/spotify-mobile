package com.ptit.spotify.fragments;

import static com.ptit.spotify.utils.ItemType.ALBUM;
import static com.ptit.spotify.utils.ItemType.ARTIST;
import static com.ptit.spotify.utils.ItemType.PLAYLIST;
import static com.ptit.spotify.utils.ItemType.SETTING_DESTINATION_ADD_TO_PLAYLIST;
import static com.ptit.spotify.utils.ItemType.SETTING_DESTINATION_ALBUM;
import static com.ptit.spotify.utils.ItemType.SETTING_DESTINATION_ARTIST;
import static com.ptit.spotify.utils.ItemType.SETTING_LIKE;
import static com.ptit.spotify.utils.ItemType.SONG;
import static com.ptit.spotify.utils.ItemType.TOP_RESULT;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;
import com.ptit.spotify.adapters.search.ButtonFilterAdapter;
import com.ptit.spotify.adapters.search.SearchResultAdapter;
import com.ptit.spotify.dto.data.ButtonFilterData;
import com.ptit.spotify.dto.data.SearchItemResultData;
import com.ptit.spotify.dto.data.SettingOptionData;
import com.ptit.spotify.dto.data.SongSettingHeaderData;
import com.ptit.spotify.itemdecorations.HorizontalViewItemDecoration;
import com.ptit.spotify.itemdecorations.VerticalViewItemDecoration;
import com.ptit.spotify.utils.ItemType;
import com.ptit.spotify.utils.OnItemButtonFilterClickedListener;
import com.ptit.spotify.utils.OnItemSearchResultClickedListener;
import com.ptit.spotify.utils.OnItemSettingClickedListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchResultFragment extends Fragment implements
        OnItemSearchResultClickedListener,
        OnItemButtonFilterClickedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<SearchItemResultData> resultItems;
    private List<SearchItemResultData> resultItemsAll;
    private List<ButtonFilterData> filterItems;
    private RecyclerView recyclerViewFilter;
    private RecyclerView recyclerViewResult;
    private ButtonFilterAdapter filterAdapter;
    private SearchResultAdapter resultAdapter;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchResultFragment newInstance(String param1, String param2) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);
        ImageButton buttonBack = view.findViewById(R.id.buttonBack);
        EditText editTextSearch = view.findViewById(R.id.editTextSearch);
        ImageButton buttonCancel = view.findViewById(R.id.buttonCancel);
        recyclerViewFilter = view.findViewById(R.id.recyclerViewFilter);
        recyclerViewResult = view.findViewById(R.id.recyclerViewResult);
        recyclerViewResult.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewFilter.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,
                false));
        int spacing = getContext().getResources().getDimensionPixelSize(R.dimen.spacing_16);
        recyclerViewFilter.addItemDecoration(new HorizontalViewItemDecoration(spacing));
        recyclerViewResult.addItemDecoration(new VerticalViewItemDecoration(spacing));
        buttonBack.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });
        buttonCancel.setOnClickListener(v -> {
            editTextSearch.setText("");
            buttonCancel.setVisibility(View.GONE);
            recyclerViewResult.setVisibility(View.GONE);
            recyclerViewFilter.setVisibility(View.GONE);
            resultItems = null;
            filterItems = null;
        });
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String currentText = s.toString();
                if (currentText.length() == 0) {
                    recyclerViewFilter.setVisibility(View.GONE);
                    recyclerViewResult.setVisibility(View.GONE);
                    buttonCancel.setVisibility(View.GONE);
                    resultItems = null;
                    filterItems = null;
                    return;
                }
                buttonCancel.setVisibility(View.VISIBLE);
                recyclerViewFilter.setVisibility(View.VISIBLE);
                recyclerViewResult.setVisibility(View.VISIBLE);
                addData();
                resultItemsAll = new ArrayList<>(resultItems);
                filterItems = new ArrayList<>();
                for (SearchItemResultData item : resultItems) {
                    if (!checkIsInList(item.getType())) {
                        filterItems.add(new ButtonFilterData(item.getType(), false));
                    }
                }
                filterItems.sort(Comparator.comparing(ButtonFilterData::getType));
                filterItems.add(0, new ButtonFilterData(TOP_RESULT, true));
                filterAdapter = new ButtonFilterAdapter(filterItems,
                        SearchResultFragment.this);
                resultAdapter = new SearchResultAdapter(resultItems, false,
                        SearchResultFragment.this);
                recyclerViewFilter.setAdapter(filterAdapter);
                recyclerViewResult.setAdapter(resultAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    private boolean checkIsInList(ItemType type) {
        for (ButtonFilterData data : filterItems) {
            if (data.getType() == type) {
                return true;
            }
        }
        return false;
    }

    void addData() {
        resultItems = new ArrayList<>();
        resultItems.add(new SearchItemResultData(
                "",
                "https://i.scdn.co/image/ab67706f00000002ca5a7517156021292e5663a6",
                "Peaceful Piano",
                "Relax and indulge with beautiful piano pieces",
                "Spotify",
                null,
                PLAYLIST)
        );
        resultItems.add(new SearchItemResultData(
                "",
                "https://i.scdn.co/image/ab67616d0000485128ccaf8cb23d857cb9361ec4",
                "Tjärnheden",
                null,
                "Farsjön",
                "Fjäderlätt",
                SONG
        ));
        resultItems.add(new SearchItemResultData(
                "",
                "https://i.scdn.co/image/ab67616d00001e02b94f78cf2a6ac9c700ee2812",
                "Emanuel Fremont",
                null,
                null,
                null,
                ARTIST
        ));
        resultItems.add(new SearchItemResultData(
                "",
                "https://i.scdn.co/image/ab67616d0000b273b94f78cf2a6ac9c700ee2812",
                "Saying Things",
                null,
                "Emanuel Fremont",
                null,
                ALBUM
        ));
    }

    @Override
    public void onSongSettingClickedListener(SearchItemResultData data) {
        List<Object> items = new ArrayList<>();
        items.add(new SongSettingHeaderData(
                data.getImageUrl(),
                data.getTitle(),
                data.getArtistName(),
                data.getAlbumName()));
        items.add(new SettingOptionData(
                R.drawable.ic_like_outlined,
                "Like",
                SETTING_LIKE
        ));
        items.add(new SettingOptionData(
                R.drawable.ic_add_to_playlist,
                "Add to playlist",
                SETTING_DESTINATION_ADD_TO_PLAYLIST
        ));
        items.add(new SettingOptionData(
                R.drawable.ic_view_album,
                "View album",
                SETTING_DESTINATION_ALBUM
        ));
        items.add(new SettingOptionData(
                R.drawable.ic_view_artist,
                "View artist",
                SETTING_DESTINATION_ARTIST
        ));
        SettingFragment settingFragment = new SettingFragment(items, new OnItemSettingClickedListener() {
            @Override
            public void onSettingItemClickedListener(Object data) {
                Toast.makeText(getContext(), data.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        settingFragment.show(getParentFragmentManager(), settingFragment.getTag());
    }

    @Override
    public void onItemClickedListener(SearchItemResultData data) {
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
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void updateButtonFilter(int position) {
        if (position != 0) {
            ItemType type = filterItems.get(position).getType();
            List<SearchItemResultData> filteredItems = resultItemsAll.stream()
                    .filter(item -> item.getType() == type)
                    .collect(Collectors.toList());
            resultAdapter = new SearchResultAdapter(filteredItems, true, this);
            recyclerViewResult.setAdapter(resultAdapter);
        } else {
            resultAdapter = new SearchResultAdapter(resultItemsAll, false, this);
            recyclerViewResult.setAdapter(resultAdapter);
        }
        filterItems = filterItems.stream()
                .peek(item -> item.setSelected(filterItems.get(position).getType() == item.getType()))
                .collect(Collectors.toList());
        filterAdapter.notifyDataSetChanged();
    }
}