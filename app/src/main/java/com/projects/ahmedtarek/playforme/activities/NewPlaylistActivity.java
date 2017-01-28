package com.projects.ahmedtarek.playforme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.projects.ahmedtarek.playforme.R;
import com.projects.ahmedtarek.playforme.Utils;
import com.projects.ahmedtarek.playforme.interfaces.OnFragmentInteractionListener;
import com.projects.ahmedtarek.playforme.newplaylistfragment.DoyoulikeFragment;
import com.projects.ahmedtarek.playforme.newplaylistfragment.MoodFragment;
import com.projects.ahmedtarek.playforme.newplaylistfragment.NewPlaylistFragment;

public class NewPlaylistActivity extends AppCompatActivity implements OnFragmentInteractionListener{

    public static final String NEW_PLAYLIST_FRAGMENT_TAG = "new_playlist";
    public static final String MOOD_PLAYLIST_FRAGMENT_TAG = "mood_playlist";
    public static final String DOYOULIKE_PLAYLIST_FRAGMENT_TAG = "doyoulike_playlist";
    public static final String FRAGMENT_TAG_KEY = "fragment_tag";
    public static final String RECOMMEND_FRAGMENT_TAG = "recommend_playlist";

    public static final String PLAYLIST_NAME_KEY = "playlist_name";
    public static final String BUNDLE_KEY = "bundle_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_playlist);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim)
                    .add(R.id.playlist_fragment_container, NewPlaylistFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public void onFragmentInteraction(Bundle bundle) {
        String tag = bundle.getString(FRAGMENT_TAG_KEY);
        if (tag != null)
        switch (tag) {
            case MOOD_PLAYLIST_FRAGMENT_TAG:
                MoodFragment moodFragment = MoodFragment.newInstance(bundle);
                openFragment(moodFragment);
                break;
            case DOYOULIKE_PLAYLIST_FRAGMENT_TAG:
                DoyoulikeFragment doyoulikeFragment = DoyoulikeFragment.newInstance(bundle);
                openFragment(doyoulikeFragment);
                break;
            case RECOMMEND_FRAGMENT_TAG:
                // TODO: 1/15/2017 use RecommendFragment
                break;
            case NEW_PLAYLIST_FRAGMENT_TAG:
                openCustomPlaylist(bundle);
                break;
        }
    }

    private void openFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim)
                .addToBackStack(null)
                .replace(R.id.playlist_fragment_container, fragment)
                .commit();
    }

    private void openCustomPlaylist(Bundle bundle) {
        Intent intent = new Intent(this, DetailPlaylistActivity.class);
        intent.putExtra(BUNDLE_KEY, bundle);
        intent.putExtra(Intent.EXTRA_TEXT, Utils.PLAYLIST_MODE);
        startActivity(intent);
    }
}
