package com.projects.ahmedtarek.playforme.newplaylistfragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projects.ahmedtarek.playforme.R;
import com.projects.ahmedtarek.playforme.adapters.SectionsPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoyoulikeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoyoulikeFragment extends Fragment {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    public DoyoulikeFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment DoyoulikeFragment.
     */
    public static DoyoulikeFragment newInstance(Bundle args) {
        DoyoulikeFragment fragment = new DoyoulikeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // get Name of the playlist from args
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_doyoulike, container, false);

        mViewPager = (ViewPager) rootView.findViewById(R.id.container);
        setupViewPager();

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return rootView;
    }

    private void setupViewPager() {
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        // Artist Tab
        Bundle bundle = getArguments();
        bundle.putString(CriteriaFragment.MODE_KEY, CriteriaFragment.ARTIST_CRITERIA);
        CriteriaFragment criteriaFragment = CriteriaFragment.newInstance(bundle);

        mSectionsPagerAdapter.addFragment(criteriaFragment, CriteriaFragment.ARTIST_CRITERIA);

        // Genre Tab
        bundle = getArguments();
        bundle.putString(CriteriaFragment.MODE_KEY, CriteriaFragment.GENRE_CRITERIA);
        criteriaFragment = CriteriaFragment.newInstance(bundle);

        mSectionsPagerAdapter.addFragment(criteriaFragment, CriteriaFragment.GENRE_CRITERIA);

        mViewPager.setAdapter(mSectionsPagerAdapter);
    }
}
