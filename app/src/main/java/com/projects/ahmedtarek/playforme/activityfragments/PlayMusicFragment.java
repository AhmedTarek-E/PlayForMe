package com.projects.ahmedtarek.playforme.activityfragments;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.projects.ahmedtarek.playforme.activities.DetailPlaylistActivity;
import com.projects.ahmedtarek.playforme.models.Song;
import com.projects.ahmedtarek.playforme.playerside.ControllerHelper;
import com.projects.ahmedtarek.playforme.playerside.MediaBrowserHelper;
import com.projects.ahmedtarek.playforme.playerside.MediaPlaybackBrowserService;

public class PlayMusicFragment extends Fragment implements View.OnClickListener {
    MediaBrowserCompat mediaBrowser;
    MediaControllerCompat mediaController;
    MediaMetadataCompat metadata;

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
        Utils.setInsets(getActivity(), rootView);
        initialize();
        return rootView;
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
    public void onPause() {
        super.onPause();
        sendActivityStartBroadcast(false);
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
        getActivity().unregisterReceiver(progressReceiver);
    }

    private void sendActivityStartBroadcast(boolean started) {
        Intent intent = new Intent(Utils.ACTION_ACTIVITY_START);
        intent.putExtra(Intent.EXTRA_STREAM, started);
        getActivity().sendBroadcast(intent);
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
                Intent intent = new Intent(getActivity(), DetailPlaylistActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, Utils.NOW_PLAYLIST_MODE);
                startActivity(intent);
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
            getActivity().registerReceiver(progressReceiver, new IntentFilter(Utils.ACTION_CURRENT_PROGRESS));
            sendActivityStartBroadcast(true);
            MediaSessionCompat.Token token = mediaBrowser.getSessionToken();
            try {
                mediaController = new MediaControllerCompat(getActivity(), token);
                ControllerHelper.setMediaController(mediaController);
                mediaController.registerCallback(controllerCallback);

                metadata = buildMetaData();
                MediaMetadataCompat currentMetadata = mediaController.getMetadata();

                if (currentMetadata != null) {
                    if (metadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)
                            .equals(currentMetadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID))) {
                        metadata = currentMetadata;
                        buildTransportControls();
                        return;
                    } else {
                        mediaController.getTransportControls().stop();
                    }
                } else {
                    if (metadata == null) {
                        getActivity().finish();
                        return;
                    }
                }

                MediaPlaybackBrowserService.setMetadata(metadata);
                mediaController.getTransportControls().play();

                buildTransportControls();
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
            setPlayPauseButtonImage(playbackState);
        }
    };

    private void setPlayPauseButtonImage(int playbackState) {
        if (mPlayPauseButton == null) {
            return;
        }
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

        setPlayPauseButtonImage(mediaController.getPlaybackState().getState());

        if (!ControllerHelper.isRepeated(getActivity())) {
            mRepeatButton.setImageResource(R.drawable.repeat_button_unselected);
        } else {
            mRepeatButton.setImageResource(R.drawable.repeat_button);
        }
        albumImageView = (ImageView) rootView.findViewById(R.id.albumImageView);
        seekBar = (SeekBar) rootView.findViewById(R.id.seek_bar);
        nowProgress = (TextView) rootView.findViewById(R.id.seekNowText);
        endProgress = (TextView) rootView.findViewById(R.id.seekEndText);

        albumImageView.setImageBitmap(metadata.getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART));

        endProgress.setText(Song.getPrettyDuration(metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)));

        seekBar.setMax((int) metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                nowProgress.setText(Song.getPrettyDuration(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaController.getTransportControls().seekTo(seekBar.getProgress());
            }
        });
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
            // TODO: 2/13/2017 shuffle playlist
        } else if (id == R.id.repeatButton) {
            if (ControllerHelper.isRepeated(getActivity())) {
                ControllerHelper.setRepeat(getActivity(), false);
                mRepeatButton.setImageResource(R.drawable.repeat_button_unselected);
            } else {
                ControllerHelper.setRepeat(getActivity(), true);
                mRepeatButton.setImageResource(R.drawable.repeat_button);
            }
            getActivity().sendBroadcast(
                    new Intent(Utils.ACTION_COMMUNICATION_ACTIVITY_SERVICE)
                            .putExtra(ControllerHelper.COMMUNICATION_KEY,
                                    ControllerHelper.REPEAT_FLAG)
            );
        }
    }

    private MediaMetadataCompat buildMetaData() {
        if (!getActivity().getIntent().hasExtra(Intent.EXTRA_STREAM)) {
            return null;
        }

        MediaBrowserCompat.MediaItem mediaItem = getActivity()
                .getIntent().getParcelableExtra(Intent.EXTRA_STREAM);

        Song song = (Song) mediaItem.getDescription()
                .getExtras()
                .getSerializable(MediaBrowserHelper.MEDIA_PLAYING_ID);

        if (song.getAlbumId() == -1) {
            return null;
        }

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

    BroadcastReceiver progressReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final int progress = intent.getIntExtra(Intent.EXTRA_STREAM, 0);
            seekBar.post(new Runnable() {
                @Override
                public void run() {
                    seekBar.setProgress(progress);
                }
            });
        }
    };
}
