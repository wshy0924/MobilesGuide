package com.ggeit.pay.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "app_depts")	//科室
public class AppDeptEntity {
	private String DEPT_NAME;
	public String getDEPT_NAME() {
		return DEPT_NAME;
	}
	public void setDEPT_NAME(String dEPT_NAME) {
		DEPT_NAME = dEPT_NAME;
	}
	public String getDEPT_ID() {
		return DEPT_ID;
	}
	public void setDEPT_ID(String dEPT_ID) {
		DEPT_ID = dEPT_ID;
	}
	public String getHOSPITAL_ID() {
		return HOSPITAL_ID;
	}
	public void setHOSPITAL_ID(String hOSPITAL_ID) {
		HOSPITAL_ID = hOSPITAL_ID;
	}
	private String DEPT_ID;
	private String HOSPITAL_ID;
}
