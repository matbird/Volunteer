
package net.yigong.view.ui;

import net.yigong.R;
import net.yigong.bmob.bean.YGUser;
import net.yigong.utils.Options;
import net.yigong.view.activity.BaseActivity;
import net.yigong.view.activity.LoginActivity_;
import net.yigong.view.activity.MoreActivity_;
import net.yigong.view.initview.SlidingMenuView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

@EViewGroup(R.layout.activity_left)
public class LeftView extends LinearLayout {

	@ViewById(R.id.iv_photo)
	protected ImageView iv_photo;
	@ViewById(R.id.nickname)
	protected TextView nickname;
	
    private final BaseActivity context;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
	protected DisplayImageOptions options;

    public LeftView(Context context) {
        super(context);
        this.context = (BaseActivity) context;
        options = Options.getListOptions();
//        initLeftView();
    }

    private void initLeftView(){
    	YGUser user = BmobUser.getCurrentUser(context, YGUser.class);
    	if(user == null){
    		nickname.setText("未登录");
    	}else{
    		nickname.setText(user.getUsername());
    		imageLoader.displayImage(user.getPhoto().getFileUrl(context) == null ? "" : user.getPhoto().getFileUrl(context), iv_photo,options);
    	}
    }
    
    @Click(R.id.pics)
    public void enterPics(View view) {
//        context.openActivity(TuPianSinaActivity_.class);
        isShow();
    }

    @Click(R.id.video)
    public void enterVideo(View view) {
//        context.openActivity(VideoActivity_.class);
        isShow();
    }

    @Click(R.id.ties)
    public void enterMessage(View view) {
//        context.openActivity(MessageActivity_.class);
        isShow();
    }

    @Click(R.id.tianqi)
    public void enterTianQi(View view) {
//        context.openActivity(WeatherActivity_.class);
        isShow();
    }

    @Click(R.id.more)
    public void enterMore(View view) {
        context.openActivity(MoreActivity_.class);
        isShow();
    }

    @Click(R.id.iv_photo)
    public void onHeadPhoto(View v){
    	YGUser user = BmobUser.getCurrentUser(context, YGUser.class);
    	if(user == null){
    		context.openActivityForResult(LoginActivity_.class, 1000);
    	}else{
    		//TODO 进入个人中心
    	}
    }
    
    public void isShow() {
        if (SlidingMenuView.instance().slidingMenu.isMenuShowing()) {
            SlidingMenuView.instance().slidingMenu.showContent();
        }
    }
    
    public TextView getNicknameView(){
    	return nickname;
    }
    
    public ImageView getPhotoView(){
    	return iv_photo;
    }
}

