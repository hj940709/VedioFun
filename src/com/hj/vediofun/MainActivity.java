package com.hj.vediofun;

import java.util.List;
import java.util.Map;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnGroupExpand;
import com.lidroid.xutils.view.annotation.event.OnItemLongClick;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private List<Map<String,String>> list;
	@ViewInject(R.id.main_expandablelistview)
	private ExpandableListView elv;
	@ViewInject(R.id.count)
	private TextView count;
	private MainAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ActionBar actionBar = this.getActionBar();
		actionBar.setCustomView(R.layout.itemcount);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_SHOW_CUSTOM);
		ViewUtils.inject(this);
		
		list = new VedioDB(this).queryList(0);
		adapter = new MainAdapter(this,list,this.count);
		elv.setAdapter(adapter);
		count.setText(list.size()+"");
		//Log.i("onCreate", "Done");
		
		
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		list = new VedioDB(this).queryList(0);
		adapter.setList(list);
		adapter.notifyDataSetChanged();
		count.setText(list.size()+"");
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch(item.getItemId())
		{
		case R.id.action_history:this.historyactivity();break;
		case R.id.action_sync:
			this.syncForm();
			break;
		case R.id.action_exit:this.finish();break;
		}	
		return super.onOptionsItemSelected(item);
	}
	@OnItemLongClick(R.id.main_expandablelistview)
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(50);
		return false;
	}
	@OnGroupExpand(R.id.main_expandablelistview)
	public void onGroupExpand(int groupPosition) {
		Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(50);
		for (int i = 0; i < list.size(); i++) {
			if (groupPosition != i) {
				elv.collapseGroup(i);
			}
		}
	}
	private void historyactivity(){
		Intent i = new Intent(MainActivity.this,HistoryActivity.class);
//		Bundle bundle = new Bundle();
//		bundle.putString("account",str_account);
//		bundle.putString("pwd", str_pwd);
//		i.putExtras(bundle);
		startActivity(i);
//		this.finish();
	}
	@SuppressLint("InflateParams")
	private void syncForm(){
		final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		final EditText et = new EditText(this);
		dialog.setTitle("Input Address:")
			.setView(et)
			.setCancelable(true)
			.setPositiveButton("Confirm", new OnClickListener(){
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					new SyncUtil(MainActivity.this,15000,et.getText().toString(),getAdapter());
				}
			})
			.setNegativeButton("Cancel", null).show();
	}
	private MainAdapter getAdapter() {
		return adapter;
	}
}
