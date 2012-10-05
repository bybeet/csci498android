package csci498.bybeet.lunchlist;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class LunchList extends ListActivity {

	public final static String ID_EXTRA = "apt.tutorial._ID";
	
	Cursor restaurants = null;
	RestaurantAdapter adapter = null;
	ArrayAdapter<String> adapterAddress = null;
	List<String> addresses = new ArrayList<String> ();
	RestaurantHelper helper;
	
	EditText name = null;
	EditText address = null;
	EditText notes = null;
	RadioGroup types = null;
	Restaurant current = null;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		//Initialize "restaurants" to all the cursor information in the db
		helper = new RestaurantHelper(this);
		restaurants = helper.getAll();
		startManagingCursor(restaurants);
		
		adapter = new RestaurantAdapter(restaurants);
		setListAdapter(adapter);
	}
	
	static class RestaurantHolder
	{
		private TextView name = null;
		private TextView address = null;
		private ImageView icon = null;

		RestaurantHolder (View row)
		{
			name = (TextView)row.findViewById(R.id.title);
			address = (TextView)row.findViewById(R.id.address);
			icon = (ImageView)row.findViewById(R.id.icon);
		}

		void populateFrom (Cursor c, RestaurantHelper helper)
		{
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

	class RestaurantAdapter extends CursorAdapter{
		RestaurantAdapter(Cursor c){
			super(LunchList.this, c);
		}

		@Override
		public void bindView (View row, Context ctxt, Cursor c) {
			RestaurantHolder holder = (RestaurantHolder)row.getTag();
			holder.populateFrom(c, helper);
		}
		
		@Override
		public View newView (Context ctxt, Cursor c, ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater();
			View row = inflater.inflate(R.layout.row, parent, false);
			RestaurantHolder holder = new RestaurantHolder(row);
			row.setTag(holder);
			return row;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.options, menu);
		return (super.onCreateOptionsMenu(menu));
	}

	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
		public void onItemClick (AdapterView<?> parent, View view, int position, long id){
			Intent i = new Intent(LunchList.this, DetailForm.class);
			
			startActivity(i);
		}
	};
	
	@Override
	public void onListItemClick(ListView list, View view, int position, long id) {
		Intent intent = new Intent(LunchList.this, DetailForm.class);
		intent.putExtra(ID_EXTRA, String.valueOf(id));
		startActivity(intent);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId()==R.id.toast) {
			String message="No restaurant selected";
			if (current!=null) {
				message=current.getNotes();
			}
			Toast.makeText(this, message, Toast.LENGTH_LONG).show();
			return(true);
		}
		
		return(super.onOptionsItemSelected(item));
	}
	
	@Override
	public void onDestroy () {
		super.onDestroy();
		helper.close();
	}
}