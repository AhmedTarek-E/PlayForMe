package com.projects.ahmedtarek.playforme.newplaylistfragment;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projects.ahmedtarek.playforme.R;
import com.projects.ahmedtarek.playforme.adapters.MoodAdapter;
import com.projects.ahmedtarek.playforme.adapters.SongAdapter;
import com.projects.ahmedtarek.playforme.models.Mood;
import com.projects.ahmedtarek.playforme.models.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoodFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoodFragment extends Fragment {
    RecyclerView.LayoutManager layoutManager;
    MoodAdapter moodAdapter;

    public MoodFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment MoodFragment.
     */
    public static MoodFragment newInstance(Bundle args) {
        MoodFragment fragment = new MoodFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: 1/15/2017 getBundle
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_mood, container, false);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);
        } else {
            layoutManager = new GridLayoutManager(getActivity(), 4, LinearLayoutManager.VERTICAL, false);
        }

        moodAdapter = new MoodAdapter(getActivity(), getMoods());

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.moodsRecyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(moodAdapter);

        // TODO: 1/15/2017 add onItemSelectedListener

        // TODO: 1/15/2017 make a playlist based on mood
        return rootView;
    }

    private List<Mood> getMoods() {
        List<Mood> moods = new ArrayList<>();
        moods.add(new Mood("Loving", R.drawable.loving));
        moods.add(new Mood("Happy", R.drawable.happy));
        moods.add(new Mood("Sad", R.drawable.sad));
        moods.add(new Mood("calm", R.drawable.calm));
        return moods;
    }
}
