package com.ptit.spotify.activities;

import static com.ptit.spotify.application.SpotifyApplication.ACTION_LIKE;
import static com.ptit.spotify.application.SpotifyApplication.ACTION_NEXT;
import static com.ptit.spotify.application.SpotifyApplication.ACTION_PAUSE;
import static com.ptit.spotify.application.SpotifyApplication.ACTION_PREV;
import static com.ptit.spotify.application.SpotifyApplication.ACTION_RESUME;
import static com.ptit.spotify.application.SpotifyApplication.ACTION_UNLIKE;
import static com.ptit.spotify.application.SpotifyApplication.OBJECT_SONG;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.ptit.spotify.R;
import com.ptit.spotify.dto.model.Artist;
import com.ptit.spotify.dto.model.Song;
import com.ptit.spotify.fragments.HomeFragment;
import com.ptit.spotify.fragments.LibraryFragment;
import com.ptit.spotify.fragments.SearchFragment;
import com.ptit.spotify.player.MusicPlayerService;
import com.ptit.spotify.utils.Constants;
import com.ptit.spotify.utils.HttpUtils;
import com.ptit.spotify.utils.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

public class ContentActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {
    public static CardView linearLayoutMiniPlayer;
    public static ImageView imageViewSong;
    public static TextView textViewSongName;
    public static TextView textViewArtistName;
    public static ImageButton buttonPlayPause;
    public static ImageButton buttonLike;
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
        buttonLike = findViewById(R.id.buttonLikeSong);
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
        linearLayoutMiniPlayer.setOnClickListener(v -> {
            Intent intent = new Intent(this, PlayerActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(OBJECT_SONG, musicPlayerService.currentSong);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_PAUSE);
        intentFilter.addAction(ACTION_RESUME);
        intentFilter.addAction(ACTION_NEXT);
        intentFilter.addAction(ACTION_PREV);
        intentFilter.addAction(ACTION_LIKE);
        intentFilter.addAction(ACTION_UNLIKE);
        registerReceiver(receiver, intentFilter);

        buttonPlayPause.setOnClickListener(v -> {
            if (musicPlayerService.isPlaying) {
                buttonPlayPause.setImageResource(R.drawable.ic_play_arrow);
                musicPlayerService.mediaPlayer.pause();
                musicPlayerService.isPlaying = false;
                musicPlayerService.sendNotificationMedia(musicPlayerService.currentSong);
                if (PlayerActivity.isBound) {
                    PlayerActivity.buttonPlayPause.setImageResource(R.drawable.ic_play_circle);
                }
                Intent intentPause = new Intent(ACTION_PAUSE);
                sendBroadcast(intentPause);
            } else {
                buttonPlayPause.setImageResource(R.drawable.ic_pause);
                musicPlayerService.mediaPlayer.start();
                musicPlayerService.isPlaying = true;
                musicPlayerService.sendNotificationMedia(musicPlayerService.currentSong);
                if (PlayerActivity.isBound) {
                    PlayerActivity.buttonPlayPause.setImageResource(R.drawable.ic_pause_circle);
                }
                Intent intentPause = new Intent(ACTION_RESUME);
                sendBroadcast(intentPause);
            }
        });
        buttonLike.setOnClickListener(v -> {
            if (musicPlayerService.isLiked) {
                buttonLike.setImageResource(R.drawable.ic_like_outlined);
                musicPlayerService.isLiked = false;
                musicPlayerService.sendNotificationMedia(musicPlayerService.currentSong);
                Intent intentLike = new Intent(ACTION_LIKE);
                sendBroadcast(intentLike);
            } else {
                buttonLike.setImageResource(R.drawable.ic_like_filled);
                musicPlayerService.isLiked = true;
                musicPlayerService.sendNotificationMedia(musicPlayerService.currentSong);
                Intent intentUnlike = new Intent(ACTION_UNLIKE);
                sendBroadcast(intentUnlike);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(musicIntent);
        unbindService(serviceConnection);
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

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

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
            Log.d("ON COMPLETE", "Repeat song");
        }
        if (newSong == null) {
            musicPlayerService.isPlaying = false;
            buttonPlayPause.setImageResource(R.drawable.ic_play_arrow);
            musicPlayerService.sendNotificationMedia(musicPlayerService.currentSong);
            return;
        }
        linearLayoutMiniPlayer.setVisibility(View.VISIBLE);
        textViewSongName.setText(newSong.getName());
        Song finalNewSong = newSong;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                Constants.getArtistByIdEndpoint(String.valueOf(newSong.getArtist_id())),
                null,
                response -> {
                    Log.i("LOG_RESPONSE", String.valueOf(response));
                    Gson gson = new Gson();
                    JSONArray items = response.optJSONArray("artists");
                    if (items == null) return;
                    Artist artist = null;
                    try {
                        artist = gson.fromJson(items.get(0).toString(), Artist.class);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    textViewArtistName.setText(artist.getName());
                    buttonPlayPause.setImageResource(R.drawable.ic_pause);

                    musicPlayerService.mediaPlayer.reset();
                    try {
                        musicPlayerService.mediaPlayer.setDataSource(finalNewSong.getUrl());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    musicPlayerService.mediaPlayer.prepareAsync();
                    musicPlayerService.isPlaying = true;
                    musicPlayerService.sendNotificationMedia(finalNewSong);
                    musicPlayerService.currentSong = finalNewSong;
                    if (PlayerActivity.isBound) {
                        PlayerActivity.textViewSongName.setText(finalNewSong.getName());
                        PlayerActivity.textViewArtistName.setText(artist.getName());
                        PlayerActivity.appCompatSeekBar.setMax(finalNewSong.getLength() * 1000);
                        PlayerActivity.textViewMax.setText(Utils.formatTime(finalNewSong.getLength() * 1000));
                        PlayerActivity.textViewCurrentPosition.setText(Utils.formatTime(0));
                        Picasso.get().load(finalNewSong.getCover_img()).into(PlayerActivity.imageViewSong);
                    }
                    Intent intent = new Intent(ACTION_NEXT);
                    sendBroadcast(intent);
                }, error -> Log.e("LOG_RESPONSE", error.toString()));
        HttpUtils.getInstance(this).getRequestQueue().add(jsonObjectRequest);
    }
}