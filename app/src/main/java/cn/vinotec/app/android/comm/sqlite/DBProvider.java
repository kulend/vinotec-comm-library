package cn.vinotec.app.android.comm.sqlite;

import java.util.List;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
/**
 * 尚未完成 query insert 的方法重写
 * @author wangxin
 *
 */
public class DBProvider extends ContentProvider {
	
	private static final int NEWS		=	1;
	private static final int BLOGS		=	2;
	private DBHelper		 mDBHelper	=	null;
	private static final 	 UriMatcher	URIMATCHER;
	static{
		URIMATCHER = new UriMatcher(UriMatcher.NO_MATCH);
	}
	

	/**
	 * Init DB
	 */
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		mDBHelper = DBHelper.getInstance(getContext());
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		return db == null ? false : true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		long rowId = 0;
		int witch = URIMATCHER.match(uri);
		switch(witch){
		case NEWS:
			break;
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		int count = 0;
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		int count = 0;
		return count;
	}
	
	
	/*********Define Insert Delete Update Query ***********/
	private Cursor query(String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		synchronized (mDBHelper) {
			SQLiteDatabase db = mDBHelper.getReadableDatabase();
			if (db == null || !db.isOpen()) {
				return null;
			}
			try {
				return db.query(table, columns, selection, selectionArgs,
						groupBy, having, orderBy);
			} catch (Exception e) {
				return null;
			}
		}
	}

	private int delete(String table, String where, String[] whereArgs) {
		synchronized (mDBHelper) {
			SQLiteDatabase db = mDBHelper.getReadableDatabase();
			if (db == null || !db.isOpen()) {
				return 0;
			}
			try {
				return db.delete(table, where, whereArgs);
			} catch (Exception e) {
				return 0;
			}
		}
	}

	private int update(String table, ContentValues values, String whereClause,
			String[] whereArgs) {
		synchronized (mDBHelper) {
			SQLiteDatabase db = mDBHelper.getReadableDatabase();
			if (db == null || !db.isOpen()) {
				return 0;
			}
			try {
				return db.update(table, values, whereClause, whereArgs);
			} catch (Exception e) {
				return 0;
			}
		}
	}

	private long insert(String table, String nullColumnHack,
			ContentValues values) {
		synchronized (mDBHelper) {
			SQLiteDatabase db = mDBHelper.getReadableDatabase();
			if (db == null || !db.isOpen()) {
				return 0;
			}
			return db.insert(table, nullColumnHack, values);
		}
	}

	private void notifyChange(List<Uri> uri) {
		ContentResolver cr = getContext().getContentResolver();
		for (int i = 0; i < uri.size(); i++) {
			cr.notifyChange(uri.get(i), null);
		}
		
	}

	private void notifyChange(Uri uri) {
		ContentResolver cr = getContext().getContentResolver();
		cr.notifyChange(uri, null);
		
	}

}
