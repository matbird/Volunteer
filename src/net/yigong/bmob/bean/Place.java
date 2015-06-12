package net.yigong.bmob.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Place extends BmobObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7488260947334640721L;
	private YGUser author;
	private String name;
	private String address;
	private BmobFile image;
	private String remark;
	private String descri;
	public YGUser getAuthor() {
		return author;
	}
	public void setAuthor(YGUser author) {
		this.author = author;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public BmobFile getImage() {
		return image;
	}
	public void setImage(BmobFile image) {
		this.image = image;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDescri() {
		return descri;
	}
	public void setDescri(String descri) {
		this.descri = descri;
	}
}
