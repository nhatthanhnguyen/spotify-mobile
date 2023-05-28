package com.ptit.spotify.adapters.add;

import static com.ptit.spotify.utils.ItemType.ARTIST;
import static com.ptit.spotify.utils.ItemType.SONG;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;
import com.ptit.spotify.dto.data.AddItemData;
import com.ptit.spotify.utils.ItemType;
import com.ptit.spotify.utils.OnItemSearchAddClickedListener;
import com.ptit.spotify.viewholders.add.ItemAddViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ItemAddAdapter extends RecyclerView.Adapter<ItemAddViewHolder> {
    private List<AddItemData> items;
    private OnItemSearchAddClickedListener onItemSearchAddClickedListener;

    @NonNull
    @Override
    public ItemAddViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_library_add_item, parent, false);
        return new ItemAddViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAddViewHolder holder, int position) {
        AddItemData data = items.get(position);
        ItemType type = data.getType();
        holder.textViewTitle.setText(data.getTitle());
        String description = getDescription(data);
        holder.textViewDescription.setVisibility(View.VISIBLE);
        if (description == null) holder.textViewDescription.setVisibility(View.GONE);
        else holder.textViewDescription.setText(getDescription(data));
        holder.imageView.setVisibility(View.VISIBLE);
        holder.imageViewCircle.setVisibility(View.VISIBLE);
        if (type == ARTIST) {
            holder.imageView.setVisibility(View.GONE);
            holder.textViewTitle.setText(data.getTitle());
            Picasso.get().load(data.getImageUrl()).into(holder.imageViewCircle);
        }

        if (type == SONG) {
            holder.imageViewCircle.setVisibility(View.GONE);
            holder.textViewTitle.setText(data.getTitle());
            Picasso.get().load(data.getImageUrl()).into(holder.imageView);
        }

        holder.itemView.setOnClickListener(v -> {
            onItemSearchAddClickedListener.onAddItemClickedListener(data);
        });
    }

    private String getDescription(AddItemData data) {
        ItemType type = data.getType();
        if (type == ARTIST) {
            return null;
        }
        if (type == SONG) {
            return data.getArtistName();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
