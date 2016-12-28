package com.hj.vediofun;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class VedioListService extends RemoteViewsService {
	@Override
	public RemoteViewsFactory onGetViewFactory(Intent arg0) {
		// TODO Auto-generated method stub
		return new ListRemoteViewFactory(this.getApplicationContext(),arg0);
	}

}
