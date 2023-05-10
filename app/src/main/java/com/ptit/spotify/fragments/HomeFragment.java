package com.ptit.spotify.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;
import com.ptit.spotify.adapters.HomeInfoAdapter;
import com.ptit.spotify.dto.data.HomeCardData;
import com.ptit.spotify.dto.data.HomeGreetingData;
import com.ptit.spotify.dto.data.HomeSectionData;
import com.ptit.spotify.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment implements HomeInfoAdapter.OnItemClickedListener {
    private RecyclerView recyclerView;
    private HomeInfoAdapter homeInfoAdapter;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        List<Object> dataItems = new ArrayList<>();
        addData(dataItems);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        homeInfoAdapter = new HomeInfoAdapter(dataItems, this);
        recyclerView.setAdapter(homeInfoAdapter);
        return view;
    }

    void addData(List<Object> dataItems) {
        dataItems.add(new HomeGreetingData());
        dataItems.add(new HomeSectionData("Recently played", Arrays.asList(
                new HomeCardData("1", "https://i.scdn.co/image/ab67706f00000002ca5a7517156021292e5663a6",
                        "Peaceful Piano",
                        "Relax and indulge with beautiful piano pieces",
                        Constants.PLAYLIST),
                new HomeCardData("2", "https://i.scdn.co/image/ab67616d00001e02b94f78cf2a6ac9c700ee2812",
                        "Saying things",
                        "Emanuel Fremont",
                        Constants.ALBUM),
                new HomeCardData("3", "https://i.scdn.co/image/ab67706f000000025551996f500ba876bda73fa5",
                        "Deep Focus",
                        "Keep calm and focus with ambient and post-rock music.",
                        Constants.PLAYLIST),
                new HomeCardData("4", "https://i.scdn.co/image/ab67706f00000002863b311d4b787ed621f7e696",
                        "Coding Mode",
                        "Dedicated to all the programmers out there.",
                        Constants.PLAYLIST),
                new HomeCardData("5", "https://seed-mix-image.spotifycdn.com/v6/img/artist/5zmB7dGi1rKdsEYVxQvJpS/en/default",
                        "Sun Of They Mix",
                        "PLACID, Shape Of Our Dreams and Josef Briem",
                        Constants.PLAYLIST),
                new HomeCardData("6", "https://i.scdn.co/image/ab67616100005174a106d7ba1ef89be4a1d31792",
                        "Agnes Lundh",
                        "Artist",
                        Constants.ARTIST),
                new HomeCardData("7", "https://i.scdn.co/image/ab6761610000517404dcd0ba0f67dd1a7bbf1c54",
                        "Sun Of They",
                        "Artist",
                        Constants.ARTIST),
                new HomeCardData("8", "https://i.scdn.co/image/ab676161000051744de66b2170a9e8049a828d5f",
                        "Emanuel Fremont",
                        "Artist",
                        Constants.ARTIST)
        )));
        dataItems.add(new HomeSectionData(new HomeCardData("2",
                "https://i.scdn.co/image/ab67706f000000025551996f500ba876bda73fa5",
                "Deep Focus",
                "Keep calm and focus with ambient and post-rock music.",
                Constants.PLAYLIST), Arrays.asList(
                new HomeCardData("9", "https://i.scdn.co/image/ab67706f00000002ce3b846bf652f5348ae98104",
                        "Atmospheric Focus",
                        "Music from the ether to focus to.",
                        Constants.PLAYLIST),
                new HomeCardData("10", "https://i.scdn.co/image/ab67706f00000002f9c4084f298abc83ee32b9ff",
                        "Relaxing Electric Guitar",
                        "Soothing, atmospheric guitar music to help you lie back, unwind and switch off.",
                        Constants.PLAYLIST),
                new HomeCardData("11", "https://i.scdn.co/image/ab67706f00000002863b311d4b787ed621f7e696",
                        "Coding Mode",
                        "Dedicated to all the programmers out there.",
                        Constants.PLAYLIST)
        )));
        dataItems.add(new HomeSectionData(new HomeCardData("7",
                "https://i.scdn.co/image/ab6761610000517404dcd0ba0f67dd1a7bbf1c54",
                "Sun Of They",
                "Artist",
                Constants.ARTIST), Arrays.asList(
                new HomeCardData("9", "https://i.scdn.co/image/ab67706f00000002ccef08a34fe2d9411ae884e9",
                        "Focus Piano",
                        "Soft piano to help you focus, work or study.",
                        Constants.PLAYLIST),
                new HomeCardData("10", "https://i.scdn.co/image/ab67706f00000002a147137323f796ef58e54411",
                        "Intense Studying",
                        "Get. It. Done. Focus-enhancing piano music to keep you motivated during your study session. ",
                        Constants.PLAYLIST),
                new HomeCardData("11", "https://i.scdn.co/image/ab67706f00000002fe24d7084be472288cd6ee6c",
                        "Instrumental Study",
                        "Focus with soft study music in the background.",
                        Constants.PLAYLIST)
        )));
    }

    @Override
    public void onItemClickedListener(String type) {
        if (type.equals(Constants.ALBUM)) {
            AlbumFragment albumFragment = new AlbumFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, albumFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        if (type.equals(Constants.ARTIST)) {
            ArtistFragment artistFragment = new ArtistFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, artistFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onItemClickedUserSettingsListener() {
        UserSettingsFragment fragment = new UserSettingsFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}