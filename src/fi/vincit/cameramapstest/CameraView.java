package fi.vincit.cameramapstest;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;


public class CameraView extends Activity
						implements SurfaceHolder.Callback {

	private Camera mCamera = null;
	private CameraSurface mSurface = null;
	private SurfaceHolder mHolder = null;
	
	public CameraView() {
		// TODO Auto-generated constructor stub
	}
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
    	
        mSurface = new CameraSurface(this);
        mHolder = mSurface.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        setContentView(mSurface);       
    }       
    
    private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.05;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
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
                }
            }
        }
        return optimalSize;
    }
    
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// Now that the size is known, set up the camera parameters and begin
        // the preview.
		Log.i("CameraMapsTest", "CameraView::surfaceChanged()");
		
        Camera.Parameters parameters = mCamera.getParameters();

        List<Size> sizes = parameters.getSupportedPreviewSizes();
        Size optimalSize = getOptimalPreviewSize(sizes, w, h);
        parameters.setPreviewSize(optimalSize.width, optimalSize.height);

        mCamera.setParameters(parameters);
        mCamera.startPreview();		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		Log.i("CameraMapsTest", "CameraView::surfaceCreated()");
		
		// TODO Auto-generated method stub
		mCamera = Camera.open();
        try {
        	mCamera.setPreviewDisplay(mSurface.getHolder());
        } catch (IOException e) {
        	mCamera.release();
        	mCamera = null;
        	Log.e("CameraMapsTest", "CameraView::onCreate(): Failed to set preview display", e);
        }
        
        mCamera.startPreview();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		Log.i("CameraMapsTest", "CameraView::surfaceDestroyed()");
		
		// TODO Auto-generated method stub
		// Surface will be destroyed when we return, so stop the preview.
        // Because the CameraDevice object is not a shared resource, it's very
        // important to release it when the activity is paused.
		if( mCamera != null ) {		
	        mCamera.stopPreview();
	        mCamera.release();
	        mCamera = null;
		}
	}
	
	@Override
    public void onBackPressed() {
		Log.i("CameraMapsTest", "CameraView::onBackPressed()");
		if( mCamera != null ) {
	    	mCamera.stopPreview();
	    	mCamera.release();
	    	mCamera = null;
		}
    	finish();
    }
}
