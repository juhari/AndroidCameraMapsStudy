package fi.vincit.cameramapstest.camera;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;

import com.google.android.maps.GeoPoint;

import fi.vincit.cameramapstest.DataBaseConnection;
import fi.vincit.cameramapstest.DemoMediaScannerClientFactory;
import fi.vincit.cameramapstest.locationphoto.LocationPhotoStoredCallback;

public class LocationPhotoFileHandler {
	
	private LocationPhotoStoredCallback mCallback = null;
	
	LocationPhotoFileHandler(LocationPhotoStoredCallback storeCallback) {
		mCallback = storeCallback;
	}
	
	public void storeNewLocationPicture(byte[] pictureData) {
		File stored = savePictureToMemCard(pictureData);
		
		final double TESTLAT = 61.446694;
        final double TESTLON = 23.857316;	 
		GeoPoint point = new GeoPoint((int)(TESTLAT*(float)1e6),(int)(TESTLON*(float)1e6));
		DataBaseConnection.createLocationPhoto(stored.toString(), point);
		
		printExifData(stored);
		
		DemoMediaScannerClientFactory.getInstance().updateMediaScanner(stored.toString());
		mCallback.insertLocationPhotoToGallery(stored.toString());
	}
	
	public File savePictureToMemCard(byte[] picture) {
		Log.i("CameraMapsTest", "CameraView::savePictureToMemCard() <--");
		
		File pic = null; 
		
		String state = Environment.getExternalStorageState();
		
		if( Environment.MEDIA_MOUNTED.equals(state) ) {
			File path = Environment.getExternalStorageDirectory();
			pic = new File(path, String.format("TestPic%d.jpg", System.currentTimeMillis()));
			//InputStream is = picture.
			try {
				//InputStream is = new ByteArrayInputStream(picture);
				OutputStream os = new FileOutputStream(pic);
				os.write(picture);
				os.close();				
			} catch (IOException e) {
				Log.e("CameraMapsTest", "CameraView::savePictureToMemCard: io exception", e);
			}
		}
		else {
			Log.e("CameraMapsTest", "CameraView::savePictureToMemCard(): no media mounted! ");			
		}
		
		Log.i("CameraMapsTest", "CameraView::savePictureToMemCard() -->");		
		return pic;
	}
	
	private void printExifData(File file) {
		try {
			Log.i("CameraMapsTest", "CameraView::printExifData(): file: " + file.toString());
			ExifInterface exifIf = new ExifInterface(file.toString());
			int width = exifIf.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0);
			Log.i("CameraMapsTest", "CameraView::printExifData(): width: " + width);
			float[] latLong = null;
			Log.i("CameraMapsTest", exifIf.toString());			
			
			Log.i("CameraMapsTest", "long" + exifIf.getAttribute(ExifInterface.TAG_DATETIME));
			boolean success = exifIf.getLatLong(latLong);
			if( success ) {
				Log.i("CameraMapsTest", "CameraView::printExifData(): lat: " + latLong[0]);
				Log.i("CameraMapsTest", "CameraView::printExifData(): long: " + latLong[1]);
			} else {
				Log.i("CameraMapsTest", "CameraView::printExifData(): no gps info on file!");
			}
		} catch(IOException e) {
			Log.e("CameraMapsTest", "CameraView::printExifData: io exception", e);
		}
	}
}
