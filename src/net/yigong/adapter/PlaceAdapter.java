package net.yigong.adapter;

import java.util.ArrayList;
import java.util.List;

import net.yigong.bean.NoticeModle;
import net.yigong.bmob.bean.Place;
import net.yigong.view.ui.PlaceItemView;
import net.yigong.view.ui.PlaceItemView_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

@EBean
public class PlaceAdapter extends BaseAdapter {

	public List<Place> lists = new ArrayList<Place>();

	private String currentItem;

	@RootContext
	Context context;

	public void appendList(List<Place> list) {
		if (!lists.containsAll(list) && list != null && list.size() > 0) {
			lists.addAll(list);
		}
		notifyDataSetChanged();
	}

	public void clear() {
		lists.clear();
		notifyDataSetChanged();
	}

	public void currentItem(String item) {
		this.currentItem = item;
	}

	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int arg0) {
		return lists.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View contentView, ViewGroup arg2) {
		PlaceItemView placeItemView;
		if(contentView == null){
			placeItemView = PlaceItemView_.build(context);
		}else{
			placeItemView = (PlaceItemView) contentView;
		}
		Place place = lists.get(position);
		placeItemView.setPlaceItemView(place);
		return placeItemView;
	}

}
