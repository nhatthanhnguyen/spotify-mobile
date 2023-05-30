package com.ptit.spotify.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.ptit.spotify.R;
import com.ptit.spotify.dto.model.Album;
import com.ptit.spotify.dto.model.Artist;
import com.ptit.spotify.dto.model.Song;
import com.ptit.spotify.fragments.HomeFragment;
import com.ptit.spotify.fragments.LibraryFragment;
import com.ptit.spotify.fragments.SearchFragment;
import com.ptit.spotify.player.MusicPlayerService;
import com.ptit.spotify.utils.Constants;
import com.ptit.spotify.utils.HttpUtils;
import com.ptit.spotify.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import lombok.SneakyThrows;

public class ContentActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {
    public static CardView linearLayoutMiniPlayer;
    public static ImageView imageViewSong;
    public static TextView textViewSongName;
    public static TextView textViewArtistName;
    public static ImageButton buttonPlayPause;
    public static SeekBar appCompatSeekBar;
    Intent musicIntent;
    public static MusicPlayerService musicPlayerService;
    boolean isBound;
    private final HomeFragment homeFragment = new HomeFragment();
    private final LibraryFragment libraryFragment = new LibraryFragment();
    private final SearchFragment searchFragment = new SearchFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        musicIntent = new Intent(this, MusicPlayerService.class);
        startService(musicIntent);
        bindService(musicIntent, serviceConnection, BIND_AUTO_CREATE);
        linearLayoutMiniPlayer = findViewById(R.id.linearLayoutMiniPlayer);
        textViewSongName = findViewById(R.id.textViewSongName);
        textViewArtistName = findViewById(R.id.textViewArtistName);
        buttonPlayPause = findViewById(R.id.buttonPlayPause);
        imageViewSong = findViewById(R.id.imageViewSong);
        appCompatSeekBar = findViewById(R.id.seekBar);

        appCompatSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    musicPlayerService.mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
                return true;
            }
            if (item.getItemId() == R.id.search) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, searchFragment).commit();
                return true;
            }
            if (item.getItemId() == R.id.library) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, libraryFragment).commit();
                return true;
            }
            return false;
        });
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicPlayerService.MusicBinder binder = (MusicPlayerService.MusicBinder) service;
            musicPlayerService = binder.getService();
            musicPlayerService.seekBarSetup();
            musicPlayerService.mediaPlayer.setOnCompletionListener(ContentActivity.this);
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    public void onCompletion(MediaPlayer mp) {
        musicPlayerService.mediaPlayer.pause();
        Song newSong;
        if (musicPlayerService.isShuffle)
            newSong = ContentActivity.musicPlayerService.skipShuffle();
        else
            newSong = ContentActivity.musicPlayerService.skipToNext(musicPlayerService.currentSong.getSong_id(), musicPlayerService.repeatMode);
        if (musicPlayerService.repeatMode == 2) {
            newSong = musicPlayerService.currentSong;
        }
        if (newSong == null) {
            musicPlayerService.isPlaying = false;
            buttonPlayPause.setImageResource(R.drawable.ic_play_arrow);
            musicPlayerService.sendNotificationMedia(musicPlayerService.currentSong);
            return;
        }
        try {
            linearLayoutMiniPlayer.setVisibility(View.VISIBLE);
            textViewSongName.setText(newSong.getName());
            JSONObject jsonBody = new JSONObject();
            String mRequestBody = jsonBody.toString();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constants.getArtistByIdEndpoint(String.valueOf(newSong.getArtist_id())), new JSONObject(), new Response.Listener<JSONObject>() {
                @SneakyThrows
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("LOG_RESPONSE", String.valueOf(response));
                    Gson gson = new Gson();
                    JSONArray items = response.optJSONArray("artist");
                    if (items != null) {
                        for (int i = 0; i < items.length(); i++) {
                            Album ab = gson.fromJson(items.get(i).toString(), Album.class);
                            final String[] artistName = {""};
                            final String[] artistImg = {""};
                            JsonObjectRequest jsonObjectArtistRequest = new JsonObjectRequest(Constants.getArtistByIdEndpoint(String.valueOf(ab.getArtist_id())), new JSONObject(), new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.i("LOG_RESPONSE", String.valueOf(response));
                                    Gson gson = new Gson();
                                    Artist at = gson.fromJson(response.toString(), Artist.class);
                                    artistName[0] = at.getName();
                                    artistImg[0] = at.getCoverImg();
                                    textViewArtistName.setText(artistName[0]);
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("LOG_RESPONSE", error.toString());
                                }
                            });
                            HttpUtils.getInstance(ContentActivity.this).getRequestQueue().add(jsonObjectArtistRequest);
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_RESPONSE", error.toString());
                }
            });
            HttpUtils.getInstance(this).getRequestQueue().add(jsonObjectRequest);

            buttonPlayPause.setImageResource(R.drawable.ic_pause);

            musicPlayerService.mediaPlayer.reset();
            musicPlayerService.mediaPlayer.setDataSource(newSong.getUrl());
            musicPlayerService.mediaPlayer.prepareAsync();
            musicPlayerService.isPlaying = true;
            musicPlayerService.sendNotificationMedia(newSong);
            musicPlayerService.currentSong = newSong;
            if (PlayerActivity.isBound) {
                PlayerActivity.textViewSongName.setText(newSong.getName());
                jsonBody = new JSONObject();
                mRequestBody = jsonBody.toString();
                jsonObjectRequest = new JsonObjectRequest(Constants.getArtistByIdEndpoint(String.valueOf(newSong.getArtist_id())), new JSONObject(), new Response.Listener<JSONObject>() {
                    @SneakyThrows
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("LOG_RESPONSE", String.valueOf(response));
                        Gson gson = new Gson();
                        JSONArray items = response.optJSONArray("artist");
                        if (items != null) {
                            for (int i = 0; i < items.length(); i++) {
                                Album ab = gson.fromJson(items.get(i).toString(), Album.class);
                                final String[] artistName = {""};
                                final String[] artistImg = {""};
                                JsonObjectRequest jsonObjectArtistRequest = new JsonObjectRequest(Constants.getArtistByIdEndpoint(String.valueOf(ab.getArtist_id())), new JSONObject(), new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.i("LOG_RESPONSE", String.valueOf(response));
                                        Gson gson = new Gson();
                                        Artist at = gson.fromJson(response.toString(), Artist.class);
                                        artistName[0] = at.getName();
                                        artistImg[0] = at.getCoverImg();
                                        textViewArtistName.setText(artistName[0]);
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("LOG_RESPONSE", error.toString());
                                    }
                                });
                                HttpUtils.getInstance(ContentActivity.this).getRequestQueue().add(jsonObjectArtistRequest);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                });
                HttpUtils.getInstance(this).getRequestQueue().add(jsonObjectRequest);
                PlayerActivity.appCompatSeekBar.setMax(newSong.getLength() * 1000);
                PlayerActivity.textViewMax.setText(Utils.formatTime(newSong.getLength() * 1000));
                PlayerActivity.textViewCurrentPosition.setText(Utils.formatTime(0));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}