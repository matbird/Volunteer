package net.yigong.view.activity;

import net.yigong.R;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity{

	@ViewById(R.id.top_head)
	protected ImageView top_head;
	@ViewById(R.id.top_more)
	protected ImageView top_more;
	@ViewById(R.id.title_recent)
	protected TextView title_recent;
	
	@AfterViews
	void initViews(){
		top_head.setVisibility(View.GONE);
		top_more.setVisibility(View.VISIBLE);
		title_recent.setText("登录");
	}
	
	@Override
	public boolean isSupportSlide() {
		return false;
	}
	
	@Click(R.id.top_more)
	protected void onRegister(View v){
		openActivity(RegisterActivity_.class);
	}
}
