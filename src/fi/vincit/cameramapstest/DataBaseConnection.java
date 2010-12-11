package fi.vincit.cameramapstest;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.maps.GeoPoint;

public class DataBaseConnection {
   
   private static final String DATABASE_CREATE_LOCATION_PHOTO = "create table locationphoto (_id integer primary key autoincrement, filename text not null, lat int not null, lon int not null, time text not null );";
   
   
   private static final String DATABASE_NAME = "CameraMapsTest";
   private static final int DATABASE_VERSION = 2;
   private static final String TABLE_LOCATION_PHOTO = "locationphoto";
   
   
   private static Context mCtx = null;
   private static DatabaseHelper mDbHelper = null;
   private static SQLiteDatabase mDb = null;   
   
   private static boolean mOpen = false;
   
	public static void open(Context ctx) throws SQLException {		
		if( !mOpen )
		{
			mCtx = ctx;		
		    mDbHelper = new DatabaseHelper(mCtx);
	    	mDb = mDbHelper.getWritableDatabase();
	    	mOpen = true; 
		}
	}
	
	public static boolean isOpen() {
		return mOpen;
	}
	
	public static void close() {
	    mDbHelper.close();
	    mOpen = false;
	}
   
	private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        	// Create tables
        	db.execSQL(DATABASE_CREATE_LOCATION_PHOTO);
        };

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("CameraMapsTest", "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS locationphoto");
            onCreate(db);
        }
    }
	   
    public static long createLocationPhoto(String filename, GeoPoint location) {
    	Log.i("CameraMapsTest", "creating location photo to: " + location);
        ContentValues values = new ContentValues();
        values.put("filename", filename);
        values.put("lat", location.getLatitudeE6());
        values.put("lon", location.getLongitudeE6());
        Date now = new Date();
        values.put("time", now.toLocaleString());
        
        return mDb.insert(TABLE_LOCATION_PHOTO, null, values);
    }	     
    
    public static Cursor fetchAllLocationPhotos(){
    	return mDb.query(TABLE_LOCATION_PHOTO, new String[] {"_id", "filename", "time", "lat", "lon"},  null, null, null, null, null);
    }   
    
    public static GeoPoint getRowGeoPoint(Cursor cursor) {	
    	int lat = cursor.getInt(3);
    	int lon = cursor.getInt(4);
    	return new GeoPoint(lat, lon);
    }
    
    public static String getRowFilename(Cursor cursor) {
    	return cursor.getString(1);
    }
}
