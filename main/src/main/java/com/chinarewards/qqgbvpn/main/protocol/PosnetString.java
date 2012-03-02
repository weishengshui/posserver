package com.chinarewards.qqgbvpn.main.protocol;

public class PosnetString {

	//字符串长度
	public int len;
	
	//字符串
	public String content;
	
	@Override
	public String toString() {
		return " [len=" + len + ", content=" + content + "]";
	}

	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}
