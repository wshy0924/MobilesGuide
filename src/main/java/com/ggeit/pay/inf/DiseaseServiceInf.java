package com.ggeit.pay.inf;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface DiseaseServiceInf {
	void insert(Map<String,Object> map);
	List<Map<String,Object>> findByOne(Map<String, Object> reqmap);
	void updatebyOne(Map<String,Object> updatemap) ;
	List<Map<String,Object>> findallFromDiseaseinfo(int current,int pageSize);
	void delete(Map<String,Object> deletemap) ;
	List<Map<String, Object>> findBysidAndhospitalid(Map<String, Object> reqmap);
	List<Map<String, Object>> findBySID(Map<String, Object> reqmap);
}
