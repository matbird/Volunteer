package net.yigong.view.activity;

import net.yigong.R;
import net.yigong.bmob.bean.YGUser;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;

import android.content.Intent;
import android.os.SystemClock;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

@EActivity(R.layout.activity_splash)
public class SplashActivity extends BaseActivity {

	@AfterInject
	void init() {
		Bmob.initialize(this, "fdea5c917238d80f14207bcfbb1e940b");
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				SystemClock.sleep(3000);
				YGUser user = BmobUser.getCurrentUser(SplashActivity.this, YGUser.class);
				if (user == null) {
					openActivityForResult(LoginActivity_.class, 1000);
				} else {
					openActivity(MainActivity_.class);
					finish();
				}
			}
		}).start();
	}
	
	@OnActivityResult(1000)
    void onResult(int resultCode, Intent data){
    	if(resultCode == 1001){
    		openActivity(MainActivity_.class);
			finish();
    	}
    }
}
