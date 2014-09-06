package polit.quiz;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

public class Syncreceiver extends BroadcastReceiver{
	Database db;
	Cursor cur;
	 Thread t; 
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		 db = new Database(arg0.getApplicationContext(),"polit.sqlite");
		 t = new Thread(new Runnable() {
	           public void run() {
	    		 InputStream is = null;
	       		 String result;  
	        	//http post
	   			try{
	   				Log.e("intetn received","db sync");
	   				HttpParams httpParameters = new BasicHttpParams();   //set connection parameters
	   				HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
	   				HttpConnectionParams.setSoTimeout(httpParameters, 10000);
	   				HttpClient httpclient = new DefaultHttpClient(httpParameters);
	   				HttpResponse response;	
	   				HttpEntity entity;
	   				JSONArray jArray;
					JSONObject json_data;
					Integer count;
	   				//HttpClient httpclient = new DefaultHttpClient();
	   				HttpPost httppost = new HttpPost("http://107.21.123.15/politquiz.php");
	   				   List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	   				   
	   				   cur = db.query("select lastsync,_id,syncreq from sync");
	   					 cur.moveToFirst();
	   					 Calendar cal = Calendar.getInstance();
	   					 Date currentime = cal.getTime();
	   					 Date syncTime = new Date(cur.getString(cur.getColumnIndex("lastsync")));
	   				     SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy");  
	   				     String dt = df.format(syncTime.getTime());
	   				     Date syncreq = new Date(cur.getString(cur.getColumnIndex("syncreq")));
	   				     Log.e("Date topic","//"+dt+"//"+syncreq);
	   				     
	   				     //topic
	   				    if(cal.getTimeInMillis() > syncreq.getTime()+( 24*3600000)){
	   				    	count = 0;
	   				    	do{
	   							nameValuePairs.add(new BasicNameValuePair("tablename", "topic"));
	   		   			        nameValuePairs.add(new BasicNameValuePair("lastsync",dt));
	   		   			        nameValuePairs.add(new BasicNameValuePair("count",count.toString()));
	   		   				        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	   		   				        response = httpclient.execute(httppost);
	   		   						entity = response.getEntity();
	   		   						 is = entity.getContent();
	   		   					  result = streamtostring(is);
	   		   					  jArray = new JSONArray(result);
								  json_data= jArray.getJSONObject(0);
								  count = json_data.getInt("next");
								  Log.e("json array",""+json_data.getString("next")+"//"+json_data.getJSONArray("data")+"//"+json_data.getJSONArray("data").length());
								  Log.e("json",""+json_data.getInt("next"));	  
								  if(json_data.getJSONArray("data").length() == 0)
									db.checkDB.execSQL("update sync set syncreq = '"+currentime+"' where _id = 1");
   		   						 else if(db.inserttopic(json_data.getJSONArray("data")) == true){
	   		   						  //syncTime = new Date(System.currentTimeMillis());
   		   							  db.checkDB.execSQL("update sync set syncreq = '"+currentime+"' where _id = 1");
	   		   						  db.checkDB.execSQL("update sync set lastsync = '"+currentime+"' where _id = 1");}
	   				    	}while(count!=0);
	   		
	   						 }
	   					 
	   					//person table
	   				    cur.moveToNext();
	   					 syncTime = new Date(cur.getString(cur.getColumnIndex("lastsync")));
	   					 syncreq = new Date(cur.getString(cur.getColumnIndex("syncreq")));
	   				     df = new SimpleDateFormat("MM/dd/yy");  
	   				     dt = df.format(syncTime.getTime());
	   				     Log.e("Date person","//"+dt);
	   				     if(cal.getTimeInMillis() > syncreq.getTime()+( 24*3600000)){
	   				     count = 0;
	   					  do{
	   					     nameValuePairs.add(new BasicNameValuePair("tablename", "person"));
		   				     nameValuePairs.add(new BasicNameValuePair("lastsync",dt));
		   				     nameValuePairs.add(new BasicNameValuePair("count",count.toString()));
		   				      httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		   				      response = httpclient.execute(httppost);
		   				      entity = response.getEntity();
		   				      is = entity.getContent();
		   				      Log.e("input length","//"+is.available());
		   					  result = streamtostring(is);
		   					  //json conversion 
		   					  jArray = new JSONArray(result);
							  json_data= jArray.getJSONObject(0);
							  count = json_data.getInt("next");
							  Log.e("json array",""+json_data.getString("next")+"//"+json_data.getJSONArray("data")+"//"+json_data.getJSONArray("data").length());
							  if(json_data.getJSONArray("data").length() == 0)
									db.checkDB.execSQL("update sync set syncreq = '"+currentime+"' where _id = 2");
							  else if(db.insertperson(json_data.getJSONArray("data")) == true){
	   					      //syncTime = new Date(System.currentTimeMillis());
								 db.checkDB.execSQL("update sync set syncreq = '"+currentime+"' where _id = 2");
	   					      db.checkDB.execSQL("update sync set lastsync = '"+currentime+"' where _id = 2");}
	   						  } while(count!=0);
	   					 }
	   				       					 
	   					 //topic quote
	   				     cur.moveToNext();
	   					 syncTime = new Date(cur.getString(cur.getColumnIndex("lastsync")));
	   					syncreq = new Date(cur.getString(cur.getColumnIndex("syncreq")));
	   				     df = new SimpleDateFormat("MM/dd/yy");  
	   				     dt = df.format(syncTime.getTime());
	   				     Log.e("Date topicquote","//"+dt);
	   				     if(cal.getTimeInMillis() > (syncreq.getTime()+( 24*3600000))){
	   				    	 count = 0;
	   				    	 do{
	   				    		nameValuePairs.add(new BasicNameValuePair("count",count.toString()));
	   				    		nameValuePairs.add(new BasicNameValuePair("tablename", "topicquote"));
		   					     nameValuePairs.add(new BasicNameValuePair("lastsync",dt));
		   					      httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		   					      response = httpclient.execute(httppost);
		   					      entity = response.getEntity();
		   					      is = entity.getContent();
		   					 	  result = streamtostring(is);
		   						  jArray = new JSONArray(result);
								  json_data= jArray.getJSONObject(0);
								  count = json_data.getInt("next");
								  if(json_data.getJSONArray("data").length() == 0)
										db.checkDB.execSQL("update sync set syncreq = '"+currentime+"' where _id = 3");
		   						  else if(db.inserttopicquote(json_data.getJSONArray("data")) == true){
		   						      //syncTime = new Date(System.currentTimeMillis());
		   							db.checkDB.execSQL("update sync set syncreq = '"+currentime+"' where _id = 3");
		   						      db.checkDB.execSQL("update sync set lastsync = '"+currentime+"' where _id = 3");}
	   				    	 }while(count != 0);
	   						 
	   				     }
	   				   
	   				     //quizquote
	   				     cur.moveToNext();
	   					 syncTime = new Date(cur.getString(cur.getColumnIndex("lastsync")));
	   					syncreq = new Date(cur.getString(cur.getColumnIndex("syncreq")));
	   				     df = new SimpleDateFormat("MM/dd/yy");  
	   				     dt = df.format(syncTime.getTime());
	   				     Log.e("Date quizquote","//"+dt);
	   				     
	   				     if(cal.getTimeInMillis() > (Date.parse(cur.getString(cur.getColumnIndex("lastsync")))+( 5 * 1000))){ 			
	   				     count = 0;
	   				     do{
	   				    	 
	   				    	nameValuePairs.add(new BasicNameValuePair("count",count.toString()));
	   				    	nameValuePairs.add(new BasicNameValuePair("tablename","quote"));
		   			        nameValuePairs.add(new BasicNameValuePair("lastsync",dt));
		   			        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		   			        response = httpclient.execute(httppost);
		   			        entity = response.getEntity();
		   					is = entity.getContent();
		   					result = streamtostring(is);
		   					jArray = new JSONArray(result);
		   					json_data= jArray.getJSONObject(0);
		   					count = json_data.getInt("next");
		   					Log.e("input length quizquote","//"+count);
		   				    if(json_data.getJSONArray("data").length() == 0)
								db.checkDB.execSQL("update sync set syncreq = '"+currentime+"' where _id = 4");
		   					else if(db.insertquizquotes(json_data.getJSONArray("data")) == true){
		   					      //syncTime = new Date(System.currentTimeMillis());
		   							db.checkDB.execSQL("update sync set syncreq = '"+currentime+"' where _id = 4");
		   							db.checkDB.execSQL("update sync set lastsync = '"+currentime+"' where _id = 4");}
	   				     }while(count!=0);
	   				     
	   				     Log.e("string",result+"//"+response.getStatusLine()); 
	   					
	   				     }
	   				     cur.close();
	   				    
	   				     }catch(Exception e){
	   				Log.e("log_tag", "Error in http connection"+e.toString());
	   				e.printStackTrace();
	   			}
	           }
	       });
	       t.start();
	     
//cal.getTimeInMillis() > (Date.parse(cur.getString(cur.getColumnIndex("lastsync"))) +( 24 * 3600 * 1000))
		
	}
	
	public String streamtostring(InputStream is){
    	//convert response to string
		try{
			int length;
			if(is.available()>0)
				length = is.available();
			else 
				length = 1;
			BufferedReader reader = new BufferedReader(new InputStreamReader(is), length);
			StringBuilder sb = new StringBuilder();
		//	sb.append(reader.readLine() + "\n");
		//	String line="0";
			String line = null;
			  while ((line = reader.readLine()) != null) 
		        {
		            sb.append(line);
		        }
			String result=sb.toString();
			is.close();
			reader = null;
			sb = null;
			Log.e("Received data",""+result);
			return result;
		}catch(Exception e){
			Log.e("log_tag", "Error converting result "+e.toString());
			e.printStackTrace();
			return null;
		}
    	
    }
}
