package com.ggeit.pay.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "body_main")	//连接的数据库集合
public class BodyMainEntity {
	
	private String BODY_ID;
	private String BODY_NAME;
	public String getBODY_ID() {
		return BODY_ID;
	}
	public void setBODY_ID(String bODY_ID) {
		BODY_ID = bODY_ID;
	}
	public String getBODY_NAME() {
		return BODY_NAME;
	}
	public void setBODY_NAME(String bODY_NAME) {
		BODY_NAME = bODY_NAME;
	}
}
