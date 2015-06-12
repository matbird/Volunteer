package net.yigong.bmob.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class ServiceObject extends BmobObject{

	private YGUser author;
	private YGUser lastEditor;
	private Place place;
	private String name;
	private BmobFile image;
	private String gender;
	private String birthday;
	private String roominfo;
	private String heathinfo;
	private String hobby;
	private String remark;
	private String unlike;
	private Integer status;
	public YGUser getAuthor() {
		return author;
	}
	public void setAuthor(YGUser author) {
		this.author = author;
	}
	public YGUser getLastEditor() {
		return lastEditor;
	}
	public void setLastEditor(YGUser lastEditor) {
		this.lastEditor = lastEditor;
	}
	public Place getPlace() {
		return place;
	}
	public void setPlace(Place place) {
		this.place = place;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BmobFile getImage() {
		return image;
	}
	public void setImage(BmobFile image) {
		this.image = image;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getRoominfo() {
		return roominfo;
	}
	public void setRoominfo(String roominfo) {
		this.roominfo = roominfo;
	}
	public String getHeathinfo() {
		return heathinfo;
	}
	public void setHeathinfo(String heathinfo) {
		this.heathinfo = heathinfo;
	}
	public String getHobby() {
		return hobby;
	}
	public void setHobby(String hobby) {
		this.hobby = hobby;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getUnlike() {
		return unlike;
	}
	public void setUnlike(String unlike) {
		this.unlike = unlike;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
