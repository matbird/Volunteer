package net.yigong.bean;

import cn.bmob.v3.BmobUser;

public class Volunteer extends BmobUser{

	private int sex;//1.male 0.female
	private String nick;
	private String photo;
	private String qq;
	private String phone;
	private int area; // 1.haidian 2.chaoyang 3.changping 4.fengtai 5.daxing
	private int degree;// 0.common 1.admin 2.subAdmin 3.daidui
	private String age;
	private String birthday;
	private int score;
	private String qqGroup;
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
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
	public int getArea() {
		return area;
	}
	public void setArea(int area) {
		this.area = area;
	}
	public int getDegree() {
		return degree;
	}
	public void setDegree(int degree) {
		this.degree = degree;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getQqGroup() {
		return qqGroup;
	}
	public void setQqGroup(String qqGroup) {
		this.qqGroup = qqGroup;
	}
}
