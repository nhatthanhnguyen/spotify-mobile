package com.ptit.spotify.fragments;

import static com.ptit.spotify.utils.ItemType.USER_SETTING_EMAIL;
import static com.ptit.spotify.utils.ItemType.USER_SETTING_LOG_OUT;

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
import com.ptit.spotify.adapters.user.UserSettingAdapter;
import com.ptit.spotify.dto.data.UserSettingHeaderData;
import com.ptit.spotify.dto.data.UserSettingOptionData;
import com.ptit.spotify.helper.SessionManager;
import com.ptit.spotify.itemdecorations.VerticalViewItemDecoration;
import com.ptit.spotify.utils.OnItemUserSettingClickedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserSettingFragment extends Fragment implements OnItemUserSettingClickedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private SessionManager sessionManager;
    public static Map<String, String> map = new HashMap<>() {{
        put("truong", "truong@gmail.com");
        put("thanh", "thanh@gmail.com");
        put("nhon", "nhon@gmail.com");
        put("truongjr", "nguyentruongjr@gmail.com");
    }};
    public static Map<String, String> map1 = new HashMap<>() {{
        put("truong", "1");
        put("thanh", "2");
        put("nhon", "3");
        put("truongjr", "4");
    }};

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserSettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserSettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserSettingFragment newInstance(String param1, String param2) {
        UserSettingFragment fragment = new UserSettingFragment();
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
        sessionManager = new SessionManager(getContext());
        View view = inflater.inflate(R.layout.fragment_user_setting, container, false);
        int spacing = getContext().getResources().getDimensionPixelSize(R.dimen.spacing_24);
        List<Object> userSettingsItems = new ArrayList<>();
        String username = "";
        addItems(userSettingsItems, username);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new VerticalViewItemDecoration(spacing));
        UserSettingAdapter adapter = new UserSettingAdapter(userSettingsItems, this);
        recyclerView.setAdapter(adapter);

        ImageButton buttonBack = view.findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });
        return view;
    }

    private void addItems(List<Object> userSettingsItems, String username) {
        // TODO DONE: LẤY THÔNG TIN USER ĐANG ĐĂNG NHẬP
        userSettingsItems.add(new UserSettingHeaderData(null, "NhatThanh"));
        userSettingsItems.add("Account");
        userSettingsItems.add(new UserSettingOptionData(
                "Email",
                map.get(username),
                USER_SETTING_EMAIL
        ));
        userSettingsItems.add("Other");
        userSettingsItems.add(new UserSettingOptionData(
                "Sign out",
                "You are sign in under the name " + username,
                USER_SETTING_LOG_OUT
        ));
    }

    @Override
    public void onSignOutClickedListener() {
        getActivity().finish();
        sessionManager.setUserId(-1);
        sessionManager.setLogin(false);
        startActivity(new Intent(getActivity(), StartActivity.class));
    }

    @Override
    public void onUserClickedListener() {
        UserFragment fragment = new UserFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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