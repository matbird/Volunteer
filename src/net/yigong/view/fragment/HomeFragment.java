package net.yigong.view.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;

import net.yigong.R;
import net.yigong.adapter.CardsAnimationAdapter;
import net.yigong.adapter.NoticeAdapter;
import net.yigong.bean.NewModle;
import net.yigong.bean.NoticeModle;
import net.yigong.http.HttpUtil;
import net.yigong.http.Url;
import net.yigong.http.json.NewListJson;
import net.yigong.utils.StringUtils;
import net.yigong.view.activity.BaseActivity;
import net.yigong.view.initview.InitView;
import net.yigong.wedget.swipelistview.SwipeListView;
import net.yigong.wedget.viewimage.Animations.DescriptionAnimation;
import net.yigong.wedget.viewimage.Animations.SliderLayout;
import net.yigong.wedget.viewimage.SliderTypes.BaseSliderView;
import net.yigong.wedget.viewimage.SliderTypes.TextSliderView;
import net.yigong.wedget.viewimage.SliderTypes.BaseSliderView.OnSliderClickListener;
import android.app.Activity;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;

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
    protected NoticeAdapter noticeAdapter;
    protected List<NoticeModle> listsModles;
    
    protected HashMap<String, String> url_maps;

    protected HashMap<String, NoticeModle> newHashMap;
    private int index = 0;
    private boolean isRefresh = false;
    @Override
    public void onAttach(Activity activity) {
    	super.onAttach(activity);
    }
    
    @AfterInject
    protected void init(){
    	listsModles = new ArrayList<NoticeModle>();
    	url_maps = new HashMap<String, String>();
    	newHashMap = new HashMap<String, NoticeModle>();
    }
    
    @AfterViews
    protected void initViews(){
    	swipeLayout.setOnRefreshListener(this);
    	InitView.instance().initSwipeRefreshLayout(swipeLayout);
    	InitView.instance().initListView(mListView, getActivity());
    	View headView = LayoutInflater.from(getActivity()).inflate(R.layout.head_item, null);
    	 mDemoSlider = (SliderLayout) headView.findViewById(R.id.slider);
         mListView.addHeaderView(headView);
    	 AnimationAdapter animationAdapter = new CardsAnimationAdapter(noticeAdapter);
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
        /*String result;
        try {
            result = HttpUtil.getByHttpClient(getActivity(), url);
            if (!StringUtils.isEmpty(result)) {
                getResult(result);
            } else {
                swipeLayout.setRefreshing(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    	
    	BmobQuery<NoticeModle> query = new BmobQuery<NoticeModle>();
    	query.order("-createdAt");
    	query.setLimit(10);
    	query.setSkip(record);
    	query.findObjects(getActivity(), new FindListener<NoticeModle>() {
			@Override
			public void onSuccess(List<NoticeModle> list) {
//				((BaseActivity)getActivity()).showCustomToast("成功:"+list.get(0).getTitle());
				if(list != null && list.size()>0){
					getResult(list);
				}else{
					swipeLayout.setRefreshing(false);
				}
			}
			@Override
			public void onError(int arg0, String arg1) {
				((BaseActivity)getActivity()).showCustomToast("失败："+arg0+arg1);
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
            /*String result = getMyActivity().getCacheStr("NewsFragment" + currentPagte);
            if (!StringUtils.isEmpty(result)) {
                getResult(result);
            }*/
        }
    }
    
    @UiThread
    public void getResult(List<NoticeModle> list) {
//        getMyActivity().setCacheStr("NewsFragment" + currentPagte, result);
        if (isRefresh) {
            isRefresh = false;
            noticeAdapter.clear();
            listsModles.clear();
        }
        mProgressBar.setVisibility(View.GONE);
        swipeLayout.setRefreshing(false);
//        List<NewModle> list = NewListJson.instance(getActivity()).readJsonNewModles(result,Url.TopId);
//        List<NoticeModle> list = new ArrayList<NoticeModle>();
        if (index == 0 && list.size() >= 4) {
            initSliderLayout(list);
        } else {
            noticeAdapter.appendList(list);
        }
        listsModles.addAll(list);
        mListView.onBottomComplete();
    }
    
    private void initSliderLayout(List<NoticeModle> newModles) {

        if (!isNullString(newModles.get(0).getImage_url()))
            newHashMap.put(newModles.get(0).getImage_url(), newModles.get(0));
        if (!isNullString(newModles.get(1).getImage_url()))
            newHashMap.put(newModles.get(1).getImage_url(), newModles.get(1));
        if (!isNullString(newModles.get(2).getImage_url()))
            newHashMap.put(newModles.get(2).getImage_url(), newModles.get(2));
        if (!isNullString(newModles.get(3).getImage_url()))
            newHashMap.put(newModles.get(3).getImage_url(), newModles.get(3));

        if (!isNullString(newModles.get(0).getImage_url()))
            url_maps.put(newModles.get(0).getTitle(), newModles.get(0).getImage_url());
        if (!isNullString(newModles.get(1).getImage_url()))
            url_maps.put(newModles.get(1).getTitle(), newModles.get(1).getImage_url());
        if (!isNullString(newModles.get(2).getImage_url()))
            url_maps.put(newModles.get(2).getTitle(), newModles.get(2).getImage_url());
        if (!isNullString(newModles.get(3).getImage_url()))
            url_maps.put(newModles.get(3).getTitle(), newModles.get(3).getImage_url());

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
        noticeAdapter.appendList(newModles);
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

}
