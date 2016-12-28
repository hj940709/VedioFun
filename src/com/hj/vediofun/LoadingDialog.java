package com.hj.vediofun;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;

public class LoadingDialog{
	private AlertDialog dialog;
	@SuppressLint("InflateParams")
	protected LoadingDialog(Context context) {
		// TODO Auto-generated constructor stub
		dialog = new AlertDialog.Builder(context).create();
		dialog.setTitle("Please Wait");
		dialog.setView(LayoutInflater.from(context).inflate(R.layout.loading, null));
		dialog.setCancelable(true);
		dialog.show();
	}
	public void dismiss(){
		dialog.dismiss();
	}
}
