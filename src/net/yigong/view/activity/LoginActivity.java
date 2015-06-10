package net.yigong.view.activity;

import net.yigong.R;
import net.yigong.bmob.bean.VoUser;
import net.yigong.utils.LogUtils;
import net.yigong.utils.Utils;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

import com.dd.processbutton.iml.ActionProcessButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
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
	
	@ViewById(R.id.met_username)
	protected MaterialEditText met_username;
	@ViewById(R.id.met_password)
	protected MaterialEditText met_password;
	
	@ViewById(R.id.btn_login)
	protected ActionProcessButton btn_login;
	
	@AfterViews
	void initViews(){
		initTitleBar();
		btn_login.setMode(ActionProcessButton.Mode.ENDLESS);
	}
	
	private void initTitleBar(){
		top_head.setVisibility(View.GONE);
		top_more.setVisibility(View.VISIBLE);
		title_recent.setText("登录");
	}
	
	@Click(R.id.btn_login)
	protected void onLogin(View v){
		String username = met_username.getText().toString();
		String password = met_password.getText().toString();
		
		if(TextUtils.isEmpty(username)){
			showCustomToast("用户名不能为空");
			return ;
		}
		if(TextUtils.isEmpty(password)){
			showCustomToast("密码不能为空");
			return ;
		}
		if(password.length() < met_password.getMinCharacters()){
			showCustomToast("密码长度不正确");
			return ;
		}
		
		String md5_password = Utils.encryptWithMD5(password);
		
		VoUser user = new VoUser();
		user.setUsername(username);
		user.setPassword(md5_password);
		
		selectStatus(false, 50);
		user.login(this, new SaveListener() {
			@Override
			public void onSuccess() {
				selectStatus(true, 100);
			}
			@Override
			public void onFailure(int arg0, String arg1) {
				selectStatus(true, -1);
				showCustomToast("登录失败:"+arg1);
			}
		});
	}
	
	private void selectStatus(boolean open ,int progress){
		met_username.setEnabled(open);
		met_password.setEnabled(open);
		btn_login.setEnabled(open);
		btn_login.setProgress(progress);
	}
	
	@Click(R.id.top_more)
	protected void onRegister(View v){
//		openActivity(RegisterActivity_.class);
		
		VoUser currentUser = BmobUser.getCurrentUser(this, VoUser.class);
		LogUtils.i("test", "userinfo:"+currentUser.getArea()+" verify:"+currentUser.getVerify());
	}
}
