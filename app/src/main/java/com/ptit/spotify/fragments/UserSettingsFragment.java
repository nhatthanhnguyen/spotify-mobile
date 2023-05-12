package com.ptit.spotify.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;
import com.ptit.spotify.activities.StartActivity;
import com.ptit.spotify.adapters.UserSettingsAdapter;
import com.ptit.spotify.dto.data.UserSettingsHeaderData;
import com.ptit.spotify.dto.data.UserSettingsOptionData;
import com.ptit.spotify.itemdecorations.VerticalViewItemDecoration;
import com.ptit.spotify.utils.TypeUserSettingsItem;

import java.util.ArrayList;
import java.util.List;

public class UserSettingsFragment extends Fragment implements UserSettingsAdapter.OnItemClickedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserSettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserSettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserSettingsFragment newInstance(String param1, String param2) {
        UserSettingsFragment fragment = new UserSettingsFragment();
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
        View view = inflater.inflate(R.layout.fragment_user_settings, container, false);
        int spacing = getContext().getResources().getDimensionPixelSize(R.dimen.spacing_24);
        List<Object> userSettingsItems = new ArrayList<>();
        addItems(userSettingsItems);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new VerticalViewItemDecoration(spacing));
        UserSettingsAdapter adapter = new UserSettingsAdapter(userSettingsItems, this);
        recyclerView.setAdapter(adapter);

        ImageButton buttonBack = view.findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });
        return view;
    }

    private void addItems(List<Object> userSettingsItems) {
        userSettingsItems.add(new UserSettingsHeaderData(null, "NhatThanh"));
        userSettingsItems.add("Account");
        userSettingsItems.add(new UserSettingsOptionData("Email", "nhatthanhlep2001@gmail.com", TypeUserSettingsItem.EMAIL));
        userSettingsItems.add("Other");
        userSettingsItems.add(new UserSettingsOptionData("Sign out", "You are sign in under the name NhatThanh", TypeUserSettingsItem.SIGN_OUT));
    }

    @Override
    public void onSignOutClickedListener() {
        getActivity().finish();
        startActivity(new Intent(getActivity(), StartActivity.class));
    }

    @Override
    public void onEmailClickedListener() {
        ChangeEmailFragment fragment = new ChangeEmailFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}