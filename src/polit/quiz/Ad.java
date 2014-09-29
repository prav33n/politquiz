package polit.quiz;

import android.app.Activity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.util.Log;
import android.widget.RelativeLayout;

public class Ad extends Activity{
	private AdView mAdView;
	private String adUnitId = "ca-app-pub-4709554896359925/2516617293";
	public Ad(RelativeLayout ads, Activity act){
		 // Create an ad
		Log.e("ads",""+ads.getId());
		mAdView = new AdView(act);
        mAdView.setAdUnitId(adUnitId);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.loadAd(new AdRequest.Builder().build());
        // until the ad is loaded.
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ads.addView(mAdView, params);
        
	}
	
	@Override
    protected void onPause() {
        mAdView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdView.resume();
    }

    @Override
    protected void onDestroy() {
        mAdView.destroy();
        super.onDestroy();
    }

}
