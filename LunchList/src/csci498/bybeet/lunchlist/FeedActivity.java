//APT pg. 259
//Middle of step #8

package csci498.bybeet.lunchlist;

import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSItem;
import org.mcsoxford.rss.RSSReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FeedActivity extends ListActivity {
	
	private InstanceState state;
	
	public static final String FEED_URL = "apt.tutorial.FEED_URL";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		state = (InstanceState)getLastNonConfigurationInstance();
		
		if(state == null) {
			state = new InstanceState();
			state.handler = new FeedHandler(this);
			
			Intent intent = new Intent(this, FeedService.class);
			intent.putExtra(FeedService.EXTRA_URL, getIntent().getStringExtra(FEED_URL));
			intent.putExtra(FeedService.EXTRA_MESSENGER, new Messenger(state.handler));
			startService(intent);
		}
		else {
			if(state.handler != null) {
				state.handler.attach(this);
			}
			
			if(state.feed != null) {
				setFeed(state.feed);
			}
		}
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		if(state.handler != null) {
			state.handler.detach();
		}
		
		return state;
	}
	
	private void setFeed(RSSFeed feed) {
		state.feed = feed;
		setListAdapter(new FeedAdapter(feed));
	}

	private void goBlooey(Throwable t) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Exception!").setMessage(t.toString()).setPositiveButton("OK", null).show();
	}
	
	private static class InstanceState {
		RSSFeed feed = null;
		FeedHandler handler = null;
	}

	private static class FeedHandler extends Handler {
		
		private RSSReader reader = new RSSReader();
		private Exception e;
		private FeedActivity activity;

		FeedHandler(FeedActivity activity) {
			attach(activity);
		}

		private void attach(FeedActivity activity) {
			this.activity = activity;
		}

		private void detach() {
			this.activity = null;
		}

		@Override
		public void handleMessage(Message message) {
			if(message.arg1 == Activity.RESULT_OK) {
				activity.setFeed((RSSFeed)message.obj);
			}
			else {
				activity.goBlooey((Exception)message.obj);
			}
		}

	}
	private class FeedAdapter extends BaseAdapter {
		RSSFeed feed = null;

		FeedAdapter(RSSFeed feed) {
			super();
			this.feed = feed;
		}

		public int getCount() {
			return feed.getItems().size();
		}

		public Object getItem(int position) {
			return feed.getItems().get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View  convertView, ViewGroup parent) {
			View row = convertView;

			if(row == null) {
				LayoutInflater inflater = getLayoutInflater();
				row = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
			}
			RSSItem item = (RSSItem)getItem(position);
			((TextView)row).setText(item.getTitle());
			return row;
		}
	}
}


