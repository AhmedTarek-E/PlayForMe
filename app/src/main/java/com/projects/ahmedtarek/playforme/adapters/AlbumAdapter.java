package com.projects.ahmedtarek.playforme.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.projects.ahmedtarek.playforme.R;
import com.projects.ahmedtarek.playforme.Utils;
import com.projects.ahmedtarek.playforme.interfaces.OnItemClickListener;
import com.projects.ahmedtarek.playforme.models.Album;
import com.projects.ahmedtarek.playforme.playerside.MediaBrowserHelper;

import java.util.List;

/**
 *
 * Created by Ahmed Tarek on 1/16/2017.
 */
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {
    private Context context;
    private List<Album> albums;
    OnItemClickListener itemClickListener;

    public AlbumAdapter(Context context, List<Album> albums, OnItemClickListener itemClickListener) {
        this.context = context;
        this.albums = albums;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AlbumViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.album_card, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int position) {
        Album album = albums.get(position);

        holder.nameView.setText(Utils.getTrimmedText(album.getAlbumName()));
        holder.artist.setText(Utils.getTrimmedText(album.getArtist()));
        Glide.with(context)
                .load(album.getAlbumArt())
                .placeholder(R.drawable.album_placeholder)
                .into(holder.albumImageView);
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    class AlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView albumImageView;
        TextView nameView, artist;

        public AlbumViewHolder(View itemView) {
            super(itemView);

            albumImageView = (ImageView) itemView.findViewById(R.id.albumImageView);
            artist = (TextView) itemView.findViewById(R.id.artistTextView);
            nameView = (TextView) itemView.findViewById(R.id.albumNameTextView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                Album album = albums.get(getLayoutPosition());

                Bundle extras = new Bundle();
                extras.putSerializable(MediaBrowserHelper.MEDIA_ALBUMS_ROOT_ID, album);

                MediaBrowserCompat.MediaItem mediaItem = new MediaBrowserCompat.MediaItem(
                        new MediaDescriptionCompat.Builder()
                                .setTitle(album.getAlbumName())
                                .setExtras(extras)
                                .setMediaId(String.valueOf(album.getId()))
                                .build(),
                        MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
                );

                itemClickListener.onItemClick(mediaItem);
            }
        }
    }
}
