package com.ptit.spotify.viewholders.playlist;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;

public class PlaylistHeaderViewHolder extends RecyclerView.ViewHolder {
    public LinearLayout linearLayoutHeader;
    public ImageButton buttonBack;
    public LinearLayout linearLayoutSearch;
    public ImageView imageViewPlaylist;
    public TextView textViewPlaylistDescription;
    public ImageView imageViewUserCreated;
    public TextView textViewUserCreated;
    public TextView textViewNumberOfLikes;
    public TextView textViewTotalLength;
    public ImageButton buttonLikePlaylist;
    public ImageButton buttonDownloadPlaylist;
    public ImageButton buttonMoreSettingPlaylist;
    public ImageButton buttonPlayPlaylist;

    public PlaylistHeaderViewHolder(@NonNull View itemView) {
        super(itemView);
        linearLayoutHeader = itemView.findViewById(R.id.linearLayoutHeader);
        buttonBack = itemView.findViewById(R.id.buttonBack);
        linearLayoutSearch = itemView.findViewById(R.id.linearLayoutSearch);
        imageViewPlaylist = itemView.findViewById(R.id.imageViewPlaylist);
        textViewPlaylistDescription = itemView.findViewById(R.id.textViewPlaylistDescription);
        imageViewUserCreated = itemView.findViewById(R.id.imageViewUserCreated);
        textViewUserCreated = itemView.findViewById(R.id.textViewUserCreated);
        textViewNumberOfLikes = itemView.findViewById(R.id.textViewNumberOfLikes);
        textViewTotalLength = itemView.findViewById(R.id.textViewTotalLength);
        buttonLikePlaylist = itemView.findViewById(R.id.buttonLikePlaylist);
        buttonDownloadPlaylist = itemView.findViewById(R.id.buttonDownloadPlaylist);
        buttonMoreSettingPlaylist = itemView.findViewById(R.id.buttonMoreSettingPlaylist);
        buttonPlayPlaylist = itemView.findViewById(R.id.buttonPlayPlaylist);
    }
}
