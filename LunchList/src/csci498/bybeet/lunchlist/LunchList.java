package csci498.bybeet.lunchlist;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class LunchList extends ListActivity {

	public final static String ID_EXTRA = "apt.tutorial._ID";

	private Cursor restaurants = null;
	private RestaurantAdapter adapter = null;
	private RestaurantHelper helper;
	private SharedPreferences prefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		//Initialize "restaurants" to all the cursor information in the db
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(prefListener);
		helper = new RestaurantHelper(this);
		initList();

	}

	private void initList() {
		if(restaurants != null) {
			stopManagingCursor(restaurants);
			restaurants.close();
		}
		
		restaurants = helper.getAll(prefs.getString("sort_order", "name"));
		startManagingCursor(restaurants);

		adapter = new RestaurantAdapter(restaurants);
		setListAdapter(adapter);
	}

	@Override
	public void onDestroy () {
		super.onDestroy();
		helper.close();
	}

	@Override
	public void onListItemClick(ListView list, View view, int position, long id) {
		Intent intent = new Intent(LunchList.this, DetailForm.class);
		intent.putExtra(ID_EXTRA, String.valueOf(id));
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.options, menu);
		return (super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.add) {
			startActivity(new Intent(LunchList.this, DetailForm.class));
			return true;
		}
		else if(item.getItemId() == R.id.prefs) {
			startActivity(new Intent(this, EditPreference.class));
			return true;
		}

		return(super.onOptionsItemSelected(item));
	}

	class RestaurantAdapter extends CursorAdapter{
		RestaurantAdapter(Cursor c){
			super(LunchList.this, c);
		}

		@Override
		public void bindView(View row, Context ctxt, Cursor c) {
			RestaurantHolder holder = (RestaurantHolder)row.getTag();
			holder.populateFrom(c, helper);
		}

		@Override
		public View newView(Context ctxt, Cursor c, ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater();
			View row = inflater.inflate(R.layout.row, parent, false);
			RestaurantHolder holder = new RestaurantHolder(row);
			row.setTag(holder);
			return row;
		}
	}

	static class RestaurantHolder
	{
		private TextView name = null;
		private TextView address = null;
		private ImageView icon = null;

		RestaurantHolder (View row){
			name = (TextView)row.findViewById(R.id.title);
			address = (TextView)row.findViewById(R.id.address);
			icon = (ImageView)row.findViewById(R.id.icon);
		}

		void populateFrom (Cursor c, RestaurantHelper helper){
			name.setText(helper.getName(c));
			address.setText(helper.getAddress(c));

			if (helper.getType(c).equals("sit_down")) 
			{ 
				icon.setImageResource(R.drawable.ball_red);
			}
			else if (helper.getType(c).equals("take_out")) 
			{
				icon.setImageResource(R.drawable.ball_yellow);
			}
			else 
			{ 
				icon.setImageResource(R.drawable.ball_green);
			}
		}
	}
	
	private SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
			if (key.equals("sort_order")){
				initList();
			}
		}
	};
}