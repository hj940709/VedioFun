package com.hj.vediofun;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.RemoteViews;

public class VedioListWidgetProvider extends AppWidgetProvider {
	private final String CHECK_ACTION = "com.hj.action.widget.CHECK";
	private final String CHECK_EXTRA = "com.hj.action.widget.CHECK_EXTRA";
	private final String REFRESH = "com.hj.action.widget.REFRESH";
	private ComponentName provider;
	private RemoteViews remoteview;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
		String action = intent.getAction();
		
		if(action.equals(this.CHECK_ACTION))
		{
			Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(50);
			new VedioDB(context).checked(intent.getStringExtra(CHECK_EXTRA));
		}
		else if(action.equals(this.REFRESH))
		{
			AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);
			this.provider = new ComponentName(context, VedioListWidgetProvider.class);
			int[] appWidgetIds = appWidgetManager.getAppWidgetIds(provider);
			appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_listview);			
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		this.remoteview = new RemoteViews(context.getPackageName(), R.layout.widget_main);
		for(int appWidgetId: appWidgetIds)
		{
			Intent intent = new Intent(context, VedioListService.class);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
			this.remoteview.setRemoteAdapter(R.id.widget_listview, intent);
			
			Intent checkintent = new Intent(CHECK_ACTION);
			checkintent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
			PendingIntent clickcheckintent = PendingIntent.getBroadcast(context,
		              0, checkintent, PendingIntent.FLAG_UPDATE_CURRENT);
		    remoteview.setPendingIntentTemplate(R.id.widget_listview, clickcheckintent);
			appWidgetManager.updateAppWidget(appWidgetId,remoteview);
		}
	}
}
