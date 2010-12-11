package fi.vincit.cameramapstest.camera;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraSurface extends SurfaceView
						   implements SurfaceHolder.Callback {	
	
	public CameraSurface(Context context) {
		super(context);
		Log.i("CameraMapsTest", "CameraSurface::CameraSurface()");
		initializeSurfaceHolderCallbacks();
		// TODO Auto-generated constructor stub
	}

	public CameraSurface(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.i("CameraMapsTest", "CameraSurface::CameraSurface()");
		initializeSurfaceHolderCallbacks();
		// TODO Auto-generated constructor stub
	}

	public CameraSurface(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Log.i("CameraMapsTest", "CameraSurface::CameraSurface()");
		initializeSurfaceHolderCallbacks();
		// TODO Auto-generated constructor stub
	}
	
	private void initializeSurfaceHolderCallbacks() {
        getHolder().addCallback(this);
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
	
    public Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
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
		
		CameraController.stopPreview();		
        setCameraParameters();
        
        try {
        	CameraController.setCameraPreviewDisplay(this.getHolder());
        } catch (IOException e) {
        	CameraController.releaseCamera();
        	Log.e("CameraMapsTest", "CameraView::onCreate(): Failed to set preview display", e);
        }
        CameraController.startPreview();		
	}
	
	private void setCameraParameters() {
		Camera.Parameters parameters = CameraController.getCameraParameters();
    	
        if( parameters != null )
        {
        	// TODO: for some reason this returns a null list on simulator, so hard code size for now
        	appendCameraParametersWithOptimalSize(parameters);	        
	        parameters.setPictureFormat(PixelFormat.JPEG);	       	        	       
	    	appendCameraParametersWithGpsData(parameters);	        
	        CameraController.setCameraParameters(parameters);
        }
	}
	
	private void appendCameraParametersWithOptimalSize(Camera.Parameters parameters) {
		List<Size> sizes = parameters.getSupportedPreviewSizes();
        Log.i("CameraMapsTest", "CameraView::setCameraParameters(): supported sizes: " + sizes);
        Size optimalSize = getOptimalPreviewSize(sizes, getWidth(), getHeight());
        if( optimalSize != null )
        	parameters.setPreviewSize(optimalSize.width, optimalSize.height);
	}
	
	private void appendCameraParametersWithGpsData(Camera.Parameters parameters) {
		parameters.removeGpsData();		
		final double TESTLAT = 61.446694;
        final double TESTLON = 23.857316;	    	
        parameters.setGpsLatitude(TESTLAT);
        parameters.setGpsLongitude(TESTLON);
        parameters.setGpsAltitude(144);
        parameters.setGpsTimestamp(System.currentTimeMillis());
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		Log.i("CameraMapsTest", "CameraView::surfaceCreated()");        
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		Log.i("CameraMapsTest", "CameraView::surfaceDestroyed()");		
		// Surface will be destroyed when we return, so stop the preview.
        // Because the CameraDevice object is not a shared resource, it's very
        // important to release it when the activity is paused.
		CameraController.releaseCamera();
	}

}
