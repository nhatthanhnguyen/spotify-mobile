package com.ptit.spotify.player;

import static com.ptit.spotify.application.SpotifyApplication.ACTION_LIKE;
import static com.ptit.spotify.application.SpotifyApplication.ACTION_NEXT;
import static com.ptit.spotify.application.SpotifyApplication.ACTION_PAUSE;
import static com.ptit.spotify.application.SpotifyApplication.ACTION_PREV;
import static com.ptit.spotify.application.SpotifyApplication.ACTION_RESUME;
import static com.ptit.spotify.application.SpotifyApplication.ACTION_UNLIKE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ptit.spotify.activities.ContentActivity;

public class MusicPlayerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case ACTION_UNLIKE:
                ContentActivity.musicPlayerService.unlikeSongReceiver();
                break;
            case ACTION_LIKE:
                ContentActivity.musicPlayerService.likeSongReceiver();
                break;
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
}
