package csci498.bybeet.lunchlist;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.app.*;
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

		void populateFrom (Restaurant r)
		{
			name.setText(r.getName());
			address.setText(r.getAddress());

			if (r.getType().equals("sit_down")) 
			{ 
				icon.setImageResource(R.drawable.ball_red);
			}
			else if (r.getType().equals("take_out")) 
			{
				icon.setImageResource(R.drawable.ball_yellow);
			}
			else 
			{ 
				icon.setImageResource(R.drawable.ball_green);
			}
		}


	}

	class RestaurantAdapter extends ArrayAdapter<Restaurant>{
		RestaurantAdapter(){
			super(LunchList.this, android.R.layout.simple_list_item_1, restaurants);
		}

		public View getView(int position, View convertView, ViewGroup parent)
		{
			View row = convertView;
			RestaurantHolder holder = null;

			if( row == null )
			{
				LayoutInflater inflater = getLayoutInflater();
				row = inflater.inflate(R.layout.row, null);

				holder = new RestaurantHolder(row);
				row.setTag(holder);
			}
			else
			{
				holder = (RestaurantHolder)row.getTag();
			}

			holder.populateFrom(restaurants.get(position));
			return(row);
		}
	}

	List<Restaurant> restaurants = new ArrayList<Restaurant> ();
	RestaurantAdapter adapter = null;
	ArrayAdapter<String> adapterAddress = null;
	List<String> addresses = new ArrayList<String> ();
	
	EditText name = null;
	EditText address = null;
	EditText notes = null;
	RadioGroup types = null;
	Restaurant current = null;
		
	int progress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Status Bar
		requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.main);

		Button save = (Button)findViewById(R.id.save);
		save.setOnClickListener(onSave);

		ListView list = (ListView)findViewById(R.id.restaurants);
		//AutoCompleteTextView textView = (AutoCompleteTextView)findViewById(R.id.addr);

		adapter = new RestaurantAdapter();
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

			current = new Restaurant();
			current.setAddress(address.getText().toString());
			current.setName(name.getText().toString());
			current.setNotes(notes.getText().toString());

			switch(types.getCheckedRadioButtonId())
			{
			case R.id.sit_down:
				current.setType("sit_down");
				break;
			case R.id.take_out:
				current.setType("take_out");
				break;
			case R.id.delivery:
				current.setType("delivery");
				break;
			}

			adapterAddress.add(address.getText().toString());
			adapter.add(current);

			name.setText(null);
			address.setText(null);
			notes.setText(null);

			getTabHost().setCurrentTab(0);
		}
	};

	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
		public void onItemClick (AdapterView<?> parent, View view, int position, long id){
			current = restaurants.get(position);
			
			name.setText(current.getName());
			address.setText(current.getAddress());
			notes.setText(current.getNotes());
			
			if(current.getType().equals("sit_down")){
				types.check(R.id.sit_down);
			}
			else if(current.getType().equals("take_out")){
				types.check(R.id.take_out);
			}
			else{
				types.check(R.id.delivery);
			}
			
			getTabHost().setCurrentTab(1);
		}
	};
	
	//Taken from APT
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
}
