package fi.vincit.cameramapstest;

import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class MapsView extends MapActivity {
	@Override
	public boolean isRouteDisplayed() {
		return false;
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapsview);
        
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
    }
}