package com.ggeit.pay.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.ggeit.pay.entity.AppDeptEntity;
import com.ggeit.pay.inf.AppDeptServiceinf;
import com.ggeit.pay.utils.BeanMapUtil;


@Service
public class AppDeptServiceImpl implements AppDeptServiceinf{
	private final static Logger logger = LoggerFactory.getLogger(AppDeptServiceImpl.class);
	
	@Autowired
	private MongoTemplate mongotemplate;

	@Override
	public void insert(Map<String, Object> map) {
		// TODO Auto-generated method stub
		AppDeptEntity dept = new AppDeptEntity();
		dept.setDEPT_ID(map.get("DEPT_ID").toString());
		dept.setDEPT_NAME(map.get("DEPT_NAME").toString());
		//dept.setHOSPITAL_ID(map.get("HOSPITAL_ID").toString());
		
		mongotemplate.save(dept, "app_depts");
	}

	@Override
	public List<Map<String, Object>> findallFromAppDept() {
		// TODO Auto-generated method stub
		List<AppDeptEntity> findList = mongotemplate.findAll( AppDeptEntity.class,"app_depts");

		//return new Gson().toJson(findList);
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
		List<AppDeptEntity> datalist = mongotemplate.find(query, AppDeptEntity.class,"app_depts");
		logger.info("datalist.stream: " + BeanMapUtil.beansToMaps(datalist));
		//String Str = new Gson().toJson(datalist);
		//return Str;
		return BeanMapUtil.beansToMaps(datalist);
	}

	@Override
	public void updatebyOne(Map<String, Object> updatemap) {
		// TODO Auto-generated method stub
		String whereStr = (String) updatemap.get("whereStr");
		String whereStrData = (String) updatemap.get("whereStrData");
		
		Query query = new Query();
	    query.addCriteria(Criteria.where(whereStr).is(whereStrData));
	    
	    Update update = new Update();
	    //update.addToSet("DEPT_ID", updatemap.get("DEPT_ID"));
	    //update.addToSet("DEPT_NAME", updatemap.get("DEPT_NAME"));
	    update.set("DEPT_ID", updatemap.get("DEPT_ID"));
	    update.set("DEPT_NAME", updatemap.get("DEPT_NAME"));
	
	    
	    mongotemplate.upsert(query, update, "app_depts");
	}

	@Override
	public void delete(Map<String, Object> deletemap) {
		// TODO Auto-generated method stub
		String whereStr = (String) deletemap.get("whereStr");
		String whereStrData = (String) deletemap.get("whereStrData");
		
		Query query = new Query();
	    query.addCriteria(Criteria.where(whereStr).is(whereStrData));
	    
	    mongotemplate.remove(query, "app_depts");
	}

}
