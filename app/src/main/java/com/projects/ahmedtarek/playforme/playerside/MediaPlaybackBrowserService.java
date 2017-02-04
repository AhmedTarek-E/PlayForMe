package com.projects.ahmedtarek.playforme.playerside;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.Process;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.NotificationCompat;
import android.widget.MediaController;

import com.projects.ahmedtarek.playforme.Utils;
import com.projects.ahmedtarek.playforme.models.Song;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * Created by Ahmed Tarek on 1/17/2017.
 */
public class MediaPlaybackBrowserService extends MediaBrowserServiceCompat implements MediaPlayer.OnPreparedListener {
    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder playbackStateBuilder;
    private IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
    private MediaPlayer mediaPlayer;
    private static MediaMetadataCompat metadata;
    private boolean isTransient = false;
    private static boolean serviceStarted = false;
    private Timer mTimer;

    private final long ONE_SEC = 1000;

    private final String TAG = "MediaPlaybackService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MediaButtonReceiver.handleIntent(mediaSession, intent);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mediaSession = new MediaSessionCompat(getApplicationContext(), TAG);
        mediaPlayer = new MediaPlayer();

        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        );

        playbackStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_NEXT |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_SEEK_TO
                );

        mediaSession.setPlaybackState(playbackStateBuilder.build());
        // Callback
        mediaSession.setCallback(new PlayForMeCallback());

        Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);

        PendingIntent mbrIntent = PendingIntent
                .getService(getBaseContext(), MediaBrowserHelper.MEDIA_PLAYBACK_REQUEST, mediaButtonIntent, 0);

        mediaSession.setMediaButtonReceiver(mbrIntent);

        setSessionToken(mediaSession.getSessionToken());
        registerReceiver(audioBecomingNoisyReceiver, intentFilter);
        registerReceiver(activityStartedReceiver, new IntentFilter(Utils.ACTION_ACTIVITY_START));
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

    public static void setMetadata(MediaMetadataCompat metadata) {
        MediaPlaybackBrowserService.metadata = metadata;
    }

    public static boolean isServiceStarted() {
        return serviceStarted;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public void onDestroy() {
        releaseMediaPlayer();
        unregisterReceiver(audioBecomingNoisyReceiver);
        unregisterReceiver(activityStartedReceiver);
        isTransient = false;
    }

    class PlayForMeCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            if (mediaSession.getController().getPlaybackState().getState() != PlaybackStateCompat.STATE_PAUSED) {
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int result = audioManager.requestAudioFocus(
                        afListener,
                        AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN
                );

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mediaSession.setActive(true);
                    if (metadata != null) {
                        mediaSession.setMetadata(metadata);
                        startService(new Intent(getApplicationContext(), MediaPlaybackBrowserService.class));
                        serviceStarted = true;
                        initMediaPlayer(Song.getSongUri(
                                Long.parseLong(ControllerHelper.getMediaController().getMetadata().getDescription().getMediaId())
                        ));
                    }
                }
            }
            if (mediaSession.getController().getPlaybackState().getState() == PlaybackStateCompat.STATE_PAUSED) {
                mediaPlayer.start();
            }

            startTimer();

            mediaSession.setPlaybackState(
                    playbackStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, mediaPlayer.getCurrentPosition(), 0)
                            .setActions(PlaybackStateCompat.ACTION_PLAY)
                            .build()
            );
            isTransient = false;
        }

        @Override
        public void onPause() {
            mediaPlayer.pause();
            mediaSession.setPlaybackState(
                    playbackStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, mediaPlayer.getCurrentPosition(), 0)
                            .setActions(PlaybackStateCompat.ACTION_PAUSE)
                            .build()
            );
            cancelTimer();
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
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audioManager.abandonAudioFocus(afListener);
            mediaPlayer.stop();
            mediaSession.setActive(false);
            mediaSession.setPlaybackState(
                    playbackStateBuilder.setState(PlaybackStateCompat.STATE_STOPPED, 0, 0)
                            .setActions(PlaybackStateCompat.ACTION_STOP)
                            .build()
            );
            stopSelf();
            serviceStarted = false;
        }

        @Override
        public void onSeekTo(long pos) {
            mediaPlayer.seekTo((int) pos);
        }
    }

    private void initMediaPlayer(Uri uri) {
        cancelTimer();
        releaseMediaPlayer();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(getApplicationContext(), uri);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void startTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
            mTimer.scheduleAtFixedRate(
                    new TimerTask() {
                        @Override
                        public void run() {
                            sendProgress();
                        }
                    },
                    0,
                    ONE_SEC / 100
            );
        }
    }

    private void cancelTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
    }

    private AudioManager.OnAudioFocusChangeListener afListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS:
                    AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    audioManager.abandonAudioFocus(afListener);
                    if (mediaPlayer != null) mediaPlayer.stop();
                    mediaSession.setPlaybackState(
                            playbackStateBuilder.setState(PlaybackStateCompat.STATE_STOPPED, 0, 0)
                                    .setActions(PlaybackStateCompat.ACTION_STOP)
                                    .build()
                    );
                    break;

                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    if (mediaSession.getController().getPlaybackState().getState() == PlaybackStateCompat.STATE_PLAYING) {
                        mediaSession.getController().getTransportControls().pause();
                        isTransient = true;
                    }
                    break;

                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    mediaPlayer.setVolume(0.5f, 0.5f);
                    break;

                case AudioManager.AUDIOFOCUS_GAIN:
                    mediaPlayer.setVolume(1, 1);
                    if (isTransient) {
                        mediaSession.getController().getTransportControls().play();
                    }
                    break;
            }
        }
    };

    private BroadcastReceiver audioBecomingNoisyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mediaPlayer.pause();
            mediaSession.setPlaybackState(
                    playbackStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, mediaPlayer.getCurrentPosition(), 0)
                            .setActions(PlaybackStateCompat.ACTION_PAUSE)
                            .build()
            );
        }
    };

    private BroadcastReceiver activityStartedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra(Intent.EXTRA_STREAM, false)) {
                if (mediaSession.getController().getPlaybackState().getState() == PlaybackStateCompat.STATE_PLAYING) {
                    startTimer();
                } else {
                    sendProgress();
                }
            } else {
                cancelTimer();
            }
        }
    };

    private void sendProgress() {
        if (mediaPlayer != null) {
            Intent intent = new Intent(Utils.ACTION_CURRENT_PROGRESS);
            intent.putExtra(Intent.EXTRA_STREAM, mediaPlayer.getCurrentPosition());
            sendBroadcast(intent);
        }
    }


}


