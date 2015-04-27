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

public class NoAdminFragment extends Fragment {

    private Button mBecomeAdminButton;
    private Button mStayAsGuestButton;
    public OnBecomeNewAdmin mCallback;

    public static NoAdminFragment newInstance() {
        NoAdminFragment fragment = new NoAdminFragment();
        return fragment;
    }

    public interface OnBecomeNewAdmin {
        public void onBecomeNewAdmin(boolean becomeAdmin);
    }

    public NoAdminFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_no_admin, container, false);
        mBecomeAdminButton = (Button) v.findViewById(R.id.become_host_button);
        mStayAsGuestButton = (Button) v.findViewById(R.id.stay_as_guest_button);

        mBecomeAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onBecomeNewAdmin(true);

            }
        });

        mStayAsGuestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onBecomeNewAdmin(false);
            }
        });



        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallback = (OnBecomeNewAdmin) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
