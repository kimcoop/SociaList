package service;

import zebrafish.util.IOUtil;
import zebrafish.util.JSONCustomList;
import zebrafish.util.UIUtil;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import edu.pitt.cs1635group3.Activities.HomeActivity;
import edu.pitt.cs1635group3.Activities.SociaListActivity;
import edu.pitt.cs1635group3.Activities.Classes.CustomList;
import edu.pitt.cs1635group3.Activities.Classes.Invite;
import edu.pitt.cs1635group3.Activities.Classes.User;
import edu.pitt.cs1635group3.Adapters.CustomListAdapter;

public class CustomListTask {
	public static final String TAG = "CustomListTask";
	protected Context context;
	protected ProgressDialog pd;
	private CustomList currList;
	private static int numLists;
	
	private static int UPDATE_LIST = 1;
	private static int REFRESH_LISTS = 2;
	private static int REFRESH_LISTS_QUIET = 3;

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
				numLists = JSONCustomList.download(context);
				
			} else if (params[0] == REFRESH_LISTS_QUIET) {

				Log.i(TAG, "Refreshing lists to get newly-accepted list");
			}
			
			//} else {
				//IOUtil.informConnectionIssue(context);
			//}

			return ""+params[0];
		}

		@Override
		protected void onPostExecute(String result) {

			if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}
			
			if (result.equals(""+UPDATE_LIST)) {
				
			} else if (result.equals(""+REFRESH_LISTS)) {
				CustomListAdapter adapter = SociaListActivity.getAdapter();
				adapter.notifyDataSetChanged();
				refreshActivity();
				String msg = UIUtil.pluralize(numLists, "list", "found");
				UIUtil.showMessageShort(context, msg);
				
			} else if (result.equals(""+REFRESH_LISTS_QUIET)) {
				CustomListAdapter adapter = SociaListActivity.getAdapter();
				adapter.notifyDataSetChanged();
				refreshActivity();
			}
			
			Log.i(TAG, result);
		}
	}
	
	public void refreshActivity() {

		((Activity)context).finish();
		((Activity)context).startActivity(((Activity) context).getIntent());
	}
	
	public void refreshListsQuiet(Context c) {
		// Called when the user accepts an invite (because the associated list needs to be pulled). Don't show any UI cues.
		context = c;
		DoCustomListTask task = new DoCustomListTask();
		task.execute(REFRESH_LISTS_QUIET);
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

