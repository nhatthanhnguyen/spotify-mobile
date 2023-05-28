package com.ptit.spotify.fragments;

import static com.ptit.spotify.utils.ItemType.ARTIST;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ptit.spotify.R;
import com.ptit.spotify.adapters.add.ItemAddAdapter;
import com.ptit.spotify.dto.data.AddItemData;
import com.ptit.spotify.dto.data.AlbumHeaderData;
import com.ptit.spotify.dto.model.Album;
import com.ptit.spotify.dto.model.Artist;
import com.ptit.spotify.itemdecorations.HorizontalViewItemDecoration;
import com.ptit.spotify.itemdecorations.VerticalViewItemDecoration;
import com.ptit.spotify.utils.Constants;
import com.ptit.spotify.utils.HttpUtils;
import com.ptit.spotify.utils.OnItemSearchAddClickedListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

public class ArtistAddFragment extends Fragment implements OnItemSearchAddClickedListener {
    private List<AddItemData> resultItems;
    private List<AddItemData> resultItemsAll;
    private RecyclerView recyclerViewFilter;
    private RecyclerView recyclerViewResult;
    private ItemAddAdapter resultAdapter;

    public ArtistAddFragment() {
        // Required empty public constructor
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
                    return;
                }
                buttonCancel.setVisibility(View.VISIBLE);
                recyclerViewFilter.setVisibility(View.VISIBLE);
                recyclerViewResult.setVisibility(View.VISIBLE);
                addData();
                resultItemsAll = new ArrayList<>(resultItems);
                resultAdapter = new ItemAddAdapter(resultItems,
                        ArtistAddFragment.this);
                recyclerViewResult.setAdapter(resultAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    void addData() {
        // TODO DONE: LẤY TOÀN BỘ NGHỆ SĨ
        JSONObject jsonBody = new JSONObject();
        final String mRequestBody = jsonBody.toString();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constants.getAllArtistEndpoint(), new JSONObject(), new Response.Listener<JSONObject>() {
            @SneakyThrows
            @Override
            public void onResponse(JSONObject response) {
                Log.i("LOG_RESPONSE", String.valueOf(response));
                Gson gson = new Gson();
                JSONArray items = response.optJSONArray("artists");
                if(items != null) {
                    for(int i = 0; i < items.length(); i++) {
                        Artist at = gson.fromJson(items.get(i).toString(), Artist.class);
                        AddItemData data = new AddItemData(
                                String.valueOf(at.getArtistID()),
                                at.getCoverImg(),
                                at.getName(),
                                at.getDescription(),
                                at.getName(),
                                null,
                                ARTIST
                        );
                        resultItems.add(data);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_RESPONSE", error.toString());
            }
        });
        HttpUtils.getInstance(getContext()).getRequestQueue().add(jsonObjectRequest);
//        resultItems = new ArrayList<>();
//        resultItems.add(new AddItemData(
//                "",
//                "https://i.scdn.co/image/ab67616d00001e02b94f78cf2a6ac9c700ee2812",
//                "Emanuel Fremont",
//                null,
//                null,
//                null,
//                ARTIST
//        ));
//        resultItems.add(new AddItemData(
//                "",
//                "https://i.scdn.co/image/ab67616d00001e02b94f78cf2a6ac9c700ee2812",
//                "Emanuel Fremont",
//                null,
//                null,
//                null,
//                ARTIST
//        ));
//        resultItems.add(new AddItemData(
//                "",
//                "https://i.scdn.co/image/ab67616d00001e02b94f78cf2a6ac9c700ee2812",
//                "Emanuel Fremont",
//                null,
//                null,
//                null,
//                ARTIST
//        ));
//        resultItems.add(new AddItemData(
//                "",
//                "https://i.scdn.co/image/ab67616d00001e02b94f78cf2a6ac9c700ee2812",
//                "Emanuel Fremont",
//                null,
//                null,
//                null,
//                ARTIST
//        ));
//        resultItems.add(new AddItemData(
//                "",
//                "https://i.scdn.co/image/ab67616d00001e02b94f78cf2a6ac9c700ee2812",
//                "Emanuel Fremont",
//                null,
//                null,
//                null,
//                ARTIST
//        ));
    }

    @Override
    public void onAddItemClickedListener(AddItemData data) {
        Toast.makeText(getContext(),
                String.format("Artist %s has been added to your library", data.getTitle()),
                Toast.LENGTH_SHORT).show();
    }
}