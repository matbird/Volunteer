package net.yigong.view.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import net.yigong.R;
import net.yigong.utils.LogUtils;
import net.yigong.utils.MatCacheUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.rengwuxian.materialedittext.MaterialEditText;

@EActivity(R.layout.activity_addplace)
public class AddPlaceActivity extends BaseActivity{
	
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
	
	@ViewById(R.id.met_address)
	protected MaterialEditText met_address;
	@ViewById(R.id.met_title)
	protected MaterialEditText met_title;
	@ViewById(R.id.edit_descri)
	protected EditText edit_descri;
	@ViewById(R.id.edit_remark)
	protected EditText edit_remark;
	@ViewById(R.id.take_layout)
	protected LinearLayout take_layout;
	@ViewById(R.id.open_layout)
	protected LinearLayout open_layout;
	@ViewById(R.id.take_pic)
	protected ImageView take_pic;
	@ViewById(R.id.open_pic)
	protected ImageView open_pic;
	
	@AfterViews
	void initViews(){
		initTitleBar();
	}
	
	private void initTitleBar(){
		top_head.setBackgroundColor(Color.TRANSPARENT);
		top_head.setImageResource(R.drawable.mat_back);
		top_more.setVisibility(View.VISIBLE);
		top_more.setImageResource(R.drawable.ok);
		title_recent.setText("添加活动点");
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
	
	/*
	 * 发表带图片
	 */
	private void publish(final String commitContent) {

		// final BmobFile figureFile = new BmobFile(QiangYu.class, new
		// File(targeturl));

		final BmobFile figureFile = new BmobFile(new File(targeturl));

		figureFile.upload(this, new UploadFileListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				LogUtils.i(TAG,"上传文件成功。" + figureFile.getFileUrl(AddPlaceActivity.this));
				publishWithoutFigure(commitContent, figureFile);

			}
			@Override
			public void onProgress(Integer arg0) {

			}
			@Override
			public void onFailure(int arg0, String arg1) {
				LogUtils.i(TAG, "上传文件失败。" + arg1);
			}
		});

	}

	private void publishWithoutFigure(final String commitContent,
			final BmobFile figureFile) {
		/*User user = BmobUser.getCurrentUser(this, User.class);

		final QiangYu qiangYu = new QiangYu();
		qiangYu.setAuthor(user);
		qiangYu.setContent(commitContent);
		if (figureFile != null) {
			qiangYu.setContentfigureurl(figureFile);
		}
		qiangYu.setLove(0);
		qiangYu.setHate(0);
		qiangYu.setShare(0);
		qiangYu.setComment(0);
		qiangYu.setPass(true);
		qiangYu.save(mContext, new SaveListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				ActivityUtil.show(EditActivity.this, "发表成功！");
				LogUtils.i(TAG, "创建成功。");
				setResult(RESULT_OK);
				finish();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ActivityUtil.show(EditActivity.this, "发表失败！yg" + arg1);
				LogUtils.i(TAG, "创建失败。" + arg1);
			}
		});*/
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
