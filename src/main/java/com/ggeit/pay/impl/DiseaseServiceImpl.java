package com.ggeit.pay.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggeit.pay.entity.DiseaseEntity;
import com.ggeit.pay.inf.DiseaseServiceInf;
import com.ggeit.pay.utils.BeanMapUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.WriteResult;
import com.mongodb.client.result.UpdateResult;

@Service
public class DiseaseServiceImpl implements DiseaseServiceInf{
	private final static Logger logger = LoggerFactory.getLogger(DiseaseServiceImpl.class);
	
	@Autowired
    private MongoTemplate mongotemplate;
	
	/**
	 * 插入数据库
	 */
	@Override
	public void insert(Map<String, Object> map) {
		// TODO Auto-generated method stub
		DiseaseEntity dis = new DiseaseEntity();
		dis.setSID((String) map.get("SID"));
		dis.setSNAME((String) map.get("SNAME"));
		dis.setSICK_ID((String) map.get("SICK_ID"));
		dis.setSICK_NAME((String) map.get("SICK_NAME"));
		dis.setDIAGNOSIS((String) map.get("DIAGNOSIS"));
		dis.setTREATMENT((String) map.get("TREATMENT"));
		dis.setSICK_REASON((String) map.get("SICK_REASON"));
		dis.setSICK_DESC((String) map.get("SICK_DESC"));
		dis.setHOSPITAL_ID((String) map.get("HOSPITAL_ID"));
		dis.setEDITABLE((String) map.get("EDITABLE"));
		mongotemplate.save(dis, "disease_info");
		//logger.info("deseaseEntity 返回：" + mongotemplate.insert(dis));
		
	}
	/**
	 * 查询所有
	 */
	@Override
	public List<Map<String,Object>> findallFromDiseaseinfo(int current,int pageSize) {
		// TODO Auto-generated method stub
		List<DiseaseEntity> findList = mongotemplate.findAll(DiseaseEntity.class);

		//return new Gson().toJson(findList);
		return BeanMapUtil.beansToMaps(findList);
	}
	/**
	 * 查询单个
	 */
	@Override
	public List<Map<String, Object>> findByOne(Map<String, Object> reqmap) {
		// TODO Auto-generated method stub
		String SICK_ID =(String) reqmap.get("SICK_ID");
		String HOSPITAL_ID =(String) reqmap.get("HOSPITAL_ID");
		
		Query query = new Query();	
		//动态拼接查询条件

		query.addCriteria(Criteria.where("SICK_ID").is(SICK_ID));  
		query.addCriteria(Criteria.where("HOSPITAL_ID").is(HOSPITAL_ID));
		List<DiseaseEntity> datalist = mongotemplate.find(query, DiseaseEntity.class,"disease_info_instance");
		//logger.info("datalist.stream: " + BeanMapUtil.beansToMaps(datalist));
		//String Str = new Gson().toJson(datalist);
		//return Str;
		return BeanMapUtil.beansToMaps(datalist);
	}
	/**
	 * 根据sid查询
	 * 返回sick_id和sick_name
	 */
	@Override
	public List<Map<String, Object>> findBySID(Map<String, Object> reqmap) {
		// TODO Auto-generated method stub
		String SID =(String) reqmap.get("SID");
		String HOSPITAL_ID =(String) reqmap.get("HOSPITAL_ID");
		
		Query query = new Query();	
		//动态拼接查询条件

		query.addCriteria(Criteria.where("SID").is(SID));  
		query.addCriteria(Criteria.where("HOSPITAL_ID").is(HOSPITAL_ID));
		List<DiseaseEntity> datalist = mongotemplate.find(query, DiseaseEntity.class,"disease_info_instance");
		//logger.info("datalist.stream: " + BeanMapUtil.beansToMaps(datalist));
		//String Str = new Gson().toJson(datalist);
		//return Str;
		return BeanMapUtil.beansToMaps(datalist);
	}
	
	@Override
	public List<Map<String, Object>> findBysidAndhospitalid(Map<String, Object> reqmap) {
		// TODO Auto-generated method stub
		String SID =(String) reqmap.get("SID");
		String HOSPITAL_ID =(String) reqmap.get("HOSPITAL_ID");
		logger.info("hospital_id= " + HOSPITAL_ID);
		
		Query query = new Query();	
		//动态拼接查询条件

		query.addCriteria(Criteria.where("SID").is(SID));  
		query.addCriteria(Criteria.where("HOSPITAL_ID").is(HOSPITAL_ID));
		
		List<DiseaseEntity> datalist = mongotemplate.find(query, DiseaseEntity.class,"disease_info_instance");
		//logger.info("datalist.stream: " + BeanMapUtil.beansToMaps(datalist));
		//String Str = new Gson().toJson(datalist);
		//return Str;
		return BeanMapUtil.beansToMaps(datalist);
	}
	/**
	 * 更新
	 * @throws JsonProcessingException 
	 */
	@Override
	public void updatebyOne(Map<String,Object> updatemap)  {
		// TODO Auto-generated method stub
		String whereStr = (String) updatemap.get("whereStr");
		String whereStrData = (String) updatemap.get("whereStrData");
		logger.info("wherestr = " + whereStr);
		logger.info("wherestrdata = " + whereStrData);
		
		Query query = new Query();
	    query.addCriteria(Criteria.where(whereStr).is(whereStrData));
	    
	    Update update = new Update();
//	    update.set("SID", updatemap.get("SID"));
	    update.set("SNAME", updatemap.get("SNAME"));
	    update.set("SICK_NAME", updatemap.get("SICK_NAME"));
//	    update.set("SICK_ID", updatemap.get("SICK_ID"));
	    update.set("DIAGNOSIS", (String)updatemap.get("DIAGNOSIS"));
	    update.set("TREATMENT", updatemap.get("TREATMENT"));
	    update.set("SICK_REASON", updatemap.get("SICK_REASON"));
	    update.set("SICK_DESC", updatemap.get("SICK_DESC"));
//	    update.set("HOSPITAL_ID", updatemap.get("HOSPITAL_ID"));
//	    update.set("EDITABLE", updatemap.get("EDITABLE"));
	
	    mongotemplate.updateFirst(query, update, "disease_info");
	}
	@Override
	public void delete(Map<String, Object> deletemap) {
		// TODO Auto-generated method stub
		String whereStr = (String) deletemap.get("whereStr");
		String whereStrData = (String) deletemap.get("whereStrData");
		
		Query query = new Query();
	    query.addCriteria(Criteria.where(whereStr).is(whereStrData));
	    
	    mongotemplate.remove(query, "disease_info");
	}
	

}
