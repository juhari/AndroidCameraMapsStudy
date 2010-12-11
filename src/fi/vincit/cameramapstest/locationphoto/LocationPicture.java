package fi.vincit.cameramapstest.locationphoto;

import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class LocationPicture extends OverlayItem {
	
	private String mPath;
	
	LocationPicture(GeoPoint location, String path) {				
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
