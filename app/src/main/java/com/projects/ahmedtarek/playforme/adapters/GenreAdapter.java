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
import com.projects.ahmedtarek.playforme.playerside.MediaBrowserHelper;

import java.util.List;

/**
 * Created by Ahmed Tarek on 1/25/2017.
 */

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {
    private Context context;
    private List<MediaBrowserCompat.MediaItem> genres;
    private OnItemClickListener itemClickListener;

    public GenreAdapter(Context context, List<MediaBrowserCompat.MediaItem> genres, OnItemClickListener itemClickListener) {
        this.context = context;
        this.genres = genres;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public GenreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GenreViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.genre_card, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(GenreViewHolder holder, int position) {
        holder.genreNameView.setText(
                genres.get(position).getDescription().getTitle()
        );
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    class GenreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView genreNameView;
        public GenreViewHolder(View itemView) {
            super(itemView);

            genreNameView = (TextView) itemView.findViewById(R.id.genreNameView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(genres.get(getLayoutPosition()));
            }
        }
    }
}
