package com.xrdev.musicast.activity;



import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xrdev.musicast.R;
import com.xrdev.musicast.model.PlaylistItem;
import com.xrdev.musicast.model.TrackItem;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class VoteFragment extends Fragment {

    private TextView mVoteFragmentTextView;
    private Button mVoteButton;
    private ImageView mDislikeImage;
    private ImageView mLikeImage;

    public static VoteFragment newInstance() {
        VoteFragment fragment = new VoteFragment();
        return fragment;
    }
    public VoteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_vote, container, false);
        mVoteFragmentTextView = (TextView) v.findViewById(R.id.text_vote_fragment);
        mVoteButton = (Button) v.findViewById(R.id.button_vote);
        mDislikeImage = (ImageView) v.findViewById(R.id.imagebutton_dislike);
        mLikeImage = (ImageView) v.findViewById(R.id.imagebutton_like);

        return v;
    }

    public void soloViews(){
        mVoteButton.setVisibility(View.VISIBLE);
        mDislikeImage.setVisibility(View.GONE);
        mLikeImage.setVisibility(View.GONE);

        mVoteButton.setText(R.string.host_party);
        mVoteFragmentTextView.setText(R.string.vote_fragment_text_solo);

        mVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Lógica do clique para mudar para Party Mode.
            }
        });

    }

    public void partyNoVotesViews(){
        mVoteButton.setVisibility(View.GONE);
        mDislikeImage.setVisibility(View.GONE);
        mLikeImage.setVisibility(View.GONE);

        mVoteFragmentTextView.setText(R.string.no_pending_votes);
    }

    public void trackChangeVoteViews(TrackItem track, int voteId){
        mVoteButton.setVisibility(View.GONE);
        mDislikeImage.setVisibility(View.VISIBLE);
        mLikeImage.setVisibility(View.VISIBLE);

        String trackString = track.getArtists() + " - " + track.getName();
        String textFormat = getResources().getString(R.string.vote_fragment_text_change_track);
        String text = String.format(textFormat, trackString);
        mVoteFragmentTextView.setText(text);

        mDislikeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Lógica do clique para enviar o voto.
            }
        });

        mLikeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Lógica do clique para enviar o voto.
            }
        });

    }

    public void playlistChangeVoteViews(PlaylistItem playlist, int voteId){
        mVoteButton.setVisibility(View.GONE);
        mDislikeImage.setVisibility(View.VISIBLE);
        mLikeImage.setVisibility(View.VISIBLE);

        String playlistString = playlist.getName();
        String textFormat = getResources().getString(R.string.vote_fragment_text_change_playlist);
        String text = String.format(textFormat, playlistString);
        mVoteFragmentTextView.setText(text);

        mDislikeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Lógica do clique para enviar o voto.
            }
        });

        mLikeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Lógica do clique para enviar o voto.
            }
        });
    }

    public void loadToPlayViews(int progress, int max){
        mVoteButton.setVisibility(View.GONE);
        mDislikeImage.setVisibility(View.GONE);
        mLikeImage.setVisibility(View.GONE);

        String textFormat = getResources().getString(R.string.vote_fragment_text_load_play);
        String text = String.format(textFormat, progress, max);
        mVoteFragmentTextView.setText(text);
    }

    public void loadToVoteViews(int progress, int max){
        mVoteButton.setVisibility(View.GONE);
        mDislikeImage.setVisibility(View.GONE);
        mLikeImage.setVisibility(View.GONE);

        String textFormat = getResources().getString(R.string.vote_fragment_text_load_vote);
        String text = String.format(textFormat, progress, max);
        mVoteFragmentTextView.setText(text);
    }


}
