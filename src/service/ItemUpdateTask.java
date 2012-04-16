package service;

import edu.pitt.cs1635group3.Item;
import edu.pitt.cs1635group3.R;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import zebrafish.util.Config;
import zebrafish.util.JSONfunctions;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ItemUpdateTask {
	public static final String TAG = "ItemUpdateTask";
	protected Context context;

	private class DoItemUpdateTask extends AsyncTask<Item, Void, String> {
		@Override
		protected String doInBackground(Item... params) {
			String response = "";

			for(Item item : params) {
				Log.i(TAG, "Updated " +item.getName());
				JSONfunctions.updateItem(item); // really will only be one item, I think -KIm
			}
			
			
			return "";
		}

		@Override
		protected void onPostExecute(String result) {
			//textView.setText(result);
			Log.i(TAG, result);
		}
	}
	
	public void update(Item i) {
		DoItemUpdateTask task = new DoItemUpdateTask();
		task.execute(i);

	}
}