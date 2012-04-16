package service;

import edu.pitt.cs1635group3.Item;
import edu.pitt.cs1635group3.R;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import zebrafish.util.JSONfunctions;
import zebrafish.util.PostAsyncServiceCall;
import zebrafish.util.PostParams;

import android.app.Activity;
import android.util.Log;


public class UpdateItemAsyncServiceCall 
	extends PostAsyncServiceCall<Item, Void, ArrayList<edu.pitt.cs1635group3.Item>> {
	
	public static final String TAG = "UpdateItemAsync";

	public UpdateItemAsyncServiceCall(Activity context) {
		super(context);
	}
	

	@Override
	public int getProgressStringId() {
		return R.string.adding_wait;
	}

	
	@Override
	protected ArrayList<edu.pitt.cs1635group3.Item> run(
		Item... args) throws IOException {
		ArrayList<edu.pitt.cs1635group3.Item> res =
			new ArrayList<edu.pitt.cs1635group3.Item>();
		
		for (Item item : args) {
			InputStream is = null;
			String result = "", resp = "";

			// http post
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(JSONfunctions.URL);
				ArrayList<NameValuePair> params = PostParams
						.formatParams("updateItem", item);
				httppost.setEntity(new UrlEncodedFormEntity(params));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();

			} catch (Exception e) {
				Log.e(TAG + "- POST ITEM",
						"Error in http connection " + e.toString());
			}
/*
			result = getResult(is);
			resp = getResponse(result, "response");
			Log.i(TAG, resp);*/

		}
		Log.i(TAG, "Successfully updated Item");
		return res;
	}
	
}
