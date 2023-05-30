package com.ptit.spotify.adapters.home;

import static com.ptit.spotify.utils.ItemType.ARTIST;
import static com.ptit.spotify.utils.ItemType.BLANK;
import static com.ptit.spotify.utils.ItemType.HOME_GREETING;
import static com.ptit.spotify.utils.ItemType.HOME_SECTION;
import static com.ptit.spotify.utils.ItemType.HOME_SECTION_MORE_LIKE;

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
import com.ptit.spotify.utils.OnItemHomeClickedListener;
import com.ptit.spotify.viewholders.blank.BlankViewHolder;
import com.ptit.spotify.viewholders.home.HomeGreetingViewHolder;
import com.ptit.spotify.viewholders.home.HomeSectionMoreLikeViewHolder;
import com.ptit.spotify.viewholders.home.HomeSectionViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Object> homeDataList;
    private OnItemHomeClickedListener onItemHomeClickedListener;

    public HomeAdapter(List<Object> homeDataList, OnItemHomeClickedListener onItemHomeClickedListener) {
        this.homeDataList = homeDataList;
        this.onItemHomeClickedListener = onItemHomeClickedListener;
    }

    @Override
    public int getItemViewType(int position) {
        Object data = homeDataList.get(position);
        if (data instanceof HomeGreetingData) {
            return HOME_GREETING.ordinal();
        }
        if (data instanceof HomeSectionData) {
            HomeSectionData sectionData = (HomeSectionData) data;
            if (sectionData.getHeader() instanceof HomeCardData) {
                return HOME_SECTION_MORE_LIKE.ordinal();
            }
            return HOME_SECTION.ordinal();
        }
        return BLANK.ordinal();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == HOME_GREETING.ordinal()) {
            view = inflater.inflate(R.layout.layout_home_greeting, parent, false);
            return new HomeGreetingViewHolder(view);
        }

        if (viewType == HOME_SECTION.ordinal()) {
            view = inflater.inflate(R.layout.layout_home_section, parent, false);
            return new HomeSectionViewHolder(view);
        }

        if (viewType == HOME_SECTION_MORE_LIKE.ordinal()) {
            view = inflater.inflate(R.layout.layout_home_section_more_like, parent, false);
            return new HomeSectionMoreLikeViewHolder(view);
        }

        view = inflater.inflate(R.layout.layout_blank_item, parent, false);
        return new BlankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int horizontalSpacing = holder.itemView.getContext().getResources().getDimensionPixelSize(R.dimen.spacing_16);
        if (holder instanceof HomeGreetingViewHolder) {
            HomeGreetingViewHolder viewHolder = (HomeGreetingViewHolder) holder;
            HomeGreetingData data = (HomeGreetingData) homeDataList.get(position);
            viewHolder.textViewGreeting.setText(data.getGreetingStr());
            viewHolder.buttonUserSettings.setOnClickListener(view -> {
                onItemHomeClickedListener.onUserSettingClickedListener();
            });

            viewHolder.buttonRecentlyPlayed.setOnClickListener(view -> {
                Toast.makeText(holder.itemView.getContext(), "You press the button recently played", Toast.LENGTH_SHORT).show();
            });
        }

        if (holder instanceof HomeSectionViewHolder) {
            HomeSectionViewHolder viewHolder = (HomeSectionViewHolder) holder;
            HomeSectionData data = (HomeSectionData) homeDataList.get(position);
            String headerStr = (String) data.getHeader();
            viewHolder.textViewSectionTitle.setText(headerStr);

            HomeCardAdapter homeCardAdapter = new HomeCardAdapter(data.getCards(), onItemHomeClickedListener);
            viewHolder.recyclerViewSection.setAdapter(homeCardAdapter);
            viewHolder.recyclerViewSection.addItemDecoration(new HorizontalViewItemDecoration(horizontalSpacing));
        }

        if (holder instanceof HomeSectionMoreLikeViewHolder) {
            HomeSectionMoreLikeViewHolder viewHolder = (HomeSectionMoreLikeViewHolder) holder;
            HomeSectionData sectionData = (HomeSectionData) homeDataList.get(position);
            HomeCardData data = (HomeCardData) sectionData.getHeader();
            viewHolder.textViewTitle.setText(data.getTitle());
            viewHolder.linearLayoutTitle.setOnClickListener(v -> {
                onItemHomeClickedListener.onCardClickedListener(data.getType(), Integer.parseInt(data.getId()));
            });
            if (data.getType().equals(ARTIST)) {
                Picasso.get().load(data.getImageUrl()).transform(new CircleTransform()).into(viewHolder.imageView);
            } else {
                Picasso.get().load(data.getImageUrl()).into(viewHolder.imageView);
            }

            HomeCardAdapter homeCardAdapter = new HomeCardAdapter(sectionData.getCards(), onItemHomeClickedListener);
            viewHolder.recyclerViewSection.setAdapter(homeCardAdapter);
            viewHolder.recyclerViewSection.addItemDecoration(new HorizontalViewItemDecoration(horizontalSpacing));
        }
    }

    @Override
    public int getItemCount() {
        return homeDataList.size();
    }
}
