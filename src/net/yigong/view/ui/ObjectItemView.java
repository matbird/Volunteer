package net.yigong.view.ui;

import net.yigong.R;
import net.yigong.bmob.bean.ServiceObject;
import net.yigong.utils.Options;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.widget.ImageView;
import android.widget.TextView;

@EViewGroup(R.layout.item_object) 
public class ObjectItemView extends CardView{

	@ViewById(R.id.name)
	protected TextView nameView;
	@ViewById(R.id.room)
	protected TextView roomView;
	@ViewById(R.id.health)
	protected TextView healthView;
	@ViewById(R.id.image)
	protected ImageView imageView;
	
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	protected DisplayImageOptions options;
	
	private Context context;
	
	public ObjectItemView(Context context) {
		super(context);
		this.context = context;
		options = Options.getHeadPhotoOptions();
	}

	public void setObjectItemView(ServiceObject obj){
		nameView.setText("姓   名："+obj.getName());
		roomView.setText("房间位置："+obj.getRoominfo()+"");
		healthView.setText("健康状况:"+obj.getHeathinfo()+"");
		imageLoader.displayImage(obj.getImage().getFileUrl(context) == null ? "" : obj.getImage().getFileUrl(context), imageView, options);
	}
}
