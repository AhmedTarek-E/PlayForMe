package com.projects.ahmedtarek.playforme.mainfragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projects.ahmedtarek.playforme.R;
import com.projects.ahmedtarek.playforme.Utils;
import com.projects.ahmedtarek.playforme.activities.DetailPlaylistActivity;
import com.projects.ahmedtarek.playforme.adapters.AlbumAdapter;
import com.projects.ahmedtarek.playforme.adapters.GenreAdapter;
import com.projects.ahmedtarek.playforme.interfaces.OnFragmentInteractionListener;
import com.projects.ahmedtarek.playforme.interfaces.OnItemClickListener;
import com.projects.ahmedtarek.playforme.models.Album;
import com.projects.ahmedtarek.playforme.playerside.ControllerHelper;
import com.projects.ahmedtarek.playforme.playerside.MediaBrowserHelper;
import com.projects.ahmedtarek.playforme.playerside.MediaPlaybackBrowserService;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements OnItemClickListener {
    private OnFragmentInteractionListener mListener;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private MediaBrowserCompat mediaBrowser;
    private RecyclerView recyclerView;

    private String mode;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(Bundle args) {
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mode = getArguments().getString(Utils.MODE_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        initialize();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);
        } else {
            layoutManager = new GridLayoutManager(getActivity(), 4, LinearLayoutManager.VERTICAL, false);
        }

        recyclerView = (RecyclerView) rootView.findViewById(R.id.homeRecyclerView);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setAdapter(adapter);

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
            String parentId = "";
            if (mode.equals(Utils.ALBUM_MODE)) {
                parentId = MediaBrowserHelper.MEDIA_ALBUMS_ROOT_ID;
            } else if (mode.equals(Utils.GENRE_MODE)) {
                parentId = MediaBrowserHelper.MEDIA_GENRE_ROOT_ID;
            }

            mediaBrowser.subscribe(parentId, subscriptionCallback);
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
                    switch (mode) {
                        case Utils.GENRE_MODE:
                            adapter = new GenreAdapter(getActivity(), children, HomeFragment.this);
                            break;
                        case Utils.ALBUM_MODE:
                            adapter = new AlbumAdapter(getActivity(), buildAlbumList(children), HomeFragment.this);
                            break;
                    }

                    if (adapter != null) {
                        recyclerView.setAdapter(adapter);
                    }
                }

                @Override
                public void onError(@NonNull String parentId) {
                    super.onError(parentId);
                }
            };

    private List<Album> buildAlbumList(List<MediaBrowserCompat.MediaItem> children) {
        List<Album> albumList = new ArrayList<>();
        for (MediaBrowserCompat.MediaItem mediaItem : children) {
            Album album = (Album) mediaItem.getDescription().getExtras()
                    .getSerializable(MediaBrowserHelper.MEDIA_ALBUMS_ROOT_ID);
            albumList.add(album);
        }
        return albumList;
    }

    @Override
    public void onItemClick(MediaBrowserCompat.MediaItem mediaItem) {
        startDetailedPlaylistActivity(mediaItem);
    }

    private void startDetailedPlaylistActivity(MediaBrowserCompat.MediaItem mediaItem) {
        Intent intent = new Intent(getActivity(), DetailPlaylistActivity.class);
        intent.putExtra(Intent.EXTRA_INTENT, mediaItem);
        intent.putExtra(Intent.EXTRA_TEXT, mode);
        startActivity(intent);
    }
}
