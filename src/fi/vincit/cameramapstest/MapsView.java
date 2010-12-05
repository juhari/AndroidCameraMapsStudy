package fi.vincit.cameramapstest;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

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
        
        mMapView.setBuiltInZoomControls(true);
        mMapView.setSatellite(true);
        mMapView.setEnabled(true);
        mMapView.setClickable(true);
                       
        initOverlays();
    }
    
    private void initOverlays() {
    	initMyLocation();
    	initPictureOverlays();
    }
    
    private void initPictureOverlays() {
    	List<Overlay> mapOverlays = mMapView.getOverlays();
    	Drawable drawable = this.getResources().getDrawable(R.drawable.icon);
    	LocationPhotoOverlay overlay = new LocationPhotoOverlay(drawable, (Context)this);
    	mapOverlays.add(overlay);
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