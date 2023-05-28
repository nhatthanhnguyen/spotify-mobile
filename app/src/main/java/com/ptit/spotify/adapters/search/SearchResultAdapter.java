package com.ptit.spotify.adapters.search;

import static com.ptit.spotify.utils.ItemType.ALBUM;
import static com.ptit.spotify.utils.ItemType.ARTIST;
import static com.ptit.spotify.utils.ItemType.PLAYLIST;
import static com.ptit.spotify.utils.ItemType.SONG;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;
import com.ptit.spotify.dto.data.SearchItemResultData;
import com.ptit.spotify.utils.ItemType;
import com.ptit.spotify.utils.OnItemSearchResultClickedListener;
import com.ptit.spotify.viewholders.search.SearchItemViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SearchResultAdapter extends RecyclerView.Adapter<SearchItemViewHolder> {
    private List<SearchItemResultData> items;
    private boolean isFiltered;
    private OnItemSearchResultClickedListener onItemSearchResultClickedListener;

    @NonNull
    @Override
    public SearchItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_search_item, parent, false);
        return new SearchItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchItemViewHolder holder, int position) {
        SearchItemResultData data = items.get(position);
        ItemType type = data.getType();
        holder.textViewTitle.setText(data.getTitle());
        String description = getDescription(data);
        holder.textViewDescription.setVisibility(View.VISIBLE);
        if (description == null) holder.textViewDescription.setVisibility(View.GONE);
        else holder.textViewDescription.setText(getDescription(data));
        holder.imageView.setVisibility(View.VISIBLE);
        holder.imageViewCircle.setVisibility(View.VISIBLE);
        if (type == ARTIST) {
            holder.buttonMoreSetting.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.GONE);
            if (data.getImageUrl() == null) holder.imageViewCircle.setVisibility(View.GONE);
            else Picasso.get().load(data.getImageUrl()).into(holder.imageViewCircle);
        }
        if (type == PLAYLIST || type == ALBUM) {
            holder.buttonMoreSetting.setVisibility(View.GONE);
            holder.imageViewCircle.setVisibility(View.GONE);
            if (data.getImageUrl() == null) holder.imageView.setVisibility(View.GONE);
            else Picasso.get().load(data.getImageUrl()).into(holder.imageView);
        }
        if (type == SONG) {
            holder.imageViewCircle.setVisibility(View.GONE);
            holder.buttonMoreSetting.setOnClickListener(v -> {
                onItemSearchResultClickedListener.onSongSettingClickedListener(data);
            });
            if (data.getImageUrl() == null) holder.imageView.setVisibility(View.GONE);
            else Picasso.get().load(data.getImageUrl()).into(holder.imageView);
        }
        holder.itemView.setOnClickListener(v -> {
            onItemSearchResultClickedListener.onItemClickedListener(data);
        });
    }

    private String getDescription(SearchItemResultData data) {
        ItemType type = data.getType();
        if (type == PLAYLIST) {
            if (isFiltered) return data.getArtistName();
            return String.format("Playlist • %s", data.getArtistName());
        }
        if (type == ARTIST) {
            if (isFiltered) return null;
            return "Artist";
        }
        if (type == SONG) {
            if (isFiltered) return data.getArtistName();
            return String.format("Song • %s", data.getArtistName());
        }
        if (type == ALBUM) {
            if (isFiltered) return data.getArtistName();
            return String.format("Album • %s", data.getArtistName());
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
