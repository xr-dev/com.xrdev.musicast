package com.xrdev.musicast.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xrdev.musicast.Application;
import com.xrdev.musicast.R;
import com.xrdev.musicast.model.TrackItem;
import com.xrdev.musicast.utils.PrefsManager;

import java.util.ArrayList;
import java.util.List;

public class QueueAdapter extends BaseAdapter {

	private final List<TrackItem> mItems = new ArrayList<TrackItem>();
	private final Context mContext;
    public OnVotedTrackListener mCallback;
    private final static String TAG = "QueueAdapter";
    private int playingIndex;


    public interface OnVotedTrackListener {
        public void onTrackVoted(TrackItem track);
    }

	public QueueAdapter(Context context) {
		mContext = context;
        mCallback = (OnVotedTrackListener) context;

    }


	// Adiciona um item a lista
	
	public void add(TrackItem item) {
		mItems.add(item);
		notifyDataSetChanged();
	}
	
	// Limpa a lista
	
	public void clear(){
		mItems.clear();
		notifyDataSetChanged();
	}

	// Retorna o número de itens na lista

	@Override
	public int getCount() {

		return mItems.size();

	}

	// Recupera um determinado item
	@Override
	public TrackItem getItem(int pos) {
		return mItems.get(pos);
	}
	

	// Pega o ID de um item

	@Override
	public long getItemId(int pos) {

		return pos;

	}




	// Método mais importante, vai criar a View para cada item. é necessário mencionar o layout em XML gerado.
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final TrackItem trackItem = mItems.get(position);

        // Inflar o layout para cada item:
        TrackHolder holder = new TrackHolder();

        int currentMode = Application.getMode();

        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.item_queue, parent, false);

            holder.queueLayout = (RelativeLayout) convertView.findViewById(R.id.queue_layout);
            holder.voteStats = (RelativeLayout) convertView.findViewById(R.id.container_queue_stats);

            holder.titleView = (TextView) convertView.findViewById(R.id.text_queue_track_name);
            holder.artistsView = (TextView) convertView.findViewById(R.id.text_queue_track_artists);
            holder.buttonLike = (ImageButton) convertView.findViewById(R.id.button_track_like);
            holder.voteCount = (TextView) convertView.findViewById(R.id.text_votes);

            convertView.setTag(holder);
        } else {
            holder = (TrackHolder) convertView.getTag();
        }

		// Montar os textviews com os dados de cada item:
		holder.titleView.setText(trackItem.getName());

		holder.artistsView.setText(trackItem.getArtists() + " - " + trackItem.getAlbum());

        holder.voteCount.setText(String.valueOf(trackItem.getVoteCount()));

        if (currentMode == Application.MODE_SOLO || currentMode == Application.MODE_UNSTARTED) {
            // TODO: adicionar um ClickListener parecido com o do TracksAdapter para tocar a música direto em caso de MODE_SOLO.
            holder.voteStats.setVisibility(View.GONE);
            if (playingIndex == position) {
                holder.titleView.setTextColor(Color.parseColor("#006600"));
                holder.artistsView.setTextColor(Color.parseColor("#006600"));
            }
        } else {
            if (playingIndex > position) {
                holder.voteStats.setVisibility(View.GONE);
                holder.titleView.setTextColor(Color.parseColor("#aaaaaa"));
                holder.artistsView.setTextColor(Color.parseColor("#aaaaaa"));
            }

            if (playingIndex == position) {
                holder.voteStats.setVisibility(View.GONE);
                holder.titleView.setTextColor(Color.parseColor("#006600"));
                holder.artistsView.setTextColor(Color.parseColor("#006600"));
            }

            if (playingIndex < position) {
                holder.voteStats.setVisibility(View.VISIBLE);
                holder.titleView.setTextColor(Color.BLACK);
                holder.artistsView.setTextColor(Color.BLACK);
            }

        }


        if (trackItem.hasVoted(PrefsManager.getUUID(mContext)))
            holder.buttonLike.setBackgroundResource(R.drawable.ic_action_good_pressed);
        else
            holder.buttonLike.setBackgroundResource(R.drawable.ic_action_good);


        holder.buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onTrackVoted(trackItem);
            }
        });


        // Retornar o item dentro do layout.
		return convertView;
	}

    public void setHighlight(int index) {
        playingIndex = index;
        notifyDataSetChanged();
    }

    static class TrackHolder {
        TextView titleView;
        TextView artistsView;
        ImageButton buttonLike;
        RelativeLayout queueLayout;
        RelativeLayout voteStats;
        TextView voteCount;
    }

	
	
	


}
