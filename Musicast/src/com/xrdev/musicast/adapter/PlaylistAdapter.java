package com.xrdev.musicast.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xrdev.musicast.Application;
import com.xrdev.musicast.R;
import com.xrdev.musicast.model.PlaylistItem;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends BaseAdapter {

	private final List<PlaylistItem> mItems = new ArrayList<PlaylistItem>();
	private final Context mContext;
	private final static String TAG = "PlaylistAdapter";


	public PlaylistAdapter(Context context) {
		mContext = context;
	}
	
	
	// Adiciona um item a lista
	
	public void add(PlaylistItem item) {

		mItems.add(item);
		notifyDataSetChanged();

	}
	
	// Limpa a lista
	
	public void clear(){

		mItems.clear();
		notifyDataSetChanged();
	
	}

	// Retorna o n�mero de itens na lista

	@Override
	public int getCount() {

		return mItems.size();

	}

	// Recupera um determinado item
	@Override
	public PlaylistItem getItem(int pos) {
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
		final PlaylistItem playlistItem = mItems.get(position);

        // Inflar o layout para cada item:

        int mode = Application.getMode();

        PlaylistHolder holder = new PlaylistHolder();

        if(convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.item_playlist, parent, false);

            holder.titleView = (TextView) convertView.findViewById(R.id.text_playlist_name);
            holder.tracksView = (TextView) convertView.findViewById(R.id.text_playlist_num_tracks);

            convertView.setTag(holder);

        } else {
            holder = (PlaylistHolder) convertView.getTag();
        }

		// Montar os textviews com os dados de cada item:
		holder.titleView.setText(playlistItem.getName());
		holder.tracksView.setText(playlistItem.getNumTracks());


		// Retornar o item dentro do layout.
		return convertView;
	}


    static class PlaylistHolder {
        TextView titleView;
        TextView tracksView;
    }

	
	
	


}
