package com.ptit.spotify.viewholders.album;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;

public class AlbumHeaderViewHolder extends RecyclerView.ViewHolder {
    public LinearLayout linearLayoutHeader;
    public ImageButton buttonBack;
    public ImageView imageViewAlbum;
    public TextView textViewAlbumName;
    public ImageView imageViewArtist;
    public TextView textViewArtistName;
    public TextView textViewAlbumDateReleased;
    public ImageButton buttonPlayAlbum;
    public ImageButton buttonLikeAlbum;
    public ImageButton buttonDownloadAlbum;
    public ImageButton buttonMoreSettingAlbum;

    public AlbumHeaderViewHolder(@NonNull View itemView) {
        super(itemView);
        linearLayoutHeader = itemView.findViewById(R.id.linearLayoutHeader);
        buttonBack = itemView.findViewById(R.id.buttonBack);
        imageViewAlbum = itemView.findViewById(R.id.imageViewAlbumImage);
        textViewAlbumName = itemView.findViewById(R.id.textViewAlbumName);
        imageViewArtist = itemView.findViewById(R.id.imageViewArtist);
        textViewArtistName = itemView.findViewById(R.id.textViewArtistName);
        textViewAlbumDateReleased = itemView.findViewById(R.id.textViewAlbumDateReleased);
        buttonPlayAlbum = itemView.findViewById(R.id.buttonPlayAlbum);
        buttonLikeAlbum = itemView.findViewById(R.id.buttonLikeAlbum);
        buttonDownloadAlbum = itemView.findViewById(R.id.buttonDownloadAlbum);
        buttonMoreSettingAlbum = itemView.findViewById(R.id.buttonMoreSettingAlbum);
    }
}
