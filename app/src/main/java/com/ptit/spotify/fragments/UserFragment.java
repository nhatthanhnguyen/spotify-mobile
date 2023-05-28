package com.ptit.spotify.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;
import com.ptit.spotify.adapters.user.UserAdapter;
import com.ptit.spotify.dto.data.UserInfoHeaderData;
import com.ptit.spotify.dto.data.UserInfoPlaylistData;
import com.ptit.spotify.itemdecorations.VerticalViewItemDecoration;
import com.ptit.spotify.utils.OnItemUserClickedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment implements OnItemUserClickedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        int spacing = getContext().getResources().getDimensionPixelSize(R.dimen.spacing_16);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new VerticalViewItemDecoration(spacing));
        List<Object> userInfoItems = new ArrayList<>();
        addItems(userInfoItems);
        UserAdapter adapter = new UserAdapter(userInfoItems, this);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void addItems(List<Object> userInfoItems) {
        userInfoItems.add(new UserInfoHeaderData(null, "NhatThanh", 0));
        userInfoItems.add("Playlists");
        userInfoItems.add(new UserInfoPlaylistData("https://i.scdn.co/image/ab67616d00001e02136a8ed571891d091ed4715b", "Best playlist ever"));
    }

    @Override
    public void onButtonBackClickedListener() {
        getParentFragmentManager().popBackStack();
    }

    @Override
    public void onEditProfileClickedListener() {
        // TODO: edit profile
        Toast.makeText(getActivity(), "You clicked edit profile", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFollowingClickedListener() {
        // TODO: following
        Toast.makeText(getActivity(), "You clicked following", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPlaylistClickedListener() {
        PlaylistFragment fragment = new PlaylistFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}