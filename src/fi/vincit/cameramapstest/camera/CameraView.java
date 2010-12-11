package fi.vincit.cameramapstest.camera;
import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import fi.vincit.cameramapstest.R;
import fi.vincit.cameramapstest.locationphoto.LocationPhotoStoredCallback;

public class CameraView extends Activity
						implements OnClickListener,
								   LocationPhotoStoredCallback {

	private CameraSurface mSurface = null;	
	
	public CameraView() {
		// TODO Auto-generated constructor stub
	}
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i("CameraMapsTest", "CameraView::onCreate() <--");
        super.onCreate(savedInstanceState);            	
        
        Log.i("CameraMapsTest", "CameraView::onCreate(): get surface");          
        
        Log.i("CameraMapsTest", "CameraView::onCreate(): set content view");
        setContentView(R.layout.cameraview);
        
        openCamera();        
        createSurface();                      
        initializeCaptureButton();                    
        
        Log.i("CameraMapsTest", "CameraView::onCreate() -->");
    }       
    
    private void openCamera() {
    	CameraController.openCamera();
    }
    
    private void createSurface() {
    	mSurface = (CameraSurface) findViewById(R.id.camerasurface);//new CameraSurface(this);
        
        if( mSurface == null )
        {
        	Log.e("CameraMapsTest", "CameraView::onCreate(): surface not created!");
        }
    }        
    
    private void initializeCaptureButton() {
    	Button captureButton = (Button) findViewById(R.id.CaptureButton);
        captureButton.setOnClickListener(this);
    }  
	
	@Override
    public void onBackPressed() {
		Log.i("CameraMapsTest", "CameraView::onBackPressed()");
		CameraController.releaseCamera();
    	finish();
    }

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view.getId() == R.id.CaptureButton )
		{
			Log.i("CameraMapsTest", "CameraView::onClick()");
			CameraController.capturePicture((LocationPhotoStoredCallback)(this));
		}
		
	}
	
	public void insertLocationPhotoToGallery(String filename) {
		ContentValues values = new ContentValues();
		values.put(Media.TITLE, filename);
		values.put(Media.DESCRIPTION, "Image by CameraMapsDemo");
		getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
	}						
}
