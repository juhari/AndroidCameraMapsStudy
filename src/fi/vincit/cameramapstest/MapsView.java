package fi.vincit.cameramapstest;

import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class MapsView extends MapActivity {
	
	private MapView mMapView;
	private MyLocationOverlay mLocationOverlay;
	private MapController mMapController;
	
	@Override
	public boolean isRouteDisplayed() {
		return false;
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapsview);
        
        mMapView = (MapView) findViewById(R.id.mapview);
        mMapController = mMapView.getController();
        
        initMyLocation();
        
        mMapView.setBuiltInZoomControls(true);
        mMapView.setSatellite(true);
        mMapView.setEnabled(true);
        mMapView.setClickable(true);
               
    }
    
    private void initMyLocation() {    	
    	mLocationOverlay = new MyLocationOverlay(this, mMapView);
    	mMapView.getOverlays().add(mLocationOverlay);
    	mLocationOverlay.enableMyLocation();    	
    	mLocationOverlay.enableCompass();
    	mLocationOverlay.runOnFirstFix(new Runnable() {
    		public void run() {
    			mMapController.setZoom(14);
    			mMapController.animateTo(mLocationOverlay.getMyLocation());
    		}
    	});
    }
}