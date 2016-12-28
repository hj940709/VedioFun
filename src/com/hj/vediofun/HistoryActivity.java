package com.hj.vediofun;

import android.support.v7.app.ActionBarActivity;

import java.util.List;
import java.util.Map;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnGroupExpand;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class HistoryActivity extends ActionBarActivity {
	
	private List<Map<String,String>> list;
	@ViewInject(R.id.history_expandablelistview)
	private ExpandableListView elv;
	@ViewInject(R.id.count)
	private TextView count;
	private HistoryAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);

		ActionBar actionBar = this.getActionBar();
		actionBar.setCustomView(R.layout.itemcount);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_SHOW_CUSTOM);
		ViewUtils.inject(this);
		
		list = new VedioDB(this).queryList(1);
		adapter = new HistoryAdapter(this,list);
		elv.setAdapter(adapter);
		count.setText(list.size()+"");
		
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		list = new VedioDB(this).queryList(1);
		adapter.setList(list);
		adapter.notifyDataSetChanged();
		count.setText(list.size()+"");
		super.onResume();	
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.history, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch(item.getItemId())
		{
//		case R.id.action_test:this.testArea(); break;
		case android.R.id.home:this.finish();break;
		}	
		return super.onOptionsItemSelected(item);
	}
	@OnGroupExpand(R.id.history_expandablelistview)
	public void onGroupExpand(int groupPosition) {
		Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(50);
		for (int i = 0; i < list.size(); i++) {
			if (groupPosition != i) {
				elv.collapseGroup(i);
			}
		}
	}
//	private void testArea(){
		
//	}
}
