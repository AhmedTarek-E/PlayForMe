package com.projects.ahmedtarek.playforme.newplaylistfragment;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.projects.ahmedtarek.playforme.R;
import com.projects.ahmedtarek.playforme.activities.NewPlaylistActivity;
import com.projects.ahmedtarek.playforme.interfaces.OnFragmentInteractionListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewPlaylistFragment extends Fragment implements View.OnClickListener{
    private OnFragmentInteractionListener interactionListener;

    public NewPlaylistFragment() {
    }

    public static NewPlaylistFragment newInstance() {
        NewPlaylistFragment fragment = new NewPlaylistFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            interactionListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_playlist, container, false);

        ImageButton moodButton = (ImageButton) rootView.findViewById(R.id.moodButton);
        ImageButton doyoulikeButton = (ImageButton) rootView.findViewById(R.id.doYouLikeButton);
        ImageButton recommendButton = (ImageButton) rootView.findViewById(R.id.recommendButton);
        ImageButton customButton = (ImageButton) rootView.findViewById(R.id.customButton);

        moodButton.setOnClickListener(this);
        doyoulikeButton.setOnClickListener(this);
        recommendButton.setOnClickListener(this);
        customButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(final View view) {
        Animator animator = AnimatorInflater.loadAnimator(getActivity(), R.animator.selected_anim);
        animator.setTarget(view);

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                doOnClick(view.getId());
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        animator.start();
    }

    private void doOnClick(int viewId) {
        String fragmentTag = NewPlaylistActivity.NEW_PLAYLIST_FRAGMENT_TAG;
        switch (viewId) {
            case R.id.moodButton:
                fragmentTag = NewPlaylistActivity.MOOD_PLAYLIST_FRAGMENT_TAG;
                break;
            case R.id.doYouLikeButton:
                fragmentTag = NewPlaylistActivity.DOYOULIKE_PLAYLIST_FRAGMENT_TAG;
                break;
            case R.id.recommendButton:
                fragmentTag = NewPlaylistActivity.RECOMMEND_FRAGMENT_TAG;
                break;
            case R.id.customButton:
                fragmentTag = NewPlaylistActivity.NEW_PLAYLIST_FRAGMENT_TAG;
                break;
        }

        Bundle bundle = new Bundle();
        bundle.putString(NewPlaylistActivity.FRAGMENT_TAG_KEY, fragmentTag);
        interactionListener.onFragmentInteraction(bundle);
    }
}
