package csci498.bybeet.lunchlist;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.app.*;
import android.content.Context;
import android.database.Cursor;
import android.widget.*;

@SuppressWarnings("deprecation")
public class LunchList extends TabActivity {

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
		
		Button save = (Button)findViewById(R.id.save);
		save.setOnClickListener(onSave);

		ListView list = (ListView)findViewById(R.id.restaurants);
		//AutoCompleteTextView textView = (AutoCompleteTextView)findViewById(R.id.addr);

		adapter = new RestaurantAdapter(restaurants);
		list.setAdapter(adapter);

		adapterAddress = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, addresses);
		
		//Initialize EditText fields
		name = (EditText)findViewById(R.id.name);
		address = (EditText)findViewById(R.id.addr);
		notes = (EditText)findViewById(R.id.notes); 
		types = (RadioGroup)findViewById(R.id.types);

		//Tab views, one for a list, one for entry
		TabHost.TabSpec spec=getTabHost().newTabSpec("tag1");
		spec.setContent(R.id.restaurants); 
		spec.setIndicator("List", getResources().getDrawable(R.drawable.list));
		getTabHost().addTab(spec);
		
		spec=getTabHost().newTabSpec("tag2");
		spec.setContent(R.id.details);
		spec.setIndicator("Details", getResources().getDrawable(R.drawable.restaurant));
		getTabHost().addTab(spec);
		getTabHost().setCurrentTab(0);

		list.setOnItemClickListener(onListClick);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.options, menu);
		return (super.onCreateOptionsMenu(menu));
	}

	private View.OnClickListener onSave = new View.OnClickListener() {
		public void onClick(View v) {
			EditText name = (EditText)findViewById(R.id.name);
			EditText address = (EditText)findViewById(R.id.addr);
			RadioGroup types = (RadioGroup)findViewById(R.id.types);
			EditText notes = (EditText)findViewById(R.id.notes);

			String type = new String();
			
			switch(types.getCheckedRadioButtonId())
			{
			case R.id.sit_down:
				type = "sit_down";
				break;
			case R.id.take_out:
				type = "take_out";
				break;
			case R.id.delivery:
				type = "delivery";
				break;
			}
			
			helper.insert(name.getText().toString(), address.getText().toString(), type , notes.getText().toString());
			restaurants.requery();
			
			name.setText(null);
			address.setText(null);
			notes.setText(null);

			getTabHost().setCurrentTab(0);
		}
	};

	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
		public void onItemClick (AdapterView<?> parent, View view, int position, long id){
			restaurants.moveToPosition(position);
			name.setText(helper.getName(restaurants));
			address.setText(helper.getAddress(restaurants));
			notes.setText(helper.getNotes(restaurants));
			
			if(helper.getType(restaurants).equals("sit_down")){
				types.check(R.id.sit_down);
			}
			else if(helper.getType(restaurants).equals("take_out")){
				types.check(R.id.take_out);
			}
			else{
				types.check(R.id.delivery);
			}
			
			getTabHost().setCurrentTab(1);
		}
	};
	
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