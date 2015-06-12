package net.yigong.view.ui;

import net.yigong.R;
import net.yigong.bmob.bean.Place;
import net.yigong.utils.Options;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.widget.ImageView;
import android.widget.TextView;

@EViewGroup(R.layout.item_place)
public class PlaceItemView extends CardView{

	@ViewById(R.id.place_image)
	protected ImageView place_image;
	@ViewById(R.id.place_title)
	protected TextView place_title;
	@ViewById(R.id.place_status)
	protected TextView place_status;
	
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	protected DisplayImageOptions options;
	
	private Context context;
	
	public PlaceItemView(Context context) {
		super(context);
		this.context = context;
		options = Options.getListOptions();
	}

	public void setPlaceItemView(Place place){
		place_title.setText(""+place.getName());
		imageLoader.displayImage(place.getImage().getFileUrl(context) == null ? "" : place.getImage().getFileUrl(context), place_image, options);
	}
}
