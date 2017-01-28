package com.projects.ahmedtarek.playforme.playerside;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.projects.ahmedtarek.playforme.Utils;

import java.util.List;

/**
 *
 * Created by Ahmed Tarek on 1/17/2017.
 */
public class MediaPlaybackBrowserService extends MediaBrowserServiceCompat {
    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder playbackStateBuilder;
    private final String TAG = "MediaPlayback Service";

    @Override
    public void onCreate() {
        super.onCreate();

        mediaSession = new MediaSessionCompat(getBaseContext(), TAG);
        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        );

        playbackStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                );

        // Callback
        mediaSession.setCallback(new PlayForMeCallback());

        Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);

        PendingIntent mbrIntent = PendingIntent
                .getService(getBaseContext(), MediaBrowserHelper.MEDIA_PLAYBACK_REQUEST, mediaButtonIntent, 0);

        mediaSession.setMediaButtonReceiver(mbrIntent);

        setSessionToken(mediaSession.getSessionToken());
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        if (allowBrowsing(clientPackageName, clientUid))
            return new BrowserRoot(MediaBrowserHelper.MEDIA_ALBUM_CONTENT_ID, null);

        return null;
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        MediaBrowserHelper helper = new MediaBrowserHelper(getApplicationContext());
        if (MediaBrowserHelper.MEDIA_ALBUMS_ROOT_ID.equals(parentId)) {
            result.sendResult(helper.getListOfAlbums());
        } else if (MediaBrowserHelper.MEDIA_GENRE_ROOT_ID.equals(parentId)) {
            result.sendResult(helper.getListOfGenres());
        } else {
            result.sendResult(null);
        }
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result, @NonNull Bundle options) {
        MediaBrowserHelper helper = new MediaBrowserHelper(getApplicationContext());

        if (MediaBrowserHelper.MEDIA_ALBUM_CONTENT_ID.equals(parentId)) {
            result.sendResult(helper.getSongsOfAlbum(options.getString(Utils.MODE_KEY)));
        } else if (MediaBrowserHelper.MEDIA_GENRE_CONTENT_ID.equals(parentId)) {
            result.sendResult(helper.getSongsOfGenre(options.getString(Utils.MODE_KEY)));
        } else {
            result.sendResult(null);
        }
    }

    private boolean allowBrowsing(String packageName, int clientUid) {
        if (Process.SYSTEM_UID == clientUid || Process.myUid() == clientUid)
            return true;

        return false;
    }

    static class PlayForMeCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            super.onPlay();
        }

        @Override
        public void onPause() {
            super.onPause();
        }

        @Override
        public void onSkipToNext() {
            super.onSkipToNext();
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
        }

        @Override
        public void onStop() {
            super.onStop();
        }

        @Override
        public void onSeekTo(long pos) {
            super.onSeekTo(pos);
        }
    }
}


