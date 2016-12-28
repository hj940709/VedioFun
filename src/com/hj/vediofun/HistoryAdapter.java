package com.hj.vediofun;

import java.util.List;
import java.util.Map;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class HistoryAdapter extends BaseExpandableListAdapter {
	private Context context;
	private List<Map<String,String>> list;
	private class ChildItem{
//		item.name1 = (TextView)arg3.findViewById();
		@ViewInject(R.id.name1)
		public TextView name1;
		@ViewInject(R.id.name2)
		public TextView name2;
		@ViewInject(R.id.season)
		public TextView season;
		@ViewInject(R.id.episode)
		public TextView episode;
		@ViewInject(R.id.nextdate)
		public TextView nextdate;
		@ViewInject(R.id.status)
		public TextView status;
		@ViewInject(R.id.detail)
		public TextView detail;
		@ViewInject(R.id.modifybtn)
		public Button modifybtn;
	}
	private class ParentItem{
		@ViewInject(R.id.name1)
		public TextView name1;
		@ViewInject(R.id.episode)
		public TextView episode;
	}
	public HistoryAdapter(Context context,List<Map<String,String>> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = list;
	}
	public void setList(List<Map<String, String>> list) {
		this.list = list;
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
        return list.get(arg0).get(arg1);
	}
	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return arg1;
	}
	@Override
	public View getChildView(final int arg0, int arg1, boolean arg2, View arg3, ViewGroup arg4) {
		// TODO Auto-generated method stub
		ChildItem item;
		
		if (arg3 == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            arg3 = inflater.inflate(R.layout.child_history_view, null);
            item = new ChildItem();
            ViewUtils.inject(item,arg3);
            arg3.setTag(item);
        }
		else
			item = (ChildItem)arg3.getTag();
		item.name1.setText(list.get(arg0).get("name1"));
		item.name2.setText(list.get(arg0).get("name2"));
		item.season.setText(list.get(arg0).get("season"));
		item.episode.setText(list.get(arg0).get("episode"));
		item.nextdate.setText(list.get(arg0).get("nextdate"));
		item.detail.setText(list.get(arg0).get("detail"));
		String status="";
		int freq = Integer.parseInt(list.get(arg0).get("freq"));
		if(freq<0)
			status += "Finished";
		else if(freq==0)
			status += "Paused";
		else if(freq==7)
			status += "Every week";
		else 
			status += "Every "+freq+" day(s)";
		if("manual".equals(list.get(arg0).get("refresh")))
			status = "Uncertain";
		item.status.setText(status);
		item.modifybtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg5) {
				// TODO Auto-generated method stub
				final Vedio selected = new Vedio(list.get(arg0));
//				
//				setList(new VedioDB(context).queryList(0));
//				notifyDataSetChanged();
				Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
				vibrator.vibrate(50);
				Intent i = new Intent(context,ModifyActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("vedio",selected);
				i.putExtras(bundle);
				context.startActivity(i);
			}		
		});
		return arg3;
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		// TODO Auto-generated method stub
		ParentItem item;
		if (arg2 == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            arg2 = inflater.inflate(R.layout.parent_view, null);
            item = new ParentItem();
            ViewUtils.inject(item,arg2);
            arg2.setTag(item);
        }
		else
			item = (ParentItem)arg2.getTag();
		item.name1.setText(list.get(arg0).get("name1"));
		String episode = "S";
		if(Integer.parseInt(list.get(arg0).get("season"))<10)
			episode += "0";
		episode += list.get(arg0).get("season")+"E";
		if(Integer.parseInt(list.get(arg0).get("episode"))<10)
			episode += "0";
		episode += list.get(arg0).get("episode");
		item.episode.setText(episode);
		return arg2;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}
}
