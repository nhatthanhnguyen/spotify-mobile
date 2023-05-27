package com.ptit.spotify.fragments;

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
import com.ptit.spotify.adapters.playlist.PlaylistAdapter;
import com.ptit.spotify.dto.data.PlaylistHeaderData;
import com.ptit.spotify.dto.data.PlaylistSongData;
import com.ptit.spotify.itemdecorations.VerticalViewItemDecoration;
import com.ptit.spotify.utils.OnItemPlaylistClickedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaylistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaylistFragment extends Fragment implements OnItemPlaylistClickedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlaylistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlaylistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaylistFragment newInstance(String param1, String param2) {
        PlaylistFragment fragment = new PlaylistFragment();
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        int spacing = getContext().getResources().getDimensionPixelSize(R.dimen.spacing_16);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new VerticalViewItemDecoration(spacing));
        List<Object> playlistItems = new ArrayList<>();
        addItems(playlistItems);
        PlaylistAdapter adapter = new PlaylistAdapter(playlistItems, this);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void addItems(List<Object> playlistItems) {
        playlistItems.add(new PlaylistHeaderData("https://i.scdn.co/image/ab67706f00000002ca5a7517156021292e5663a6",
                "Peaceful piano to help you slow down, breathe, and relax.",
                "https://i.scdn.co/image/ab67757000003b8255c25988a6ac314394d3fbf5",
                "Spotify",
                7035021,
                10000,
                true,
                false));
        playlistItems.add(new PlaylistSongData("https://i.scdn.co/image/ab67616d0000485128ccaf8cb23d857cb9361ec4",
                "Tjärnheden",
                "Farsjön",
                false,
                false));
        playlistItems.add(new PlaylistSongData("https://i.scdn.co/image/ab67616d00004851ba1332de8185cce3a9490e74",
                "Quand vous souriez",
                "Libor Kolman",
                false,
                false));
        playlistItems.add(new PlaylistSongData("https://i.scdn.co/image/ab67616d0000485147b70771cb7375cd30ceec54",
                "Allena",
                "M. Ljungström",
                true,
                true));
    }

    @Override
    public void onMoreSettingsClickedListener() {
        Toast.makeText(getContext(), "More settings clicked listener", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackButtonClickedListener() {
        getParentFragmentManager().popBackStack();
    }

    @Override
    public void onSearchClickedListener() {
        // TODO: search
    }
}