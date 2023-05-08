package com.ptit.spotify.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;
import com.ptit.spotify.activities.SettingsActivity;
import com.ptit.spotify.itemdecorations.HorizontalViewItemDecoration;
import com.ptit.spotify.models.data.GreetingData;
import com.ptit.spotify.models.data.SectionData;
import com.ptit.spotify.models.data.SectionMoreLikeData;
import com.ptit.spotify.utils.CircleTransform;
import com.ptit.spotify.utils.Constants;
import com.ptit.spotify.viewholders.GreetingViewHolder;
import com.ptit.spotify.viewholders.SectionMoreLikeViewHolder;
import com.ptit.spotify.viewholders.SectionViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int UNKNOWN_DATA = -1;
    private final int GREETING_DATA = 0;
    private final int SECTION_DATA = 1;
    private final int SECTION_MORE_LIKE_DATA = 2;
    private List<Object> homeDataList;
    private OnItemClickedListener itemClickedListener;

    public HomeAdapter(List<Object> homeDataList, OnItemClickedListener itemClickedListener) {
        this.homeDataList = homeDataList;
        this.itemClickedListener = itemClickedListener;
    }

    @Override
    public int getItemViewType(int position) {
        Object data = homeDataList.get(position);
        if (data instanceof GreetingData) {
            return GREETING_DATA;
        }
        if (data instanceof SectionData) {
            return SECTION_DATA;
        }
        if (data instanceof SectionMoreLikeData) {
            return SECTION_MORE_LIKE_DATA;
        }
        return UNKNOWN_DATA;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case GREETING_DATA:
                View viewHello = inflater.inflate(R.layout.layout_home_greeting, parent, false);
                return new GreetingViewHolder(viewHello);
            case SECTION_DATA:
                View viewSection = inflater.inflate(R.layout.layout_home_section, parent, false);
                return new SectionViewHolder(viewSection);
            case SECTION_MORE_LIKE_DATA:
                View viewSectionMoreLike = inflater.inflate(R.layout.layout_home_section_more_like, parent, false);
                return new SectionMoreLikeViewHolder(viewSectionMoreLike);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int horizontalSpacing = holder.itemView.getContext().getResources().getDimensionPixelSize(R.dimen.spacing_16);
        if (holder instanceof GreetingViewHolder) {
            GreetingViewHolder greetingViewHolder = (GreetingViewHolder) holder;
            GreetingData greetingDataItem = (GreetingData) homeDataList.get(position);
            greetingViewHolder.textViewGreeting.setText(greetingDataItem.getGreetingStr());
            greetingViewHolder.buttonSettings.setOnClickListener(view -> {
                Context context = view.getContext();
                Intent intent = new Intent(context, SettingsActivity.class);
                context.startActivity(intent);
            });
        }

        if (holder instanceof SectionViewHolder) {
            SectionViewHolder sectionViewHolder = (SectionViewHolder) holder;
            SectionData sectionDataItem = (SectionData) homeDataList.get(position);
            sectionViewHolder.textViewSectionTitle.setText(sectionDataItem.getTitle());

            CardAdapter cardAdapter = new CardAdapter(sectionDataItem.getCards(), itemClickedListener);
            sectionViewHolder.recyclerViewSection.setAdapter(cardAdapter);
            sectionViewHolder.recyclerViewSection.addItemDecoration(new HorizontalViewItemDecoration(horizontalSpacing));
        }

        if (holder instanceof SectionMoreLikeViewHolder) {
            SectionMoreLikeViewHolder sectionMoreLikeViewHolder = (SectionMoreLikeViewHolder) holder;
            SectionMoreLikeData sectionMoreLikeData = (SectionMoreLikeData) homeDataList.get(position);
            sectionMoreLikeViewHolder.textViewTitle.setText(sectionMoreLikeData.getTitle());
            if (sectionMoreLikeData.getType().equals(Constants.ALBUM)) {
                sectionMoreLikeViewHolder.linearLayoutTitle.setOnClickListener(view -> {
                    itemClickedListener.onItemClickedListener(Constants.ALBUM);
                });
            }
            if (sectionMoreLikeData.getType().equals(Constants.ARTIST)) {
                sectionMoreLikeViewHolder.linearLayoutTitle.setOnClickListener(view -> {
                    itemClickedListener.onItemClickedListener(Constants.ARTIST);
                });
            }
            if (sectionMoreLikeData.getType().equals(Constants.ARTIST)) {
                Picasso.get().load(sectionMoreLikeData.getImageUrl()).transform(new CircleTransform()).into(sectionMoreLikeViewHolder.imageView);
            } else {
                Picasso.get().load(sectionMoreLikeData.getImageUrl()).into(sectionMoreLikeViewHolder.imageView);
            }

            CardAdapter cardAdapter = new CardAdapter(sectionMoreLikeData.getCards(), itemClickedListener);
            sectionMoreLikeViewHolder.recyclerViewSection.setAdapter(cardAdapter);
            sectionMoreLikeViewHolder.recyclerViewSection.addItemDecoration(new HorizontalViewItemDecoration(horizontalSpacing));
        }
    }

    @Override
    public int getItemCount() {
        return homeDataList.size();
    }

    public interface OnItemClickedListener {
        void onItemClickedListener(String type);
    }
}
