package fi.vincit.cameramapstest;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.maps.ItemizedOverlay;

public class LocationPhotoOverlay extends ItemizedOverlay<LocationPicture> {
	
	private Context mContext;
	private DataBaseConnection mDatabase;
	private ArrayList<LocationPicture> mOverlays = new ArrayList<LocationPicture>();
	
	public LocationPhotoOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
		
		mDatabase = new DataBaseConnection(mContext);
		// TODO when to close the db?
		mDatabase.open();
		
		scanDataBase();
	}
	
	public void addLocationPicture(LocationPicture picture) {
		mOverlays.add(picture);
		populate();
	}

	private void scanDataBase() {
		Log.i("CameraMapsTest", "scanDataBase <--");
		Cursor cursor = mDatabase.fetchAllLocationPhotos();
		Log.i("CameraMapsTest", "db columns: " + cursor.getColumnName(0));
		Log.i("CameraMapsTest", "db columns: " + cursor.getColumnName(1));
		Log.i("CameraMapsTest", "db columns: " + cursor.getColumnName(2));
		Log.i("CameraMapsTest", "db columns: " + cursor.getColumnName(3));
		Log.i("CameraMapsTest", "db columns: " + cursor.getColumnName(4));
		cursor.moveToFirst();
		Log.i("CameraMapsTest", "scanDataBase, items found: " + cursor.getCount());
		
		while( !cursor.isAfterLast() ) {
			addLocationPicture(new LocationPicture(mDatabase.getRowGeoPoint(cursor), mDatabase.getRowFilename(cursor)));
			cursor.moveToNext();
		}
		
		Log.i("CameraMapsTest", "scanDataBase -->");
	}
	
	@Override
	protected LocationPicture createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
	
	@Override
	protected boolean onTap(int index) {
		Log.i("CameraMapsTest", "clicked on overlay #" + index);
		LocationPicture pic = mOverlays.get(index);
		Log.i("CameraMapsTest", "overlay picture found at: " + pic.getPath());
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		BitmapDrawable picture = new BitmapDrawable(pic.getPath());
		dialog.setTitle(pic.getTitle());
		dialog.setIcon(picture);
		dialog.show();
		return true;
	}

}
