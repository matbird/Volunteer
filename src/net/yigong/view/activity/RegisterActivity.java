package net.yigong.view.activity;

import net.yigong.R;
import net.yigong.bmob.bean.VoUser;
import net.yigong.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.listener.SaveListener;

import com.dd.processbutton.iml.ActionProcessButton;
import com.rengwuxian.materialedittext.MaterialEditText;

@EActivity(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {

	@ViewById(R.id.top_head)
	protected ImageView top_head;
	@ViewById(R.id.top_more)
	protected ImageView top_more;
	@ViewById(R.id.title_recent)
	protected TextView title_recent;
	
	@ViewById(R.id.met_fm)
	protected MaterialEditText met_fm;
	@ViewById(R.id.met_nickname)
	protected MaterialEditText met_nickname;
	@ViewById(R.id.met_realname)
	protected MaterialEditText met_realname;
	@ViewById(R.id.met_password)
	protected MaterialEditText met_password;
	@ViewById(R.id.met_confirm)
	protected MaterialEditText met_confirm;
	@ViewById(R.id.met_qq)
	protected MaterialEditText met_qq;
	
	@ViewById(R.id.tv_hd)
	protected TextView tv_hd;
	@ViewById(R.id.tv_cp)
	protected TextView tv_cp;
	@ViewById(R.id.tv_cy)
	protected TextView tv_cy;
	@ViewById(R.id.tv_dx)
	protected TextView tv_dx;
	@ViewById(R.id.tv_ft)
	protected TextView tv_ft;
	@ViewById(R.id.tv_sjs)
	protected TextView tv_sjs;
	
	@ViewById(R.id.btn_register)
	protected ActionProcessButton btn_register;
	
	@AfterViews
	void initViews(){
		initTitleBar();
		btn_register.setMode(ActionProcessButton.Mode.ENDLESS);
	}
	
	private void initTitleBar(){
		top_head.setBackgroundColor(Color.TRANSPARENT);
		top_head.setImageResource(R.drawable.mat_back);
		title_recent.setText("注册");
	}
	
	@Click(R.id.btn_register)
	protected void onRegister(View v){
		String nickname = met_nickname.getText().toString();
		String realname = met_realname.getText().toString();
		String qq = met_qq.getText().toString();
		String fm = met_fm.getText().toString();
		String password = met_password.getText().toString();
		String confirm = met_confirm.getText().toString();
		if(TextUtils.isEmpty(nickname)){
			showCustomToast("用户名不能为空");
			return ;
		}
		if(TextUtils.isEmpty(realname)){
			showCustomToast("没有填写真实姓名");
			return ;
		}
		if(TextUtils.isEmpty(qq)){
			showCustomToast("QQ号不能为空");
			return ;
		}
		if(TextUtils.isEmpty(fm)){
			showCustomToast("请选择分盟");
			return ;
		}
		if(TextUtils.isEmpty(password)){
			showCustomToast("密码不能为空");
			return ;
		}
		if(TextUtils.isEmpty(confirm)){
			showCustomToast("确认密码不能为空");
			return ;
		}
		if(nickname.length() < met_nickname.getMinCharacters()){
			showCustomToast("昵称长度不正确");
			return ;
		}
		if(realname.length() < met_realname.getMinCharacters()){
			showCustomToast("真实姓名长度不正确");
			return ;
		}
		if(qq.length() < met_qq.getMinCharacters()){
			showCustomToast("QQ长度不正确");
			return ;
		}
		if(password.length() < met_password.getMinCharacters()){
			showCustomToast("密码长度不正确");
			return ;
		}
		if(!password.equals(confirm)){
			showCustomToast("两次输入的密码不一致");
			return ;
		}
		
		String md5_password = Utils.encryptWithMD5(password);
		
		VoUser user = new VoUser();
		user.setUsername(nickname);
		user.setPassword(md5_password);
		user.setRealname(realname);
		user.setQq(qq);
		user.setArea(fm);
		user.setDegree(0);
		user.setVerify(0);
		
		selectStatus(false, 50);
		user.signUp(this, new SaveListener(){
			@Override
			public void onFailure(int arg0, String arg1) {
				selectStatus(true, -1);
				if(arg0 == 202){
					showCustomToast("用户名已被注册.");
					met_nickname.setText("");
				}else{
					showCustomToast("注册失败:"+arg1);
				}
			}

			@Override
			public void onSuccess() {
				selectStatus(true, 100);
				showCustomToast("注册成功.");
				finish();
			}
		});
	}
	
	private void selectStatus(boolean open,int progress){
		met_nickname.setEnabled(open);
		met_realname.setEnabled(open);
		met_qq.setEnabled(open);
		met_fm.setEnabled(open);
		met_password.setEnabled(open);
		met_confirm.setEnabled(open);
		
		tv_hd.setEnabled(open);
		tv_cp.setEnabled(open);
		tv_cy.setEnabled(open);
		tv_dx.setEnabled(open);
		tv_ft.setEnabled(open);
		tv_sjs.setEnabled(open);
		
		btn_register.setEnabled(open);
		
		btn_register.setProgress(progress);
	}
	
	@Click(R.id.top_head)
	protected void onTopHead(View v){
		this.finish();
	}
	@Click(R.id.tv_hd)
	protected void onSelectHD(View v){
		met_fm.setText("海淀");
	}
	@Click(R.id.tv_cp)
	protected void onSelectCP(View v){
		met_fm.setText("昌平");
	}
	@Click(R.id.tv_cy)
	protected void onSelectCY(View v){
		met_fm.setText("朝阳");
	}
	@Click(R.id.tv_dx)
	protected void onSelectDX(View v){
		met_fm.setText("大兴");
	}
	@Click(R.id.tv_ft)
	protected void onSelectFT(View v){
		met_fm.setText("丰台");
	}
	@Click(R.id.tv_sjs)
	protected void onSelectSJS(View v){
		met_fm.setText("石景山");
	}
}
