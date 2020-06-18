package com.ggeit.pay.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ggeit.pay.entity.BodydetailEntity;
import com.ggeit.pay.inf.BodydetailServiceInf;
import com.ggeit.pay.utils.BeanMapUtil;



@Service
public class BodydetailServiceImpl implements BodydetailServiceInf {
private final static Logger logger = LoggerFactory.getLogger(DiseaseServiceImpl.class);
	
	@Autowired
    private MongoTemplate mongotemplate;

	@Override
	public void insert(Map<String, Object> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Map<String, Object>> findallFromBodydetail() {
		// TODO Auto-generated method stub
		List<BodydetailEntity> findList = mongotemplate.findAll( BodydetailEntity.class,"body_detail");
		return BeanMapUtil.beansToMaps(findList);
	}

	@Override
	public List<Map<String, Object>> findByOne(Map<String, Object> reqmap) {
		// TODO Auto-generated method stub
		String whereStr =(String) reqmap.get("whereStr");
		String whereStrdata =(String) reqmap.get("whereStrdata");
		logger.info("whereStr = " + whereStr);
		Query query = new Query();	
		query.addCriteria(Criteria.where(whereStr).is(whereStrdata));
		List<BodydetailEntity> datalist = mongotemplate.find(query, BodydetailEntity.class,"body_detail");
		logger.info("datalist.stream: " + BeanMapUtil.beansToMaps(datalist));
		//String Str = new Gson().toJson(datalist);
		//return Str;
		return BeanMapUtil.beansToMaps(datalist);
	}

	@Override
	public void updatebyOne(Map<String, Object> updatemap) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Map<String, Object> deletemap) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	

}
