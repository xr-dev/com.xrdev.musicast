package com.xrdev.musicast.adapter;

import java.util.ArrayList;
import java.util.List;

import com.xrdev.musicast.model.VideoItem;

import com.xrdev.musicast.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class VideoListAdapter extends BaseAdapter {

	private final List<VideoItem> mItems = new ArrayList<VideoItem>();
	private final Context mContext;
	private final static String TAG = "TCC";
	
	
	public VideoListAdapter(Context context) {
		mContext = context;
	}
	
	
	// Adiciona um item a lista
	
	public void add(VideoItem item) {

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
	public VideoItem getItem(int pos) {
		return mItems.get(pos);
	}
	

	// Pega o ID de um item

	@Override
	public long getItemId(int pos) {

		return pos;

	}


	// Método mais importante, vai criar a View para cada item. É necessário mencionar o layout em XML gerado.
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final VideoItem videoItem = (VideoItem) mItems.get(position);
		
		// Inflar o layout para cada item:
		LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout itemLayout = (RelativeLayout) li.inflate(R.layout.item_video, null);
		
		// Montar os textviews com os dados de cada item:
		final TextView titleView = (TextView) itemLayout.findViewById(R.id.text_video_title);
		titleView.setText(videoItem.getTitle());
		
		final TextView descriptionView = (TextView) itemLayout.findViewById(R.id.text_video_description);
		descriptionView.setText(videoItem.getDescription());

		final TextView durationView = (TextView) itemLayout.findViewById(R.id.text_video_duration);
		durationView.setText(Integer.toString(videoItem.getDuration()));

		// Retornar o item dentro do layout.
		return itemLayout;
	}


	
	
	


}
