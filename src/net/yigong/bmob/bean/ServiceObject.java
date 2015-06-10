package net.yigong.bmob.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class ServiceObject extends BmobObject{

	private String name;
	private BmobFile pic;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BmobFile getPic() {
		return pic;
	}
	public void setPic(BmobFile pic) {
		this.pic = pic;
	}
	
}
