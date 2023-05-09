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
import com.ptit.spotify.viewholders.HomeGreetingViewHolder;
import com.ptit.spotify.viewholders.HomeSectionMoreLikeViewHolder;
import com.ptit.spotify.viewholders.HomeSectionViewHolder;
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
                return new HomeGreetingViewHolder(viewHello);
            case SECTION_DATA:
                View viewSection = inflater.inflate(R.layout.layout_home_section, parent, false);
                return new HomeSectionViewHolder(viewSection);
            case SECTION_MORE_LIKE_DATA:
                View viewSectionMoreLike = inflater.inflate(R.layout.layout_home_section_more_like, parent, false);
                return new HomeSectionMoreLikeViewHolder(viewSectionMoreLike);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int horizontalSpacing = holder.itemView.getContext().getResources().getDimensionPixelSize(R.dimen.spacing_16);
        if (holder instanceof HomeGreetingViewHolder) {
            HomeGreetingViewHolder homeGreetingViewHolder = (HomeGreetingViewHolder) holder;
            GreetingData greetingDataItem = (GreetingData) homeDataList.get(position);
            homeGreetingViewHolder.textViewGreeting.setText(greetingDataItem.getGreetingStr());
            homeGreetingViewHolder.buttonSettings.setOnClickListener(view -> {
                Context context = view.getContext();
                Intent intent = new Intent(context, SettingsActivity.class);
                context.startActivity(intent);
            });
        }

        if (holder instanceof HomeSectionViewHolder) {
            HomeSectionViewHolder homeSectionViewHolder = (HomeSectionViewHolder) holder;
            SectionData sectionDataItem = (SectionData) homeDataList.get(position);
            homeSectionViewHolder.textViewSectionTitle.setText(sectionDataItem.getTitle());

            CardAdapter cardAdapter = new CardAdapter(sectionDataItem.getCards(), itemClickedListener);
            homeSectionViewHolder.recyclerViewSection.setAdapter(cardAdapter);
            homeSectionViewHolder.recyclerViewSection.addItemDecoration(new HorizontalViewItemDecoration(horizontalSpacing));
        }

        if (holder instanceof HomeSectionMoreLikeViewHolder) {
            HomeSectionMoreLikeViewHolder homeSectionMoreLikeViewHolder = (HomeSectionMoreLikeViewHolder) holder;
            SectionMoreLikeData sectionMoreLikeData = (SectionMoreLikeData) homeDataList.get(position);
            homeSectionMoreLikeViewHolder.textViewTitle.setText(sectionMoreLikeData.getTitle());
            if (sectionMoreLikeData.getType().equals(Constants.ALBUM)) {
                homeSectionMoreLikeViewHolder.linearLayoutTitle.setOnClickListener(view -> {
                    itemClickedListener.onItemClickedListener(Constants.ALBUM);
                });
            }
            if (sectionMoreLikeData.getType().equals(Constants.ARTIST)) {
                homeSectionMoreLikeViewHolder.linearLayoutTitle.setOnClickListener(view -> {
                    itemClickedListener.onItemClickedListener(Constants.ARTIST);
                });
            }
            if (sectionMoreLikeData.getType().equals(Constants.ARTIST)) {
                Picasso.get().load(sectionMoreLikeData.getImageUrl()).transform(new CircleTransform()).into(homeSectionMoreLikeViewHolder.imageView);
            } else {
                Picasso.get().load(sectionMoreLikeData.getImageUrl()).into(homeSectionMoreLikeViewHolder.imageView);
            }

            CardAdapter cardAdapter = new CardAdapter(sectionMoreLikeData.getCards(), itemClickedListener);
            homeSectionMoreLikeViewHolder.recyclerViewSection.setAdapter(cardAdapter);
            homeSectionMoreLikeViewHolder.recyclerViewSection.addItemDecoration(new HorizontalViewItemDecoration(horizontalSpacing));
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
