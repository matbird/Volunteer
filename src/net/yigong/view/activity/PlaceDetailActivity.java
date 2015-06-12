package net.yigong.view.activity;

import net.yigong.R;
import net.yigong.bmob.bean.Place;
import net.yigong.utils.Options;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

@EActivity(R.layout.activity_place_detail)
public class PlaceDetailActivity extends BaseActivity {

	private Place place;

	@ViewById(R.id.top_head)
	protected ImageView top_head;
	@ViewById(R.id.top_more)
	protected ImageView top_more;
	@ViewById(R.id.title_recent)
	protected TextView title_recent;

	@ViewById(R.id.image)
	protected ImageView imageView;
	@ViewById(R.id.address)
	protected TextView addressView;
	@ViewById(R.id.descri)
	protected TextView descriView;
	@ViewById(R.id.remark)
	protected TextView remarkView;

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	protected DisplayImageOptions options;

	@AfterInject
	void init() {
		place = (Place) getIntent().getExtras().getSerializable("newModle");
		options = Options.getListOptions();
	}

	@AfterViews
	void initViews() {
		initTitleBar();
		initContent();
	}

	private void initTitleBar() {
		top_head.setBackgroundColor(Color.TRANSPARENT);
		top_head.setImageResource(R.drawable.mat_back);
		top_more.setVisibility(View.VISIBLE);
		top_more.setImageResource(R.drawable.ic_action_edit);
		title_recent.setText(place == null ? "活动点详情" : place.getName());
	}

	private void initContent() {
		if (place == null)
			return;
		addressView.setText(Html.fromHtml(place.getAddress() + ""));
		descriView.setText(Html.fromHtml(place.getDescri() + ""));
		remarkView.setText(Html.fromHtml(place.getRemark() + ""));
		imageLoader.displayImage(place.getImage().getFileUrl(this) == null ? ""
				: place.getImage().getFileUrl(this), imageView, options);
	}

	@Click(R.id.top_more)
	protected void addObject(View v) {
		Bundle bundle = new Bundle();
		bundle.putSerializable("newModle", place);
		Class<?> class1 = AddObjectActivity_.class;
		openActivity(class1, bundle, 0);
	}

	@Click(R.id.top_head)
	protected void back(View v) {
		finish();
	}

	@Click(R.id.look_all)
	protected void lookAllObject(View v) {
		openActivity(ObjectListActivity_.class);
	}
}
