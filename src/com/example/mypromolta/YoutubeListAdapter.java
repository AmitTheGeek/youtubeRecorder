package com.example.mypromolta;



import java.util.List;

import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class YoutubeListAdapter extends ArrayAdapter<VideoProperties> {


	private List<VideoProperties> videoList;
	private static LayoutInflater inflater = null;
	ViewHolder holder = null;
	Context ctx;
	
	
	
	public YoutubeListAdapter(Context ctx, List<VideoProperties> videos) {
		
		
		super(ctx, 0, videos);
		this.ctx = ctx;
		videoList = videos;
		inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	
	@Override
	public int getCount() {
		return videoList.size();
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		
		if (convertView == null){
			convertView = inflater.inflate(R.layout.youtube_list, null);		
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.subText = (TextView) convertView.findViewById(R.id.number);
			holder.thumbnail = (ImageView) convertView.findViewById(R.id.list_image);
			
			convertView.setTag(holder);
			
		}else{
			holder = (ViewHolder) convertView.getTag();				
		}
		final VideoProperties video = videoList.get(position);

		
		
		
		holder.name.setText(video.name);	
		holder.name.setTextColor(Color.parseColor("#040404"));
		//holder.subText.setText(osType);
		
		Picasso.with(ctx).load(video.thumbnailUrl).into(holder.thumbnail);
	
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Activity act = (Activity) ctx;
				Intent intent = new Intent(ctx, YoutubeViewActivity.class);
				intent.putExtra("video", video.videoid);
				act.startActivityForResult(intent, 999);
				
			}
		});
		
		return convertView;
	}

	
	
	private static class ViewHolder {
		public TextView name;
		public TextView subText;
		public ImageView thumbnail;
	   
	}
}