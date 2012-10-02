package csci498.bybeet.lunchlist;

import android.content.Context;
import android.database.*;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RestaurantHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "lunchlist.db";
	private static final int SCHEMA_VERSION = 1;
	
	public RestaurantHelper (Context context) {
		super( context, DATABASE_NAME, null, SCHEMA_VERSION );
	}
	
	@Override
	public void onCreate (SQLiteDatabase db) {
		db.execSQL("CREATE TABLE restaurants (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, address TEXT, type TEXT, notes TEXT);");
	}
	
	@Override
	public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
		//Not currently needed, not called until 2nd db schema
	}
	
}