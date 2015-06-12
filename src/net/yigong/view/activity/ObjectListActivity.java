package net.yigong.view.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.yigong.R;
import net.yigong.adapter.CardsAnimationAdapter;
import net.yigong.adapter.ObjectAdapter;
import net.yigong.adapter.PlaceAdapter;
import net.yigong.bmob.bean.Place;
import net.yigong.bmob.bean.ServiceObject;
import net.yigong.utils.StringUtils;
import net.yigong.view.initview.InitView;
import net.yigong.wedget.swipelistview.SwipeListView;
import net.yigong.wedget.viewimage.Animations.DescriptionAnimation;
import net.yigong.wedget.viewimage.Animations.SliderLayout;
import net.yigong.wedget.viewimage.SliderTypes.BaseSliderView;
import net.yigong.wedget.viewimage.SliderTypes.TextSliderView;
import net.yigong.wedget.viewimage.SliderTypes.BaseSliderView.OnSliderClickListener;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;

import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

@EActivity(R.layout.activity_object_list)
public class ObjectListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,OnSliderClickListener{

	@ViewById(R.id.top_head)
	protected ImageView top_head;
	@ViewById(R.id.top_more)
	protected ImageView top_more;
	@ViewById(R.id.title_recent)
	protected TextView title_recent;
	
	protected SliderLayout mDemoSlider;
	@ViewById(R.id.swipe_container)
    protected SwipeRefreshLayout swipeLayout;
    @ViewById(R.id.listview)
    protected SwipeListView mListView;
    @ViewById(R.id.progressBar)
    protected ProgressBar mProgressBar;
    @Bean
    protected ObjectAdapter objectAdapter;
    protected List<ServiceObject> listsModles;
    
    protected HashMap<String, String> url_maps;

    protected HashMap<String, ServiceObject> newHashMap;
    private int index = 0;
    private boolean isRefresh = false;
    
    /**
     * 当前页
     */
    public int currentPagte = 1;
    
    @AfterInject
    protected void init(){
    	listsModles = new ArrayList<ServiceObject>();
    	url_maps = new HashMap<String, String>();
    	newHashMap = new HashMap<String, ServiceObject>();
    }
    
    @AfterViews
    protected void initViews(){
    	swipeLayout.setOnRefreshListener(this);
    	InitView.instance().initSwipeRefreshLayout(swipeLayout);
    	InitView.instance().initListView(mListView, this);
    	View headView = LayoutInflater.from(this).inflate(R.layout.head_item, null);
    	 mDemoSlider = (SliderLayout) headView.findViewById(R.id.slider);
         mListView.addHeaderView(headView);
    	 AnimationAdapter animationAdapter = new CardsAnimationAdapter(objectAdapter);
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
         
         initTitleBar();
    }
    
    private void initTitleBar(){
		top_head.setBackgroundColor(Color.TRANSPARENT);
		top_head.setImageResource(R.drawable.mat_back);
		title_recent.setText("对象列表");
	}
    
    @Background
    void loadNewList(int record) {
    	BmobQuery<ServiceObject> query = new BmobQuery<ServiceObject>();
    	query.setLimit(10);
    	query.order("-updateAt");
    	query.addQueryKeys("objectId,name,heathinfo,image,roominfo");
    	query.findObjects(this, new FindListener<ServiceObject>() {
			@Override
			public void onError(int arg0, String arg1) {
				showCustomToast("失败："+arg0+arg1);
			}
			@Override
			public void onSuccess(List<ServiceObject> list) {
				if(list != null && list.size()>0){
					getResult(list);
				}else{
					swipeLayout.setRefreshing(false);
				}
			}
		});
    }
    
    private void loadData(int record) {
        if (this.hasNetWork()) {
            loadNewList(record);
        } else {
            mListView.onBottomComplete();
            mProgressBar.setVisibility(View.GONE);
            showShortToast(getString(R.string.not_network));
        }
    }
    
    @UiThread
    public void getResult(List<ServiceObject> list) {
        if (isRefresh) {
            isRefresh = false;
            objectAdapter.clear();
            listsModles.clear();
        }
        mProgressBar.setVisibility(View.GONE);
        swipeLayout.setRefreshing(false);
        if (index == 0 && list.size() >= 4) {
            initSliderLayout(list);
        } else {
            objectAdapter.appendList(list);
        }
        listsModles.addAll(list);
        mListView.onBottomComplete();
    }
    
    private void initSliderLayout(List<ServiceObject> newModles) {

        if (!isNullString(newModles.get(0).getImage().getFileUrl(this)))
            newHashMap.put(newModles.get(0).getImage().getFileUrl(this), newModles.get(0));
        if (!isNullString(newModles.get(1).getImage().getFileUrl(this)))
            newHashMap.put(newModles.get(1).getImage().getFileUrl(this), newModles.get(1));
        if (!isNullString(newModles.get(2).getImage().getFileUrl(this)))
            newHashMap.put(newModles.get(2).getImage().getFileUrl(this), newModles.get(2));
        if (!isNullString(newModles.get(3).getImage().getFileUrl(this)))
            newHashMap.put(newModles.get(3).getImage().getFileUrl(this), newModles.get(3));

        if (!isNullString(newModles.get(0).getImage().getFileUrl(this)))
            url_maps.put(newModles.get(0).getName(), newModles.get(0).getImage().getFileUrl(this));
        if (!isNullString(newModles.get(1).getImage().getFileUrl(this)))
            url_maps.put(newModles.get(1).getName(), newModles.get(1).getImage().getFileUrl(this));
        if (!isNullString(newModles.get(2).getImage().getFileUrl(this)))
            url_maps.put(newModles.get(2).getName(), newModles.get(2).getImage().getFileUrl(this));
        if (!isNullString(newModles.get(3).getImage().getFileUrl(this)))
            url_maps.put(newModles.get(3).getName(), newModles.get(3).getImage().getFileUrl(this));

        for (String name : url_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
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
        objectAdapter.appendList(newModles);
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
//                url_maps.clear();
                mDemoSlider.removeAllSliders();
            }
        }, 2000);
	}
	
	private boolean isNullString(String imgUrl){
		if (StringUtils.isEmpty(imgUrl)) {
            return true;
        }
        return false;
	}
}
