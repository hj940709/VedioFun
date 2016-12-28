package com.hj.vediofun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VedioDB extends SQLiteOpenHelper {
	private SQLiteDatabase db;
	private String table = "Vediolist";
	private Context context;
	private final String REFRESH = "com.hj.action.widget.REFRESH";
	public VedioDB(Context context) {
		super(context, "vediofun", null, 1);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.db = getWritableDatabase();
	}
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		arg0.execSQL("create table if not exists "+this.table
				+ "("
				+ "id integer,"
				+"name1 text primary key,"
				+"name2 text,"
				+"season int default 1,"
				+"episode int default 1,"
				+"nextdate date,"
				+"refresh char(6) check(refresh='auto'or refresh='manual') default 'auto' not null,"
				+"freq int default 7,"
				+"detail text,"
				+"version datetime"
				+ ");");
		
		arg0.execSQL("insert into vediolist values(1,'1','',1,1,date('2014-01-01'),'manual',7,'','2014-01-01 00:00:00');");
		arg0.execSQL("insert into vediolist values(2,'12','',1,1,date('2014-01-01','-1 day'),'auto',7,'','2014-01-01 00:00:00');");
		arg0.execSQL("insert into vediolist values(3,'123','',1,1,date('2014-01-01','-2 day'),'auto',7,'','2014-01-01 00:00:00');");
		arg0.execSQL("insert into vediolist values(4,'1234','',1,1,date('2014-01-01','-3 day'),'auto',7,'','2014-01-01 00:00:00');");
		arg0.execSQL("insert into vediolist values(5,'12345','',1,1,date('2014-01-01','-4 day'),'manual',7,'','2014-01-01 00:00:00');");
		arg0.execSQL("insert into vediolist values(6,'123456','',1,1,date('2014-01-01','-5 day'),'auto',7,'','2014-01-01 00:00:00');");
		arg0.execSQL("insert into vediolist values(7,'1234567','',1,1,date('2014-01-01','-6 day'),'auto',7,'','2014-01-01 00:00:00');");
		arg0.execSQL("insert into vediolist values(8,'12345678','',1,1,date('2014-01-01','-7 day'),'auto',7,'','2014-01-01 00:00:00');");
		arg0.execSQL("insert into vediolist values(9,'123456789','',1,1,date('2014-01-01'),'auto',7,'','2014-01-01 00:00:00');");
		//this.db.close();
	}
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		this.onCreate(arg0);
	}
	public boolean insert(ContentValues icv){
		try{
			this.db.insert(this.table, null, icv);
			this.db.close();
		}catch(Exception e){
			this.db.close();
			return false;
		}
		this.refreshWidget();
		return true;
	}
	public boolean init(ArrayList<ContentValues> icv){
		try{
			this.db.delete(this.table, null, null);
			for(ContentValues cv : icv)
				this.db.insert(this.table, null, cv);
			this.db.close();
		}catch(Exception e){
			this.db.close();
			return false;
		}
		this.refreshWidget();
		return true;
	}
	public boolean update(ContentValues ucv,String id){
		try{
			this.db.update(this.table, ucv,"id=?", new String[]{id});
			this.db.close();
		}catch(Exception e){
			this.db.close();
			return false;
		}
		this.refreshWidget();
		return true;
	}
	public boolean checked(String id){
		try{
			this.db.execSQL("update "+this.table+
					" set nextdate=date(nextdate,'+'||freq||' day'),"
					+ "episode=episode+1,version=datetime('now','localtime') "
					+ "where id=?", new String[]{id});
			this.db.close();
		}catch(Exception e){
			this.db.close();
			return false;
		}
		this.refreshWidget();
		return true;
	}
	
	public ArrayList<Map<String,String>> queryList(int ch){
		String sql = "select * from "+this.table+" where nextdate<=date('now') and freq>0;";
		if(ch==1)
			sql = "select * from "+this.table+" ;";
		else if(ch==2)
			sql = "select * from "+this.table+" where nextdate<=date('now') and freq>0 and refresh='auto';";
		Cursor c = this.db.rawQuery(sql,null);
		ArrayList<Map<String,String>> list = new ArrayList<Map<String,String>>();
		while(c!=null&&c.moveToNext())		
			list.add(this.toMap(c));
		if(c!=null) c.close();
		this.db.close();
		return list;
	}
	
	private Map<String, String> toMap(Cursor c) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("id", c.getString(c.getColumnIndex("id")));
		map.put("name1", c.getString(c.getColumnIndex("name1")));
		map.put("name2", c.getString(c.getColumnIndex("name2")));
		map.put("season", c.getString(c.getColumnIndex("season")));
		map.put("episode", c.getString(c.getColumnIndex("episode")));
		map.put("nextdate", c.getString(c.getColumnIndex("nextdate")));
		map.put("refresh", c.getString(c.getColumnIndex("refresh")));
		map.put("freq", c.getString(c.getColumnIndex("freq")));
		map.put("detail", c.getString(c.getColumnIndex("detail")));
		map.put("version", c.getString(c.getColumnIndex("version")));
		return map;
	}
	public void refreshWidget(){
		this.context.sendBroadcast(new Intent(this.REFRESH));
	}
}
