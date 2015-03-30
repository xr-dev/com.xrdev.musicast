package com.xrdev.musicast.activity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.xrdev.musicast.Application;
import com.xrdev.musicast.R;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.xrdev.musicast.activity.CastSelectorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link com.xrdev.musicast.activity.CastSelectorFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class CastSelectorFragment extends Fragment {

    private Button mSoloButton;
    private Button mPartyButton;

    public static CastSelectorFragment newInstance() {
        CastSelectorFragment fragment = new CastSelectorFragment();
        return fragment;
    }


    public CastSelectorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_selector, container, false);
        mSoloButton = (Button) v.findViewById(R.id.solo_button);
        mPartyButton = (Button) v.findViewById(R.id.party_button);


        return v;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


}
