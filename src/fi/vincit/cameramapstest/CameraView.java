package fi.vincit.cameramapstest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import fi.vincit.cameramapstest.DemoMediaScannerClient;

public class CameraView extends Activity
						implements SurfaceHolder.Callback 
								 , OnClickListener
								 , ShutterCallback
								 , PictureCallback{

	private Camera mCamera = null;
	private CameraSurface mSurface = null;
	private SurfaceHolder mHolder = null;
	private DemoMediaScannerClient mScanner = null;
	private boolean mPreviewRunning = false;
	
	public CameraView() {
		// TODO Auto-generated constructor stub
	}
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i("CameraMapsTest", "CameraView::onCreate() <--");
        super.onCreate(savedInstanceState);            	
        
        Log.i("CameraMapsTest", "CameraView::onCreate(): get surface");
        
        mScanner = new DemoMediaScannerClient(this.getBaseContext());
        
        Log.i("CameraMapsTest", "CameraView::onCreate(): set content view");
        setContentView(R.layout.cameraview);
        mSurface = (CameraSurface) findViewById(R.id.camerasurface);//new CameraSurface(this);
        
        if( mSurface == null )
        {
        	Log.e("CameraMapsTest", "CameraView::onCreate(): surface not created!");
        }
        mHolder = mSurface.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);               
        
        Button captureButton = (Button) findViewById(R.id.CaptureButton);
        captureButton.setOnClickListener(this);
        
        Log.i("CameraMapsTest", "CameraView::onCreate() -->");
    }       
    
    private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.05;
        double targetRatio = (double) w / h;
        if (sizes == null) {
        	Log.i("CameraMapsTest", "CameraView::getOptimalPreviewSize(): no sizes given! returning null!");
        	return null;
        }

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
            	Log.i("CameraMapsTest", "CameraView::getOptimalPreviewSize(): found optimal size: " + optimalSize);
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {        	
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                    Log.i("CameraMapsTest", "CameraView::getOptimalPreviewSize(): optimal size not found, using: " + optimalSize);
                }
            }
        }
        
        Log.i("CameraMapsTest", "CameraView::getOptimalPreviewSize(): optimal size: " + optimalSize);
        return optimalSize;
    }
    
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// Now that the size is known, set up the camera parameters and begin
        // the preview.
		Log.i("CameraMapsTest", "CameraView::surfaceChanged()");
		
		if( mPreviewRunning ) {
			mCamera.stopPreview();
		}
		
        setCameraParameters();
        
        try {
        	mCamera.setPreviewDisplay(mSurface.getHolder());
        } catch (IOException e) {
        	mCamera.release();
        	mCamera = null;
        	Log.e("CameraMapsTest", "CameraView::onCreate(): Failed to set preview display", e);
        }
        
        mPreviewRunning = true;
        mCamera.startPreview();		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		Log.i("CameraMapsTest", "CameraView::surfaceCreated()");
		
		// TODO Auto-generated method stub
		mCamera = Camera.open();               
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		Log.i("CameraMapsTest", "CameraView::surfaceDestroyed()");
		
		// TODO Auto-generated method stub
		// Surface will be destroyed when we return, so stop the preview.
        // Because the CameraDevice object is not a shared resource, it's very
        // important to release it when the activity is paused.
		if( mCamera != null ) {		
			mPreviewRunning = false;
	        mCamera.stopPreview();
	        mCamera.release();
	        mCamera = null;
		}
	}
	
	@Override
    public void onBackPressed() {
		Log.i("CameraMapsTest", "CameraView::onBackPressed()");
		if( mCamera != null ) {
			mPreviewRunning = false;
	    	mCamera.stopPreview();
	    	mCamera.release();
	    	mCamera = null;
		}
    	finish();
    }

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view.getId() == R.id.CaptureButton )
		{
			Log.i("CameraMapsTest", "CameraView::onClick()");
			mCamera.takePicture(this, this, this);
		}
		
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		// TODO Auto-generated method stub¨
		if( data != null) {
			Log.i("CameraMapsTest", "CameraView::onPictureTaken(), data length: " + data.length);
			// store data!
			Bitmap picture = BitmapFactory.decodeByteArray(data, 0, data.length);
			Log.i("CameraMapsTest", "CameraView::onPictureTaken(), bitmap created: " + picture.getWidth() + ":" + picture.getHeight());
			File stored = savePictureToMemCard(data);
			
			printExifData(stored);
			
			mScanner.updateMediaScanner(stored.toString());
		}
		else {
			Log.i("CameraMapsTest", "CameraView::onPictureTaken(), data is null! ");
		}
	}

	@Override
	public void onShutter() {
		Log.i("CameraMapsTest", "CameraView::onShutter()");
		// TODO Auto-generated method stub
		
	}
	
	private void storeToGallery(String filename) {
		ContentValues values = new ContentValues();
		values.put(Media.TITLE, filename);
		values.put(Media.DESCRIPTION, "Image by CameraMapsDemo");
		getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
	}
	
	private File savePictureToMemCard(byte[] picture) {
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
			
			//exifIf.setAttribute(ExifInterface.TAG_GPS_LATITUDE, "61.446694");
			//exifIf.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, "23.857316");
			//exifIf.saveAttributes();
			
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
	
	private void setCameraParameters() {
		Camera.Parameters parameters = mCamera.getParameters();
    	
        if( parameters != null )
        {
        	// TODO: for some reason this returns a null list on simulator, so hard code size for now
	        List<Size> sizes = parameters.getSupportedPreviewSizes();
	        Log.i("CameraMapsTest", "CameraView::setCameraParameters(): supported sizes: " + sizes);
	        Size optimalSize = getOptimalPreviewSize(sizes, mSurface.getWidth(), mSurface.getHeight());
	        if( optimalSize != null )
	        	parameters.setPreviewSize(optimalSize.width, optimalSize.height);

	        parameters.setPictureFormat(PixelFormat.JPEG);
	        
	        parameters.removeGpsData();
	        parameters.setGpsLatitude(61.446694);
	        parameters.setGpsLongitude(23.857316);
	        parameters.setGpsAltitude(144);
	        parameters.setGpsTimestamp(System.currentTimeMillis());
	        
	        mCamera.setParameters(parameters);
        }
	}
}
