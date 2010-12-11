package fi.vincit.cameramapstest.camera;

import java.io.IOException;

import fi.vincit.cameramapstest.locationphoto.LocationPhotoStoredCallback;

import android.hardware.Camera;
import android.view.SurfaceHolder;

public class CameraController {

	private static boolean mPreviewRunning = false;
	private static boolean mCameraOpen = false;
	private static Camera mCamera;
	private static CameraCallbackListener mCameraCallbackListener = null;
	
	public CameraController() {
		
	}
	
	public static void openCamera() {
		if( !mCameraOpen ) {
			mCamera = Camera.open();
			mCameraOpen = true;
		}
	}
	
	public static void releaseCamera() {
		if( mCameraOpen ) {
			stopPreview();
			mCamera.release();
			mCameraOpen = false;
		}
	}
	
	public static boolean isPreviewRunning() {
		return mPreviewRunning;
	}
	
	public static void startPreview() {
		if( !mPreviewRunning ) {
			mPreviewRunning = true;
			mCamera.startPreview();
		}
	}
	
	public static void stopPreview() {
		if( mPreviewRunning ) {
			mPreviewRunning = false;
			mCamera.stopPreview();
		}
	}
	
	public static void setCameraPreviewDisplay(SurfaceHolder holder) throws IOException {
		if( mCameraOpen )
		{
			mCamera.setPreviewDisplay(holder);
		}
	}
	
	public static void capturePicture(LocationPhotoStoredCallback callback) {
		if( mCameraCallbackListener == null )
		{
			mCameraCallbackListener = new CameraCallbackListener(callback);
		}
		mCamera.takePicture(mCameraCallbackListener, mCameraCallbackListener, mCameraCallbackListener);
	}
	
	public static void setCameraParameters(Camera.Parameters params) {
		mCamera.setParameters(params);
	}
	
	public static Camera.Parameters getCameraParameters() {
		return mCamera.getParameters();
	}

}
