package com.ptit.spotify.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ptit.spotify.R;
import com.ptit.spotify.adapters.setting.SettingAdapter;
import com.ptit.spotify.itemdecorations.SettingItemDecoration;
import com.ptit.spotify.utils.OnItemSettingClickedListener;

import java.util.List;

public class SettingFragment extends BottomSheetDialogFragment {
    private List<Object> items;
    private Object headerData;
    private OnItemSettingClickedListener onItemSettingClickedListener;

    public SettingFragment(
            List<Object> items,
            Object headerData,
            OnItemSettingClickedListener onItemSettingClickedListener
    ) {
        this.items = items;
        this.headerData = headerData;
        this.onItemSettingClickedListener = onItemSettingClickedListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_setting, null);
        bottomSheetDialog.setContentView(view);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayout);
        SettingAdapter settingAdapter = new SettingAdapter(items, headerData, onItemSettingClickedListener);
        recyclerView.setAdapter(settingAdapter);
        int height = getContext().getResources().getDimensionPixelOffset(R.dimen.spacing_16);
        recyclerView.addItemDecoration(new SettingItemDecoration(height));
        return bottomSheetDialog;
    }
}
