package com.ptit.spotify.viewholders.setting;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;

public class SettingOptionViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageViewSetting;
    public TextView textViewSetting;

    public SettingOptionViewHolder(@NonNull View itemView) {
        super(itemView);
        imageViewSetting = itemView.findViewById(R.id.imageViewSetting);
        textViewSetting = itemView.findViewById(R.id.textViewSetting);
    }
}
