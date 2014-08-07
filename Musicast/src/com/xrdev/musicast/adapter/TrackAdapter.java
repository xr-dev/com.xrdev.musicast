package com.xrdev.musicast.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
		LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout itemLayout = (RelativeLayout) li.inflate(R.layout.item_track, null);
		
		// Montar os textviews com os dados de cada item:
		final TextView titleView = (TextView) itemLayout.findViewById(R.id.text_track_name);
		titleView.setText(trackItem.getName());
		
		final TextView artistsView = (TextView) itemLayout.findViewById(R.id.text_track_artists);
		artistsView.setText(trackItem.getArtists());

        final TextView albumView = (TextView) itemLayout.findViewById(R.id.text_track_album);
        albumView.setText(trackItem.getAlbum());

		// Retornar o item dentro do layout.
		return itemLayout;
	}


	
	
	


}
