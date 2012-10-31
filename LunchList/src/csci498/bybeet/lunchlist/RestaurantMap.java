package csci498.bybeet.lunchlist;

import android.os.Bundle;

import com.google.android.maps.MapActivity;

public class RestaurantMap extends MapActivity {

	@Override
	public void onCreate(Bundle savedInstanceBundle) {
		super.onCreate(savedInstanceBundle);
		setContentView(R.layout.map);
	}
	
	@Override
	public boolean isRouteDisplayed(){
		return false;
	}
	
}
