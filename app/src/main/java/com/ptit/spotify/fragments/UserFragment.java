package com.ptit.spotify.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ptit.spotify.R;
import com.ptit.spotify.adapters.user.UserAdapter;
import com.ptit.spotify.dto.data.UserInfoHeaderData;
import com.ptit.spotify.dto.data.UserInfoPlaylistData;
import com.ptit.spotify.dto.model.PlayList;
import com.ptit.spotify.itemdecorations.VerticalViewItemDecoration;
import com.ptit.spotify.utils.Constants;
import com.ptit.spotify.utils.HttpUtils;
import com.ptit.spotify.utils.OnItemUserClickedListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

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
        String userId = "";
        List<Object> userInfoItems = new ArrayList<>();
        addItems(userInfoItems, userId);
        UserAdapter adapter = new UserAdapter(userInfoItems, this);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void addItems(List<Object> userInfoItems, String username) {
        // TODO DONE: LẤY THÔNG TIN CỦA USER ĐÃ ĐĂNG NHẬP
        userInfoItems.add(new UserInfoHeaderData(null, username, 0));
        // TODO DONE: LẤY NHỮNG PLAYLIST CỦA USER ĐÃ TẠO
        userInfoItems.add("Playlists");
        JsonObjectRequest jsonObjectPlaylistRequest = new JsonObjectRequest(Constants.getPlaylistByUserIdEndpoint(UserSettingFragment.map1.get(username)), new JSONObject(), new Response.Listener<JSONObject>() {
            @SneakyThrows
            @Override
            public void onResponse(JSONObject response) {
                Log.i("LOG_RESPONSE", String.valueOf(response));
                Gson gson = new Gson();
                JSONArray items = response.optJSONArray("playlists");
                if (items != null) {
                    for (int i = 0; i < items.length(); i++) {
                        PlayList at = gson.fromJson(items.get(i).toString(), PlayList.class);
                        UserInfoPlaylistData data = new UserInfoPlaylistData(at.getCover_img(), at.getName());
                        userInfoItems.add(data);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_RESPONSE", error.toString());
            }
        });
        HttpUtils.getInstance(getContext()).getRequestQueue().add(jsonObjectPlaylistRequest);
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