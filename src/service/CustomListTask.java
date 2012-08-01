package service;

import java.util.ArrayList;


import zebrafish.util.IOUtil;
import zebrafish.util.JSONCustomList;
import zebrafish.util.JSONItem;
import zebrafish.util.UIUtil;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import edu.pitt.cs1635group3.Activities.BrowseForListActivity;
import edu.pitt.cs1635group3.Activities.BrowseInsideListActivity;
import edu.pitt.cs1635group3.Activities.CreateListActivity;
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
	private static int currPK;
	private static ArrayList<Integer> currItemPKs;
	private static String searchTerm;
	private static ArrayList<CustomList> results;
	
	private static int UPDATE_LIST = 1;
	private static int REFRESH_LISTS = 2;
	private static int REFRESH_LISTS_QUIET = 3;
	private static int RESERVE_PRIMARY_KEY = 4;
	private static int UNCREATE_LIST = 5;
	private static int CREATE_AS_UPDATE = 6;
	private static int REFRESH_MY_ITEMS = 7;
	private static int RESERVE_PK_BROWSE =8;
	private static int BROWSE = 9;
	private static int SAVE_LIST = 10;

	private class DoCustomListTask extends AsyncTask<Integer, Void, String> {
		@Override
		protected String doInBackground(Integer... params) {
			String response = "";
			
			if (params[0] == UPDATE_LIST) {
				
				Log.i(TAG, "Updated " + currList.getName()+ " whose pk is " +currList.getID());
				JSONCustomList.updateList(currList);

			} else if (params[0] == REFRESH_LISTS) {
				
				Log.i(TAG, "Refreshing lists");
				numLists = JSONCustomList.download(context);
				
			} else if (params[0] == REFRESH_LISTS_QUIET) {

				Log.i(TAG, "Refreshing lists to get newly-accepted list");
				JSONCustomList.download(context);
				
			} else if (params[0] == RESERVE_PRIMARY_KEY) {
				
				currPK = JSONCustomList.getListPK();
				Log.i(TAG, "Reserving pk for new list");
				
			} else if (params[0] == UNCREATE_LIST) {
				
				JSONCustomList.deleteList(currPK);
				for (int i : currItemPKs) { // delete the items as well
					JSONItem.deleteItem(i);
				}
				
			} else if (params[0] == CREATE_AS_UPDATE) {
				
				Log.i(TAG, "Updated " + currList.getName()+ " whose pk is " +currList.getID());
				JSONCustomList.createAsUpdate(currList);
				
			} else if (params[0] == REFRESH_MY_ITEMS) {
				Log.i(TAG, "Refreshing My Items");
				numLists = JSONCustomList.download(context);

			} else if (params[0] == RESERVE_PK_BROWSE) {

				currPK = JSONCustomList.getListPK();
				
			} else if (params[0] == BROWSE) {
				
				results = JSONCustomList.browseForList(context, searchTerm);
				
			} else if (params[0] == SAVE_LIST){
				CreateListActivity.saveList();
			}
			
			

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
				if (adapter != null) adapter.notifyDataSetChanged();
				refreshActivity();
				String msg = UIUtil.pluralize(numLists, "list", "found");
				UIUtil.showMessageShort(context, msg);
				
			} else if (result.equals(""+SAVE_LIST)) {
				CustomListAdapter adapter = SociaListActivity.getAdapter();
				if (adapter != null) adapter.notifyDataSetChanged();
				else Log.i(TAG, "adapter was NULL!!");
				UIUtil.showMessage(context, "List Created!");
				((Activity)context).finish();
				Intent i = new Intent(context, SociaListActivity.class);
				((Activity)context).startActivity(i);;
				
			}else if (result.equals(""+REFRESH_LISTS_QUIET)) {

				CustomListAdapter adapter = SociaListActivity.getAdapter();
				if (adapter != null) adapter.notifyDataSetChanged();
				
			} else if (result.equals(""+RESERVE_PRIMARY_KEY)) {
				
				CreateListActivity.setListID(currPK);
				
			} else if (result.equals(""+UNCREATE_LIST)) {
				Log.i(TAG, "uncreate list complete");
			} else if (result.equals(""+REFRESH_MY_ITEMS)){
				CustomListAdapter adapter = SociaListActivity.getAdapter();
				if (adapter != null) adapter.notifyDataSetChanged();
				refreshActivity();

			} else if (result.equals(""+RESERVE_PK_BROWSE)) {

				BrowseInsideListActivity.setListID(currPK);
			} else if (result.equals(""+BROWSE)) {
				BrowseForListActivity.display(results);
			}
			
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

	public void refreshMyItems(Context c) {
		context = c;
		
		pd = ProgressDialog.show(context, "Refreshing", "Please wait...", true, false, null);
		DoCustomListTask task = new DoCustomListTask();
		task.execute(REFRESH_MY_ITEMS);
	}
	
	public void update(CustomList list) {
		currList = list;
		DoCustomListTask task = new DoCustomListTask();
		task.execute(UPDATE_LIST);
	}
	
	public void reservePrimaryKey() {
		Log.i(TAG, "reserving pk");
		DoCustomListTask task = new DoCustomListTask();
		task.execute(RESERVE_PRIMARY_KEY);
	}
	
	public void reservePrimaryKeyBrowse() {
		DoCustomListTask task = new DoCustomListTask();
		task.execute(RESERVE_PK_BROWSE);
	}
	
	public void uncreateList(int id, ArrayList<Integer> itemPKs) {
		Log.i(TAG, "uncreating list");
		currPK = id;
		currItemPKs = itemPKs;

		DoCustomListTask task = new DoCustomListTask();
		task.execute(UNCREATE_LIST);
	}

	public void createAsUpdate(CustomList list) {
		currList = list;
		DoCustomListTask task = new DoCustomListTask();
		task.execute(CREATE_AS_UPDATE);
	}
	
	public void browse(Context c, String tag) {
		context = c;
		pd = ProgressDialog.show(context, "Searching", "Please wait...", true, false, null);
		searchTerm = tag;
		DoCustomListTask task = new DoCustomListTask();
		task.execute(BROWSE);
	}
	
	public void saveList(Context c) {
		context = c;
		DoCustomListTask task = new DoCustomListTask();
		task.execute(SAVE_LIST);

	}
}

