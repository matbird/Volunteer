package net.yigong.view.fragment;

import org.androidannotations.annotations.EFragment;

import net.yigong.R;
import net.yigong.wedget.viewimage.SliderTypes.BaseSliderView;
import net.yigong.wedget.viewimage.SliderTypes.BaseSliderView.OnSliderClickListener;
import android.support.v4.widget.SwipeRefreshLayout;

@EFragment(R.layout.fragment_main)
public class HomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,OnSliderClickListener{

	@Override
	public void onSliderClick(BaseSliderView slider) {
		
	}

	@Override
	public void onRefresh() {
		
	}

}
