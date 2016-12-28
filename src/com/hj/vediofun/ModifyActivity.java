package com.hj.vediofun;

import android.support.v7.app.ActionBarActivity;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnCompoundButtonCheckedChange;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;

public class ModifyActivity extends ActionBarActivity {
	@ViewInject(R.id.season)
	private NumberPicker season;
	@ViewInject(R.id.episode)
	private NumberPicker episode;
	@ViewInject(R.id.freq)
	private NumberPicker freq;
	@ViewInject(R.id.nextdate)
	private DatePicker nextdate;
	@ViewInject(R.id.refresh)
	private Switch refresh;
	@ViewInject(R.id.end)
	private Switch end;
	@ViewInject(R.id.name1)
	private EditText name1;
	@ViewInject(R.id.name2)
	private EditText name2;
	@ViewInject(R.id.detail)
	private EditText detail;
	private Vedio selected;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify);
		ViewUtils.inject(this);
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		
		season.setMaxValue(20);
		season.setMinValue(1);
		episode.setMaxValue(1000);
		episode.setMinValue(1);
		freq.setMaxValue(1000);
		freq.setMinValue(0);
		
		Bundle bundle = getIntent().getExtras();
		this.selected = (Vedio)bundle.get("vedio");
		
		this.name1.setText(selected.getName1());
		this.name2.setText(selected.getName2());
		this.episode.setValue(Integer.parseInt(selected.getEpisode()));
		this.season.setValue(Integer.parseInt(selected.getSeason()));
		this.detail.setText(selected.getDetail());
		String[] temp = selected.getNextdate().split("-");
		int year = Integer.parseInt(temp[0]);
		int month = Integer.parseInt(temp[1])-1;
		int day = Integer.parseInt(temp[2]);
		this.nextdate.updateDate(year, month, day);
		int freq = Integer.parseInt(selected.getFreq());
		if("manual".equals(selected.getRefresh()))
			this.refresh.setChecked(false);
		else
			this.refresh.setChecked(true);
		switch(freq)
		{
		case -1:
			this.freq.setValue(1);
			this.end.setChecked(true);
			break;
		
		default:
			this.freq.setValue(freq);
			break;
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.modify, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch(item.getItemId())
		{
		case android.R.id.home:this.finish();break;
		}	
		return super.onOptionsItemSelected(item);
	}
	@OnCompoundButtonCheckedChange(R.id.refresh)
	public void onAutoCheckedChanged(CompoundButton buttonView,  
            boolean isChecked) {
		this.freq.setEnabled(isChecked);
	}
	@OnCompoundButtonCheckedChange(R.id.end)
	public void onEndCheckedChanged(CompoundButton buttonView,  
            boolean isChecked) {
		this.season.setEnabled(!isChecked);
		this.episode.setEnabled(!isChecked);
		this.freq.setEnabled(!isChecked);
		this.nextdate.setEnabled(!isChecked);
		this.refresh.setEnabled(!isChecked);
	}
	@OnClick({R.id.end,R.id.refresh})
	public void onSwitchClick(View arg0){
		Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(50);
	}
	@OnClick(R.id.save)
	public void onBtnClick(View arg0){
		Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(50);
		this.selected.setName1(this.name1.getText().toString());
		this.selected.setName2(this.name2.getText().toString());
		this.selected.setDetail(this.detail.getText().toString());
		if(this.refresh.isChecked())
			this.selected.setRefresh("auto");
		else
			this.selected.setRefresh("manual");
		this.selected.setSeason(this.season.getValue()+"");
		this.selected.setEpisode(this.episode.getValue()+"");
		this.selected.setFreq(this.freq.getValue()+"");
		if(this.end.isChecked())
			this.selected.setFreq("-1");
		int arg1 = this.nextdate.getYear();
		int arg2 = this.nextdate.getMonth();
		int arg3 = this.nextdate.getDayOfMonth();
		String date = arg1+"-";
		if(arg2+1<10)
			date += "0";
		date += (arg2+1)+"-";
		if(arg3<10)
			date += "0";
		date += arg3;
		this.selected.setNextdate(date);
		this.selected.setVersion(new DateTime().getDateTime());
		new VedioDB(this).update(selected.toUpdateContentValues(), selected.getId());
		this.finish();
	}
}
