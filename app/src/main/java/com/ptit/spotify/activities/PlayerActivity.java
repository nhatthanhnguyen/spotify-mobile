package com.ptit.spotify.activities;

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

import org.json.JSONArray;
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
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Song song = (Song) bundle.getSerializable(OBJECT_SONG);
        textViewSongName.setText(song.getName());
        JSONObject jsonBody = new JSONObject();
        final String mRequestBody = jsonBody.toString();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constants.getArtistByIdEndpoint(String.valueOf(song.getArtist_id())), new JSONObject(), new Response.Listener<JSONObject>() {
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
                        HttpUtils.getInstance(PlayerActivity.this).getRequestQueue().add(jsonObjectArtistRequest);
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
        appCompatSeekBar.setProgress(0);
        appCompatSeekBar.setMax(song.getLength() * 1000);
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
        try {
            ContentActivity.linearLayoutMiniPlayer.setVisibility(View.VISIBLE);
            ContentActivity.textViewSongName.setText(newSong.getName());
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
                                    ContentActivity.textViewArtistName.setText(artistName[0]);
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("LOG_RESPONSE", error.toString());
                                }
                            });
                            HttpUtils.getInstance(PlayerActivity.this).getRequestQueue().add(jsonObjectArtistRequest);
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
            ContentActivity.buttonPlayPause.setImageResource(R.drawable.ic_pause);
            ContentActivity.appCompatSeekBar.setMax(newSong.getLength() * 1000);

            ContentActivity.musicPlayerService.mediaPlayer.reset();
            ContentActivity.musicPlayerService.mediaPlayer.setDataSource(newSong.getUrl());
            ContentActivity.musicPlayerService.mediaPlayer.prepareAsync();
            ContentActivity.musicPlayerService.isPlaying = true;
            ContentActivity.musicPlayerService.sendNotificationMedia(newSong);
            ContentActivity.musicPlayerService.currentSong = newSong;
            if (PlayerActivity.isBound) {
                textViewSongName.setText(newSong.getName());
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
                                HttpUtils.getInstance(PlayerActivity.this).getRequestQueue().add(jsonObjectArtistRequest);
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
                appCompatSeekBar.setMax(newSong.getLength() * 1000);
                textViewMax.setText(Utils.formatTime(newSong.getLength() * 1000));
                textViewCurrentPosition.setText(Utils.formatTime(0));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        if (musicPlayerService.isShuffle)
            newSong = ContentActivity.musicPlayerService.skipShuffle();
        else
            newSong = ContentActivity.musicPlayerService.skipToNext(ContentActivity.musicPlayerService.currentSong.getSong_id(), musicPlayerService.repeatMode);
        if (newSong == null) {
            ContentActivity.musicPlayerService.isPlaying = false;
            ContentActivity.buttonPlayPause.setImageResource(R.drawable.ic_play_arrow);
            if (PlayerActivity.isBound) {
                PlayerActivity.buttonPlayPause.setImageResource(R.drawable.ic_play_circle);
            }
            ContentActivity.musicPlayerService.sendNotificationMedia(ContentActivity.musicPlayerService.currentSong);
            return;
        }
        try {
            ContentActivity.linearLayoutMiniPlayer.setVisibility(View.VISIBLE);
            ContentActivity.textViewSongName.setText(newSong.getName());
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
                                    ContentActivity.textViewArtistName.setText(artistName[0]);
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("LOG_RESPONSE", error.toString());
                                }
                            });
                            HttpUtils.getInstance(PlayerActivity.this).getRequestQueue().add(jsonObjectArtistRequest);
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
            ContentActivity.buttonPlayPause.setImageResource(R.drawable.ic_pause);
            ContentActivity.appCompatSeekBar.setMax(newSong.getLength() * 1000);

            ContentActivity.musicPlayerService.mediaPlayer.reset();
            ContentActivity.musicPlayerService.mediaPlayer.setDataSource(newSong.getUrl());
            ContentActivity.musicPlayerService.mediaPlayer.prepareAsync();
            ContentActivity.musicPlayerService.isPlaying = true;
            ContentActivity.musicPlayerService.sendNotificationMedia(newSong);
            ContentActivity.musicPlayerService.currentSong = newSong;
            if (PlayerActivity.isBound) {
                textViewSongName.setText(newSong.getName());
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
                                HttpUtils.getInstance(PlayerActivity.this).getRequestQueue().add(jsonObjectArtistRequest);
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
                appCompatSeekBar.setMax(newSong.getLength() * 1000);
                textViewMax.setText(Utils.formatTime(newSong.getLength() * 1000));
                textViewCurrentPosition.setText(Utils.formatTime(0));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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