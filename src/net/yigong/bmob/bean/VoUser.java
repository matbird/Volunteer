package net.yigong.bmob.bean;

import cn.bmob.v3.BmobUser;

public class VoUser extends BmobUser{

//	private int sex;//1.male 0.female
	private String realname;
//	private String photo;
	private String qq;
	private String phone;
	private String area; // 1.haidian 2.chaoyang 3.changping 4.fengtai 5.daxing
	private int degree;// 0.common 1.admin 2.subAdmin 3.daidui
//	private String age;
//	private String birthday;
//	private int score;
//	private String qqGroup;
	private int verify;// 0.not verify  1.has verify
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public int getDegree() {
		return degree;
	}
	public void setDegree(int degree) {
		this.degree = degree;
	}
	public int getVerify() {
		return verify;
	}
	public void setVerify(int verify) {
		this.verify = verify;
	}
	
}
