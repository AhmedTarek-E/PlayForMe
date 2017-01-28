package com.projects.ahmedtarek.playforme.activityfragments;

import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projects.ahmedtarek.playforme.R;
import com.projects.ahmedtarek.playforme.adapters.PlaylistAdapter;
import com.projects.ahmedtarek.playforme.models.Playlist;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class AllPlaylistsFragment extends Fragment {
    RecyclerView.LayoutManager layoutManager;
    PlaylistAdapter playlistAdapter;

    public AllPlaylistsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_playlists, container, false);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);
        } else {
            layoutManager = new GridLayoutManager(getActivity(), 4, LinearLayoutManager.VERTICAL, false);
        }

        // TODO: 1/9/2017 get playlists
        playlistAdapter = new PlaylistAdapter(new ArrayList<Playlist>(), getActivity());

        RecyclerView recyclerView = (RecyclerView)
                rootView.findViewById(R.id.allPlaylistsRecyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(playlistAdapter);

        return rootView;
    }
}
