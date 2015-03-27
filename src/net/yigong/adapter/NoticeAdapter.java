package net.yigong.adapter;

import java.util.ArrayList;
import java.util.List;

import net.yigong.bean.NoticeModle;
import net.yigong.view.ui.NoticeItemView;
import net.yigong.view.ui.NoticeItemView_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

@EBean
public class NoticeAdapter extends BaseAdapter{
	public List<NoticeModle> lists = new ArrayList<NoticeModle>();
	
	private String currentItem;
	
	@RootContext
	Context context;
	
	public void appendList(List<NoticeModle> list){
		if(!lists.containsAll(list) && list != null && list.size()>0){
			lists.addAll(list);
		}
		notifyDataSetChanged();
	}

	public void clear(){
		lists.clear();
		notifyDataSetChanged();
	}
	
	public void currentItem(String item){
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
		NoticeItemView noticeItemView;
		if(convertView == null){
			noticeItemView = NoticeItemView_.build(context);
		}else{
			noticeItemView = (NoticeItemView) convertView;
		}
		
		NoticeModle noticeModle = lists.get(position);
		noticeItemView.setNoticeItemView(noticeModle);
		return noticeItemView;
	}

}
