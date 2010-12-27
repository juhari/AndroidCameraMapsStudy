package fi.vincit.cameramapstest.locationphoto;

import java.io.File;
import java.io.FileNotFoundException;

import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;


public class LocationPicture extends OverlayItem {
	
	private String mPath;
	
	public static LocationPicture createLocationPicture(GeoPoint location, String path) {
		File photoFile = new File(path);
		if( photoFile.exists() ) {
			return new LocationPicture(location, path);
		}
		else {
			return null;
		}
	}
	
	private LocationPicture(GeoPoint location, String path) {				
		super(location, "Another test picture!", path);
		
		Log.i("CameraMapsTest", "new location picture, location: " + location + ", path: " + path);
		//setMarker(new BitmapDrawable(path));
		setPath(path);
	}

	public void setPath(String mPath) {
		this.mPath = mPath;
	}

	public String getPath() {
		return mPath;
	}
}
