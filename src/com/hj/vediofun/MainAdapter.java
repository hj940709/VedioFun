package com.hj.vediofun;

import java.util.List;
import java.util.Map;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;


@SuppressLint("InflateParams")
public class MainAdapter extends BaseExpandableListAdapter {
	private Context context;
	private List<Map<String,String>> list;
	private TextView count;
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
		@ViewInject(R.id.checkedbtn)
		public Button checkedbtn;
		@ViewInject(R.id.pausedbtn)
		public Button pausedbtn;
		@ViewInject(R.id.endedbtn)
		public Button endedbtn;
	}
	private class ParentItem{
		@ViewInject(R.id.name1)
		public TextView name1;
		@ViewInject(R.id.episode)
		public TextView episode;
	}
	public MainAdapter(Context context,List<Map<String,String>> list, TextView count) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = list;
		this.count = count;
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
            arg3 = inflater.inflate(R.layout.child_view, null);
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
		item.checkedbtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg5) {
				// TODO Auto-generated method stub
				Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
				vibrator.vibrate(50);
				final Vedio selected = new Vedio(list.get(arg0));
				final int episode = Integer.parseInt(selected.getEpisode())+1;
				if(selected.getRefresh().equals("auto"))
					if(new VedioDB(context).checked(selected.getId()))
						Log.i("MainAdapter", "Vedio Checked");
					else
						Log.i("MainAdapter", "Vedio Failed");
				else
				{
					final DateTime dt = new DateTime();
					int year=dt.getYear();
			        int month=dt.getMonth();
			        int day=dt.getDay();
			        
			        final DatePickerDialog dpd = 
			        		new DatePickerDialog(context, null,year,month,day);
			        dpd.setCancelable(true);
			        dpd.setCanceledOnTouchOutside(true);
			        dpd.setButton(DialogInterface.BUTTON_POSITIVE, "Done",
			        		new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface di, int i) {
							// TODO Auto-generated method stub
							DatePicker dp = dpd.getDatePicker();
							int arg1 = dp.getYear();
							int arg2 = dp.getMonth();
							int arg3 = dp.getDayOfMonth();
							String date = arg1+"-";
							if(arg2+1<10)
								date += "0";
							date += (arg2+1)+"-";
							if(arg3<10)
								date += "0";
							date += arg3;
							selected.setNextdate(date);;
							selected.setEpisode(episode+"");
							selected.setVersion(dt.getDateTime());
							new VedioDB(context).update(selected.toUpdateContentValues(),selected.getId());
							setList(new VedioDB(context).queryList(0));
							notifyDataSetChanged();
							count.setText(list.size()+"");
						}});
			        dpd.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
			        		new DialogInterface.OnClickListener(){
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									// TODO Auto-generated method stub
									return;
								}});
			        dpd.show();
				}
				setList(new VedioDB(context).queryList(0));
				notifyDataSetChanged();
				count.setText(list.size()+"");
			}		
		});
		item.pausedbtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg5) {
				// TODO Auto-generated method stub
				Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
				vibrator.vibrate(50);
				Vedio selected = new Vedio(list.get(arg0));
				selected.setFreq("0");
				selected.setVersion(new DateTime().getDateTime());
				new VedioDB(context).update(selected.toUpdateContentValues(),selected.getId());
				setList(new VedioDB(context).queryList(0));
				notifyDataSetChanged();
				count.setText(list.size()+"");
			}
		});
		item.endedbtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg5) {
				// TODO Auto-generated method stub
				Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
				vibrator.vibrate(50);
				Vedio selected = new Vedio(list.get(arg0));
				selected.setFreq("-1");
				selected.setVersion(new DateTime().getDateTime());
				new VedioDB(context).update(selected.toUpdateContentValues(),selected.getId());
				setList(new VedioDB(context).queryList(0));
				notifyDataSetChanged();
				count.setText(list.size()+"");
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
