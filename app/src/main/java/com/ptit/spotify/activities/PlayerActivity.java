package com.ptit.spotify.activities;

import static com.ptit.spotify.application.SpotifyApplication.ACTION_NEXT;
import static com.ptit.spotify.application.SpotifyApplication.ACTION_PREV;
import static com.ptit.spotify.application.SpotifyApplication.OBJECT_SONG;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.ptit.spotify.R;
import com.ptit.spotify.dto.model.Album;
import com.ptit.spotify.dto.model.Artist;
import com.ptit.spotify.dto.model.Song;
import com.ptit.spotify.player.MusicPlayerService;
import com.ptit.spotify.utils.Constants;
import com.ptit.spotify.utils.HttpUtils;
import com.ptit.spotify.utils.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import lombok.SneakyThrows;

public class PlayerActivity extends AppCompatActivity {

    public static MusicPlayerService musicPlayerService;
    public static boolean isBound;
    Intent musicIntent;
    ImageButton buttonBack;
    public static ImageView imageViewSong;
    public static TextView textViewSongName, textViewArtistName;
    public static AppCompatSeekBar appCompatSeekBar;
    public static TextView textViewCurrentPosition, textViewMax;
    public static ImageButton buttonTimer;
    public static ImageButton buttonShuffle, buttonPrevious, buttonPlayPause, buttonNext, buttonRepeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        setControl();
        musicIntent = new Intent(this, MusicPlayerService.class);
        startService(musicIntent);
        bindService(musicIntent, serviceConnection, BIND_AUTO_CREATE);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Song song = (Song) bundle.getSerializable(OBJECT_SONG);
        Picasso.get().load(song.getCover_img()).into(imageViewSong);
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
        appCompatSeekBar.setProgress(0);
        appCompatSeekBar.setMax(song.getLength() * 1000);
        JsonObjectRequest jsonObjectArtistRequest = new JsonObjectRequest(
                Request.Method.GET,
                Constants.getArtistByIdEndpoint(String.valueOf(song.getArtist_id())),
                null,
                response -> {
                    Log.i("LOG_RESPONSE", String.valueOf(response));
                    Gson gson1 = new Gson();
                    JSONArray artists = response.optJSONArray("artists");
                    if (artists == null) return;
                    Artist artist = null;
                    try {
                        artist = gson1.fromJson(artists.get(0).toString(), Artist.class);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    textViewSongName.setText(song.getName());
                    textViewArtistName.setText(artist.getName());
                    buttonBack.setOnClickListener(v -> {
                        finish();
                    });
                    buttonPlayPause.setOnClickListener(v -> {
                        buttonPlayPauseClicked();
                    });
                    buttonShuffle.setOnClickListener(v -> {
                        buttonShuffleClicked();
                    });
                    buttonNext.setOnClickListener(v -> {
                        buttonNextClicked();
                    });
                    buttonPrevious.setOnClickListener(v -> {
                        buttonPreviousClicked();
                    });
                    buttonRepeat.setOnClickListener(v -> {
                        buttonRepeatClicked();
                    });
                    buttonTimer.setOnClickListener(v -> {
                        if (ContentActivity.musicPlayerService.min5 ||
                                ContentActivity.musicPlayerService.min10 ||
                                ContentActivity.musicPlayerService.min15)
                            buttonTimerSetOrNotSetTimerClicked();
                        else
                            buttonTimerSetTimerClicked();
                    });
                }, error -> Log.e("LOG_RESPONSE", error.toString()));
        HttpUtils.getInstance(PlayerActivity.this).getRequestQueue().add(jsonObjectArtistRequest);
    }

    private void setControl() {
        buttonBack = findViewById(R.id.buttonBack);
        imageViewSong = findViewById(R.id.imageViewSong);
        textViewSongName = findViewById(R.id.textViewSongName);
        textViewArtistName = findViewById(R.id.textViewArtistName);
        appCompatSeekBar = findViewById(R.id.seekBar);
        textViewCurrentPosition = findViewById(R.id.textViewCurrentPosition);
        textViewMax = findViewById(R.id.textViewMax);
        buttonShuffle = findViewById(R.id.buttonShuffle);
        buttonPrevious = findViewById(R.id.buttonPrevious);
        buttonPlayPause = findViewById(R.id.buttonPlayPause);
        buttonNext = findViewById(R.id.buttonNext);
        buttonRepeat = findViewById(R.id.buttonRepeat);
        buttonTimer = findViewById(R.id.buttonTimer);
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isBound = true;
            MusicPlayerService.MusicBinder binder = (MusicPlayerService.MusicBinder) service;
            musicPlayerService = binder.getService();
            musicPlayerService.seekBarSetup();
            textViewCurrentPosition.setText(Utils.formatTime(musicPlayerService.mediaPlayer.getCurrentPosition()));
            if (musicPlayerService.isPlaying) {
                buttonPlayPause.setImageResource(R.drawable.ic_pause_circle);
            } else {
                buttonPlayPause.setImageResource(R.drawable.ic_play_circle);
            }

            if (musicPlayerService.isShuffle) {
                buttonShuffle.setImageResource(R.drawable.ic_shuffle_on);
            } else {
                buttonShuffle.setImageResource(R.drawable.ic_shuffle);
            }

            switch (musicPlayerService.repeatMode) {
                case 0:
                    buttonRepeat.setImageResource(R.drawable.ic_repeat);
                    break;
                case 1:
                    buttonRepeat.setImageResource(R.drawable.ic_repeat_1);
                    break;
                case 2:
                    buttonRepeat.setImageResource(R.drawable.ic_repeat_2);
                    break;
            }

            if (ContentActivity.musicPlayerService.min5 ||
                    ContentActivity.musicPlayerService.min10 ||
                    ContentActivity.musicPlayerService.min15) {
                buttonTimer.setImageResource(R.drawable.ic_alarm_on);
            } else {
                buttonTimer.setImageResource(R.drawable.ic_alarm);
            }
            textViewMax.setText(Utils.formatTime(musicPlayerService.currentSong.getLength() * 1000));
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        stopService(musicIntent);
        unbindService(serviceConnection);
    }

    private void buttonPlayPauseClicked() {
        if (musicPlayerService.isPlaying) {
            musicPlayerService.isPlaying = false;
            musicPlayerService.mediaPlayer.pause();
            buttonPlayPause.setImageResource(R.drawable.ic_play_circle);
            musicPlayerService.sendNotificationMedia(musicPlayerService.currentSong);
            ContentActivity.buttonPlayPause.setImageResource(R.drawable.ic_play_arrow);
        } else {
            musicPlayerService.isPlaying = true;
            musicPlayerService.mediaPlayer.start();
            buttonPlayPause.setImageResource(R.drawable.ic_pause_circle);
            musicPlayerService.sendNotificationMedia(musicPlayerService.currentSong);
            ContentActivity.buttonPlayPause.setImageResource(R.drawable.ic_pause);
        }
    }

    private void buttonRepeatClicked() {
        musicPlayerService.repeatMode = (musicPlayerService.repeatMode + 1) % 3;
        switch (musicPlayerService.repeatMode) {
            case 0:
                buttonRepeat.setImageResource(R.drawable.ic_repeat);
                break;
            case 1:
                buttonRepeat.setImageResource(R.drawable.ic_repeat_1);
                break;
            case 2:
                buttonRepeat.setImageResource(R.drawable.ic_repeat_2);
                break;
        }
    }

    private void buttonPreviousClicked() {
        ContentActivity.musicPlayerService.mediaPlayer.pause();
        Song newSong;
        if (ContentActivity.musicPlayerService.isShuffle)
            newSong = ContentActivity.musicPlayerService.skipShuffle();
        else
            newSong = ContentActivity.musicPlayerService.skipToPrevious(musicPlayerService.currentSong.getSong_id(), ContentActivity.musicPlayerService.repeatMode);
        if (newSong == null) {
            ContentActivity.musicPlayerService.isPlaying = false;
            ContentActivity.buttonPlayPause.setImageResource(R.drawable.ic_play_arrow);
            ContentActivity.musicPlayerService.sendNotificationMedia(ContentActivity.musicPlayerService.currentSong);
            return;
        }
        ContentActivity.linearLayoutMiniPlayer.setVisibility(View.VISIBLE);
        ContentActivity.textViewSongName.setText(newSong.getName());
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
                    ContentActivity.buttonPlayPause.setImageResource(R.drawable.ic_pause);
                    ContentActivity.appCompatSeekBar.setMax(newSong.getLength() * 1000);

                    ContentActivity.musicPlayerService.mediaPlayer.reset();
                    try {
                        ContentActivity.musicPlayerService.mediaPlayer.setDataSource(newSong.getUrl());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    ContentActivity.musicPlayerService.mediaPlayer.prepareAsync();
                    ContentActivity.musicPlayerService.isPlaying = true;
                    ContentActivity.musicPlayerService.sendNotificationMedia(newSong);
                    ContentActivity.musicPlayerService.currentSong = newSong;
                    ContentActivity.textViewArtistName.setText(artist.getName());
                    if (PlayerActivity.isBound) {
                        textViewSongName.setText(newSong.getName());
                        JsonObjectRequest objectRequest = new JsonObjectRequest(
                                Request.Method.GET,
                                Constants.getArtistByIdEndpoint(String.valueOf(newSong.getArtist_id())),
                                null,
                                response1 -> {
                                    Log.i("LOG_RESPONSE", String.valueOf(response1));
                                    Gson gson1 = new Gson();
                                    JSONArray items1 = response1.optJSONArray("artists");
                                    if (items1 == null) return;
                                    try {
                                        Artist artist1 = gson1.fromJson(items1.get(0).toString(), Artist.class);
                                        appCompatSeekBar.setMax(newSong.getLength() * 1000);
                                        textViewArtistName.setText(artist1.getName());
                                        textViewMax.setText(Utils.formatTime(newSong.getLength() * 1000));
                                        textViewCurrentPosition.setText(Utils.formatTime(0));
                                        Picasso.get().load(newSong.getCover_img()).into(imageViewSong);
                                        Intent intent = new Intent(ACTION_PREV);
                                        sendBroadcast(intent);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }, error -> Log.e("LOG_RESPONSE", error.toString()));
                        HttpUtils.getInstance(this).getRequestQueue().add(objectRequest);
                    }
                }, error -> Log.e("LOG_RESPONSE", error.toString()));
        HttpUtils.getInstance(this).getRequestQueue().add(jsonObjectRequest);

    }

    private void buttonShuffleClicked() {
        if (musicPlayerService.isShuffle) {
            musicPlayerService.isShuffle = false;
            buttonShuffle.setImageResource(R.drawable.ic_shuffle);
        } else {
            musicPlayerService.isShuffle = true;
            buttonShuffle.setImageResource(R.drawable.ic_shuffle_on);
        }
    }

    private void buttonNextClicked() {
        ContentActivity.musicPlayerService.mediaPlayer.pause();
        Song newSong;
        if (ContentActivity.musicPlayerService.isShuffle)
            newSong = ContentActivity.musicPlayerService.skipShuffle();
        else
            newSong = ContentActivity.musicPlayerService.skipToNext(musicPlayerService.currentSong.getSong_id(), ContentActivity.musicPlayerService.repeatMode);
        if (newSong == null) {
            ContentActivity.musicPlayerService.isPlaying = false;
            ContentActivity.buttonPlayPause.setImageResource(R.drawable.ic_play_arrow);
            ContentActivity.musicPlayerService.sendNotificationMedia(ContentActivity.musicPlayerService.currentSong);
            return;
        }
        ContentActivity.linearLayoutMiniPlayer.setVisibility(View.VISIBLE);
        ContentActivity.textViewSongName.setText(newSong.getName());
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
                    ContentActivity.buttonPlayPause.setImageResource(R.drawable.ic_pause);
                    ContentActivity.appCompatSeekBar.setMax(newSong.getLength() * 1000);

                    ContentActivity.musicPlayerService.mediaPlayer.reset();
                    try {
                        ContentActivity.musicPlayerService.mediaPlayer.setDataSource(newSong.getUrl());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    ContentActivity.musicPlayerService.mediaPlayer.prepareAsync();
                    ContentActivity.musicPlayerService.isPlaying = true;
                    ContentActivity.musicPlayerService.sendNotificationMedia(newSong);
                    ContentActivity.musicPlayerService.currentSong = newSong;
                    ContentActivity.textViewArtistName.setText(artist.getName());
                    if (PlayerActivity.isBound) {
                        textViewSongName.setText(newSong.getName());
                        JsonObjectRequest objectRequest = new JsonObjectRequest(
                                Request.Method.GET,
                                Constants.getArtistByIdEndpoint(String.valueOf(newSong.getArtist_id())),
                                null,
                                response1 -> {
                                    Log.i("LOG_RESPONSE", String.valueOf(response1));
                                    Gson gson1 = new Gson();
                                    JSONArray items1 = response1.optJSONArray("artists");
                                    if (items1 == null) return;
                                    try {
                                        Artist artist1 = gson1.fromJson(items1.get(0).toString(), Artist.class);
                                        appCompatSeekBar.setMax(newSong.getLength() * 1000);
                                        textViewArtistName.setText(artist1.getName());
                                        textViewMax.setText(Utils.formatTime(newSong.getLength() * 1000));
                                        textViewCurrentPosition.setText(Utils.formatTime(0));
                                        Picasso.get().load(newSong.getCover_img()).into(imageViewSong);
                                        Intent intent = new Intent(ACTION_NEXT);
                                        sendBroadcast(intent);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }, error -> Log.e("LOG_RESPONSE", error.toString()));
                        HttpUtils.getInstance(this).getRequestQueue().add(objectRequest);
                    }
                }, error -> Log.e("LOG_RESPONSE", error.toString()));
        HttpUtils.getInstance(this).getRequestQueue().add(jsonObjectRequest);
    }

    private void buttonTimerSetTimerClicked() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.layout_bottom_timer_set);
        dialog.show();
        dialog.findViewById(R.id.min_5).setOnClickListener(v -> {
            Toast.makeText(getBaseContext(), "Music will stop after 5 minutes", Toast.LENGTH_SHORT).show();
            buttonTimer.setImageResource(R.drawable.ic_alarm_on);
            ContentActivity.musicPlayerService.min5 = true;
            ContentActivity.musicPlayerService.min10 = false;
            ContentActivity.musicPlayerService.min15 = false;
            ContentActivity.musicPlayerService.startTimer();
            dialog.dismiss();
        });
        dialog.findViewById(R.id.min_10).setOnClickListener(v -> {
            Toast.makeText(getBaseContext(), "Music will stop after 10 minutes", Toast.LENGTH_SHORT).show();
            buttonTimer.setImageResource(R.drawable.ic_alarm_on);
            ContentActivity.musicPlayerService.min5 = false;
            ContentActivity.musicPlayerService.min10 = true;
            ContentActivity.musicPlayerService.min15 = false;
            ContentActivity.musicPlayerService.startTimer();
            dialog.dismiss();
        });
        dialog.findViewById(R.id.min_15).setOnClickListener(v -> {
            Toast.makeText(getBaseContext(), "Music will stop after 15 minutes", Toast.LENGTH_SHORT).show();
            buttonTimer.setImageResource(R.drawable.ic_alarm_on);
            ContentActivity.musicPlayerService.min5 = false;
            ContentActivity.musicPlayerService.min10 = false;
            ContentActivity.musicPlayerService.min15 = true;
            ContentActivity.musicPlayerService.startTimer();
            dialog.dismiss();
        });
    }

    private void buttonTimerSetOrNotSetTimerClicked() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.layout_bottom_timer_set_or_not);
        dialog.show();
        dialog.findViewById(R.id.min_5).setOnClickListener(v -> {
            Toast.makeText(getBaseContext(), "Music will stop after 5 minutes", Toast.LENGTH_SHORT).show();
            buttonTimer.setImageResource(R.drawable.ic_alarm_on);
            ContentActivity.musicPlayerService.min5 = true;
            ContentActivity.musicPlayerService.min10 = false;
            ContentActivity.musicPlayerService.min15 = false;
            ContentActivity.musicPlayerService.startTimer();
            dialog.dismiss();
        });
        dialog.findViewById(R.id.min_10).setOnClickListener(v -> {
            Toast.makeText(getBaseContext(), "Music will stop after 10 minutes", Toast.LENGTH_SHORT).show();
            buttonTimer.setImageResource(R.drawable.ic_alarm_on);
            ContentActivity.musicPlayerService.min5 = false;
            ContentActivity.musicPlayerService.min10 = true;
            ContentActivity.musicPlayerService.min15 = false;
            ContentActivity.musicPlayerService.startTimer();
            dialog.dismiss();
        });
        dialog.findViewById(R.id.min_15).setOnClickListener(v -> {
            Toast.makeText(getBaseContext(), "Music will stop after 15 minutes", Toast.LENGTH_SHORT).show();
            buttonTimer.setImageResource(R.drawable.ic_alarm_on);
            ContentActivity.musicPlayerService.min5 = false;
            ContentActivity.musicPlayerService.min10 = false;
            ContentActivity.musicPlayerService.min15 = true;
            ContentActivity.musicPlayerService.startTimer();
            dialog.dismiss();
        });
        dialog.findViewById(R.id.cancel).setOnClickListener(v -> {
            Toast.makeText(getBaseContext(), "Turn off the timer", Toast.LENGTH_SHORT).show();
            buttonTimer.setImageResource(R.drawable.ic_alarm);
            ContentActivity.musicPlayerService.min5 = false;
            ContentActivity.musicPlayerService.min10 = false;
            ContentActivity.musicPlayerService.min15 = false;
            dialog.dismiss();
        });
    }
}