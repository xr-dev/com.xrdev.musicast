package com.xrdev.musicast.activity;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.xrdev.musicast.Application;
import com.xrdev.musicast.R;
import com.xrdev.musicast.utils.JsonConverter;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.xrdev.musicast.activity.ModeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link com.xrdev.musicast.activity.ModeFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ModeFragment extends Fragment {

    private Button mSoloButton;
    private Button mPartyButton;
    public OnModeSelectedListener mCallback;

    public static ModeFragment newInstance() {
        ModeFragment fragment = new ModeFragment();
        return fragment;
    }

    public interface OnModeSelectedListener {
        public void onModeSelected(int mode);
    }

    public ModeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mode, container, false);
        mSoloButton = (Button) v.findViewById(R.id.solo_button);
        mPartyButton = (Button) v.findViewById(R.id.party_button);

        mSoloButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onModeSelected(Application.MODE_SOLO);

            }
        });

        mPartyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onModeSelected(Application.MODE_PARTY);
            }
        });

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallback = (OnModeSelectedListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
