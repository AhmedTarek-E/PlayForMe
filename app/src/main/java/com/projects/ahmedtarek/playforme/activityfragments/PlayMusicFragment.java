package com.projects.ahmedtarek.playforme.activityfragments;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.projects.ahmedtarek.playforme.R;
import com.projects.ahmedtarek.playforme.Utils;
import com.projects.ahmedtarek.playforme.models.Song;
import com.projects.ahmedtarek.playforme.playerside.ControllerHelper;
import com.projects.ahmedtarek.playforme.playerside.MediaBrowserHelper;
import com.projects.ahmedtarek.playforme.playerside.MediaPlaybackBrowserService;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlayMusicFragment extends Fragment implements View.OnClickListener {
    MediaBrowserCompat mediaBrowser;
    MediaControllerCompat mediaController;


    // view
    ImageButton mPlayPauseButton;
    ImageButton mNextButton;
    ImageButton mPreviousButton;
    ImageButton mShuffleButton;
    ImageButton mRepeatButton;

    ImageView albumImageView;
    SeekBar seekBar;
    TextView nowProgress;
    TextView endProgress;

    public PlayMusicFragment() {
        setHasOptionsMenu(true);
    }

    public static PlayMusicFragment newInstance() {
        PlayMusicFragment fragment = new PlayMusicFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_play_music, container, false);
        setInsets(getActivity(), rootView);
        initialize();
        return rootView;
    }

    public static void setInsets(Activity context, View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        SystemBarTintManager tintManager = new SystemBarTintManager(context);
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        view.setPadding(0, 0, 0, config.getNavigationBarHeight());
    }

    private void initialize() {
        mediaBrowser = new MediaBrowserCompat(
                getActivity(),
                new ComponentName(getActivity(), MediaPlaybackBrowserService.class),
                connectionCallback,
                null
        );
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mediaBrowser != null) {
            mediaBrowser.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mediaController != null) {
            mediaController.unregisterCallback(controllerCallback);
        }
        if (mediaBrowser != null) {
            mediaBrowser.disconnect();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_play_music, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_playlist:
                // TODO: 1/7/2017 open Playlist Activity
                return true;
            case R.id.action_details:
                // TODO: 1/7/2017 open Details
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private final MediaBrowserCompat.ConnectionCallback connectionCallback = new MediaBrowserCompat.ConnectionCallback() {
        @Override
        public void onConnectionSuspended() {
            super.onConnectionSuspended();
        }

        @Override
        public void onConnected() {
            MediaSessionCompat.Token token = mediaBrowser.getSessionToken();
            try {
                mediaController = new MediaControllerCompat(getActivity(), token);
                ControllerHelper.setMediaController(mediaController);
                buildTransportControls();
                mediaController.registerCallback(controllerCallback);
                MediaPlaybackBrowserService.setMetadata(buildMetaData());
                mediaController.getTransportControls().play();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConnectionFailed() {
            super.onConnectionFailed();
        }
    };

    MediaControllerCompat.Callback controllerCallback = new MediaControllerCompat.Callback() {
        @Override
        public void onAudioInfoChanged(MediaControllerCompat.PlaybackInfo info) {
            super.onAudioInfoChanged(info);
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            super.onMetadataChanged(metadata);
        }

        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            int playbackState = state.getState();
            switch (playbackState) {
                case PlaybackStateCompat.STATE_PLAYING:
                    mPlayPauseButton.setImageResource(R.drawable.pause_button);
                    break;
                case PlaybackStateCompat.STATE_PAUSED:
                    mPlayPauseButton.setImageResource(R.drawable.play_button);
                    break;
                case PlaybackStateCompat.STATE_STOPPED:
                    mPlayPauseButton.setImageResource(R.drawable.play_button);
                    break;
            }
        }
    };

    private void buildTransportControls() {
        View rootView = getView();

        mPlayPauseButton = (ImageButton) rootView.findViewById(R.id.playButton);
        mNextButton = (ImageButton) rootView.findViewById(R.id.nextButton);
        mPreviousButton = (ImageButton) rootView.findViewById(R.id.previousButton);
        mShuffleButton = (ImageButton) rootView.findViewById(R.id.shuffleButton);
        mRepeatButton = (ImageButton) rootView.findViewById(R.id.repeatButton);

        mPlayPauseButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        mPreviousButton.setOnClickListener(this);
        mShuffleButton.setOnClickListener(this);
        mRepeatButton.setOnClickListener(this);

        MediaMetadataCompat metadata = mediaController.getMetadata();
        PlaybackStateCompat playbackState = mediaController.getPlaybackState();

        albumImageView = (ImageView) rootView.findViewById(R.id.albumImageView);
        seekBar = (SeekBar) rootView.findViewById(R.id.seek_bar);
        nowProgress = (TextView) rootView.findViewById(R.id.seekNowText);
        endProgress = (TextView) rootView.findViewById(R.id.seekEndText);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.playButton) {
            int pbState = mediaController.getPlaybackState().getState();
            if (pbState == PlaybackStateCompat.STATE_PLAYING) {
                // pause
                mediaController.getTransportControls().pause();
            } else {
                // play
                //MediaPlaybackBrowserService.setMetadata(buildMetaData());
                mediaController.getTransportControls().play();
            }
        } else if (id == R.id.nextButton) {
            mediaController.getTransportControls().skipToNext();
        } else if (id == R.id.previousButton) {
            mediaController.getTransportControls().skipToPrevious();
        } else if (id == R.id.shuffleButton) {
            // todo shuffle playlist
        } else if (id == R.id.repeatButton) {
            // TODO: 1/28/2017 repeat song
        }
    }

    private MediaMetadataCompat buildMetaData() {
        MediaBrowserCompat.MediaItem mediaItem = getActivity()
                .getIntent().getParcelableExtra(Intent.EXTRA_STREAM);

        Song song = (Song) mediaItem.getDescription()
                .getExtras()
                .getSerializable(MediaBrowserHelper.MEDIA_PLAYING_ID);

        Cursor cursor = getActivity().getContentResolver().query(
                ContentUris.withAppendedId(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, song.getAlbumId()),
                new String[] {MediaStore.Audio.Albums.ALBUM_ART},
                null,
                null,
                null
        );

        String albumArt = null;
        if (cursor != null && cursor.moveToFirst()) {
            albumArt = cursor.getString(0);
            cursor.close();
        }
        Bitmap bitmap = null;
        if (albumArt != null) {
            bitmap = BitmapFactory.decodeFile(albumArt);
        }
        return new MediaMetadataCompat.Builder()
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bitmap)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, song.getArtist())
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, song.getTitle())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, song.getDuration())
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, String.valueOf(song.getId()))
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, song.getAlbum())
                .build();

    }
}
