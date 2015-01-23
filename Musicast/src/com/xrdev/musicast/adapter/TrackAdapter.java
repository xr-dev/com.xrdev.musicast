package com.xrdev.musicast.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xrdev.musicast.R;
import com.xrdev.musicast.model.TrackItem;

import java.util.ArrayList;
import java.util.List;

public class TrackAdapter extends BaseAdapter {

	private final List<TrackItem> mItems = new ArrayList<TrackItem>();
	private final Context mContext;
	private final static String TAG = "TrackAdapter";


	public TrackAdapter(Context context) {
		mContext = context;
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

        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.item_track, parent, false);

            holder.titleView = (TextView) convertView.findViewById(R.id.text_track_name);
            holder.artistsView = (TextView) convertView.findViewById(R.id.text_track_artists);
            holder.albumView = (TextView) convertView.findViewById(R.id.text_track_album);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.pbar_youtube_fetch);
            holder.videoFound = (TextView) convertView.findViewById(R.id.text_video_found);

            convertView.setTag(holder);
        } else {
            holder = (TrackHolder) convertView.getTag();
        }

		// Montar os textviews com os dados de cada item:
		// final TextView titleView = (TextView) itemLayout.findViewById(R.id.text_track_name);
		holder.titleView.setText(trackItem.getName());
		
		// final TextView artistsView = (TextView) itemLayout.findViewById(R.id.text_track_artists);
		holder.artistsView.setText(trackItem.getArtists());

        // final TextView albumView = (TextView) itemLayout.findViewById(R.id.text_track_album);
        holder.albumView.setText(trackItem.getAlbum());

        if (trackItem.wasSearched()) {
            holder.progressBar.setVisibility(View.GONE);
            holder.videoFound.setVisibility(View.VISIBLE);
            if (trackItem.wasFound()){
                holder.videoFound.setText(R.string.string_video_found);
            }
            else {
                holder.videoFound.setText(R.string.string_video_not_found);
            }
        } else {
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.videoFound.setVisibility(View.GONE);
        }

		// Retornar o item dentro do layout.
		return convertView;
	}

    static class TrackHolder {
        TextView titleView;
        TextView artistsView;
        TextView albumView;
        ProgressBar progressBar;
        TextView videoFound;
    }

	
	
	


}
