package com.projects.ahmedtarek.playforme.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.projects.ahmedtarek.playforme.R;
import com.projects.ahmedtarek.playforme.models.Mood;

import java.util.List;

/**
 * Created by Ahmed Tarek on 1/15/2017.
 */
public class MoodAdapter extends RecyclerView.Adapter<MoodAdapter.MoodViewHolder> {
    Context context;
    private List<Mood> moods;

    public MoodAdapter(Context context, List<Mood> moods) {
        this.context = context;
        this.moods = moods;
    }

    @Override
    public MoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mood_card, parent, false);
        return new MoodViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MoodViewHolder holder, int position) {
        Mood mood = moods.get(position);
        holder.moodNameView.setText(mood.getMoodName());
        holder.moodIconView.setImageResource(mood.getMoodIcon());
    }

    @Override
    public int getItemCount() {
        return moods.size();
    }

    class MoodViewHolder extends RecyclerView.ViewHolder {
        ImageView moodIconView;
        TextView moodNameView;

        public MoodViewHolder(View itemView) {
            super(itemView);
            moodIconView = (ImageView) itemView.findViewById(R.id.moodIconView);
            moodNameView = (TextView) itemView.findViewById(R.id.moodName);
        }
    }
}
