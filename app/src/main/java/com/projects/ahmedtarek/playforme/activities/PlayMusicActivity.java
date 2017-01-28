package com.projects.ahmedtarek.playforme.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.projects.ahmedtarek.playforme.activityfragments.PlayMusicFragment;
import com.projects.ahmedtarek.playforme.R;

public class PlayMusicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
