package com.jeremy.antdlib.serviceimpl;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jeremy.antdlib.entity.DepartEntity;
import com.jeremy.antdlib.serviceinf.DepartServiceInf;
import com.jeremy.antdlib.utils.BeanMapUtil;

@Service
public class DepartServiceImpl implements DepartServiceInf{
	private final static Logger logger = LoggerFactory.getLogger(DepartServiceImpl.class);
	
	@Autowired
    private MongoTemplate mongotemplate;
	
	/**
	 * 插入数据库
	 */
	@Override
	public void insert(Map<String, Object> map) {
		// TODO Auto-generated method stub
		DepartEntity dept = new DepartEntity();
		dept.setDEPT_ID((String) map.get("DEPT_ID"));
		dept.setDEPT_NAME((String) map.get("DEPT_NAME"));
		dept.setSICK_ID((String) map.get("SICK_ID"));
		
		mongotemplate.save(dept, "recommended_dept");
		
	}
	/**
	 * 查询所有
	 */
	@Override
	public List<Map<String, Object>> findallFromDepartinfo() {
		// TODO Auto-generated method stub
		List<DepartEntity> findList = mongotemplate.findAll( DepartEntity.class,"recommended_dept_dictionary");

		//return new Gson().toJson(findList);
		return BeanMapUtil.beansToMaps(findList);
	}
	/**
	 * 查询单个
	 */
	@Override
	public List<Map<String, Object>> findByOne(Map<String, Object> reqmap) {
		// TODO Auto-generated method stub
		String whereStr =(String) reqmap.get("whereStr");
		String whereStrdata =(String) reqmap.get("whereStrdata");
		logger.info("whereStr = " + whereStr);
		Query query = new Query();	
		query.addCriteria(Criteria.where(whereStr).is(whereStrdata));
		List<DepartEntity> datalist = mongotemplate.find(query, DepartEntity.class,"recommended_dept_dictionary");
		logger.info("datalist.stream: " + BeanMapUtil.beansToMaps(datalist));
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
		
		Query query = new Query();
	    query.addCriteria(Criteria.where(whereStr).is(whereStrData));
	    
	    Update update = new Update();
	    update.addToSet("DEPT_ID", updatemap.get("DEPT_ID"));
	    update.addToSet("DEPT_NAME", updatemap.get("DEPT_NAME"));
	    update.addToSet("SICK_ID", updatemap.get("SICK_ID"));
	
	    
	    mongotemplate.upsert(query, update, "recommended_dept_dictionary");
	}
	
	@Override
	public void delete(Map<String, Object> deletemap) {
		// TODO Auto-generated method stub
		String whereStr = (String) deletemap.get("whereStr");
		String whereStrData = (String) deletemap.get("whereStrData");
		
		Query query = new Query();
	    query.addCriteria(Criteria.where(whereStr).is(whereStrData));
	    
	    mongotemplate.remove(query, "recommended_dept_dictionary");
	}
	

}
