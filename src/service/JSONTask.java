package service;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import zebrafish.util.IOUtil;
import zebrafish.util.JSONItem;
import zebrafish.util.JSONfunctions;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import edu.pitt.cs1635group3.Activities.Classes.Item;

public class JSONTask {
	public static final String TAG = "JSONTask";
	protected Context context;
	private static ArrayList<NameValuePair> params;
	private static String retVal;
	private static int POST = 1;
	//private static int DELETE_ITEM = 2;

	private class DoItemTask extends AsyncTask<Integer, Void, String> {
		@Override
		protected String doInBackground(Integer... params) {
			String response = "";
			
			if (params[0]==POST) {
					Log.i(TAG, "Inside POST ");
					response = JSONfunctions.postToCloud(JSONTask.params);
					Log.i(TAG, "After postToCloud ");
			} /*else if (params[0]==DELETE_ITEM) {

				JSONItem.deleteItem(currItem.getID());
			}*/

			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			
			Log.i(TAG, "Finished "+ result);
			retVal = result;
			
		}
	}

	public String postToCloud(ArrayList<NameValuePair> p) {
		params = p;
		DoItemTask task = new DoItemTask();
		task.execute(POST);
				return retVal;


	}
}