package net.yigong.bean;

import java.io.Serializable;

import com.bmob.BmobProFile;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class NoticeModle extends BmobObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6226323362587136200L;

	private String title;
	private String time;// 活动时间
	private String end_time;// 活动时间
	private int point;  // 活动地点
	private int number; // 报名人数
	private String sign_style;// 报名格式
	private String manager_info;
	private String assistor_info;
	private String promulgator_info;
	private int status; // 活动状态 1.招募中 2.活动中 3.已结束
	private String bak_content;
	private String image_url;

	public NoticeModle(){
	}
	
	public String getPromulgator_info() {
		return promulgator_info;
	}
	public void setPromulgator_info(String promulgator_info) {
		this.promulgator_info = promulgator_info;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getImage_url() {
		return image_url;
	}
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getSign_style() {
		return sign_style;
	}
	public void setSign_style(String sign_style) {
		this.sign_style = sign_style;
	}
	public String getManager_info() {
		return manager_info;
	}
	public void setManager_info(String manager_info) {
		this.manager_info = manager_info;
	}
	public String getAssistor_info() {
		return assistor_info;
	}
	public void setAssistor_info(String assistor_info) {
		this.assistor_info = assistor_info;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getBak_content() {
		return bak_content;
	}
	public void setBak_content(String bak_content) {
		this.bak_content = bak_content;
	}
	
}
