package com.ptit.spotify.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.spotify.R;
import com.ptit.spotify.dto.data.HomeCardData;
import com.ptit.spotify.dto.data.HomeGreetingData;
import com.ptit.spotify.dto.data.HomeSectionData;
import com.ptit.spotify.itemdecorations.HorizontalViewItemDecoration;
import com.ptit.spotify.utils.CircleTransform;
import com.ptit.spotify.utils.Constants;
import com.ptit.spotify.utils.TypeHomeItem;
import com.ptit.spotify.viewholders.HomeInfoGreetingViewHolder;
import com.ptit.spotify.viewholders.HomeInfoSectionMoreLikeViewHolder;
import com.ptit.spotify.viewholders.HomeInfoSectionViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Object> homeDataList;
    private OnItemClickedListener itemClickedListener;

    public HomeInfoAdapter(List<Object> homeDataList, OnItemClickedListener itemClickedListener) {
        this.homeDataList = homeDataList;
        this.itemClickedListener = itemClickedListener;
    }

    @Override
    public int getItemViewType(int position) {
        Object data = homeDataList.get(position);
        if (data instanceof HomeGreetingData) {
            return TypeHomeItem.GREETING.ordinal();
        }
        if (data instanceof HomeSectionData) {
            HomeSectionData sectionData = (HomeSectionData) data;
            if (sectionData.getHeader() instanceof HomeCardData) {
                return TypeHomeItem.SECTION_MORE_LIKE.ordinal();
            }
            return TypeHomeItem.SECTION.ordinal();
        }
        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TypeHomeItem.GREETING.ordinal()) {
            View viewHello = inflater.inflate(R.layout.layout_home_greeting, parent, false);
            return new HomeInfoGreetingViewHolder(viewHello);
        }

        if (viewType == TypeHomeItem.SECTION.ordinal()) {
            View viewSection = inflater.inflate(R.layout.layout_home_section, parent, false);
            return new HomeInfoSectionViewHolder(viewSection);
        }

        if (viewType == TypeHomeItem.SECTION_MORE_LIKE.ordinal()) {
            View viewSectionMoreLike = inflater.inflate(R.layout.layout_home_section_more_like, parent, false);
            return new HomeInfoSectionMoreLikeViewHolder(viewSectionMoreLike);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int horizontalSpacing = holder.itemView.getContext().getResources().getDimensionPixelSize(R.dimen.spacing_16);
        if (holder instanceof HomeInfoGreetingViewHolder) {
            HomeInfoGreetingViewHolder greetingViewHolder = (HomeInfoGreetingViewHolder) holder;
            HomeGreetingData homeGreetingDataItem = (HomeGreetingData) homeDataList.get(position);
            greetingViewHolder.textViewGreeting.setText(homeGreetingDataItem.getGreetingStr());
            greetingViewHolder.buttonUserSettings.setOnClickListener(view -> {
                itemClickedListener.onItemClickedUserSettingsListener();
            });

            greetingViewHolder.buttonRecentlyPlayed.setOnClickListener(view -> {
                Toast.makeText(holder.itemView.getContext(), "You press the button recently played", Toast.LENGTH_SHORT).show();
            });

//            greetingViewHolder.buttonNotification.setOnClickListener(view -> {
//                Toast.makeText(holder.itemView.getContext(), "You press the button notification", Toast.LENGTH_SHORT).show();
//            });
        }

        if (holder instanceof HomeInfoSectionViewHolder) {
            HomeInfoSectionViewHolder sectionViewHolder = (HomeInfoSectionViewHolder) holder;
            HomeSectionData sectionDataItem = (HomeSectionData) homeDataList.get(position);
            String headerStr = (String) sectionDataItem.getHeader();
            sectionViewHolder.textViewSectionTitle.setText(headerStr);

            HomeInfoCardAdapter homeInfoCardAdapter = new HomeInfoCardAdapter(sectionDataItem.getCards(), itemClickedListener);
            sectionViewHolder.recyclerViewSection.setAdapter(homeInfoCardAdapter);
            sectionViewHolder.recyclerViewSection.addItemDecoration(new HorizontalViewItemDecoration(horizontalSpacing));
        }

        if (holder instanceof HomeInfoSectionMoreLikeViewHolder) {
            HomeInfoSectionMoreLikeViewHolder sectionViewHolder = (HomeInfoSectionMoreLikeViewHolder) holder;
            HomeSectionData sectionData = (HomeSectionData) homeDataList.get(position);
            HomeCardData headerData = (HomeCardData) sectionData.getHeader();
            sectionViewHolder.textViewTitle.setText(headerData.getTitle());
            if (headerData.getType().equals(Constants.ALBUM)) {
                sectionViewHolder.linearLayoutTitle.setOnClickListener(view -> {
                    itemClickedListener.onItemClickedListener(Constants.ALBUM);
                });
            }
            if (headerData.getType().equals(Constants.ARTIST)) {
                sectionViewHolder.linearLayoutTitle.setOnClickListener(view -> {
                    itemClickedListener.onItemClickedListener(Constants.ARTIST);
                });
            }
            if (headerData.getType().equals(Constants.PLAYLIST)) {
                sectionViewHolder.linearLayoutTitle.setOnClickListener(v -> {
                    itemClickedListener.onItemClickedListener(Constants.PLAYLIST);
                });
            }

            if (headerData.getType().equals(Constants.ARTIST)) {
                Picasso.get().load(headerData.getImageUrl()).transform(new CircleTransform()).into(sectionViewHolder.imageView);
            } else {
                Picasso.get().load(headerData.getImageUrl()).into(sectionViewHolder.imageView);
            }

            HomeInfoCardAdapter homeInfoCardAdapter = new HomeInfoCardAdapter(sectionData.getCards(), itemClickedListener);
            sectionViewHolder.recyclerViewSection.setAdapter(homeInfoCardAdapter);
            sectionViewHolder.recyclerViewSection.addItemDecoration(new HorizontalViewItemDecoration(horizontalSpacing));
        }
    }

    @Override
    public int getItemCount() {
        return homeDataList.size();
    }

    public interface OnItemClickedListener {
        void onItemClickedListener(String type);

        void onItemClickedUserSettingsListener();
    }
}
