package com.hj.vediofun;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;

public class SyncUtil {
	public SyncUtil(final Context context,int connTimeout,String url,
			final MainAdapter adapter){
		HttpUtils http = new HttpUtils(connTimeout);
		RequestParams params = new RequestParams();
		ArrayList<Vedio> list = new ArrayList<Vedio>();
		for(Map<String, String> map : new VedioDB(context).queryList(1))
			list.add(new Vedio(map));
		Gson gson = new Gson();
		String send="";
		try {
			send = URLEncoder.encode(gson.toJson(list),"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		params.addBodyParameter("vediolist", send);
		http.send(HttpMethod.POST, "http://"+url+"/VedioFunPC/SyncServlet", params, new RequestCallBack<String>(){
			LoadingDialog ld =  null;		
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				ld = new LoadingDialog(context);
				super.onStart();
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				ld.dismiss();
				Toast.makeText(context, "UpLoad Failed", Toast.LENGTH_SHORT).show();
			}
	
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "Upload Success", Toast.LENGTH_SHORT).show();
				try{
					String result = URLDecoder.decode(arg0.result, "UTF-8");
					Gson gson = new Gson();
					JsonParser parser = new JsonParser();
			        JsonArray jarray = parser.parse(result).getAsJsonArray();
			        ArrayList<ContentValues> list = new ArrayList<ContentValues>();
			        for(JsonElement json : jarray)
			        	list.add(gson.fromJson(json, Vedio.class).toInsertContentValues());
			        new VedioDB(context).init(list);
			        Toast.makeText(context, "Loading Success", Toast.LENGTH_SHORT).show();
			        adapter.setList(new VedioDB(context).queryList(0));
			        adapter.notifyDataSetChanged();
				}catch(Exception e)
				{
					Toast.makeText(context, "Loading Failed", Toast.LENGTH_SHORT).show();
				}
				finally
				{
					ld.dismiss();
				}
			}
		 });
	}
}
