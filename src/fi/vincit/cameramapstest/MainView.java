package fi.vincit.cameramapstest;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import fi.vincit.cameramapstest.camera.CameraView;
import fi.vincit.cameramapstest.maps.MapsView;

public class MainView extends Activity implements OnClickListener {
	
	private HashMap<View, Intent> mButtonMap;
	
	/** called when created */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i("CameraMapsTest", "MainView::onCreate()");
		super.onCreate(savedInstanceState);
		
		DataBaseConnection.open(getApplicationContext());
		DemoMediaScannerClientFactory.createInstance(getApplicationContext());		
		
		setContentView(R.layout.mainview);
		
		mButtonMap = new HashMap<View, Intent> ();
		
		Button mapButton = createButtonByViewId(R.id.MapsButton);
		mButtonMap.put(mapButton, new Intent(MainView.this, MapsView.class));
		
		mapButton = createButtonByViewId(R.id.CameraButton);
		mButtonMap.put(mapButton, new Intent(MainView.this, CameraView.class));
	}	
	
	private Button createButtonByViewId(int id) {
		Button button = (Button) findViewById(id);
		button.setOnClickListener(this);
		return button;
	}
	
	@Override
	public void onDestroy() {
		DataBaseConnection.close();
		DemoMediaScannerClientFactory.getInstance().disconnect();	
		super.onDestroy();			
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i = mButtonMap.get(v);
		startActivity(i);
	}
	
}
