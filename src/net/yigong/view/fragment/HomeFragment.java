package net.yigong.view.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.yigong.R;
import net.yigong.adapter.CardsAnimationAdapter;
import net.yigong.adapter.PlaceAdapter;
import net.yigong.bean.NewModle;
import net.yigong.bmob.bean.Place;
import net.yigong.view.activity.BaseActivity;
import net.yigong.view.activity.DetailsActivity_;
import net.yigong.view.activity.ImageDetailActivity_;
import net.yigong.view.activity.PlaceDetailActivity_;
import net.yigong.view.initview.InitView;
import net.yigong.wedget.swipelistview.SwipeListView;
import net.yigong.wedget.viewimage.Animations.DescriptionAnimation;
import net.yigong.wedget.viewimage.Animations.SliderLayout;
import net.yigong.wedget.viewimage.SliderTypes.BaseSliderView;
import net.yigong.wedget.viewimage.SliderTypes.BaseSliderView.OnSliderClickListener;
import net.yigong.wedget.viewimage.SliderTypes.TextSliderView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;

@EFragment(R.layout.fragment_main)
public class HomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,OnSliderClickListener{

	protected SliderLayout mDemoSlider;
	@ViewById(R.id.swipe_container)
    protected SwipeRefreshLayout swipeLayout;
    @ViewById(R.id.listview)
    protected SwipeListView mListView;
    @ViewById(R.id.progressBar)
    protected ProgressBar mProgressBar;
    @Bean
    protected PlaceAdapter placeAdapter;
    protected List<Place> listsModles;
    
    protected HashMap<String, String> url_maps;

    protected HashMap<String, Place> newHashMap;
    private int index = 0;
    private boolean isRefresh = false;
    @Override
    public void onAttach(Activity activity) {
    	super.onAttach(activity);
    }
    
    @AfterInject
    protected void init(){
    	listsModles = new ArrayList<Place>();
    	url_maps = new HashMap<String, String>();
    	newHashMap = new HashMap<String, Place>();
    }
    
    @AfterViews
    protected void initViews(){
    	swipeLayout.setOnRefreshListener(this);
    	InitView.instance().initSwipeRefreshLayout(swipeLayout);
    	InitView.instance().initListView(mListView, getActivity());
    	View headView = LayoutInflater.from(getActivity()).inflate(R.layout.head_item, null);
    	 mDemoSlider = (SliderLayout) headView.findViewById(R.id.slider);
         mListView.addHeaderView(headView);
    	 AnimationAdapter animationAdapter = new CardsAnimationAdapter(placeAdapter);
         animationAdapter.setAbsListView(mListView);
         mListView.setAdapter(animationAdapter);
         
         loadData(index);

         mListView.setOnBottomListener(new OnClickListener() {
             @Override
             public void onClick(View v) {
            	 if(index%10 == 0){
            		 currentPagte++;
            		 index = index + 10;
            		 loadData(index);
            	 }
             }
         });
    }
    
    @Background
    void loadNewList(int record) {
    	BmobQuery<Place> query = new BmobQuery<Place>();
    	query.setLimit(10);
    	query.order("createdAt");
    	query.setSkip(record);
    	query.findObjects(getActivity(), new FindListener<Place>() {
			@Override
			public void onError(int arg0, String arg1) {
				((BaseActivity)getActivity()).showCustomToast("失败："+arg0+arg1);
			}
			@Override
			public void onSuccess(List<Place> list) {
				if(list != null && list.size()>0){
					getResult(list);
				}else{
					swipeLayout.setRefreshing(false);
				}
			}
		});
    }
    
    private void loadData(int record) {
        if (getMyActivity().hasNetWork()) {
            loadNewList(record);
        } else {
            mListView.onBottomComplete();
            mProgressBar.setVisibility(View.GONE);
            getMyActivity().showShortToast(getString(R.string.not_network));
        }
    }
    
    @UiThread
    public void getResult(List<Place> list) {
        if (isRefresh) {
            isRefresh = false;
            placeAdapter.clear();
            listsModles.clear();
        }
        mProgressBar.setVisibility(View.GONE);
        swipeLayout.setRefreshing(false);
        if (index == 0 && list.size() >= 4) {
            initSliderLayout(list);
        } else {
            placeAdapter.appendList(list);
        }
        listsModles.addAll(list);
        mListView.onBottomComplete();
    }
    
    private void initSliderLayout(List<Place> newModles) {

        if (!isNullString(newModles.get(0).getImage().getFileUrl(getActivity())))
            newHashMap.put(newModles.get(0).getImage().getFileUrl(getActivity()), newModles.get(0));
        if (!isNullString(newModles.get(1).getImage().getFileUrl(getActivity())))
            newHashMap.put(newModles.get(1).getImage().getFileUrl(getActivity()), newModles.get(1));
        if (!isNullString(newModles.get(2).getImage().getFileUrl(getActivity())))
            newHashMap.put(newModles.get(2).getImage().getFileUrl(getActivity()), newModles.get(2));
        if (!isNullString(newModles.get(3).getImage().getFileUrl(getActivity())))
            newHashMap.put(newModles.get(3).getImage().getFileUrl(getActivity()), newModles.get(3));

        if (!isNullString(newModles.get(0).getImage().getFileUrl(getActivity())))
            url_maps.put(newModles.get(0).getName(), newModles.get(0).getImage().getFileUrl(getActivity()));
        if (!isNullString(newModles.get(1).getImage().getFileUrl(getActivity())))
            url_maps.put(newModles.get(1).getName(), newModles.get(1).getImage().getFileUrl(getActivity()));
        if (!isNullString(newModles.get(2).getImage().getFileUrl(getActivity())))
            url_maps.put(newModles.get(2).getName(), newModles.get(2).getImage().getFileUrl(getActivity()));
        if (!isNullString(newModles.get(3).getImage().getFileUrl(getActivity())))
            url_maps.put(newModles.get(3).getName(), newModles.get(3).getImage().getFileUrl(getActivity()));

        for (String name : url_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(getActivity());
            textSliderView.setOnSliderClickListener(this);
            textSliderView
                    .description(name)
                    .image(url_maps.get(name));

            textSliderView.getBundle()
                    .putString("extra", name);
            mDemoSlider.addSlider(textSliderView);
        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        placeAdapter.appendList(newModles);
    }
    
	@Override
	public void onSliderClick(BaseSliderView slider) {
		
	}

	@Override
	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                currentPagte = 1;
                isRefresh = true;
                loadData(0);
                url_maps.clear();
                mDemoSlider.removeAllSliders();
            }
        }, 2000);
	}

	@ItemClick(R.id.listview)
	protected void onItemClick(int position){
		Place place = listsModles.get(position-1);
		enterPlaceDetailActivity(place);
	}
	
	public void enterPlaceDetailActivity(Place newModle) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("newModle", newModle);
        Class<?> class1 = PlaceDetailActivity_.class;
        ((BaseActivity) getActivity()).openActivity(class1, bundle, 0);
    }
}
