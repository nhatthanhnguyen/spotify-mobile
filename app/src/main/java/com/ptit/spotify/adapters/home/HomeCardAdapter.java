package com.ptit.spotify.adapters.home;

import static com.ptit.spotify.utils.ItemType.ARTIST;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;
import com.ptit.spotify.dto.data.HomeCardData;
import com.ptit.spotify.utils.CircleTransform;
import com.ptit.spotify.utils.ItemType;
import com.ptit.spotify.utils.OnItemHomeClickedListener;
import com.ptit.spotify.viewholders.home.HomeCardViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeCardAdapter extends RecyclerView.Adapter<HomeCardViewHolder> {
    private List<HomeCardData> homeCardDataItems;
    private OnItemHomeClickedListener onItemHomeClickedListener;

    public HomeCardAdapter(List<HomeCardData> homeCardDataItems, OnItemHomeClickedListener onItemHomeClickedListener) {
        this.homeCardDataItems = homeCardDataItems;
        this.onItemHomeClickedListener = onItemHomeClickedListener;
    }

    @Override
    public int getItemViewType(int position) {
        ItemType type = homeCardDataItems.get(position).getType();
        return type.ordinal();
    }

    @NonNull
    @Override
    public HomeCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_home_card, parent, false);
        return new HomeCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeCardViewHolder holder, int position) {
        HomeCardData data = homeCardDataItems.get(position);
        final ItemType type = data.getType();
        holder.itemView.setOnClickListener(v -> {
            onItemHomeClickedListener.onCardClickedListener(data.getType());
        });

        holder.textViewTitle.setText(data.getTitle());
        holder.textViewTitle.setGravity(type.equals(ARTIST) ? Gravity.CENTER : Gravity.START);
        holder.textViewTitle.setVisibility(type.equals(ARTIST) ? TextView.VISIBLE : data.getTitle() == null ? TextView.GONE : TextView.VISIBLE);

        holder.textViewDescription.setText(data.getDescription());
        holder.textViewDescription.setVisibility(type.equals(ARTIST) ? TextView.GONE : data.getDescription() == null ? TextView.GONE : TextView.VISIBLE);

        ViewGroup.LayoutParams imageLayoutParams = holder.imageView.getLayoutParams();
        holder.imageView.setLayoutParams(imageLayoutParams);

        if (data.getType().equals(ARTIST)) {
            Picasso.get().load(data.getImageUrl()).transform(new CircleTransform()).into(holder.imageView);
        } else {
            Picasso.get().load(data.getImageUrl()).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return homeCardDataItems.size();
    }
}
