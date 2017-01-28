package com.projects.ahmedtarek.playforme.activityfragments;

import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projects.ahmedtarek.playforme.R;
import com.projects.ahmedtarek.playforme.Utils;
import com.projects.ahmedtarek.playforme.activities.PlayMusicActivity;
import com.projects.ahmedtarek.playforme.adapters.AlbumAdapter;
import com.projects.ahmedtarek.playforme.adapters.GenreAdapter;
import com.projects.ahmedtarek.playforme.adapters.PlaylistAdapter;
import com.projects.ahmedtarek.playforme.adapters.SongAdapter;
import com.projects.ahmedtarek.playforme.interfaces.OnItemClickListener;
import com.projects.ahmedtarek.playforme.mainfragments.HomeFragment;
import com.projects.ahmedtarek.playforme.models.Album;
import com.projects.ahmedtarek.playforme.models.Playlist;
import com.projects.ahmedtarek.playforme.models.Song;
import com.projects.ahmedtarek.playforme.playerside.ControllerHelper;
import com.projects.ahmedtarek.playforme.playerside.MediaBrowserHelper;
import com.projects.ahmedtarek.playforme.playerside.MediaPlaybackBrowserService;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailPlaylistFragment extends Fragment implements OnItemClickListener{
    RecyclerView.LayoutManager layoutManager;
    SongAdapter songAdapter;
    RecyclerView recyclerView;
    MediaBrowserCompat mediaBrowser;
    String TAG = "details";

    public DetailPlaylistFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_playlist, container, false);

        initialize();

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.songsRecyclerView);
        recyclerView.setLayoutManager(layoutManager);

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
    public void onStop() {
        super.onStop();
        if (mediaBrowser != null) {
            mediaBrowser.disconnect();
        }
    }

    private final MediaBrowserCompat.ConnectionCallback connectionCallback = new MediaBrowserCompat.ConnectionCallback() {
        @Override
        public void onConnectionSuspended() {
            super.onConnectionSuspended();
        }

        @Override
        public void onConnected() {
            String mode = getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);
            String parentId = "";
            switch (mode) {
                case Utils.ALBUM_MODE:
                    parentId = MediaBrowserHelper.MEDIA_ALBUM_CONTENT_ID;
                    break;
                case Utils.GENRE_MODE:
                    parentId = MediaBrowserHelper.MEDIA_GENRE_CONTENT_ID;
                    break;
            }
            Bundle options = new Bundle();
            MediaBrowserCompat.MediaItem mediaItem = getActivity().getIntent()
                    .getParcelableExtra(Intent.EXTRA_INTENT);
            options.putString(Utils.MODE_KEY, mediaItem.getMediaId());

            mediaBrowser.subscribe(parentId, options, subscriptionCallback);
            MediaSessionCompat.Token token = mediaBrowser.getSessionToken();
            try {
                MediaControllerCompat mediaController = new MediaControllerCompat(getActivity(), token);
                ControllerHelper.setMediaController(mediaController);

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConnectionFailed() {
            super.onConnectionFailed();
        }
    };

    private final MediaBrowserCompat.SubscriptionCallback subscriptionCallback =
            new MediaBrowserCompat.SubscriptionCallback() {
                @Override
                public void onChildrenLoaded(@NonNull String parentId, List<MediaBrowserCompat.MediaItem> children) {
                    super.onChildrenLoaded(parentId, children);
                }

                @Override
                public void onChildrenLoaded(@NonNull String parentId, List<MediaBrowserCompat.MediaItem> children, @NonNull Bundle options) {
                    super.onChildrenLoaded(parentId, children, options);

                    songAdapter = new SongAdapter(getActivity(), buildSongList(children), DetailPlaylistFragment.this);
                    recyclerView.setAdapter(songAdapter);
                }

                @Override
                public void onError(@NonNull String parentId) {
                    super.onError(parentId);
                }
            };

    private List<Song> buildSongList(List<MediaBrowserCompat.MediaItem> children) {
        List<Song> songList = new ArrayList<>();
        for (MediaBrowserCompat.MediaItem mediaItem : children) {
            Song song = (Song) mediaItem.getDescription().getExtras()
                    .getSerializable(MediaBrowserHelper.MEDIA_CONTENT_KEY);
            songList.add(song);
        }
        return songList;
    }

    @Override
    public void onItemClick(MediaBrowserCompat.MediaItem mediaItem) {
        Intent intent = new Intent(getActivity(), PlayMusicActivity.class);
        intent.putExtra(Intent.EXTRA_INTENT, mediaItem);
        startActivity(intent);
    }
}
