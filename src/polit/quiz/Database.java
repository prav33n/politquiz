package polit.quiz;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.view.View;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper{

String DB_PATH,DB_NAME;
Context con;
Cursor Cur;
View V;
AssetManager assets;
SQLiteDatabase checkDB;
public Database(Context con, String DB_NAME) {
	super(con,DB_NAME,null,2);
	try {
	DB_NAME = "politquiz.sqlite";
	this.con = con;
	//if(!checkDB.isOpen())
	checkDB = SQLiteDatabase.openDatabase(con.getDatabasePath(DB_NAME).toString(), null,SQLiteDatabase.OPEN_READWRITE);
	Log.e("database constructor","Open");
    }
	catch(Exception e) {Log.e("constructor",""+e.getMessage());}
}

public boolean isopen() {
	return checkDB.isOpen();
	
}

public boolean closedb() {
	boolean test = checkDB.isOpen();
	if(test)
	checkDB.close();
	Log.e("Databse closed",""+ test);
	return test;
}

public long insert(String DB_TABLE, ContentValues values) {
   	ContentValues initialValues = new ContentValues();
	checkDB.beginTransaction();
	long test = checkDB.insertOrThrow(DB_TABLE, null, initialValues);
	checkDB.setTransactionSuccessful();
	checkDB.endTransaction();
	Log.e("insert", ""+test);
	//checkDB.close();
	return test;
 }


public boolean insertquizquotes(JSONArray jArray) {
	 
	checkDB.beginTransaction();
	try {
	    SQLiteStatement insert = null;
	   insert = checkDB.compileStatement("INSERT OR REPLACE INTO \"quizquotes\" (\"id\",\"personid\",\"deleted\",\"updated\",\"quotequiz\",\"pubdate\",\"pubsource\",\"bitly\",\"pubtitle\",\"pubauthor\",\"_id\") VALUES (?,?,?,?,?,?,?,?,?,?,?)");
	   //Parse the staring to json foramt 
		   //JSONArray jArray = new JSONArray(result);
			 JSONObject json_data=null;
			 Log.e("test insert quotes",""+jArray.length());
			 for(int i=0;i<jArray.length();i++){
				json_data = jArray.getJSONObject(i);
				insert.bindLong(1, Integer.parseInt(json_data.getString("id")));
				insert.bindLong(2, Integer.parseInt(json_data.getString("personid")));
				insert.bindLong(3, Integer.parseInt(json_data.getString("deleted")));
		        insert.bindString(4, json_data.getString("updated"));
		        insert.bindString(5, json_data.getString("quote"));
		        insert.bindString(6, json_data.getString("pubdate"));
		        insert.bindString(7, json_data.getString("pubsource"));
		        insert.bindString(8, json_data.getString("bitly"));
		        insert.bindString(9, json_data.getString("pubtitle"));
		        insert.bindString(10, json_data.getString("pubauthor"));
		        insert.bindLong(11, Integer.parseInt(json_data.getString("_id")));
		        insert.execute();
		        insert.clearBindings();
		        			
				//HashMap<String, String> map = new HashMap<String, String>();
	        	//map.put("_id",  json_data.getString("_id"));
	        	//map.put("quote", json_data.getString("quote"));
	        	//map.put("bitly",json_data.getString("bitly"));
	        	//map.put("pubsource",json_data.getString("pubsource"));
	        	//map.put("pubdate",json_data.getString("pubdate"));
	        	//mylist.add(map);
				 }       
	    checkDB.setTransactionSuccessful();
		 return true;
	}
	catch (Exception e) {
	    String errMsg = (e.getMessage() == null) ? "bulkInsert failed" : e.getMessage();
	    Log.e("bulkInsert:", errMsg);
		 return false;
	}
	finally {
	    checkDB.endTransaction();
	  }
 }


public boolean inserttopic(JSONArray jArray) {
	checkDB.beginTransaction();
	try {
	   SQLiteStatement insert = null;
	   insert = checkDB.compileStatement("INSERT OR REPLACE INTO \"topic\" (\"id\",\"updated\",\"deleted\",\"topic\",\"_id\") VALUES (?,?,?,?,?)");
	   //Parse the staring to json foramt 
		   
			 JSONObject json_data=null;
			Log.e("test insert topic",""+jArray.length());
			 for(int i=0;i<jArray.length();i++){
	 			json_data = jArray.getJSONObject(i);
				insert.bindLong(1, Integer.parseInt(json_data.getString("id")));//Integer.parseInt(json_data.getString("id"))
				insert.bindString(2, json_data.getString("updated"));
				insert.bindLong(3, Integer.parseInt(json_data.getString("deleted")));
		        insert.bindString(4, json_data.getString("topic"));
		        insert.bindLong(5, Integer.parseInt(json_data.getString("id")));//Integer.parseInt(json_data.getString("id"))
		        insert.execute();
		        insert.clearBindings();
		 	 }       
	    checkDB.setTransactionSuccessful();
		 return true;

	}
	catch (Exception e) {
	    String errMsg = (e.getMessage() == null) ? "bulkInsert failed" : e.getMessage();
	    Log.e("bulkInsert:", errMsg);
		 return false;

	}
	finally {
	    checkDB.endTransaction();
	  }
 }


public boolean insertperson(JSONArray jArray) {
	checkDB.beginTransaction();
	try {
	    SQLiteStatement insert = null;
	   insert = checkDB.compileStatement("INSERT OR REPLACE INTO \"person\" (\"id\",\"deleted\",\"updated\",\"firstname\",\"lastname\",\"fullname\",\"politicalaffiliation\",\"shortbio\",\"bio\",\"biosource\",\"biourl\",\"picturefilename\",\"_id\",\"race\") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
	   //Parse the staring to json foramt 
		 //  JSONArray jArray = new JSONArray(result);
			 JSONObject json_data=null;
			 Log.e("test insert person",""+jArray.length());
			 for(int i=0;i<jArray.length();i++){
				json_data = jArray.getJSONObject(i);
				insert.bindLong(1, Integer.parseInt(json_data.getString("id"))); //Integer.parseInt(json_data.getString("id"))
				insert.bindLong(2, Integer.parseInt(json_data.getString("deleted")));
				insert.bindString(3, json_data.getString("updated"));
				insert.bindString(4, json_data.getString("firstname"));
		        insert.bindString(5, json_data.getString("lastname"));
		        insert.bindString(6, json_data.getString("fullname"));
		        insert.bindString(7, json_data.getString("politicalaffiliation"));
		        insert.bindString(8, json_data.getString("shortbio"));
		        insert.bindString(9, json_data.getString("bio"));
		        insert.bindString(10, json_data.getString("biosource"));
		        insert.bindString(11, json_data.getString("biourl"));
		        insert.bindString(12, json_data.getString("picturefilename"));
		        insert.bindLong(13, Integer.parseInt(json_data.getString("id"))); //Integer.parseInt(json_data.getString("id"))
		        insert.bindString(14, json_data.getString("race"));
		        insert.execute();
		        insert.clearBindings();
		        			
				//HashMap<String, String> map = new HashMap<String, String>();
	        	//map.put("_id",  json_data.getString("_id"));
	        	//map.put("quote", json_data.getString("quote"));
	        	//map.put("bitly",json_data.getString("bitly"));
	        	//map.put("pubsource",json_data.getString("pubsource"));
	        	//map.put("pubdate",json_data.getString("pubdate"));
	        	//mylist.add(map);
				 }       
	    checkDB.setTransactionSuccessful();
	    return true;
	}
	catch (Exception e) {
	    String errMsg = (e.getMessage() == null) ? "bulkInsert failed" : e.getMessage();
	    Log.e("bulkInsert:", errMsg);
	    return false;
	}
	finally {
	    checkDB.endTransaction();
	  }
	 
 }

public boolean inserttopicquote(JSONArray jArray) {
	checkDB.beginTransaction();
	try {
	   SQLiteStatement insert = null;
	   insert = checkDB.compileStatement("INSERT OR REPLACE INTO \"topicquote\" (\"id\",\"deleted\",\"updated\",\"topicid\",\"quoteid\",\"_id\") VALUES (?,?,?,?,?,?)");
	   //Parse the staring to json format
		//JSONArray jArray = new JSONArray(result);
		JSONObject json_data=null;
		Log.e("test insert topicquote",""+jArray.length());
			 for(int i=0;i<jArray.length();i++){
	 			json_data = jArray.getJSONObject(i);
	 			insert.bindLong(1, Integer.parseInt(json_data.getString("id")));//Integer.parseInt(json_data.getString("id"))
	 			insert.bindLong(2, Integer.parseInt(json_data.getString("deleted")));
	 			insert.bindString(3, json_data.getString("updated"));
  		        insert.bindLong(4,Integer.parseInt(json_data.getString("topicid")));
		        insert.bindLong(5,Integer.parseInt(json_data.getString("quoteid")));
		        insert.bindLong(6, Integer.parseInt(json_data.getString("id")));
		        insert.execute();
		        insert.clearBindings();
		 	 }       
	    checkDB.setTransactionSuccessful();
	    return true;
	}
	catch (Exception e) {
	    String errMsg = (e.getMessage() == null) ? "bulkInsert failed" : e.getMessage();
	    Log.e("bulkInsert:", errMsg);
	    return false;
	}
	finally {
	    checkDB.endTransaction();
	  }
 }



public Cursor queryall(String DB_TABLE) {
	Cur = checkDB.rawQuery("Select * from "+DB_TABLE, null);
	return Cur;
	}

public Cursor query(String query) {
	try {
	Cur = checkDB.rawQuery(query, null);
    Log.e("sql query",""+Cur.getColumnCount());
	return Cur;}
	catch(Exception e) {Log.e("sql query",e.getMessage()); return null;}
	
}

@Override
public void onCreate(SQLiteDatabase db) {
	// TODO Auto-generated method stub
	
}

public int count(String tablename){
	String query = "select count(*) from "+tablename;
	try {
		Cur = checkDB.rawQuery(query, null);
		Cur.moveToFirst();
		Log.e("sql query",""+Cur.getInt(0));
		return Cur.getInt(0);}
		catch(Exception e) {Log.e("sql query",e.getMessage()); return 0;}
}

@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("TAG", "Upgrading database from version " + oldVersion 
                + " to "
                + newVersion + ", which will destroy all old data");
        if(oldVersion == 2 && newVersion == 3)
        {
            db.execSQL("ALTER TABLE xyz ADD bobby int default 0");    
        }
        else
        {
            db.execSQL("DROP TABLE IF EXISTS xyz");
            onCreate(db);
        }
    }
	
}

