package polit.quiz;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.google.ads.*; 

public class Score extends Activity{
	private AdView adView;
	Database db;
	Cursor cur;
	String query;
	TextView score;
	int qscore, answered;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.score);
        
        if (customTitleSupported) {
            getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                    R.layout.quizheader);
        }
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        findViewById(R.id.imageButton1).setVisibility(View.GONE);
        db= new Database(getApplicationContext(),"politquiz.sqite");
        query  ="select count(answer) from quiz where answer = 1";
        cur = db.query(query);
        cur.moveToFirst();
        int answer = cur.getInt(0);
        query ="select count(answered) from quiz where answered  = 1";
        cur = db.query(query);
        cur.moveToFirst();
        answered = cur.getInt(0);
        qscore = answer;
        
        query = "select * from quizstats";
        cur = db.query(query);
        int id = cur.getCount()+1;
        String stat = answer+" out of "+answered;
        query = "INSERT INTO quizstats VALUES ("+id+",'"+stat+"',DATETIME('NOW'))";
        db.checkDB.execSQL(query);
        
        
        
        ListView list = (ListView)findViewById(R.id.qlist);
        
        // Create an ad.
        adView = new AdView(this, AdSize.BANNER, "a1501f0f491755e");
        // Add the AdView to the view hierarchy. The view will have no size
        // until the ad is loaded.
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.ads);
        layout.addView(adView);
        adView.loadAd(new AdRequest());
        
        //list header and footer
       // LayoutInflater inflater = this.getLayoutInflater();
        //RelativeLayout listFooterView = (RelativeLayout)inflater.inflate(R.layout.scorefooter, null);
        //list.addFooterView(listFooterView);
        //RelativeLayout listHeaderView = (RelativeLayout)inflater.inflate(R.layout.scoreheader, null);
        //list.addHeaderView(listHeaderView);
        //-score = (TextView)listHeaderView.findViewById(R.id.sscore);
        score = (TextView)findViewById(R.id.sscore);
        score.setText("Your Score : " +answer+ " out of " + answered);       
        query = "select  * from quiz inner join quizquotes on quiz.[quoteid] = quizquotes.[id] inner join person on quizquotes.[personid] = person.[id] where quiz.[answered]=1";
        cur = db.query(query);
        startManagingCursor(cur);
        Log.e("query string",""+query+"//"+cur.getCount());
    //    LayoutInflater inflater = getLayoutInflater();
    //    ViewGroup header = (ViewGroup)inflater.inflate(R.layout.scoreheader, list, false);
    //    list.addHeaderView(header);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.score,cur, new String[] {"pubdate","quotequiz","pubsource","bitly","_id","firstname","politicalaffiliation","answer","redundant","meaningless","meaningless","updated"}, 
  	    		new int[] {R.id.qpubdate,R.id.qquote,R.id.qsource,R.id.qshare,R.id.qview,R.id.qauthor,R.id.qaffiliation,R.id.qanswer,R.id.sfooter,R.id.sheader,R.id.ads});
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
        	
	  	    	   public boolean setViewValue(View view, Cursor cur, int columnIndex) {
	  	    		
	  	    		   	if(columnIndex == cur.getColumnIndex("pubdate"))
		  	    		{
		  	    			TextView tv = (TextView)view;
		  	    			try {
								Date date = new SimpleDateFormat("yyyy-MM-dd").parse(cur.getString(columnIndex));
								SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");  
							    String dt = df.format(date.getTime());
								tv.setText(dt);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								tv.setText(cur.getString(columnIndex));
							}
		  	    			
		  	    		}
		  	    		else if(columnIndex == cur.getColumnIndex("quotequiz"))
		  	    		{
		  	    			TextView tv = (TextView)view;
		  	    			tv.setText(cur.getString(columnIndex));
		  	    		}
		  	    		else if(columnIndex == cur.getColumnIndex("pubsource"))
		  	    		{
		  	    			TextView tv = (TextView)view;
		  	    			tv.setText("Source: "+cur.getString(columnIndex));
		  	    		}
		  	    		else if(columnIndex == cur.getColumnIndex("pubtitle"))
		  	    		{
		  	    			TextView tv = (TextView)view;
		  	    			tv.setText(cur.getString(columnIndex));
		  	    		}
		  	    		else if(columnIndex == cur.getColumnIndex("bitly"))
		  	    		{
		  	    			Button button = (Button)view;
		  	    			button.setTag(cur.getString(columnIndex));
		  	    		}
		  	    		else if(columnIndex == cur.getColumnIndex("_id"))
		  	    		{
		  	    			Button button = (Button)view;
		  	    			button.setTag(cur.getString(cur.getColumnIndex("bitly")));
		  	    		}
		  	    		else if(columnIndex == cur.getColumnIndex("firstname")){
		  	    			TextView tv = (TextView)view;
		  	    			tv.setText("Said By : "+cur.getString(columnIndex)+" "+cur.getString(cur.getColumnIndex("lastname")));
		  	    		}
		  	    		else if(columnIndex == cur.getColumnIndex("politicalaffiliation")){
		  	    			TextView tv = (TextView)view;
		  	    			tv.setText("Affiliation : "+cur.getString(columnIndex));
		  	    		}
		  	    		else if(columnIndex == cur.getColumnIndex("answer")){
		  	    			CheckBox ch = (CheckBox)view;
		  	    			ch.setFocusable(false);
		  	    			//TextView tv = (TextView)view;
		  	    			if(cur.getInt(columnIndex)== 0)
		  	    				ch.setChecked(false);
		  	    			else
		  	    				ch.setChecked(true);
		  	    			ch.setClickable(false);
		  	    		}
	  	    		   else if(columnIndex == cur.getColumnIndex("redundant") ||columnIndex == cur.getColumnIndex("meaningless")||columnIndex == cur.getColumnIndex("updated")){
		  	    			RelativeLayout rl = (RelativeLayout)view;
		  	    			 rl.setVisibility(View.GONE);
		  	    		}
	  	    		   	
	  	    	 	return true;
	  	    	    }
  	    	});
  	      list.setAdapter(adapter);
		  list.setFocusable(false);	
          findViewById(R.id.quotesdisplay).setVisibility(View.GONE);
 	}
	
	  public void sharelink(View v) {
	    	//startActivity (new Intent(con, Display.class));
	    	Button button = (Button)v.findViewById(R.id.qshare);
	    	String candidatename = "xyz";
	    	String url = button.getTag().toString();	    	
	    	//create the send intent
	    	Intent shareIntent = 
	    	 new Intent(android.content.Intent.ACTION_SEND);
	    	//set the type
	    	shareIntent.setType("text/plain");
       	//add a subject
	    	 //      shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "<Politician> said this about <topic>");
	    	      //build the body of the message to be shared
//	    	      String shareMessage = url;
	    	//      String shareMessage = "Check out <articleUrl> about <Politican> on <Topic>. Got it from <bitly>";
	    	
	    	String subject = "";
	    	String shareMessage = "";
	    	subject = "Quote by "+candidatename;
	    	shareMessage = "Check out "+url+". Got it from http://bit.ly/R_or_D";    	
	    	shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"");
	    	//build the body of the message to be shared
	    	
	      	//add the message
	    	shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, 
	    	 shareMessage);
	    	Log.e("button selected",shareMessage+"//"+subject);
	    	//start the chooser for sharing
	    	startActivity(Intent.createChooser(shareIntent, 
	    	 "Quotes Share"));
/*	    	
	    	 Intent share = new Intent(Intent.ACTION_SEND_MULTIPLE);
	    	    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); 
	    	    share.setType("text/plain");
	    	    share.putExtra(Intent.EXTRA_TITLE,"Topic & person name");
	    	    share.putExtra(Intent.EXTRA_SUBJECT,"quote");
	    	    share.putExtra(Intent.EXTRA_TEXT,url);
	    	    startActivity(Intent.createChooser(share, "Share Quotes")); */
	     	}
	    
	  public void sharescore(View v){
		  Intent shareIntent = 
		    	 new Intent(android.content.Intent.ACTION_SEND);
		    	shareIntent.setType("text/plain");
		    	String shareMessage = "I just got "+qscore+" out of "+answered+" in http://bit.ly/R_or_D" ;
		      	shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"");
		    	shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,shareMessage);
		    	Log.e("button selected",shareMessage);
		    	startActivity(Intent.createChooser(shareIntent, "Application Share"));
	  }
	  
	    public void viewmore(View v) {
	    	//startActivity (new Intent(con, Display.class));
	    	Button button = (Button)v.findViewById(R.id.qview);
			String url = button.getTag().toString();
			Uri puri = Uri.parse(url);   
			Intent launchBrowser = new Intent(Intent.ACTION_VIEW, puri);   
		//	  Log.e("button selected",""+url);
		//   Intent i = new Intent(getApplicationContext(), Webview.class);
		 //  i.putExtra("url",url);
		 //  i.putExtra("viewtype","internet");
		   startActivity(launchBrowser);
	      	}
	    
	    public void startnew(View v){
	    	startActivity(new Intent(getApplicationContext(),Home.class));
		  }
	    public void showstats(View v){
	    	startActivity(new Intent(getApplicationContext(),Quizstats.class));
		  }
	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	     if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	       startActivity(new Intent(getApplicationContext(),Home.class));
	      }
	     return super.onKeyDown(keyCode, event);
	 }
}
