package net.yigong.view.ui;

import net.yigong.R;
import net.yigong.bean.NoticeModle;
import net.yigong.utils.Options;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

@EViewGroup(R.layout.item_notice)
public class NoticeItemView extends LinearLayout{
	
	@ViewById(R.id.item_title)
	protected TextView vTitle;
	@ViewById(R.id.item_area)
	protected TextView vArea;
	@ViewById(R.id.item_manager)
	protected TextView vManager;
	@ViewById(R.id.item_status)
	protected TextView vStatus;
	@ViewById(R.id.item_time)
	protected TextView vTime;
	@ViewById(R.id.layout_view)
	protected RelativeLayout vView;
	@ViewById(R.id.item_image)
	protected ImageView vImage;

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	protected DisplayImageOptions options;
	private String[] areas;
	private int iWhite;
	private int iRed;
	private int iBlue;
	
	public NoticeItemView(Context context) {
		super(context);
		options = Options.getListOptions();
		areas = context.getResources().getStringArray(R.array.area_array);
		iWhite = context.getResources().getColor(android.R.color.white);
		iRed = context.getResources().getColor(android.R.color.holo_red_light);
		iBlue = context.getResources().getColor(android.R.color.holo_blue_light);
	}

	public void setNoticeItemView(NoticeModle notice){
		int iStatus = notice.getStatus();
		switch (iStatus) {
		case 1:
			vArea.setTextColor(iWhite);
			vStatus.setTextColor(iRed);
			vTime.setTextColor(iWhite);
			vManager.setTextColor(iWhite);
			vStatus.setText("招人中...");
			vTime.setVisibility(View.VISIBLE);
			vView.setBackgroundResource(R.drawable.notice_zhaomu);
			break;
		case 2:
			vArea.setTextColor(iWhite);
			vStatus.setTextColor(iWhite);
			vTime.setTextColor(iWhite);
			vManager.setTextColor(iWhite);
			vStatus.setText("活动中...");
			vTime.setVisibility(View.VISIBLE);
			vView.setBackgroundResource(R.drawable.notice_jinxing);
			break;
		case 3:
			vStatus.setText("活动结束");
			vTime.setVisibility(View.INVISIBLE);
			vView.setBackgroundResource(R.drawable.notice_success);
			break;
		}
		
		vTitle.setText(notice.getTitle());
		vArea.setText("活动地点:"+areas[notice.getPoint()]);
		vManager.setText("负责人:"+notice.getManager_info());
		vTime.setText("活动时间："+notice.getTime());
		imageLoader.displayImage(notice.getImage_url(), vImage,options);
	}
}
