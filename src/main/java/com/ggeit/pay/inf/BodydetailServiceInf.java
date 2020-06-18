package com.ggeit.pay.inf;

import java.util.List;
import java.util.Map;

public interface BodydetailServiceInf {
	void insert(Map<String,Object> map);
	List<Map<String,Object>> findallFromBodydetail();
	List<Map<String,Object>> findByOne(Map<String, Object> reqmap);
	void updatebyOne(Map<String,Object> updatemap) ;
	void delete(Map<String,Object> deletemap) ;

}
