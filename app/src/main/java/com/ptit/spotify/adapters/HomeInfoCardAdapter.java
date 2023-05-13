package com.ptit.spotify.adapters;

import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;
import com.ptit.spotify.dto.data.HomeCardData;
import com.ptit.spotify.utils.CircleTransform;
import com.ptit.spotify.utils.Constants;
import com.ptit.spotify.utils.TypeEntity;
import com.ptit.spotify.viewholders.HomeInfoCardViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeInfoCardAdapter extends RecyclerView.Adapter<HomeInfoCardViewHolder> {
    private List<HomeCardData> homeCardDataItems;
    private HomeInfoAdapter.OnItemClickedListener itemClickedListener;

    public HomeInfoCardAdapter(List<HomeCardData> homeCardDataItems, HomeInfoAdapter.OnItemClickedListener itemClickedListener) {
        this.homeCardDataItems = homeCardDataItems;
        this.itemClickedListener = itemClickedListener;
    }

    @Override
    public int getItemViewType(int position) {
        String type = homeCardDataItems.get(position).getType();
        if (type.equals(Constants.ARTIST)) return TypeEntity.ARTIST.ordinal();
        if (type.equals(Constants.ALBUM)) return TypeEntity.ALBUM.ordinal();
        if (type.equals(Constants.PLAYLIST)) return TypeEntity.PLAYLIST.ordinal();
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public HomeInfoCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_home_card, parent, false);
        return new HomeInfoCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeInfoCardViewHolder holder, int position) {
        HomeCardData homeCardDataItem = homeCardDataItems.get(position);
        Resources resources = holder.itemView.getResources();
        int smallWidth = resources.getDimensionPixelSize(R.dimen.card_width_small);
        int normalWidth = resources.getDimensionPixelSize(R.dimen.card_width);
        int cardSize = homeCardDataItem.isSmallCard() ? smallWidth : normalWidth;
        final String type = homeCardDataItem.getType();

        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(cardSize, LinearLayout.LayoutParams.WRAP_CONTENT);
        holder.linearLayoutCard.setLayoutParams(linearLayoutParams);
        if (homeCardDataItem.getType().equals(Constants.ALBUM)) {
            holder.itemView.setOnClickListener(view -> {
                itemClickedListener.onItemClickedListener(Constants.ALBUM);
            });
        }

        if (homeCardDataItem.getType().equals(Constants.ARTIST)) {
            holder.itemView.setOnClickListener(v -> {
                itemClickedListener.onItemClickedListener(Constants.ARTIST);
            });
        }

        if (homeCardDataItem.getType().equals(Constants.PLAYLIST)) {
            holder.itemView.setOnClickListener(v -> {
                itemClickedListener.onItemClickedListener(Constants.PLAYLIST);
            });
        }

        holder.textViewTitle.setText(homeCardDataItem.getTitle());
        holder.textViewTitle.setGravity(type.equals(Constants.ARTIST) ? Gravity.CENTER : Gravity.START);
        holder.textViewTitle.setVisibility(type.equals(Constants.ARTIST) ? TextView.VISIBLE : homeCardDataItem.isHiddenTitle() ? TextView.GONE : TextView.VISIBLE);

        holder.textViewDescription.setText(homeCardDataItem.getDescription());
        holder.textViewDescription.setVisibility(type.equals(Constants.ARTIST) ? TextView.GONE : homeCardDataItem.isHiddenDescription() ? TextView.GONE : TextView.VISIBLE);

        ViewGroup.LayoutParams imageLayoutParams = holder.imageView.getLayoutParams();
        imageLayoutParams.width = cardSize;
        imageLayoutParams.height = cardSize;
        holder.imageView.setLayoutParams(imageLayoutParams);

        if (homeCardDataItem.getType().equals(Constants.ARTIST)) {
            Picasso.get().load(homeCardDataItem.getImageUrl()).transform(new CircleTransform()).into(holder.imageView);
        } else {
            Picasso.get().load(homeCardDataItem.getImageUrl()).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return homeCardDataItems.size();
    }
}
