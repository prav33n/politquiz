package polit.quiz;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class Copyfiles{
	
final String DB_NAME = "politquiz.sqlite";

Context con;
AssetManager assetManager;
public Copyfiles(Context con){
	final String DB_PATH = "/data/data/"+con.getApplicationContext().getPackageName()+"/databases/";
	try {
	InputStream myinput = con.getAssets().open(DB_NAME);
	Log.e("Database directory",DB_PATH+DB_NAME);
	File f = new File(DB_PATH+DB_NAME); 
	if(f.exists()) {Log.e("DB exists",""+con.getDatabasePath(DB_NAME));}
	else {
	File dir  = new File(DB_PATH);
	dir.mkdirs();
	OutputStream myoutput =  new FileOutputStream(DB_PATH+DB_NAME);
	byte[] buffer = new byte[1024];
	int length;
	while ((length = myinput.read(buffer)) > 0) {
	myoutput.write(buffer, 0, length);
	}
	// Close the streams
	myoutput.flush();
	myoutput.close();
	myinput.close();}
	Log.e("DBcopy","completed");}
	catch(IOException e) {Log.e("DBcopy","failed");}
}
}

