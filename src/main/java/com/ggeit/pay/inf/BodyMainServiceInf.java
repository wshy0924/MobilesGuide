package com.ggeit.pay.inf;

import java.util.List;
import java.util.Map;

public interface BodyMainServiceInf {
	void insert(Map<String,Object> map);
	List<Map<String,Object>> findallFromBodymain();
	List<Map<String,Object>> findByOne(Map<String, Object> reqmap);
	void updatebyOne(Map<String,Object> updatemap) ;
	void delete(Map<String,Object> deletemap) ;
}
