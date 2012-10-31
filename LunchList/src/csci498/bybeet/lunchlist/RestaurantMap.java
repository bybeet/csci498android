package csci498.bybeet.lunchlist;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class RestaurantMap extends MapActivity {

	private MapView map;
	
	public static final String EXTRA_LATITUDE = "apt.tutorial.EXTRA_LATITUDE";
	public static final String EXTRA_LONGITUDE = "apt.tutorial.EXTRA_LONGITUDE";
	public static final String EXTRA_NAME = "apt.tutorial.EXTRA_NAME";
	
	@Override
	public void onCreate(Bundle savedInstanceBundle) {
		super.onCreate(savedInstanceBundle);
		setContentView(R.layout.map);
		
		double latitude = getIntent().getDoubleExtra(EXTRA_LATITUDE, 0);
		double longitude = getIntent().getDoubleExtra(EXTRA_LONGITUDE, 0);
		
		map = (MapView)findViewById(R.id.map);
		map.getController().setZoom(17);
		
		GeoPoint status = new GeoPoint((int)(latitude*1000000.0),(int)(longitude*1000000.0));
		map.getController().setCenter(status);
		map.setBuiltInZoomControls(true);
		
		Drawable marker = getResources().getDrawable(R.drawable.marker);
		map.getOverlays().add(new RestaurantOverlay(marker, status, getIntent().getStringExtra(EXTRA_NAME)));
	}
	
	@Override
	public boolean isRouteDisplayed(){
		return false;
	}
	
	private class RestaurantOverlay extends ItemizedOverlay<OverlayItem> {
		
		private OverlayItem item;
		
		public RestaurantOverlay(Drawable marker, GeoPoint point, String name) {
			super(marker);
			boundCenterBottom(marker);
			item = new OverlayItem(point, name, name);
			populate();
		}
		
		@Override
		protected OverlayItem createItem(int i) {
			return item;
		}
		
		@Override
		public int size() {
			return 1;
		}
	
	}
	
}
