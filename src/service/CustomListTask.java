package service;

import zebrafish.util.IOUtil;
import zebrafish.util.JSONCustomList;
import zebrafish.util.UIUtil;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import edu.pitt.cs1635group3.Activities.SociaListActivity;
import edu.pitt.cs1635group3.Activities.Classes.CustomList;
import edu.pitt.cs1635group3.Adapters.CustomListAdapter;

public class CustomListTask {
	public static final String TAG = "CustomListTask";
	protected Context context;
	protected ProgressDialog pd;
	private CustomList currList;
	private static int numLists;
	
	private static int UPDATE_LIST = 1;
	private static int REFRESH_LISTS = 2;

	private class DoCustomListTask extends AsyncTask<Integer, Void, String> {
		@Override
		protected String doInBackground(Integer... params) {
			String response = "";
			
			//if (IOUtil.isOnline(context)) {
			
			if (params[0] == UPDATE_LIST) {
				
				Log.i(TAG, "Updated " + currList.getName());
				JSONCustomList.updateList(currList);

			} else if (params[0] == REFRESH_LISTS) {
				Log.i(TAG, "Refreshing lists");
				JSONCustomList.getLists(context);
			}
			
			//} else {
				//IOUtil.informConnectionIssue(context);
			//}

			return ""+params[0];
		}

		@Override
		protected void onPostExecute(String result) {

			if (result.equals(""+UPDATE_LIST)) {
				
			} else if (result.equals(""+REFRESH_LISTS)) {
				CustomListAdapter adapter = SociaListActivity.getAdapter();
				adapter.notifyDataSetChanged();
				pd.dismiss();
				UIUtil.showMessage(context, "Lists refreshed.");
			}
			
			Log.i(TAG, result);
		}
	}
	
	
	public void refreshLists(Context c) {
		context = c;
		
		pd = ProgressDialog.show(context, "Refreshing", "Please wait...", true, false, null);
		DoCustomListTask task = new DoCustomListTask();
		task.execute(REFRESH_LISTS);
	}

	public void update(CustomList list) {
		currList = list;
		DoCustomListTask task = new DoCustomListTask();
		task.execute(UPDATE_LIST);
	}
}

