package com.ptit.spotify.player;

import static com.ptit.spotify.application.SpotifyApplication.ACTION_NEXT;
import static com.ptit.spotify.application.SpotifyApplication.ACTION_PAUSE;
import static com.ptit.spotify.application.SpotifyApplication.ACTION_PREV;
import static com.ptit.spotify.application.SpotifyApplication.ACTION_RESUME;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import lombok.SneakyThrows;

public class MusicPlayerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case ACTION_PREV:
                ContentActivity.musicPlayerService.prevSongReceiver(ContentActivity.musicPlayerService.currentSong);
                break;
            case ACTION_PAUSE:
                ContentActivity.musicPlayerService.pauseSongReceiver();
                break;
            case ACTION_RESUME:
                ContentActivity.musicPlayerService.resumeSongReceiver();
                break;
            case ACTION_NEXT:
                ContentActivity.musicPlayerService.nextSongReceiver(ContentActivity.musicPlayerService.currentSong);
                break;
        }
    }

    private void nextSong(Song currentSong) {
    }

    private void resumeSong() {
        ContentActivity.musicPlayerService.mediaPlayer.start();
        ContentActivity.musicPlayerService.isPlaying = true;
        ContentActivity.buttonPlayPause.setImageResource(R.drawable.ic_pause);
        ContentActivity.musicPlayerService.sendNotificationMedia(ContentActivity.musicPlayerService.currentSong);
        if (PlayerActivity.isBound) {
            PlayerActivity.buttonPlayPause.setImageResource(R.drawable.ic_pause_circle);
        }
    }

    private void pauseSong() {
        ContentActivity.musicPlayerService.mediaPlayer.pause();
        ContentActivity.musicPlayerService.isPlaying = false;
        ContentActivity.buttonPlayPause.setImageResource(R.drawable.ic_play_arrow);
        ContentActivity.musicPlayerService.sendNotificationMedia(ContentActivity.musicPlayerService.currentSong);
        if (PlayerActivity.isBound) {
            PlayerActivity.buttonPlayPause.setImageResource(R.drawable.ic_play_circle);
        }
    }
}
