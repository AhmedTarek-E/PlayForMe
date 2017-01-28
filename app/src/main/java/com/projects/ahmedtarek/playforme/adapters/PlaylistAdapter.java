package com.projects.ahmedtarek.playforme.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.projects.ahmedtarek.playforme.R;
import com.projects.ahmedtarek.playforme.models.Playlist;

import java.util.ArrayList;

/**
 * Created by Ahmed Tarek on 1/8/2017.
 */
public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {
    ArrayList<Playlist> playlists = new ArrayList<>();
    Context context;

    public PlaylistAdapter(ArrayList<Playlist> playlists, Context context) {
        this.playlists = playlists;
        this.context = context;
    }

    @Override
    public PlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playlist_card, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaylistViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    class PlaylistViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView playlistName;

        public PlaylistViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.playlistImage);
            playlistName = (TextView) itemView.findViewById(R.id.playlistName);
        }
    }
}
