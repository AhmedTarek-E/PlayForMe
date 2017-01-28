package com.projects.ahmedtarek.playforme.newplaylistfragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projects.ahmedtarek.playforme.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CriteriaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CriteriaFragment extends Fragment {
    public static final String GENRE_CRITERIA = "Genre";
    public static final String ARTIST_CRITERIA = "Artist";
    public static final String MODE_KEY = "mood_key";

    private String mode;

    public CriteriaFragment() {
        // Required empty public constructor
    }

    public static CriteriaFragment newInstance(Bundle args) {
        CriteriaFragment fragment = new CriteriaFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           mode = getArguments().getString(MODE_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_criteria, container, false);
        // TODO: 1/15/2017 init the recyclerView
        if (mode != null)
            // TODO: 1/15/2017 do things in switch case
            switch (mode) {
                case GENRE_CRITERIA:
                    break;
                case ARTIST_CRITERIA:
                    break;
            }
        return rootView;
    }

}
