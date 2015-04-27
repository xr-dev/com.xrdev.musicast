package com.xrdev.musicast.activity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.xrdev.musicast.Application;
import com.xrdev.musicast.R;
import com.xrdev.musicast.utils.PrefsManager;

public class PartyFragment extends Fragment {

    private Button mGotItButton;
    private TextView mGuestDescriptionText;
    private MusicastActivity mActivity;

    public static PartyFragment newInstance() {
        PartyFragment fragment = new PartyFragment();
        return fragment;
    }


    public PartyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_party, container, false);
        mGotItButton = (Button) v.findViewById(R.id.button_got_it);
        mGuestDescriptionText = (TextView) v.findViewById(R.id.text_party_description);

        String thisUuid = PrefsManager.getUUID(getActivity().getApplicationContext());
        String adminUuid = Application.getAdmin();

        if (thisUuid.equals(adminUuid))
            mGuestDescriptionText.setText(R.string.string_party_admin);
        else
            mGuestDescriptionText.setText(R.string.string_party_guest);

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
