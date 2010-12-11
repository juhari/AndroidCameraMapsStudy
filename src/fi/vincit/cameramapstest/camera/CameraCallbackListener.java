package fi.vincit.cameramapstest.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.util.Log;
import fi.vincit.cameramapstest.locationphoto.LocationPhotoStoredCallback;

public class CameraCallbackListener implements ShutterCallback
											 , PictureCallback {

	LocationPhotoFileHandler mFileHandler = null;
	
	public CameraCallbackListener(LocationPhotoStoredCallback storeCallback) {
		mFileHandler = new LocationPhotoFileHandler(storeCallback);
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		if( data != null) {
			Log.i("CameraMapsTest", "CameraView::onPictureTaken(), data length: " + data.length);
			// store data!
			printPictureSizeInfo(data);					
			mFileHandler.storeNewLocationPicture(data);
			
		}
		else {
			Log.i("CameraMapsTest", "CameraView::onPictureTaken(), data is null! ");
		}
	}
	
	private void printPictureSizeInfo(byte[] data) {
		Bitmap picture = BitmapFactory.decodeByteArray(data, 0, data.length);
		Log.i("CameraMapsTest", "CameraView::onPictureTaken(), bitmap created: " + picture.getWidth() + ":" + picture.getHeight());
	}

	@Override
	public void onShutter() {
		// TODO Auto-generated method stub
		
	}
	
}
