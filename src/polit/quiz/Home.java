package polit.quiz;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.ads.*; 

public class Home extends Activity {
	private AdView adView;
	Database db;
	Cursor cur;
	String query, update;
	TextView tv,qno,qdetails;
	RadioGroup choice;
	RadioButton button, democrat, republican;
	int iqno = 1, i;
	Button previous,next;
	quiz[] storage = new quiz[10];
	Date answerdt = new Date(Calendar.getInstance().getTimeInMillis());
    @Override
    public void onCreate(Bundle savedInstanceState) {
      	super.onCreate(savedInstanceState);
        final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_home);
        if (customTitleSupported) {
            getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                    R.layout.quizheader);
        }
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        new Copyfiles(this.getApplicationContext());
        //sync code
        try {
		     Intent intent = new Intent(getApplicationContext(), Syncreceiver.class); 
			 intent.putExtra("syncmessage","database");
			 // use a static variable for the request code the same code should be used for canceling the broadcast. 
			 PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(),1, intent,PendingIntent.FLAG_UPDATE_CURRENT);
			 // Get the AlarmManager service
			 AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			// Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
			 Calendar cal = Calendar.getInstance();
			 am.set(AlarmManager.RTC,cal.getTimeInMillis(), sender);
			 }
        catch(Exception e) {Log.e("error",e.getMessage());} 
        
        // Create an ad.
        adView = new AdView(this, AdSize.BANNER, "a1501f0f491755e");
        // Add the AdView to the view hierarchy. The view will have no size
        // until the ad is loaded.
       RelativeLayout layout = (RelativeLayout)findViewById(R.id.ads);
        layout.addView(adView);
        adView.loadAd(new AdRequest());

        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device.
     /*   AdRequest adRequest = new AdRequest();
        adRequest.addTestDevice(AdRequest.TEST_EMULATOR);

        // Start loading the ad in the background.
        adView.loadAd(adRequest);*/
    
        db = new Database(getApplicationContext(),"politquiz.sqlite");
        db.checkDB.execSQL("update quiz set answer = 0, answered = 0, quoteid=0,choice = null");
        choice = (RadioGroup)findViewById(R.id.hanswer);
        previous = (Button)findViewById(R.id.previous);
    	next = (Button)findViewById(R.id.next);
    	democrat = (RadioButton)findViewById(R.id.democrat);
    	republican = (RadioButton)findViewById(R.id.republican);
    	Log.e("quotes count",""+db.count("quizquotes"));
       /* String qlist = generaterandom(db.count("quizquotes")).toString();
        query = "select * from quizquotes INNER JOIN person ON quizquotes.[personid] = person.[_id] where quizquotes.[id] in ("+qlist.substring(1, qlist.length()-1)+ ")";
        Log.e("query string",query);
        //query= "select _id,personid,quote from quote where _id in (" +qlist.substring(1, qlist.length()-1)+ ")";
        cur = db.query(query);
        startManagingCursor(cur);
        cur.moveToFirst();	
        tv = (TextView)findViewById(R.id.tquestiontext);
        qno = (TextView)findViewById(R.id.tquestionno);
        qdetails = (TextView)findViewById(R.id.tquestiondetails);
        tv.setText(cur.getString(cur.getColumnIndex("quotequiz")));
        qdetails.setText(" 1 of 10 ");
        choice.setOnCheckedChangeListener(CC);
        for(int i=0; i<cur.getCount();i++){
        	storage[i] =  new quiz();
        	storage[i].qid = i+1;
        	storage[i].quoteid = cur.getInt(0);
        	storage[i].choice = null;
        	storage[i].answer=0;
        	storage[i].answered = 0;
        	cur.moveToNext();
        }
        cur.moveToFirst();*/
}
   
    public RadioGroup.OnCheckedChangeListener CC = new RadioGroup.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(RadioGroup group, int checked) {
        // TODO Auto-generated method stub
        try{
        if(checked != -1){
        	RadioButton ch = (RadioButton) group.findViewById(checked);
        	storage[cur.getPosition()].choice = (String) ch.getText();
        	storage[cur.getPosition()].answered = 1;
        }
        Log.e("Choice stored",""+storage[cur.getPosition()].choice+"//"+cur.getPosition());}
        catch(Exception e){e.printStackTrace();}


   }
  };
     
    public void onclicknext(View v){
    	if(!cur.isLast()){
    		choice.clearCheck();
        	cur.moveToNext();
    		if(storage[cur.getPosition()].answered==1){
    			if(storage[cur.getPosition()].choice.equalsIgnoreCase("Republican"))
	   	    		choice.check(R.id.republican);
	    		else if(storage[cur.getPosition()].choice.equalsIgnoreCase("Democrat"))
	   	    		choice.check(R.id.democrat);
    		}
    	}
    	iqno = cur.getPosition() + 1;
        tv.setText(cur.getString(cur.getColumnIndex("quotequiz")));
        qdetails.setText(iqno+" of 10");
   
    }
     
    /**
     * @param v
     * 
     */
    public void onclickprevious(View v){  	
    	
    	if(!cur.isFirst()){
    		choice.clearCheck();
    		cur.moveToPrevious();
    		if(storage[cur.getPosition()].answered==1){
    			if(storage[cur.getPosition()].choice.equalsIgnoreCase("Republican"))
	   	    		choice.check(R.id.republican);
	    		else if(storage[cur.getPosition()].choice.equalsIgnoreCase("Democrat"))
	   	    		choice.check(R.id.democrat);
    		}	
    	}
    	iqno = cur.getPosition() + 1;
        tv.setText(cur.getString(cur.getColumnIndex("quotequiz")));
        qdetails.setText(iqno+" of 10");
    }
    
    public void onshare(View v){
    	Intent shareIntent = 
	    	 new Intent(android.content.Intent.ACTION_SEND);
	    	//set the type
	    	shareIntent.setType("text/plain");
       	//add a subject
	    	String shareMessage = "Check out http://bit.ly/R_or_D";
	      	shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"");
	    	//build the body of the message to be shared
	       	//add the message
	    	shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,shareMessage);
	    	Log.e("button selected",shareMessage);
	    	//start the chooser for sharing
	    	startActivity(Intent.createChooser(shareIntent, "Application Share"));
    }
    
    public void oncalculate(View v){
    	cur.moveToFirst();
    	for(int i= 0; i< cur.getCount();i++){
    		if(cur.getString(cur.getColumnIndex("politicalaffiliation")).equalsIgnoreCase(storage[i].choice))
    			storage[i].answer = 1;
    		update = "update quiz set choice ='"+storage[i].choice+"', answered = "+storage[i].answered+",time = (DATETIME('NOW')), answer ="+storage[i].answer+",quoteid ="+storage[i].quoteid+" where _id = "+storage[i].qid;
    		db.checkDB.execSQL(update);
      		Log.e("Array values",""+storage[i].qid+"//"+storage[i].quoteid+"//"+storage[i].choice+"//"+storage[i].answer+"//"+storage[i].answered+"//"+cur.getString(cur.getColumnIndex("politicalaffiliation")));
    		cur.moveToNext();
    	}
    	cur.close();
      startActivity(new Intent(getApplicationContext(), Score.class)); }
    
    public ArrayList<Integer> generaterandom(int max){
    	ArrayList<Integer> rand = new ArrayList<Integer>();
    	for(int i = 0; i<10; i++){
    		int min = 1;
    		Random r = new Random();
    		int ri = r.nextInt((max  + 1)- i) + i;
    		if(rand.contains(ri)){
    			Log.e("repeated",""+ri);
    		 ri = r.nextInt((max  + 1)- i) + i;}
    		rand.add(ri);
    	}
    	Log.e("random array",""+rand.toString());
    	return rand;
    }
    
    private class quiz{
    	int qid;
    	int quoteid;
    	String choice;
    	int answer;
    	int answered;
    }  
    
	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	     if ((keyCode == KeyEvent.KEYCODE_D)) {
	       choice.check(R.id.democrat);
	     }
	     else if((keyCode == KeyEvent.KEYCODE_E)){
	    	 choice.check(R.id.republican);
	     }
	     else if(keyCode == KeyEvent.KEYCODE_BACK){
	    	 Intent intent = new Intent(Intent.ACTION_MAIN);
	    	    intent.addCategory(Intent.CATEGORY_HOME);
	    	    startActivity(intent);
	     }
	     return super.onKeyDown(keyCode, event);
	 }
	 
		public void onClickInfo(View v) {
			Intent i = new Intent(getApplicationContext(), Webview.class);
		    i.putExtra("viewtype","local");
	        startActivity(i);  
					}
		
		@Override
		public void onResume() {
		    super.onResume();  // Always call the superclass method first
		   
		    do{
		    String qlist = generaterandom(db.count("quizquotes")).toString();
		    query = "select * from quizquotes INNER JOIN person ON quizquotes.[personid] = person.[_id] where quizquotes.[id] in ("+qlist.substring(1, qlist.length()-1)+ ") order by random()";
	       //query= "select _id,personid,quote from quote where _id in (" +qlist.substring(1, qlist.length()-1)+ ")";
	        cur = db.query(query);
	        Log.e("query string",query+"//"+cur.getCount()); }
		    while(cur.getCount()!= 10);
	        startManagingCursor(cur);
	        cur.moveToFirst();	
	        tv = (TextView)findViewById(R.id.tquestiontext);
	        qno = (TextView)findViewById(R.id.tquestionno);
	        qdetails = (TextView)findViewById(R.id.tquestiondetails);
	        tv.setText(cur.getString(cur.getColumnIndex("quotequiz")));
	        qdetails.setText("1 of 10");
	        choice.clearCheck();
	        choice.setOnCheckedChangeListener(CC);
	        for(int i=0; i<cur.getCount();i++){
	        	storage[i] =  new quiz();
	        	storage[i].qid = i+1;
	        	storage[i].quoteid = cur.getInt(0);
	        	storage[i].choice = null;
	        	storage[i].answer=0;
	        	storage[i].answered = 0;
	        	Log.e("quotes id",""+cur.getInt(0));
	        	cur.moveToNext();
	        }
	        cur.moveToFirst();
		}
		
		
		/** Called before the activity is destroyed. */
		  @Override
		  public void onDestroy() {
		    // Destroy the AdView.
		    if (adView != null) {
		      adView.destroy();
		    }

		    super.onDestroy();
		  }
    }



