package com.ptit.spotify.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ptit.spotify.R;

public class SearchFragment extends Fragment {
    public SearchFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ImageButton buttonMicro = view.findViewById(R.id.buttonMicro);
        LinearLayout linearLayoutSearch = view.findViewById(R.id.linearLayoutSearch);
        buttonMicro.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Micro has been start", Toast.LENGTH_SHORT).show();
        });
        linearLayoutSearch.setOnClickListener(v -> {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            SearchResultFragment fragment = new SearchResultFragment();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
        return view;
    }
}