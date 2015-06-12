package net.yigong.view.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import net.yigong.R;
import net.yigong.bmob.bean.ServiceObject;
import net.yigong.bmob.bean.YGUser;
import net.yigong.utils.LogUtils;
import net.yigong.utils.MatCacheUtils;
import net.yigong.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.dd.processbutton.iml.ActionProcessButton;
import com.rengwuxian.materialedittext.MaterialEditText;

@EActivity(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {
	
	private static final int REQUEST_CODE_ALBUM = 1;
	private static final int REQUEST_CODE_CAMERA = 2;
	private static final String TAG = "test";
	
	String dateTime;
	String targeturl = null;

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
	
	@ViewById(R.id.take_layout)
	protected LinearLayout take_layout;
	@ViewById(R.id.open_layout)
	protected LinearLayout open_layout;
	@ViewById(R.id.take_pic)
	protected ImageView take_pic;
	@ViewById(R.id.open_pic)
	protected ImageView open_pic;
	
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
		
		YGUser user = new YGUser();
		user.setUsername(nickname);
		user.setPassword(md5_password);
		user.setRealname(realname);
		user.setQq(qq);
		user.setArea(fm);
		user.setDegree(0);
		user.setVerify(0);
		
		selectStatus(false, 50);
		
		if (targeturl == null) {
			publishWithoutFigure(user, null);
		} else {
			publish(user);
		}
	}
	
	private void publish(final YGUser obj) {
		final BmobFile figureFile = new BmobFile(new File(targeturl));

		figureFile.upload(this, new UploadFileListener() { 

			@Override
			public void onSuccess() {
				LogUtils.i(TAG,"上传文件成功。" + figureFile.getFileUrl(RegisterActivity.this));
				publishWithoutFigure(obj, figureFile);
			}
			@Override
			public void onProgress(Integer arg0) {

			}
			@Override
			public void onFailure(int arg0, String arg1) {
				LogUtils.i(TAG, "上传文件失败。" + arg1);
				showShortToast("图片上传失败.");
				selectStatus(true, -1);
			}
		});

	}

	private void publishWithoutFigure(final YGUser obj,final BmobFile figureFile) {
		
		if(figureFile != null)
			obj.setPhoto(figureFile);
		obj.signUp(this, new SaveListener(){
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
				showShortToast("注册成功.");
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
	
	@Click(R.id.open_layout)
	protected void onOpenLayout(View v){
		Date date1 = new Date(System.currentTimeMillis());
		dateTime = date1.getTime() + "";
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI,
				"image/*");
		startActivityForResult(intent, REQUEST_CODE_ALBUM);
	}
	
	@Click(R.id.take_layout)
	protected void onTakeLayout(View v){
		Date date = new Date(System.currentTimeMillis());
		dateTime = date.getTime() + "";
		File f = new File(MatCacheUtils.getCacheDirectory(this, true,"pic") + dateTime);
		if (f.exists()) {
			f.delete();
		}
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Uri uri = Uri.fromFile(f);
		Log.e("uri", uri + "");

		Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		camera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(camera, REQUEST_CODE_CAMERA);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogUtils.i(TAG, "get album:");
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_CODE_ALBUM:
				String fileName = null;
				if (data != null) {
					Uri originalUri = data.getData();
					ContentResolver cr = getContentResolver();
					Cursor cursor = cr.query(originalUri, null, null, null,
							null);
					if (cursor.moveToFirst()) {
						do {
							fileName = cursor.getString(cursor
									.getColumnIndex("_data"));
							LogUtils.i(TAG, "get album:" + fileName);
						} while (cursor.moveToNext());
					}
					Bitmap bitmap = compressImageFromFile(fileName);
					targeturl = saveToSdCard(bitmap);
					open_pic.setBackgroundDrawable(new BitmapDrawable(bitmap));
					take_layout.setVisibility(View.GONE);
				}
				break;
			case REQUEST_CODE_CAMERA:
				String files = MatCacheUtils.getCacheDirectory(this, true,
						"pic") + dateTime;
				File file = new File(files);
				if (file.exists()) {
					Bitmap bitmap = compressImageFromFile(files);
					targeturl = saveToSdCard(bitmap);
					take_pic.setBackgroundDrawable(new BitmapDrawable(bitmap));
					open_layout.setVisibility(View.GONE);
				} else {

				}
				break;
			default:
				break;
			}
		}
	}
	
	private Bitmap compressImageFromFile(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;// 只读边,不读内容
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 800f;//
		float ww = 480f;//
		int be = 1;
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置采样率

		newOpts.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
		newOpts.inPurgeable = true;// 同时设置才会有效
		newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		// return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
		// 其实是无效的,大家尽管尝试
		return bitmap;
	}

	public String saveToSdCard(Bitmap bitmap) {
		String files = MatCacheUtils.getCacheDirectory(this, true, "pic")
				+ dateTime + "_11.jpg";
		File file = new File(files);
		try {
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LogUtils.i(TAG, file.getAbsolutePath());
		return file.getAbsolutePath();
	}
}
