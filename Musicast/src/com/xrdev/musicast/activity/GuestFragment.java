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

public class GuestFragment extends Fragment {

    private Button mGotItButton;
    private MusicastActivity mActivity;

    public static GuestFragment newInstance() {
        GuestFragment fragment = new GuestFragment();
        return fragment;
    }


    public GuestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_guest, container, false);
        mGotItButton = (Button) v.findViewById(R.id.solo_button);

        mGotItButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.loadPlaylistsFragment();
            }
        });

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (MusicastActivity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
