package com.hj.vediofun;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

public class ListRemoteViewFactory implements RemoteViewsFactory {
	private Context context;
	private List<Map<String,String>> list;
	private final String CHECK_EXTRA = "com.hj.action.widget.CHECK_EXTRA";
	public ListRemoteViewFactory(Context context, Intent intent) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = new VedioDB(context).queryList(2);
		if(Looper.myLooper() == null){
            Looper.prepare();
        }
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public RemoteViews getLoadingView() {
		// TODO Auto-generated method stub
		return new RemoteViews(context.getPackageName(),R.layout.widgetloading);
	}

	@Override
	public RemoteViews getViewAt(int arg0) {
		// TODO Auto-generated method stub
		if(arg0<0||arg0>list.size())
			return null;
		Vedio selected = new Vedio(list.get(arg0));
		RemoteViews rv = new RemoteViews(context.getPackageName(),R.layout.widgetlistview);
		rv.setTextViewText(R.id.wname1, selected.getName1());
		rv.setTextViewText(R.id.wname2, selected.getName2());
		String episode = "S";
		if(Integer.parseInt(selected.getSeason())<10)
			episode += "0";
		episode += selected.getSeason()+"E";
		if(Integer.parseInt(selected.getEpisode())<10)
			episode += "0";
		episode += selected.getEpisode();
		rv.setTextViewText(R.id.wepisode, episode);
		
		Intent intent = new Intent();
		intent.putExtra(CHECK_EXTRA, selected.getId());
		rv.setOnClickFillInIntent(R.id.widgetbtn, intent);
		return rv;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDataSetChanged() {
		// TODO Auto-generated method stub
		this.list = new VedioDB(context).queryList(2);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		this.list.clear();
	}

}
