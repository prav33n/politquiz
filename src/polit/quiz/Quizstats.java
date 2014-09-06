package polit.quiz;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class Quizstats extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.stats);
        if (customTitleSupported) {
            getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                    R.layout.quizheader);
        }
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        findViewById(R.id.imageButton1).setVisibility(View.GONE);
        Database db= new Database(getApplicationContext(),"politquiz.sqite");
        String query  ="select * from quizstats";
        Cursor cur = db.query(query);
        ListView list = (ListView)findViewById(R.id.scorelist);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.stats,cur, new String[] {"time","score"}, 
  	    		new int[] {R.id.time,R.id.score});
        list.setAdapter(adapter);
       
	}
}
