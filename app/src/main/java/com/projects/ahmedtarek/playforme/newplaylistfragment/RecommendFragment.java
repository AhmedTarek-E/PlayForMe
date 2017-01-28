package com.projects.ahmedtarek.playforme.newplaylistfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projects.ahmedtarek.playforme.R;

public class RecommendFragment extends Fragment {

    public RecommendFragment() {
        // Required empty public constructor
    }

    public static RecommendFragment newInstance(Bundle args) {
        RecommendFragment fragment = new RecommendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // TODO: 1/16/2017 create recommended playlist
        return inflater.inflate(R.layout.fragment_recommend, container, false);
    }

}
