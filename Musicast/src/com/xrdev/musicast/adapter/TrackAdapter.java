package com.xrdev.musicast.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xrdev.musicast.Application;
import com.xrdev.musicast.R;
import com.xrdev.musicast.model.TrackItem;

import java.util.ArrayList;
import java.util.List;

public class TrackAdapter extends BaseAdapter {

	private final List<TrackItem> mItems = new ArrayList<TrackItem>();
	private final Context mContext;
    public OnAddedTrackListener mCallback;
	private final static String TAG = "TrackAdapter";


	public TrackAdapter(Context context) {
		mContext = context;
        mCallback = (OnAddedTrackListener) context;
	}
	

    public interface OnAddedTrackListener {
        public void onTrackVoted(TrackItem track);
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
        int currentMode = Application.getMode();

        TrackHolder holder = new TrackHolder();

        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.item_track, parent, false);

            holder.titleView = (TextView) convertView.findViewById(R.id.text_track_name);
            holder.artistsView = (TextView) convertView.findViewById(R.id.text_track_artists);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.pbar_youtube_fetch);
            holder.buttonAdd = (ImageButton) convertView.findViewById(R.id.button_track_add);

            convertView.setTag(holder);
        } else {
            holder = (TrackHolder) convertView.getTag();
        }

		// Montar os textviews com os dados de cada item:
		holder.titleView.setText(trackItem.getName());

		holder.artistsView.setText(trackItem.getArtists() + " - " + trackItem.getAlbum());

        if (Application.getMode() == Application.MODE_SOLO || currentMode == Application.MODE_UNSTARTED)
            holder.buttonAdd.setVisibility(View.GONE);

        if (trackItem.wasSearched()) {
            holder.progressBar.setVisibility(View.GONE);
            if (trackItem.wasFound()) {
                holder.titleView.setTextColor(Color.parseColor("#000000"));
                holder.artistsView.setTextColor(Color.parseColor("#000000"));
            }
        } else {
            holder.progressBar.setVisibility(View.VISIBLE);
        }

        holder.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onTrackVoted(trackItem);
            }
        });

		// Retornar o item dentro do layout.
		return convertView;
	}

    static class TrackHolder {
        TextView titleView;
        TextView artistsView;
        ProgressBar progressBar;
        ImageButton buttonAdd;
    }

	
	
	


}
