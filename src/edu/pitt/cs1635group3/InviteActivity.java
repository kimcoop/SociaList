package edu.pitt.cs1635group3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class InviteActivity extends Activity {

	 	private static final String DEBUG_TAG = "InviteActivity";
	    private static final int CONTACT_PICKER_RESULT = 1001;
	    
	    String massage = "";
	    String email = "";
        String phone = "";

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.invite);
	    }

	    public void doLaunchContactPicker(View view) {
	        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
	                Contacts.CONTENT_URI);
	        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
	    }

	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (resultCode == RESULT_OK) {
	                Cursor cursor = null;
	                Cursor cursorPhone = null;
	                
	                Spinner spinner = (Spinner) findViewById(R.id.invite_type_spinner);
	                String invite_type = (String) spinner.getItemAtPosition(spinner.getSelectedItemPosition());
	                
	                
	                
	                try {
	                    Bundle extras = data.getExtras();
	                    Set<String> keys = extras.keySet();
	                    Iterator<String> iterate = keys.iterator();
	                    while (iterate.hasNext()) {
	                        String key = iterate.next();
	                        Log.v(DEBUG_TAG, key + "[" + extras.get(key) + "]");
	                    }

	                    Uri result = data.getData();
	                    Log.v(DEBUG_TAG, "Got a contact result: "
	                            + result.toString());

	                    // get the contact id from the Uri
	                    String id = result.getLastPathSegment();
	                    
	                    if(invite_type.equals("Send Email")){
		                    // query for everything email
		                    cursor = getContentResolver().query(Email.CONTENT_URI,
		                            null, Email.CONTACT_ID + "=?", new String[] { id },
		                            null);
		                    int emailIdx = cursor.getColumnIndex(Email.DATA);
		                    
		                    // let's just get the first email
		                    if (cursor.moveToFirst()) {

		                        
		                          //Iterate all columns. :) 
		                          String columns[] = cursor.getColumnNames(); 
		                          for (String column : columns) { 
		                        	  int index = cursor.getColumnIndex(column);
		                        	  Log.v(DEBUG_TAG, "Column: " + column + " == [" +
		                        			  cursor.getString(index) + "]"); 
		                          }
		                         

		                        email = cursor.getString(emailIdx);
		                        
		                        Log.v(DEBUG_TAG, "Got email: " + email);
		                        

		                    } else {
		                        Log.w(DEBUG_TAG, "No email results");
		                    }
	                    }
	                    
	                    if(invite_type.equals("Send SMS")){
		                    // query for everything phone
		                    cursorPhone = getContentResolver().query(Phone.CONTENT_URI,
		                            null, Phone.CONTACT_ID + "=?", new String[] { id },
		                            null);
		                    
		                    int phoneIdx = cursorPhone.getColumnIndex(Phone.DATA);
		                    // let's just get the first phone
		                    if (cursorPhone.moveToFirst()) {
		                          //Iterate all columns. :) 
		                          String columnsPhone[] = cursorPhone.getColumnNames(); 
		                          for (String column : columnsPhone) { 
		                        	  int index = cursorPhone.getColumnIndex(column);
		                        	  Log.v(DEBUG_TAG, "Column: " + column + " == [" +
		                        			  cursorPhone.getString(index) + "]"); 
		                          }
		                        
		                        phone = cursorPhone.getString(phoneIdx);
		                        Log.v(DEBUG_TAG, "Got phone: " + phone);

		                    } else {
		                        Log.w(DEBUG_TAG, "No phone results");
		                    }
	                    }

	                   
	                    
	                } catch (Exception e) {
	                    Log.e(DEBUG_TAG, "Failed to get data", e);
	                } finally {
	                    if (cursor != null) {
	                        cursor.close();
	                    }
	                    if(invite_type.equals("Send Email")){
		                    EditText emailEntry = (EditText) findViewById(R.id.invite_email);
		                    emailEntry.setText(email);
		                    if (email.length() == 0) {
		                        Toast.makeText(this, "No email found for contact.",
		                                Toast.LENGTH_LONG).show();
		                    }
	                    }
	                    
	                    if(invite_type.equals("Send SMS")){
	                    	EditText massageEntry = (EditText) findViewById(R.id.invite_email);
	                    	massageEntry.setText(phone);
	                    	
	                    	if (phone.length() == 0) {
		                        Toast.makeText(this, "No phone found for contact.",
		                                Toast.LENGTH_LONG).show();
		                    }
	                    }

	                }

	        } else {
	            Log.w(DEBUG_TAG, "Warning: activity result not ok");
	        }
	    }
	    
	    public void sendInvitation(View v){
	    	String smsContent = "Join my SociaList# 123456";
	    	
	    	Spinner spinner = (Spinner) findViewById(R.id.invite_type_spinner);
            String invite_type = (String) spinner.getItemAtPosition(spinner.getSelectedItemPosition());
            
            if(invite_type.equals("Send SMS")){
            	
            	Uri smsUri = Uri.parse("sms:6102356128");
            	Intent sendIntent = new Intent(Intent.ACTION_VIEW,smsUri);
            	sendIntent.putExtra("sms_body", smsContent); 
            	sendIntent.setType("vnd.android-dir/mms-sms");
            	startActivity(sendIntent);   
            	
            	
            	/*
            	 PendingIntent pi = PendingIntent.getActivity(this, 0,
            	            new Intent(this, Object.class), 0);                
            	 SmsManager sms = SmsManager.getDefault();
            	 sms.sendTextMessage(phone, null, smsContent, pi, null);     
            	*/
            
            } else if(invite_type.equals("Send Email")){
            	String subject = "Invitation to my SociaList";
            	String emailContent = "Join my SociaList# 123456";
            	
            	Intent emailIntent = new Intent(Intent.ACTION_SEND);
            	emailIntent .setType("plain/text");
            	emailIntent .putExtra(Intent.EXTRA_EMAIL, email);
            	emailIntent .putExtra(Intent.EXTRA_SUBJECT, subject );
            	emailIntent .putExtra(Intent.EXTRA_TEXT, emailContent);
            	startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            	
            }
            
            
	    	
	    	
	    }

}