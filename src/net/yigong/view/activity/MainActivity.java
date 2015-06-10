package net.yigong.view.activity;

import java.io.File;
import java.util.ArrayList;

import net.yigong.App;
import net.yigong.R;
import net.yigong.adapter.YiGongFragmentPagerAdapter;
import net.yigong.bean.NewModle;
import net.yigong.bean.NoticeModle;
import net.yigong.bmob.bean.ServiceObject;
import net.yigong.channel.ChannelItem;
import net.yigong.channel.ChannelManage;
import net.yigong.utils.BaseTools;
import net.yigong.view.fragment.HomeFragment_;
import net.yigong.view.fragment.NewsFragment_;
import net.yigong.view.initview.SlidingMenuView;
import net.yigong.view.ui.LeftView;
import net.yigong.view.ui.LeftView_;
import net.yigong.wedget.ColumnHorizontalScrollView;
import net.yigong.wedget.slidemenu.SlidingMenu;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {
        
	/** 自定义HorizontalScrollView */
	@ViewById(R.id.mColumnHorizontalScrollView)
	protected ColumnHorizontalScrollView mColumnHorizontalScrollView;
	@ViewById(R.id.mRadioGroup_content)
	protected LinearLayout mRadioGroup_content;
	@ViewById(R.id.ll_more_columns)
	protected LinearLayout ll_more_columns;
	@ViewById(R.id.rl_column)
    protected RelativeLayout rl_column;
    @ViewById(R.id.button_more_columns)
    protected ImageView button_more_columns;
    @ViewById(R.id.mViewPager)
    protected ViewPager mViewPager;
    @ViewById(R.id.shade_left)
    protected ImageView shade_left;
    @ViewById(R.id.shade_right)
    protected ImageView shade_right;
    protected LeftView mLeftView;

    protected SlidingMenu side_drawer;
    private YiGongFragmentPagerAdapter mAdapetr;
    
    private Fragment newfragment;
    
    private double back_pressed;

    public static boolean isChange = false;
    
    /** 屏幕宽度 */
    private int mScreenWidth = 0;
    /** Item宽度 */
    private int mItemWidth = 0;
    /** 当前选中的栏目 */
    private int columnSelectIndex = 0;
    /** 用户选择的新闻分类列表 */
    protected static ArrayList<ChannelItem> userChannelLists;
    private ArrayList<Fragment> fragments;
    
	@Override
	public boolean isSupportSlide() {
		return false;
	}

    @SuppressLint("InlinedApi") 
    @AfterInject
    void init(){
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
    	mScreenWidth = BaseTools.getWindowsWidth(this);
    	mItemWidth = mScreenWidth / 7;// 一个Item宽度为屏幕的1/7
    	userChannelLists = new ArrayList<ChannelItem>();
    	fragments = new ArrayList<Fragment>();
    	
    	Bmob.initialize(this, "fdea5c917238d80f14207bcfbb1e940b");
    }
    
    @AfterViews
    void initView(){
    	try {
			initSlideMenu();
			initViewPager();
			setChangelView();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    protected void initSlideMenu(){
    	mLeftView = LeftView_.build(this);
    	side_drawer = SlidingMenuView.instance().initSlidingMenuView(this,mLeftView);
    }
    
    private void initViewPager(){
    	mAdapetr = new YiGongFragmentPagerAdapter(getSupportFragmentManager());
    	mViewPager.setOffscreenPageLimit(1);
    	mViewPager.setAdapter(mAdapetr);
    	mViewPager.setOnPageChangeListener(pageListener);
    }
    
    @Click(R.id.top_head)
    protected void onMenu(){
    	if(side_drawer.isMenuShowing()){
    		side_drawer.showContent();
    	}else{
    		side_drawer.showMenu();
    	}
    }
    
    @Click(R.id.button_more_columns)
    protected void onMoreColumns(View view) {
    	String picPath = "/storage/emulated/0/test.png";
    	BmobFile bmobFile = new BmobFile(new File(picPath));
    	bmobFile.uploadblock(this, new UploadFileListener() {
			@Override
			public void onSuccess() {
				showCustomToast("success.");
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				showCustomToast("error:"+arg1+"  code:"+arg0);
			}
		});
    	
    	/*String picPath = "sdcard/test.png";
    	BmobFile bmobFile = new BmobFile(new File(picPath));
    	ServiceObject obj = new ServiceObject();
    	obj.setName("王奶奶");
    	obj.setPic(bmobFile);
    	obj.save(this, new SaveListener() {
			@Override
			public void onSuccess() {
				showCustomToast("success.");
			}
			@Override
			public void onFailure(int arg0, String arg1) {
				showCustomToast("error:"+arg1+"  code:"+arg0);
			}
		});*/
    	
    	
//    	openActivity(LoginActivity_.class);
    	/*// add 
    	NoticeModle notice = new NoticeModle();
    	notice.setTitle("2015年海淀分盟第一次活动通知");
    	notice.setAssistor_info("山大王");
    	notice.setManager_info("刘老头");
    	notice.setPoint(4);
    	notice.setStatus(1);
    	notice.setTime("周六 2015年3月28日 上午8：40到11：30");
    	notice.setEnd_time("2015.01.09 20:00");
    	notice.setNumber(25);
    	notice.setPromulgator_info("Laowang");
    	notice.setImage_url("xxx");
    	notice.setSign_style("Tom+海淀+玉福+5+");
    	
    	notice.save(this, new SaveListener() {
			@Override
			public void onSuccess() {
				MainActivity.this.showCustomToast("成功");
			}
			@Override
			public void onFailure(int arg0, String arg1) {
				MainActivity.this.showCustomToast("失败："+arg1);
			}
		});*/
    	
    	/*// find
    	BmobQuery<NoticeModle> query = new BmobQuery<NoticeModle>();
    	query.findObjects(this, new FindListener<NoticeModle>() {
			@Override
			public void onSuccess(List<NoticeModle> list) {
				MainActivity.this.showCustomToast("成功:"+list.get(0).getTitle());
			}
			@Override
			public void onError(int arg0, String arg1) {
				MainActivity.this.showCustomToast("失败："+arg0+arg1);
			}
		});*/
    	
    	/*String path = "/storage/emulated/0/Pictures/Screenshots/Screenshot_2015-03-19-14-07-33.jpeg";
    	BTPFileResponse response = BmobProFile.getInstance(MainActivity.this).upload(path, new UploadListener() {

            @Override
            public void onSuccess(String fileName,String url) {
                // TODO Auto-generated method stub
//                dialog.dismiss();
//                showToast("文件已上传成功："+fileName);
            	MainActivity.this.showCustomToast("成功："+url);
            	String fileUrl = BmobProFile.getInstance(MainActivity.this).signURL(fileName, url, "1160f1b6deb0d0691a852fc32187ab0a", 0, null);
            	//                                         signURL(String fileName,String fileUrl,String accessKey,long effectTime,String secretKey)
            	Log.i("test","成功："+fileUrl);
            }

            @Override
            public void onProgress(int ratio) {
                // TODO Auto-generated method stub
//                BmobLog.i("MainActivity -onProgress :"+ratio);
            	Log.i("test", "progress:---->"+ratio);
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                // TODO Auto-generated method stub
//                showToast("上传出错："+errormsg);
            	MainActivity.this.showCustomToast("失败："+statuscode+"  msg"+errormsg);
            	Log.i("test", "失败："+statuscode+"  msg"+errormsg);
            }
        });*/
    	
    	/*CharSequence[] items = {"相册", "相机"};    
    	   new AlertDialog.Builder(this)  
    	    .setTitle("选择图片来源")  
    	    .setItems(items, new DialogInterface.OnClickListener() {  
    	        public void onClick(DialogInterface dialog, int which) {  
    	            if( which == 0 ){  
    	                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  
    	                intent.addCategory(Intent.CATEGORY_OPENABLE);  
    	                intent.setType("image/*");  
    	                startActivityForResult(Intent.createChooser(intent, "选择图片"), 0);   
    	            }else{  
    	                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);    
    	                startActivityForResult(intent, 1);    
    	            }  
    	        }  
    	    })  
    	    .create().show();   */
    }
    
    /**
     * 当栏目项发生变化时候调用
     */
    public void setChangelView() {
        initColumnData();

    }
    
    /** 获取Column栏目 数据 */
    private void initColumnData() {
        userChannelLists = ((ArrayList<ChannelItem>) ChannelManage.getManage(
                App.getApp().getSQLHelper()).getUserChannel());
        initTabColumn();
        initFragment();
    }
    
    /**
     * 初始化Column栏目项
     */
    private void initTabColumn() {
        mRadioGroup_content.removeAllViews();
        int count = userChannelLists.size();
        mColumnHorizontalScrollView.setParam(this, mScreenWidth, mRadioGroup_content, shade_left,
                shade_right, ll_more_columns, rl_column);
        for (int i = 0; i < count; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth,
                    LayoutParams.WRAP_CONTENT);
            params.leftMargin = 5;
            params.rightMargin = 5;
            // TextView localTextView = (TextView)
            // mInflater.inflate(R.layout.column_radio_item, null);
            TextView columnTextView = new TextView(this);
            columnTextView.setTextAppearance(this, R.style.top_category_scroll_view_item_text);
            // localTextView.setBackground(getResources().getDrawable(R.drawable.top_category_scroll_text_view_bg));
            columnTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
            columnTextView.setGravity(Gravity.CENTER);
            columnTextView.setPadding(5, 5, 5, 5);
            columnTextView.setId(i);
            columnTextView.setText(userChannelLists.get(i).getName());
            columnTextView.setTextColor(getResources().getColorStateList(
                    R.color.top_category_scroll_text_color_day));
            if (columnSelectIndex == i) {
                columnTextView.setSelected(true);
            }
            columnTextView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
                        View localView = mRadioGroup_content.getChildAt(i);
                        if (localView != v)
                            localView.setSelected(false);
                        else {
                            localView.setSelected(true);
                            mViewPager.setCurrentItem(i);
                        }
                    }
                }
            });
            mRadioGroup_content.addView(columnTextView, i, params);
        }
    }
    
    /**
     * 初始化Fragment
     */
    private void initFragment() {
        fragments.clear();
        int count = userChannelLists.size();
        for (int i = 0; i < count; i++) {
            // Bundle data = new Bundle();
            String nameString = userChannelLists.get(i).getName();
            // data.putString("text", nameString);
            // data.putInt("id", userChannelList.get(i).getId());
            // initFragment(nameString);
            // map.put(nameString, nameString);
            // newfragment.setArguments(data);
            fragments.add(initFragment(nameString));
            // fragments.add(nameString);
        }
        mAdapetr.appendList(fragments);
    }
    
    public Fragment initFragment(String channelName) {
        if (channelName.equals("活动")) {
            newfragment = new HomeFragment_();
        } 
        else if (channelName.equals("新闻")) {
            newfragment = new NewsFragment_();
        } 
        
        /*else if (channelName.equals("娱乐")) {
            newfragment = new YuLeFragment_();
        } else if (channelName.equals("体育")) {
            newfragment = new TiYuFragment_();
        } else if (channelName.equals("财经")) {
            newfragment = new CaiJingFragment_();
        } else if (channelName.equals("科技")) {
            newfragment = new KeJiFragment_();
        } else if (channelName.equals("电影")) {
            newfragment = new DianYingFragment_();
        } else if (channelName.equals("汽车")) {
            newfragment = new QiCheFragment_();
        } else if (channelName.equals("笑话")) {
            newfragment = new XiaoHuaFragment_();
        } else if (channelName.equals("时尚")) {
            newfragment = new ShiShangFragment_();
        } else if (channelName.equals("北京")) {
            newfragment = new BeiJingFragment_();
        } else if (channelName.equals("军事")) {
            newfragment = new JunShiFragment_();
        } else if (channelName.equals("房产")) {
            newfragment = new FangChanFragment_();
        } else if (channelName.equals("游戏")) {
            newfragment = new YouXiFragment_();
        } else if (channelName.equals("情感")) {
            newfragment = new QinGanFragment_();
        } else if (channelName.equals("精选")) {
            newfragment = new JingXuanFragment_();
        } else if (channelName.equals("电台")) {
            newfragment = new DianTaiFragment_();
        } else if (channelName.equals("图片")) {
            newfragment = new TuPianFragment_();
        } else if (channelName.equals("NBA")) {
            newfragment = new NBAFragment_();
        } else if (channelName.equals("数码")) {
            newfragment = new ShuMaFragment_();
        } else if (channelName.equals("移动")) {
            newfragment = new YiDongFragment_();
        } else if (channelName.equals("彩票")) {
            newfragment = new CaiPiaoFragment_();
        } else if (channelName.equals("教育")) {
            newfragment = new JiaoYuFragment_();
        } else if (channelName.equals("论坛")) {
            newfragment = new LunTanFragment_();
        } else if (channelName.equals("旅游")) {
            newfragment = new LvYouFragment_();
        } else if (channelName.equals("手机")) {
            newfragment = new ShouJiFragment_();
        } else if (channelName.equals("博客")) {
            newfragment = new BoKeFragment_();
        } else if (channelName.equals("社会")) {
            newfragment = new SheHuiFragment_();
        } else if (channelName.equals("家居")) {
            newfragment = new JiaJuFragment_();
        } else if (channelName.equals("暴雪")) {
            newfragment = new BaoXueYouXiFragment_();
        } else if (channelName.equals("亲子")) {
            newfragment = new QinZiFragment_();
        } else if (channelName.equals("CBA")) {
            newfragment = new CBAFragment_();
        }*/
        return newfragment;
    }
    
    /**
     * ViewPager切换监听方法
     */
    public OnPageChangeListener pageListener = new OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            mViewPager.setCurrentItem(position);
            selectTab(position);
        }
    };
    
    /**
     * 选择的Column里面的Tab
     */
    private void selectTab(int tab_postion) {
        columnSelectIndex = tab_postion;
        for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
            View checkView = mRadioGroup_content.getChildAt(tab_postion);
            int k = checkView.getMeasuredWidth();
            int l = checkView.getLeft();
            int i2 = l + k / 2 - mScreenWidth / 2;
            // rg_nav_content.getParent()).smoothScrollTo(i2, 0);
            mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
            // mColumnHorizontalScrollView.smoothScrollTo((position - 2) *
            // mItemWidth , 0);
        }
        // 判断是否选中
        for (int j = 0; j < mRadioGroup_content.getChildCount(); j++) {
            View checkView = mRadioGroup_content.getChildAt(j);
            boolean ischeck;
            if (j == tab_postion) {
                ischeck = true;
            } else {
                ischeck = false;
            }
            checkView.setSelected(ischeck);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (isChange) {
                setChangelView();
                // initColumnData();
                // initTabColumn();
                isChange = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(resultCode == RESULT_OK){  
            Uri uri = data.getData();   
            String [] proj={MediaStore.Images.Media.DATA};  
            Cursor cursor = managedQuery( uri,  
                    proj,                 // Which columns to return  
                    null,       // WHERE clause; which rows to return (all rows)  
                    null,       // WHERE clause selection arguments (none)  
                    null);                 // Order-by clause (ascending by name)  
              
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);  
            cursor.moveToFirst();  
              
            String path = cursor.getString(column_index);  
            Bitmap bmp = BitmapFactory.decodeFile(path);  
            Log.i("test", "the path is :" + path);  
        }else{  
            Toast.makeText(this, "请重新选择图片", Toast.LENGTH_SHORT).show();  
        }  
    }

    /**
     * 点击两次返回退出系统
     * 
     * @param view
     */
    @Override
    public void onBackPressed() {
        if (side_drawer.isMenuShowing()) {
            side_drawer.showContent();
        } else {
            System.out.println(isShow());
            if (isShow()) {
                dismissProgressDialog();
            } else {
                if (back_pressed + 3000 > System.currentTimeMillis()) {
                    finish();
                    super.onBackPressed();
                }
                else
                    showCustomToast(getString(R.string.press_again_exit));
                // Toast.makeText(this, getString(R.string.press_again_exit),
                // 1).show();

                back_pressed = System.currentTimeMillis();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
    }
}
