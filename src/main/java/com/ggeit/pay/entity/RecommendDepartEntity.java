package com.ggeit.pay.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "recommended_dept")	//连接的数据库集合
public class RecommendDepartEntity {

	private String DEPT_ID;
	private String DEPT_NAME;
	private String SICK_ID;
	private String HOS_DEPT_ID;
	private String HOS_DEPT_NAME;
	private String HOSPITAL_ID;
	public String getDEPT_ID() {
		return DEPT_ID;
	}
	public void setDEPT_ID(String dEPT_ID) {
		DEPT_ID = dEPT_ID;
	}
	public String getDEPT_NAME() {
		return DEPT_NAME;
	}
	public void setDEPT_NAME(String dEPT_NAME) {
		DEPT_NAME = dEPT_NAME;
	}
	public String getSICK_ID() {
		return SICK_ID;
	}
	public void setSICK_ID(String sICK_ID) {
		SICK_ID = sICK_ID;
	}
	public String getHOS_DEPT_ID() {
		return HOS_DEPT_ID;
	}
	public void setHOS_DEPT_ID(String hOS_DEPT_ID) {
		HOS_DEPT_ID = hOS_DEPT_ID;
	}
	public String getHOS_DEPT_NAME() {
		return HOS_DEPT_NAME;
	}
	public void setHOS_DEPT_NAME(String hOS_DEPT_NAME) {
		HOS_DEPT_NAME = hOS_DEPT_NAME;
	}
	public String getHOSPITAL_ID() {
		return HOSPITAL_ID;
	}
	public void setHOSPITAL_ID(String hOSPITAL_ID) {
		HOSPITAL_ID = hOSPITAL_ID;
	}


	

}
