package net.yigong.view.activity;

import net.yigong.R;
import net.yigong.bean.NewDetailModle;
import net.yigong.bean.NewModle;
import net.yigong.http.HttpUtil;
import net.yigong.http.json.NewDetailJson;
import net.yigong.utils.Options;
import net.yigong.utils.StringUtils;
import net.yigong.wedget.ProgressPieView;
import net.yigong.wedget.htmltextview.HtmlTextView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

@EActivity(R.layout.activity_details)
public class DetailsActivity extends BaseActivity implements ImageLoadingListener,ImageLoadingProgressListener{
	
	@ViewById(R.id.new_title)
    protected TextView newTitle;
    @ViewById(R.id.new_time)
    protected TextView newTime;
    @ViewById(R.id.wb_details)
    protected HtmlTextView webView;
    // @ViewById(R.id.progressBar)
    // protected ProgressBar progressBar;
    @ViewById(R.id.progressPieView)
    protected ProgressPieView mProgressPieView;
    @ViewById(R.id.new_img)
    protected ImageView newImg;
    @ViewById(R.id.img_count)
    protected TextView imgCount;
    @ViewById(R.id.play)
    protected ImageView mPlay;
    
    private NewModle newModle;
    private String newID;
    private String newUrl;
    
    private String imgCountString;
    
    protected DisplayImageOptions options;
    protected ImageLoader imageLoader;
    
    private NewDetailModle newDetailModle;
    @AfterInject
    public void init(){
    	try {
			newModle = (NewModle) getIntent().getExtras().getSerializable("newModle");
			newID = newModle.getDocid();
			newUrl = getUrl(newID);
			imageLoader = ImageLoader.getInstance();
			options = Options.getListOptions();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    @AfterViews
    public void initWebView(){
    	try {
			mProgressPieView.setShowText(true);
			mProgressPieView.setShowImage(false);
			showProgressDialog();
			loadData(newUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    private void loadData(String url){
    	if(hasNetWork()){
    		loadNewDetailData(url);
    	}else{
    		dismissProgressDialog();
    		showShortToast(getString(R.string.not_network));
    		String result = getCacheStr(newUrl);
    		if(!StringUtils.isEmpty(result)){
    			getResult(result);
    		}
    	}
    }
    
    @Background
    public void loadNewDetailData(String url){
    	String result ;
    	try {
			result = HttpUtil.getByHttpClient(this, url);
			getResult(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    @UiThread
    public void getResult(String result){
    	newDetailModle = NewDetailJson.instance(this).readJsonNewModles(result, newID);
    	if(newDetailModle == null)
    		return ;
    	setCacheStr(newUrl, result);
    	if(!"".equals(newDetailModle.getUrl_mp4())){
    		imageLoader.displayImage(newDetailModle.getCover(), newImg, options, this, this);
    		newImg.setVisibility(View.VISIBLE);
    	}else{
    		if(newDetailModle.getImgList().size() > 0){
    			imgCountString = "共"+newDetailModle.getImgList().size()+"张";
    			imageLoader.displayImage(newDetailModle.getImgList().get(0), newImg, options, this, this);
    			newImg.setVisibility(View.VISIBLE);
    		}
    	}
    	
    	newTitle.setText(newDetailModle.getTitle());
    	newTime.setText("来源:"+newDetailModle.getSource()+" "+newDetailModle.getPtime());
    	String content = newDetailModle.getBody();
        content = content.replace("<!--VIDEO#1--></p><p>", "");
        content = content.replace("<!--VIDEO#2--></p><p>", "");
        content = content.replace("<!--VIDEO#3--></p><p>", "");
        content = content.replace("<!--VIDEO#4--></p><p>", "");
        content = content.replace("<!--REWARD#0--></p><p>", "");
        webView.setHtmlFromString(content, false);
        dismissProgressDialog();
    }
    
    @Click(R.id.new_img)
    public void imageMore(View view){
    	try {
            Bundle bundle = new Bundle();
            bundle.putSerializable("newDetailModle", newDetailModle);
            if (!"".equals(newDetailModle.getUrl_mp4())) {
                bundle.putString("playUrl", newDetailModle.getUrl_mp4());
//                openActivity(VideoPlayActivity_.class, bundle, 0);
            } else {
                openActivity(ImageDetailActivity_.class, bundle, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
	@Override
	public void onProgressUpdate(String imageUri, View view, int current,
			int total) {
		int currentpro = (current * 100) / total;
		if(currentpro == 100){
			mProgressPieView.setVisibility(View.GONE);
			mProgressPieView.setShowText(false);
		}else{
			mProgressPieView.setVisibility(View.VISIBLE);
			mProgressPieView.setText(currentpro+"%");
			mProgressPieView.setProgress(currentpro);
		}
	}

	@Override
	public void onLoadingStarted(String imageUri, View view) {
		mProgressPieView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onLoadingFailed(String imageUri, View view,
			FailReason failReason) {
		mProgressPieView.setVisibility(View.GONE);
	}

	@Override
	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		if(!"".equals(newDetailModle.getUrl_mp4())){
			mPlay.setVisibility(View.VISIBLE);
		}else{
			imgCount.setVisibility(View.VISIBLE);
			imgCount.setText(imgCountString);
		}
		mProgressPieView.setVisibility(View.GONE);
	}

	@Override
	public void onLoadingCancelled(String imageUri, View view) {
		mProgressPieView.setVisibility(View.GONE);
	}

}
