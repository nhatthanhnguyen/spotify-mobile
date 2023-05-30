package com.ptit.spotify.player;

import static com.ptit.spotify.application.SpotifyApplication.ACTION_NEXT;
import static com.ptit.spotify.application.SpotifyApplication.ACTION_PAUSE;
import static com.ptit.spotify.application.SpotifyApplication.ACTION_PREV;
import static com.ptit.spotify.application.SpotifyApplication.ACTION_RESUME;
import static com.ptit.spotify.application.SpotifyApplication.CHANNEL_ID;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ptit.spotify.R;
import com.ptit.spotify.activities.ContentActivity;
import com.ptit.spotify.activities.PlayerActivity;
import com.ptit.spotify.dto.model.Album;
import com.ptit.spotify.dto.model.Artist;
import com.ptit.spotify.dto.model.Song;
import com.ptit.spotify.utils.Constants;
import com.ptit.spotify.utils.HttpUtils;
import com.ptit.spotify.utils.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

public class MusicPlayerService extends Service implements MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener {
    private final IBinder binder = new MusicBinder();
    private Runnable runnable;
    public MediaPlayer mediaPlayer = null;
    public boolean isPlaying;
    public boolean isShuffle;
    public int repeatMode = 0;
    public Song currentSong;
    public static List<Song> songs = new ArrayList<>();
    public static boolean[] selected;
    public boolean min5 = false;
    public boolean min10 = false;
    public boolean min15 = false;
    public CountDownTimer countDownTimer;

    public class MusicBinder extends Binder {
        public MusicPlayerService getService() {
            return MusicPlayerService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void sendNotificationMedia(Song song) {
        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(this, "tag");

        Intent prevIntent = (new Intent(getBaseContext(), MusicPlayerReceiver.class)).setAction(ACTION_PREV);
        PendingIntent prevPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent pauseIntent = (new Intent(getBaseContext(), MusicPlayerReceiver.class)).setAction(ACTION_PAUSE);
        PendingIntent pausePendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent resumeIntent = (new Intent(getBaseContext(), MusicPlayerReceiver.class)).setAction(ACTION_RESUME);
        PendingIntent resumePendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, resumeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent nextIntent = (new Intent(getBaseContext(), MusicPlayerReceiver.class)).setAction(ACTION_NEXT);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MusicPlayerService.this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.spotify_icon_rgb_white)
                        .setSound(null)
                        .setContentTitle(song.getName())
                        .setLargeIcon(bitmap)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .addAction(R.drawable.ic_skip_previous, "Previous", prevPendingIntent)
                        .addAction(isPlaying ? R.drawable.ic_pause : R.drawable.ic_play_arrow, isPlaying ? "Pause" : "Play",
                                isPlaying ? pausePendingIntent : resumePendingIntent)
                        .addAction(R.drawable.ic_skip_next, "Next", nextPendingIntent)
                        .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                                .setShowActionsInCompactView(0, 1, 2)
                                .setMediaSession(mediaSessionCompat.getSessionToken())
                        );
                JSONObject jsonBody = new JSONObject();
                String mRequestBody = jsonBody.toString();
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
                                        notificationBuilder.setSubText(artistName[0]);
                                        notificationBuilder.setContentText(artistName[0]);
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("LOG_RESPONSE", error.toString());
                                    }
                                });
                                HttpUtils.getInstance(getBaseContext()).getRequestQueue().add(jsonObjectArtistRequest);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                });
                HttpUtils.getInstance(getBaseContext()).getRequestQueue().add(jsonObjectRequest);
                startForeground(1, notificationBuilder.build());
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.get().load(song.getUrl().replace("http", "https")).into(target);
    }

    public void seekBarSetup() {
        runnable = () -> {
            ContentActivity.appCompatSeekBar.setProgress(mediaPlayer.getCurrentPosition());
            if (PlayerActivity.isBound) {
                PlayerActivity.textViewCurrentPosition.setText(Utils.formatTime(mediaPlayer.getCurrentPosition()));
                PlayerActivity.appCompatSeekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
            new Handler(Looper.getMainLooper()).postDelayed(runnable, 1000);
        };
        new Handler(Looper.getMainLooper()).postDelayed(runnable, 0);
    }

    public void startTimer() {
        long milliseconds = 0;
        if (ContentActivity.musicPlayerService.min5) milliseconds = 5000;
        if (ContentActivity.musicPlayerService.min10) milliseconds = 10000;
        if (ContentActivity.musicPlayerService.min15) milliseconds = 15000;
        countDownTimer = new CountDownTimer(milliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                if (min5 || min10 || min15) {
                    pauseSong();
                    if (PlayerActivity.isBound) {
                        PlayerActivity.buttonTimer.setImageResource(R.drawable.ic_alarm);
                        min5 = false;
                        min10 = false;
                        min15 = false;
                    }
                }
                countDownTimer = null;
            }
        }.start();

    }

    private void pauseSong() {
        mediaPlayer.pause();
        isPlaying = false;
        sendNotificationMedia(currentSong);
        ContentActivity.buttonPlayPause.setImageResource(R.drawable.ic_play_arrow);
        PlayerActivity.buttonPlayPause.setImageResource(R.drawable.ic_play_circle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void toggleSelected(int id) {
        for (int i = 0; i < songs.size(); ++i) {
            selected[i] = !selected[i];
        }
    }

    public Song skipToNext(int id, int repeatMode) {
        int i;
        for (i = 0; i < songs.size(); ++i) {
            if (songs.get(i).getSong_id() == id) {
                break;
            }
        }
        if (repeatMode == 1) {
            if (i == songs.size() - 1) {
                return songs.get(0);
            }
            return songs.get(i + 1);
        }
        if (i == songs.size() - 1) {
            return null;
        }
        return songs.get(i + 1);
    }

    public Song skipToPrevious(int id, int repeatMode) {
        int i;
        for (i = 0; i < songs.size(); ++i) {
            if (songs.get(i).getSong_id() == id) {
                break;
            }
        }
        if (repeatMode == 1) {
            if (i == 0) {
                return songs.get(songs.size() - 1);
            }
            return songs.get(i - 1);
        }
        if (i == 0) {
            return null;
        }
        return songs.get(i - 1);
    }

    public Song skipShuffle() {
        int i = Utils.generateRandomNumberInRange(songs.size());
        return songs.get(i);
    }

    public void prevSongReceiver(Song currentSong) {
        ContentActivity.musicPlayerService.mediaPlayer.pause();
        Song newSong;
        if (ContentActivity.musicPlayerService.isShuffle)
            newSong = ContentActivity.musicPlayerService.skipShuffle();
        else
            newSong = ContentActivity.musicPlayerService.skipToPrevious(currentSong.getSong_id(), ContentActivity.musicPlayerService.repeatMode);
        if (newSong == null) {
            ContentActivity.musicPlayerService.isPlaying = false;
            ContentActivity.buttonPlayPause.setImageResource(R.drawable.ic_play_arrow);
            ContentActivity.musicPlayerService.sendNotificationMedia(currentSong);
            return;
        }
        try {
            ContentActivity.linearLayoutMiniPlayer.setVisibility(View.VISIBLE);
            ContentActivity.textViewSongName.setText(newSong.getSong_id());
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
                            HttpUtils.getInstance(getBaseContext()).getRequestQueue().add(jsonObjectArtistRequest);
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_RESPONSE", error.toString());
                }
            });
            HttpUtils.getInstance(getBaseContext()).getRequestQueue().add(jsonObjectRequest);
            ContentActivity.buttonPlayPause.setImageResource(R.drawable.ic_pause);
            ContentActivity.appCompatSeekBar.setMax(newSong.getLength() * 1000);

            ContentActivity.musicPlayerService.mediaPlayer.reset();
            ContentActivity.musicPlayerService.mediaPlayer.setDataSource(newSong.getUrl());
            ContentActivity.musicPlayerService.mediaPlayer.prepareAsync();
            ContentActivity.musicPlayerService.isPlaying = true;
            ContentActivity.musicPlayerService.sendNotificationMedia(newSong);
            ContentActivity.musicPlayerService.currentSong = newSong;
            if (PlayerActivity.isBound) {
                PlayerActivity.textViewSongName.setText(newSong.getSong_id());
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
                                        PlayerActivity.textViewArtistName.setText(artistName[0]);
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("LOG_RESPONSE", error.toString());
                                    }
                                });
                                HttpUtils.getInstance(getBaseContext()).getRequestQueue().add(jsonObjectArtistRequest);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                });
                HttpUtils.getInstance(getBaseContext()).getRequestQueue().add(jsonObjectRequest);
                PlayerActivity.appCompatSeekBar.setMax(newSong.getLength() * 1000);
                PlayerActivity.textViewMax.setText(Utils.formatTime(newSong.getLength() * 1000));
                PlayerActivity.textViewCurrentPosition.setText(Utils.formatTime(0));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void pauseSongReceiver() {
        ContentActivity.musicPlayerService.mediaPlayer.pause();
        ContentActivity.musicPlayerService.isPlaying = false;
        ContentActivity.buttonPlayPause.setImageResource(R.drawable.ic_play_arrow);
        ContentActivity.musicPlayerService.sendNotificationMedia(ContentActivity.musicPlayerService.currentSong);
        if (PlayerActivity.isBound) {
            PlayerActivity.buttonPlayPause.setImageResource(R.drawable.ic_play_circle);
        }
    }

    public void resumeSongReceiver() {
        ContentActivity.musicPlayerService.mediaPlayer.start();
        ContentActivity.musicPlayerService.isPlaying = true;
        ContentActivity.buttonPlayPause.setImageResource(R.drawable.ic_pause);
        ContentActivity.musicPlayerService.sendNotificationMedia(ContentActivity.musicPlayerService.currentSong);
        if (PlayerActivity.isBound) {
            PlayerActivity.buttonPlayPause.setImageResource(R.drawable.ic_pause_circle);
        }
    }

    public void nextSongReceiver(Song currentSong) {
        ContentActivity.musicPlayerService.mediaPlayer.pause();
        Song newSong;
        if (ContentActivity.musicPlayerService.isShuffle)
            newSong = ContentActivity.musicPlayerService.skipShuffle();
        else
            newSong = ContentActivity.musicPlayerService.skipToNext(currentSong.getSong_id(), ContentActivity.musicPlayerService.repeatMode);
        if (newSong == null) {
            ContentActivity.musicPlayerService.isPlaying = false;
            ContentActivity.buttonPlayPause.setImageResource(R.drawable.ic_play_arrow);
            if (PlayerActivity.isBound) {
                PlayerActivity.buttonPlayPause.setImageResource(R.drawable.ic_play_circle);
            }
            ContentActivity.musicPlayerService.sendNotificationMedia(currentSong);
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
                            HttpUtils.getInstance(getBaseContext()).getRequestQueue().add(jsonObjectArtistRequest);
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_RESPONSE", error.toString());
                }
            });
            HttpUtils.getInstance(getBaseContext()).getRequestQueue().add(jsonObjectRequest);
            ContentActivity.buttonPlayPause.setImageResource(R.drawable.ic_pause);
            ContentActivity.appCompatSeekBar.setMax(newSong.getLength() * 1000);

            ContentActivity.musicPlayerService.mediaPlayer.reset();
            ContentActivity.musicPlayerService.mediaPlayer.setDataSource(newSong.getUrl());
            ContentActivity.musicPlayerService.mediaPlayer.prepareAsync();
            ContentActivity.musicPlayerService.isPlaying = true;
            ContentActivity.musicPlayerService.sendNotificationMedia(newSong);
            ContentActivity.musicPlayerService.currentSong = newSong;
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
                                        PlayerActivity.textViewArtistName.setText(artistName[0]);
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("LOG_RESPONSE", error.toString());
                                    }
                                });
                                HttpUtils.getInstance(getBaseContext()).getRequestQueue().add(jsonObjectArtistRequest);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                });
                HttpUtils.getInstance(getBaseContext()).getRequestQueue().add(jsonObjectRequest);
                PlayerActivity.appCompatSeekBar.setMax(newSong.getLength() * 1000);
                PlayerActivity.textViewMax.setText(Utils.formatTime(newSong.getLength() * 1000));
                PlayerActivity.textViewCurrentPosition.setText(Utils.formatTime(0));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (mediaPlayer != null) mediaPlayer.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
    }
}
