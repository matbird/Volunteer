package net.yigong.adapter;

import java.util.ArrayList;
import java.util.List;

import net.yigong.bmob.bean.Place;
import net.yigong.bmob.bean.ServiceObject;
import net.yigong.view.ui.ObjectItemView;
import net.yigong.view.ui.ObjectItemView_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

@EBean
public class ObjectAdapter extends BaseAdapter {

	public List<ServiceObject> lists = new ArrayList<ServiceObject>();

	private String currentItem;

	@RootContext
	Context context;

	public void appendList(List<ServiceObject> list) {
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
	public Object getItem(int position) {
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ObjectItemView objectItemView;
		if (convertView == null) {
			objectItemView = ObjectItemView_.build(context);
		} else {
			objectItemView = (ObjectItemView) convertView;
		}
		ServiceObject object = lists.get(position);
		objectItemView.setObjectItemView(object);
		return objectItemView;
	}

}
