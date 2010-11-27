package fi.vincit.cameramapstest;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.util.Log;

public class DemoMediaScannerClient implements MediaScannerConnectionClient {

	MediaScannerConnection mConnection = null;
	private boolean mScannerConnected = false;
	private boolean mScanCompleted = false;
	
	public DemoMediaScannerClient(Context context) {
		mConnection = new MediaScannerConnection(context, this);
		mConnection.connect();
	}
	
	@Override
	public void onMediaScannerConnected() {
		// TODO Auto-generated method stub
		mScannerConnected = true;
	}

	@Override
	public void onScanCompleted(String path, Uri uri) {
		// TODO Auto-generated method stub
		Log.i("CameraMapsTest", "onScanCompleted(): msc.scanFile() path = "
				+ path + " uri = " + uri.toString());	
		
		mScanCompleted = true;
	}
	
	public void updateMediaScanner(String filename) {
		if( mScannerConnected )
		{
			mScanCompleted = false;
			mConnection.scanFile(filename, "image/jpeg");
			
			while( mScanCompleted == false ) {
				try {
					Thread.sleep(200);
				} catch( Exception e) {					
				}				
			}
			Log.i("CameraMapsTest", "end scan, mConnectionPath = " + filename);
		}
	}

}
