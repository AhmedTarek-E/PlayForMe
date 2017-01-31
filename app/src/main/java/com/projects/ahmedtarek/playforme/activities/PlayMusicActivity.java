package com.projects.ahmedtarek.playforme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.projects.ahmedtarek.playforme.activityfragments.PlayMusicFragment;
import com.projects.ahmedtarek.playforme.R;
import com.projects.ahmedtarek.playforme.models.Song;
import com.projects.ahmedtarek.playforme.playerside.MediaBrowserHelper;

public class PlayMusicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        MediaBrowserCompat.MediaItem mediaItem = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);

        TextView titleView = (TextView) findViewById(R.id.titleTextView);
        TextView artistView = (TextView) findViewById(R.id.artistTextView);

        Song song = (Song) mediaItem.getDescription().getExtras().getSerializable(MediaBrowserHelper.MEDIA_PLAYING_ID);

        titleView.setText(song.getTitle());
        artistView.setText(song.getArtist());

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.music_fragment_container, PlayMusicFragment.newInstance())
                    .commit();
        }
    }
}
