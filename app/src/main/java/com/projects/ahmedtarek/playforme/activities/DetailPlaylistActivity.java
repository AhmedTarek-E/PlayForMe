package com.projects.ahmedtarek.playforme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.projects.ahmedtarek.playforme.R;
import com.projects.ahmedtarek.playforme.Utils;

public class DetailPlaylistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_playlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        MediaBrowserCompat.MediaItem mediaItem = getIntent().getParcelableExtra(Intent.EXTRA_INTENT);
        toolbar.setTitle(mediaItem.getDescription().getTitle());
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addSongButton);
        if (getIntent().getStringExtra(Intent.EXTRA_TEXT).equals(Utils.PLAYLIST_MODE)) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: 1/11/2017 add song
                }
            });
        } else {
            fab.setVisibility(View.GONE);
        }
    }

}
