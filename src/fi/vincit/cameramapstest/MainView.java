package fi.vincit.cameramapstest;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainView extends Activity implements OnClickListener {
	
	private HashMap<View, Intent> mButtonMap;
	
	/** called when created */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.mainview);
		
		mButtonMap = new HashMap<View, Intent> ();
		
		Button mapsButton = (Button) findViewById(R.id.MapsButton);
		mapsButton.setOnClickListener(this);
		mButtonMap.put(mapsButton, new Intent(MainView.this, MapsView.class));
		
		Button button = (Button) findViewById(R.id.CameraButton);
		button.setOnClickListener(this);
		mButtonMap.put(button, new Intent(MainView.this, MapsView.class));
	}	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i = mButtonMap.get(v);
		startActivity(i);
	}
	
}
