package com.ptit.spotify.adapters.album;

import static android.view.View.VISIBLE;
import static com.ptit.spotify.utils.ItemType.ALBUM;
import static com.ptit.spotify.utils.ItemType.ALBUM_HEADER;
import static com.ptit.spotify.utils.ItemType.ALBUM_SONG;
import static com.ptit.spotify.utils.ItemType.BLANK;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ptit.spotify.R;
import com.ptit.spotify.activities.ContentActivity;
import com.ptit.spotify.activities.PlayerActivity;
import com.ptit.spotify.dto.data.AlbumHeaderData;
import com.ptit.spotify.dto.data.AlbumSongData;
import com.ptit.spotify.dto.model.Artist;
import com.ptit.spotify.dto.model.Song;
import com.ptit.spotify.utils.Constants;
import com.ptit.spotify.utils.OnItemAlbumClickedListener;
import com.ptit.spotify.viewholders.album.AlbumHeaderViewHolder;
import com.ptit.spotify.viewholders.album.AlbumSongViewHolder;
import com.ptit.spotify.viewholders.blank.BlankViewHolder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter {
    private List<Object> albumData;
    private OnItemAlbumClickedListener onItemAlbumClickedListener;

    public AlbumAdapter(List<Object> albumData, OnItemAlbumClickedListener onItemAlbumClickedListener) {
        this.albumData = albumData;
        this.onItemAlbumClickedListener = onItemAlbumClickedListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == ALBUM_HEADER.ordinal()) {
            view = inflater.inflate(R.layout.layout_album_header, parent, false);
            return new AlbumHeaderViewHolder(view);
        }

        if (viewType == ALBUM_SONG.ordinal()) {
            view = inflater.inflate(R.layout.layout_album_song, parent, false);
            return new AlbumSongViewHolder(view);
        }

        view = inflater.inflate(R.layout.layout_blank_item, parent, false);
        return new BlankViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AlbumHeaderViewHolder) {
            AlbumHeaderViewHolder viewHolder = (AlbumHeaderViewHolder) holder;
            AlbumHeaderData data = (AlbumHeaderData) albumData.get(position);
            viewHolder.buttonBack.setOnClickListener(v -> {
                onItemAlbumClickedListener.onBackButtonClickedListener();
            });
            viewHolder.textViewAlbumDateReleased.setText(data.getAlbumDateReleased());
            viewHolder.textViewAlbumName.setText(data.getAlbumName());
            viewHolder.buttonMoreSettingAlbum.setOnClickListener(v -> {
                onItemAlbumClickedListener.onAlbumSettingClickedListener(data);
            });
            viewHolder.textViewArtistName.setText(data.getArtistName());
            if (data.isPlaying()) {
                viewHolder.buttonPlayPauseAlbum.setImageResource(R.drawable.ic_pause_green_large);
            } else {
                viewHolder.buttonPlayPauseAlbum.setImageResource(R.drawable.ic_play_green_large);
            }

            if (data.isLiked()) {
                viewHolder.buttonLikeAlbum.setImageResource(R.drawable.ic_like_filled);
            } else {
                viewHolder.buttonLikeAlbum.setImageResource(R.drawable.ic_like_outlined);
            }

            viewHolder.buttonLikeAlbum.setOnClickListener(v -> {
                if (data.isLiked()) {
                    data.setLiked(false);
                    notifyDataSetChanged();
                } else {
                    data.setLiked(true);
                    notifyDataSetChanged();
                }
            });

            viewHolder.buttonPlayPauseAlbum.setOnClickListener(v -> {
                ContentActivity.linearLayoutMiniPlayer.setVisibility(VISIBLE);
                if (data.isPlaying()) {
                    data.setPlaying(false);
                    viewHolder.buttonPlayPauseAlbum.setImageResource(R.drawable.ic_play_green_large);
                    ContentActivity.musicPlayerService.mediaPlayer.pause();
                    ContentActivity.musicPlayerService.isPlaying = false;
                    ContentActivity.buttonPlayPause.setImageResource(R.drawable.ic_play_arrow);
                    ContentActivity.musicPlayerService.sendNotificationMedia(ContentActivity.musicPlayerService.currentSong);
                    if (PlayerActivity.isBound) {
                        PlayerActivity.buttonPlayPause.setImageResource(R.drawable.ic_play_circle);
                    }
                    this.notifyDataSetChanged();
                } else { // album đang dừng
                    data.setPlaying(true);
                    RequestQueue requestQueue = Volley.newRequestQueue(viewHolder.itemView.getContext());
                    @SuppressLint("NotifyDataSetChanged") JsonObjectRequest jsonObjectSongRequest = new JsonObjectRequest(
                            Request.Method.GET,
                            Constants.getSongByAlbumIdEndpoint(String.valueOf(data.getId())),
                            null,
                            responseSong -> {
                                Log.i("LOG_RESPONSE", String.valueOf(responseSong));
                                Gson gson = new Gson();
                                JSONArray itemSongs = responseSong.optJSONArray("songs");
                                if (itemSongs == null) return;
                                List<Song> songs = new ArrayList<>();
                                for (int i = 0; i < itemSongs.length(); i++) {
                                    Song song = null;
                                    try {
                                        song = gson.fromJson(itemSongs.get(i).toString(), Song.class);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                    songs.add(song);
                                }
                                viewHolder.buttonPlayPauseAlbum.setImageResource(R.drawable.ic_pause_green_large);
                                ContentActivity.musicPlayerService.songs = songs;
                                // trước đó đã có bài hát?
                                if (ContentActivity.musicPlayerService.currentSong != null) {
                                    if (ContentActivity.musicPlayerService.type == ALBUM && ContentActivity.musicPlayerService.id == data.getId()) {
                                        ContentActivity.musicPlayerService.mediaPlayer.start();
                                        ContentActivity.musicPlayerService.isPlaying = true;
                                        ContentActivity.buttonPlayPause.setImageResource(R.drawable.ic_pause);
                                        ContentActivity.musicPlayerService.sendNotificationMedia(ContentActivity.musicPlayerService.currentSong);
                                        return;
                                    }
                                    ContentActivity.musicPlayerService.currentSong = ContentActivity.musicPlayerService.songs.get(0);
                                    setSelected((AlbumSongData) albumData.get(1));
                                    ContentActivity.musicPlayerService.mediaPlayer.reset();
                                    ContentActivity.musicPlayerService.type = ALBUM;
                                    ContentActivity.musicPlayerService.id = data.getId();
                                    ContentActivity.musicPlayerService.isPlaying = true;
                                    try {
                                        ContentActivity.musicPlayerService.mediaPlayer.setDataSource(
                                                ContentActivity.musicPlayerService.currentSong.getUrl().replace("http", "https"));
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    ContentActivity.musicPlayerService.mediaPlayer.prepareAsync();
                                    ContentActivity.buttonPlayPause.setImageResource(R.drawable.ic_pause);
                                    ContentActivity.textViewSongName.setText(((AlbumSongData) albumData.get(1)).getName());
                                    ContentActivity.textViewArtistName.setText(((AlbumSongData) albumData.get(1)).getArtistName());
                                    ContentActivity.appCompatSeekBar.setMax(ContentActivity.musicPlayerService.currentSong.getLength() * 1000);
                                    ContentActivity.appCompatSeekBar.setProgress(0);
                                    ContentActivity.musicPlayerService.sendNotificationMedia(ContentActivity.musicPlayerService.currentSong);
                                    this.notifyDataSetChanged();
                                    if (PlayerActivity.isBound) {
                                        PlayerActivity.buttonPlayPause.setImageResource(R.drawable.ic_pause_circle);
                                        PlayerActivity.textViewSongName.setText(((AlbumSongData) albumData.get(1)).getName());
                                        PlayerActivity.textViewArtistName.setText(((AlbumSongData) albumData.get(1)).getArtistName());
                                        PlayerActivity.appCompatSeekBar.setMax(ContentActivity.musicPlayerService.currentSong.getLength() * 1000);
                                        PlayerActivity.appCompatSeekBar.setProgress(0);
                                    }
                                } else { // chưa có bài hát
                                    ContentActivity.musicPlayerService.type = ALBUM;
                                    ContentActivity.musicPlayerService.id = data.getId();
                                    ContentActivity.musicPlayerService.currentSong = ContentActivity.musicPlayerService.songs.get(0);
                                    setSelected((AlbumSongData) albumData.get(1));
                                    try {
                                        ContentActivity.musicPlayerService.isPlaying = true;
                                        ContentActivity.musicPlayerService.mediaPlayer.setDataSource(
                                                ContentActivity.musicPlayerService.currentSong.getUrl().replace("http", "https")
                                        );
                                        ContentActivity.musicPlayerService.mediaPlayer.prepareAsync();
                                        ContentActivity.buttonPlayPause.setImageResource(R.drawable.ic_pause);
                                        ContentActivity.textViewSongName.setText(((AlbumSongData) albumData.get(1)).getName());
                                        ContentActivity.textViewArtistName.setText(((AlbumSongData) albumData.get(1)).getArtistName());
                                        ContentActivity.appCompatSeekBar.setMax(ContentActivity.musicPlayerService.currentSong.getLength() * 1000);
                                        ContentActivity.appCompatSeekBar.setProgress(0);
                                        ContentActivity.musicPlayerService.sendNotificationMedia(ContentActivity.musicPlayerService.currentSong);
                                        if (PlayerActivity.isBound) {
                                            PlayerActivity.buttonPlayPause.setImageResource(R.drawable.ic_pause);
                                            PlayerActivity.textViewSongName.setText(((AlbumSongData) albumData.get(1)).getName());
                                            PlayerActivity.textViewArtistName.setText(((AlbumSongData) albumData.get(1)).getArtistName());
                                            PlayerActivity.appCompatSeekBar.setMax(ContentActivity.musicPlayerService.currentSong.getLength() * 1000);
                                            PlayerActivity.appCompatSeekBar.setProgress(0);
                                        }
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    this.notifyDataSetChanged();
                                }
                            }, error -> Log.e("LOG_RESPONSE", error.toString()));
                    requestQueue.add(jsonObjectSongRequest);
                }
            });

            if (!data.getImageUrl().isEmpty()) {
                Picasso.get().load(data.getImageUrl()).error(R.drawable.spotify_icon_rgb_white).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Palette.from(bitmap).generate(palette -> {
                            if (palette != null) {
                                Palette.Swatch dominantSwatch = palette.getDominantSwatch();
                                if (dominantSwatch != null) {
                                    GradientDrawable gradientDrawable = new GradientDrawable(
                                            GradientDrawable.Orientation.TOP_BOTTOM,
                                            new int[]{dominantSwatch.getRgb(),
                                                    viewHolder.itemView.getContext().getColor(R.color.dark_black)}
                                    );
                                    viewHolder.imageViewAlbum.setImageBitmap(bitmap);
                                    viewHolder.linearLayoutHeader.setBackground(gradientDrawable);
                                }
                            }
                        });
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }
            if (!data.getImageArtistUrl().isEmpty()) {
                Picasso.get().load(data.getImageArtistUrl()).error(R.drawable.spotify_icon_rgb_white).into(viewHolder.imageViewArtist);
            }
        }

        if (holder instanceof AlbumSongViewHolder) {
            AlbumSongViewHolder songViewHolder = (AlbumSongViewHolder) holder;
            AlbumSongData songData = (AlbumSongData) albumData.get(position);
            songViewHolder.textViewSongName.setText(songData.getName());
            songViewHolder.textViewArtistName.setText(songData.getArtistName());
            if (songData.isSelected()) {
                songViewHolder.textViewSongName.setTextColor(holder.itemView.getResources().getColor(R.color.bright_lime_green, holder.itemView.getContext().getTheme()));
            } else {
                songViewHolder.textViewSongName.setTextColor(holder.itemView.getResources().getColor(R.color.white, holder.itemView.getContext().getTheme()));
            }
            songViewHolder.itemView.setOnClickListener(v -> {
                ContentActivity.linearLayoutMiniPlayer.setVisibility(VISIBLE);
                ContentActivity.musicPlayerService.mediaPlayer.reset();
                RequestQueue requestQueue = Volley.newRequestQueue(holder.itemView.getContext());
                JsonObjectRequest jsonObjectListSong = new JsonObjectRequest(
                        Request.Method.GET,
                        Constants.getSongByAlbumIdEndpoint(String.valueOf(((AlbumHeaderData) albumData.get(0)).getId())),
                        null,
                        responseSongs -> {
                            Log.i("LOG_RESPONSE", String.valueOf(responseSongs));
                            Gson gson = new Gson();
                            JSONArray itemSongs = responseSongs.optJSONArray("songs");
                            if (itemSongs == null) return;
                            List<Song> listSong = new ArrayList<>();
                            for (int i = 0; i < itemSongs.length(); i++) {
                                Song song = null;
                                try {
                                    song = gson.fromJson(itemSongs.get(i).toString(), Song.class);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                listSong.add(song);
                            }
                            @SuppressLint("NotifyDataSetChanged") JsonObjectRequest jsonObjectSongRequest = new JsonObjectRequest(
                                    Request.Method.GET,
                                    Constants.getSongByIdEndpoint(songData.getId()),
                                    null,
                                    responseSong -> {
                                        Log.i("LOG_RESPONSE", String.valueOf(responseSong));
                                        JSONArray itemSongById = responseSong.optJSONArray("songs");
                                        if (itemSongById == null) return;
                                        Song song;
                                        try {
                                            song = gson.fromJson(itemSongById.get(0).toString(), Song.class);
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                        JsonObjectRequest jsonObjectArtistRequest = new JsonObjectRequest(
                                                Request.Method.GET,
                                                Constants.getArtistByIdEndpoint(String.valueOf(song.getArtist_id())),
                                                null,
                                                response -> {
                                                    Log.i("LOG_RESPONSE", String.valueOf(response));
                                                    JSONArray itemArtists = response.optJSONArray("artists");
                                                    if (itemArtists == null) return;
                                                    Artist artist;
                                                    try {
                                                        artist = gson.fromJson(itemArtists.get(0).toString(), Artist.class);
                                                    } catch (JSONException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                    AlbumHeaderData headerData = (AlbumHeaderData) albumData.get(0);
                                                    if (!ContentActivity.musicPlayerService.isPlaying)
                                                        ContentActivity.musicPlayerService.isPlaying = true;
                                                    headerData.setPlaying(true);
                                                    setSelected((AlbumSongData) albumData.get(position));
                                                    ContentActivity.musicPlayerService.mediaPlayer.reset();
                                                    ContentActivity.musicPlayerService.songs = listSong;
                                                    ContentActivity.musicPlayerService.type = ALBUM;
                                                    ContentActivity.musicPlayerService.id = ((AlbumHeaderData) albumData.get(0)).getId();
                                                    ContentActivity.musicPlayerService.currentSong = song;
                                                    try {
                                                        ContentActivity.musicPlayerService.mediaPlayer.setDataSource(ContentActivity.musicPlayerService.currentSong.getUrl());
                                                    } catch (IOException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                    ContentActivity.musicPlayerService.mediaPlayer.prepareAsync();
                                                    ContentActivity.buttonPlayPause.setImageResource(R.drawable.ic_pause);
                                                    ContentActivity.textViewSongName.setText(song.getName());
                                                    ContentActivity.textViewArtistName.setText(artist.getName());
                                                    ContentActivity.appCompatSeekBar.setMax(song.getLength() * 1000);
                                                    ContentActivity.appCompatSeekBar.setProgress(0);
                                                    ContentActivity.musicPlayerService.sendNotificationMedia(song);
                                                    if (PlayerActivity.isBound) {
                                                        PlayerActivity.buttonPlayPause.setImageResource(R.drawable.ic_pause);
                                                        PlayerActivity.textViewSongName.setText(song.getName());
                                                        PlayerActivity.textViewArtistName.setText(artist.getName());
                                                        PlayerActivity.appCompatSeekBar.setMax(song.getLength() * 1000);
                                                        PlayerActivity.appCompatSeekBar.setProgress(0);
                                                    }
                                                    this.notifyDataSetChanged();
                                                },
                                                error -> Log.e("LOG_RESPONSE", error.toString())
                                        );
                                        requestQueue.add(jsonObjectArtistRequest);
                                    }, error -> Log.e("LOG_RESPONSE", error.toString()));
                            requestQueue.add(jsonObjectSongRequest);
                        },
                        error -> Log.e("LOG_RESPONSE", error.toString())
                );
                requestQueue.add(jsonObjectListSong);
            });
        }
    }

    private void setSelected(AlbumSongData data) {
        for (int i = 1; i < albumData.size(); ++i) {
            AlbumSongData item = (AlbumSongData) albumData.get(i);
            item.setSelected(data.getId().equals(item.getId()));
        }
    }

    private AlbumSongData findById(int id) {
        for (int i = 1; i < albumData.size(); ++i) {
            AlbumSongData item = (AlbumSongData) albumData.get(i);
            if (item.getId().equals(String.valueOf(id))) {
                return item;
            }
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        Object data = albumData.get(position);
        if (data instanceof AlbumHeaderData) {
            return ALBUM_HEADER.ordinal();
        }
        if (data instanceof AlbumSongData) {
            return ALBUM_SONG.ordinal();
        }
        return BLANK.ordinal();
    }

    @Override
    public int getItemCount() {
        return albumData.size();
    }
}
