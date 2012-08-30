package csci498.bybeet.lunchlist;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;

public class LunchList extends Activity {

	List<Restaurant> restaurants = new ArrayList<Restaurant> ();
	ArrayAdapter<Restaurant> adapter = null;
	ArrayAdapter<String> adapterAddress = null;
	List<String> addresses = new ArrayList<String> ();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_list);
        
        Button save = (Button)findViewById(R.id.save);
        save.setOnClickListener(onSave);
        
        ListView list = (ListView)findViewById(R.id.restaurants);
        AutoCompleteTextView textView = (AutoCompleteTextView)findViewById(R.id.addr);
        
        adapter = new ArrayAdapter<Restaurant>(this, android.R.layout.simple_list_item_1, restaurants);
        list.setAdapter(adapter);
        
        adapterAddress = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, addresses);
        textView.setAdapter(adapterAddress);
        
        //Radio button group in Java code.
        /*
        RadioButton sitDown = new RadioButton(this);
        sitDown.setText("Sit-Down");
        RadioButton takeOut = new RadioButton(this);
        takeOut.setText("Take-out");
        RadioButton delivery = new RadioButton(this);
        delivery.setText("Delivery");
        RadioGroup typesGroup = new RadioGroup(this);
        typesGroup.addView( sitDown , 0);
        typesGroup.addView( takeOut, 1);
        typesGroup.addView( delivery, 2);
        
        addContentView(typesGroup, new LayoutParams( R.layout.activity_lunch_list , 0));
        setContentView(typesGroup);     
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_lunch_list, menu);
        return true;
    }
    
    private View.OnClickListener onSave = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			EditText name = (EditText)findViewById(R.id.name);
			EditText address = (EditText)findViewById(R.id.addr);
			RadioGroup types = (RadioGroup)findViewById(R.id.types);
			
			Restaurant restaurant = new Restaurant();
			restaurant.setAddress(address.getText().toString());
			restaurant.setName(name.getText().toString());
			
			switch(types.getCheckedRadioButtonId())
			{
			case R.id.sit_down:
				restaurant.setType("sit_down");
				break;
			case R.id.take_out:
				restaurant.setType("take_out");
				break;
			case R.id.delivery:
				restaurant.setType("delivery");
				break;
			}
			
			adapterAddress.add(address.getText().toString());
			adapter.add(restaurant);
			
			name.setText(null);
			address.setText(null);
			
			
		}
	};
}
