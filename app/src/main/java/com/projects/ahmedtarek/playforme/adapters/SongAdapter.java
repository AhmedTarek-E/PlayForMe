package com.projects.ahmedtarek.playforme.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.projects.ahmedtarek.playforme.R;
import com.projects.ahmedtarek.playforme.interfaces.OnItemClickListener;
import com.projects.ahmedtarek.playforme.models.Album;
import com.projects.ahmedtarek.playforme.models.Song;
import com.projects.ahmedtarek.playforme.playerside.MediaBrowserHelper;

import java.util.List;

/**
 *
 * Created by Ahmed Tarek on 1/10/2017.
 */
public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    Context context;
    List<Song> songs;
    OnItemClickListener itemClickListener;

    public SongAdapter(Context context, List<Song> songs, OnItemClickListener itemClickListener) {
        this.context = context;
        this.songs = songs;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_card, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.songNameView.setText(song.getTitle());
        holder.artistView.setText(song.getArtist());
        holder.durationView.setText(song.getDuration());
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView songNameView;
        TextView artistView;
        TextView durationView;

        public SongViewHolder(View itemView) {
            super(itemView);

            songNameView = (TextView) itemView.findViewById(R.id.songName);
            artistView = (TextView) itemView.findViewById(R.id.artistTextView);
            durationView = (TextView) itemView.findViewById(R.id.durationTextView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                Song song = songs.get(getLayoutPosition());

                Bundle extras = new Bundle();
                extras.putSerializable(MediaBrowserHelper.MEDIA_PLAYING_ID, song);

                MediaBrowserCompat.MediaItem mediaItem = new MediaBrowserCompat.MediaItem(
                        new MediaDescriptionCompat.Builder()
                                .setTitle(song.getTitle())
                                .setExtras(extras)
                                .setMediaId(String.valueOf(song.getId()))
                                .build(),
                        MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
                );

                itemClickListener.onItemClick(mediaItem);
            }
        }
    }
}
