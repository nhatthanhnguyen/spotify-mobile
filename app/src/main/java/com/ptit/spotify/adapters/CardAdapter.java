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
import com.ptit.spotify.models.data.CardData;
import com.ptit.spotify.utils.CircleTransform;
import com.ptit.spotify.utils.Constants;
import com.ptit.spotify.utils.Type;
import com.ptit.spotify.viewholders.CardViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardViewHolder> {
    private List<CardData> cardDataItems;
    private HomeAdapter.OnItemClickedListener itemClickedListener;

    public CardAdapter(List<CardData> cardDataItems, HomeAdapter.OnItemClickedListener itemClickedListener) {
        this.cardDataItems = cardDataItems;
        this.itemClickedListener = itemClickedListener;
    }

    @Override
    public int getItemViewType(int position) {
        String type = cardDataItems.get(position).getType();
        if (type.equals(Constants.ARTIST)) return Type.ARTIST.ordinal();
        if (type.equals(Constants.ALBUM)) return Type.ALBUM.ordinal();
        if (type.equals(Constants.PLAYLIST)) return Type.PLAYLIST.ordinal();
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_home_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardData cardDataItem = cardDataItems.get(position);
        Resources resources = holder.itemView.getResources();
        int smallWidth = resources.getDimensionPixelSize(R.dimen.card_width_small);
        int normalWidth = resources.getDimensionPixelSize(R.dimen.card_width);
        int cardSize = cardDataItem.isSmallCard() ? smallWidth : normalWidth;
        final String type = cardDataItem.getType();

        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(cardSize, LinearLayout.LayoutParams.WRAP_CONTENT);
        holder.linearLayoutCard.setLayoutParams(linearLayoutParams);
        if (cardDataItem.getType().equals(Constants.ALBUM)) {
            holder.itemView.setOnClickListener(view -> {
                itemClickedListener.onItemClickedListener(Constants.ALBUM);
            });
        }
        holder.textViewTitle.setText(cardDataItem.getTitle());
        holder.textViewTitle.setGravity(type.equals(Constants.ARTIST) ? Gravity.CENTER : Gravity.START);
        holder.textViewTitle.setVisibility(type.equals(Constants.ARTIST) ? TextView.VISIBLE : cardDataItem.isHiddenTitle() ? TextView.GONE : TextView.VISIBLE);

        holder.textViewDescription.setText(cardDataItem.getDescription());
        holder.textViewDescription.setVisibility(type.equals(Constants.ARTIST) ? TextView.GONE : cardDataItem.isHiddenDescription() ? TextView.GONE : TextView.VISIBLE);

        ViewGroup.LayoutParams imageLayoutParams = holder.imageView.getLayoutParams();
        imageLayoutParams.width = cardSize;
        imageLayoutParams.height = cardSize;
        holder.imageView.setLayoutParams(imageLayoutParams);

        if (cardDataItem.getType().equals(Constants.ARTIST)) {
            Picasso.get().load(cardDataItem.getImageUrl()).transform(new CircleTransform()).into(holder.imageView);
        } else {
            Picasso.get().load(cardDataItem.getImageUrl()).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return cardDataItems.size();
    }
}
