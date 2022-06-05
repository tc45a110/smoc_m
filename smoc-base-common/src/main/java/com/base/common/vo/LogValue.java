/**
 * @desc
 * 
 */
package com.base.common.vo;

public class LogValue{
	//日志内容
	private String content;
	//日志分类,用于生成文件名
	private String category;
	//日志标识
	private String lable;
	
	public LogValue(String content, String category, String lable) {
		super();
		this.content = content;
		this.category = category;
		this.lable = lable;
	}

	public String getContent() {
		return content;
	}

	public String getCategory() {
		return category;
	}

	public String getLable() {
		return lable;
	}
	
}


