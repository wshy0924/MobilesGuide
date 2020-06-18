package com.jeremy.antdlib.serviceimpl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.jeremy.antdlib.entity.BodydetailEntity;
import com.jeremy.antdlib.serviceinf.BodyMainServiceInf;
import com.jeremy.antdlib.utils.BeanMapUtil;
@Service
public class BodyMainServiceImpl implements BodyMainServiceInf {
private final static Logger logger = LoggerFactory.getLogger(DiseaseServiceImpl.class);
	
	@Autowired
    private MongoTemplate mongotemplate;

	@Override
	public void insert(Map<String, Object> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Map<String, Object>> findallFromBodymain() {
		// TODO Auto-generated method stub
		List<BodydetailEntity> findList = mongotemplate.findAll( BodydetailEntity.class,"body_main");
		return BeanMapUtil.beansToMaps(findList);
	}

	@Override
	public List<Map<String, Object>> findByOne(Map<String, Object> reqmap) {
		// TODO Auto-generated method stub
		return null;
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
