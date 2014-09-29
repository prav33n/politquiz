package polit.quiz;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.webkit.WebView;

public class Webview extends Activity {
	WebView webview;
	final Activity activity = this;
	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	     setContentView(R.layout.webview);
		 webview = (WebView)findViewById(R.id.webview);
		 webview.getSettings().setJavaScriptEnabled(true);
		 //webview.getSettings().setPluginsEnabled(true);
		 Bundle bundle = getIntent().getExtras() ;
		 String url = bundle.getString("url");
		 String viewtype = bundle.getString("viewtype");
		 if(viewtype.equals("local"))
			 webview.loadUrl("file:///android_asset/termsconditions.html"); 
		 else
			 webview.loadUrl(url);
		
	 }
	 
}
