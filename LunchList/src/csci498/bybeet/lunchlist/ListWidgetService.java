package csci498.bybeet.lunchlist;

import android.content.Intent;
import android.widget.RemoteViewsService;
import android.widget.RemoteViewsService.RemoteViewsFactory;



public class ListWidgetService extends RemoteViewsService {
	
	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent){
		return (new ListViewFactory(this.getApplicationContext(), intent));
	}
}