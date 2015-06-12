package net.yigong.view.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import net.yigong.R;
import net.yigong.bmob.bean.Place;
import net.yigong.bmob.bean.ServiceObject;
import net.yigong.bmob.bean.YGUser;
import net.yigong.utils.LogUtils;
import net.yigong.utils.MatCacheUtils;
import net.yigong.utils.StringUtils;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.rengwuxian.materialedittext.MaterialEditText;

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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@EActivity(R.layout.activity_addobject)
public class AddObjectActivity extends BaseActivity{

	private static final int REQUEST_CODE_ALBUM = 1;
	private static final int REQUEST_CODE_CAMERA = 2;
	private static final String TAG = "test";
	
	String dateTime;
	String targeturl = null;
	private Place place;
	
	@ViewById(R.id.top_head)
	protected ImageView top_head;
	@ViewById(R.id.top_more)
	protected ImageView top_more;
	@ViewById(R.id.title_recent)
	protected TextView title_recent;
	
	@ViewById(R.id.met_name)
	protected MaterialEditText met_name;
	@ViewById(R.id.met_gender)
	protected MaterialEditText met_gender;
	@ViewById(R.id.met_birth)
	protected MaterialEditText met_birth;
	@ViewById(R.id.met_roominfo)
	protected MaterialEditText met_roominfo;
	@ViewById(R.id.met_healthinfo)
	protected MaterialEditText met_healthinfo;
	@ViewById(R.id.met_hobby)
	protected MaterialEditText met_hobby;
	@ViewById(R.id.met_unlike)
	protected MaterialEditText met_unlike;
	@ViewById(R.id.met_remark)
	protected MaterialEditText met_remark;
	
	@ViewById(R.id.take_layout)
	protected LinearLayout take_layout;
	@ViewById(R.id.open_layout)
	protected LinearLayout open_layout;
	@ViewById(R.id.take_pic)
	protected ImageView take_pic;
	@ViewById(R.id.open_pic)
	protected ImageView open_pic;
	
	
	@AfterInject
	void init(){
		place = (Place) getIntent().getExtras().getSerializable("newModle");
	}
	
	@AfterViews
	void initViews(){
		initTitleBar();
	}
	
	private void initTitleBar(){
		top_head.setBackgroundColor(Color.TRANSPARENT);
		top_head.setImageResource(R.drawable.mat_back);
		top_more.setVisibility(View.VISIBLE);
		top_more.setImageResource(R.drawable.ok);
		title_recent.setText("添加服务对象");
	}
	
	@Click(R.id.top_head)
	protected void onTopHead(View v){
		this.finish();
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
	
	@Click(R.id.top_more)
	protected void addObject(View v ){
		String name = met_name.getText().toString().trim();
		String gender = met_gender.getText().toString().trim();
		String birth = met_birth.getText().toString().trim();
		String roominfo = met_roominfo.getText().toString().trim();
		String healthinfo = met_healthinfo.getText().toString().trim();
		String hobby = met_hobby.getText().toString().trim();
		String unlike = met_unlike.getText().toString().trim();
		String remark = met_remark.getText().toString().trim();
		
		if(StringUtils.isEmpty(name)){
			showShortToast("名字不能为空.");
			return ;
		}
		if(StringUtils.isEmpty(roominfo)){
			showShortToast("位置不能为空");
			return ;
		}
		if(StringUtils.isEmpty(healthinfo)){
			showShortToast("健康状况不能为空");
			return ;
		}
		
		showProgressDialog();
		
		YGUser user = BmobUser.getCurrentUser(this, YGUser.class);
		if(user == null){
			showShortToast("您还没有登录");
			dismissProgressDialog();
			return ;
		}
		
		ServiceObject obj = new ServiceObject();
		obj.setAuthor(user);
		obj.setLastEditor(user);
		obj.setPlace(place);
		obj.setName(name);
		obj.setGender(gender+"");
		obj.setBirthday(birth+"");
		obj.setRoominfo(roominfo+"");
		obj.setHeathinfo(healthinfo+"");
		obj.setHobby(hobby+"");
		obj.setUnlike(unlike+"");
		obj.setRemark(remark+"");
		obj.setStatus(0);
		
		if (targeturl == null) {
//			publishWithoutFigure(obj, null);
			showShortToast("必须要选择一张照片哦");
			dismissProgressDialog();
			return ;
		} else {
			publish(obj);
		}
		
	}
	
	private void publish(final ServiceObject obj) {
		final BmobFile figureFile = new BmobFile(new File(targeturl));

		figureFile.upload(this, new UploadFileListener() { 

			@Override
			public void onSuccess() {
				LogUtils.i(TAG,"上传文件成功。" + figureFile.getFileUrl(AddObjectActivity.this));
				publishWithoutFigure(obj, figureFile);

			}
			@Override
			public void onProgress(Integer arg0) {

			}
			@Override
			public void onFailure(int arg0, String arg1) {
				LogUtils.i(TAG, "上传文件失败。" + arg1);
				showShortToast("图片上传失败.");
				dismissProgressDialog();
			}
		});

	}

	private void publishWithoutFigure(final ServiceObject obj,final BmobFile figureFile) {
		
		if(figureFile != null)
			obj.setImage(figureFile);
		obj.save(this, new SaveListener() {
			@Override
			public void onSuccess() {
				showShortToast("添加成功.");
				dismissProgressDialog();
				finish();
			}
			@Override
			public void onFailure(int arg0, String arg1) {
				showShortToast("添加失败.");
				dismissProgressDialog();
			}
		});
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
