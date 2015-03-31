package net.yigong.adapter;

import java.util.ArrayList;
import java.util.List;

import net.yigong.view.ui.NewImageView;
import net.yigong.view.ui.NewImageView_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

@EBean
public class ImageAdapter extends BaseAdapter {
	@RootContext
	Context context;
	public List<String> lists = new ArrayList<String>();
	
	public void appendList(List<String> list){
		if(!lists.contains(list)&&list != null&&list.size()>0){
			lists.addAll(list);
		}
		notifyDataSetChanged();
	}
	
	public void clear(){
		lists.clear();
		notifyDataSetChanged();
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
		NewImageView newImageView ;
		if(convertView == null){
			newImageView = NewImageView_.build(context);
		}else{
			newImageView = (NewImageView) convertView;
		}
		newImageView.setImage(lists, position);
		return newImageView;
	}

}
