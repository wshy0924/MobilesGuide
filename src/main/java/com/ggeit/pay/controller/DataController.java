package com.ggeit.pay.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.ggeit.pay.config.MobileGuidConstants;
import com.ggeit.pay.impl.BodyMainServiceImpl;
import com.ggeit.pay.impl.BodydetailServiceImpl;
import com.ggeit.pay.impl.DiseaseServiceImpl;
import com.ggeit.pay.impl.RecommendDepartDicServiceImpl;
import com.ggeit.pay.impl.RecommendDepartServiceImpl;
import com.ggeit.pay.inf.RecommendDepartServiceInf;
import com.ggeit.pay.utils.JsonUtils;
import com.ggeit.pay.utils.PayUtil;


@RestController
@CrossOrigin
@RequestMapping("/lay")	
public class DataController {
	private final static Logger logger = LoggerFactory.getLogger(DataController.class);
	
	@Autowired
	private BodyMainServiceImpl bodymainservice;
	@Autowired
	private BodydetailServiceImpl bodydetailservice;
	@Autowired
	private DiseaseServiceImpl diseaseservice;
	@Autowired
	private RecommendDepartServiceImpl recommenddepartService;
	@Autowired
	private RecommendDepartDicServiceImpl recommenddepartdicService;
	/**
	 * 查询一级身体部位
	 * @param request
	 * @param response
	 * @param jsonreq
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/Body_main", method = RequestMethod.POST)
	public Map<String,Object> Bodymain(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String,Object> resultmap = new HashMap<String,Object>();	//返回结果
		
		//接收客户端请求，询身体主部位，返回身体部位和对应标识
		List<Map<String, Object>> bodymaininfo = null;
		try {
			bodymaininfo = bodymainservice.findallFromBodymain();
			resultmap.put("returnCode","0000");
			resultmap.put("data", bodymaininfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resultmap.put("returnCode", "0001");//数据库连接异常
			resultmap.put("returnInfo", "数据库连接超时");
		}
		
		logger.info("resultmap = " + resultmap);
		
		return resultmap;
		
	}
	/**
	 * 查询二级身体部位信息
	 * @param request
	 * @param response
	 * @param jsonreq
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/Body_detail", method = RequestMethod.POST)
	public Map<String,Object> Bodydetail(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String jsonreq) throws Exception {
		logger.info("jsonreq = " + jsonreq);
		Map<String,Object> reqmap = new HashMap<String,Object>();	//客户端原始请求map
		Map<String,Object> datamap = new HashMap<String,Object>();	//客户端原始请求Data数据
		Map<String,Object> resultmap = new HashMap<String,Object>();	//返回结果
		//接收客户端请求数据并转换成map
		reqmap = JsonUtils.JsonToMapObj(jsonreq);
		logger.info("Body_detail reqmap = " + reqmap);
		
		datamap = (Map<String, Object>) reqmap.get("tradeParam");	//请求体数据
		logger.info("Body_detail datamap = " + datamap);
		String body_id = datamap.get("body_id").toString();
		String hospital_id = datamap.get("hospital_id").toString();
		
		//---------------------step1  验证签名-----------------------------
		logger.info("---------------------step1  验证签名-----------------------------");
		String sign = (String) reqmap.get("sign");		//获取sign
		String key = MobileGuidConstants.GGMD5KEY;		//国光MD5密钥
		if (!PayUtil.verifySign(datamap,key,sign)) {
			resultmap.put("returnInfo", "签名错误");
			resultmap.put("returnCode", "0003");
			
			return resultmap;
		}
		
		//---------------------step2  验证参数完整性-----------------------------
		logger.info("---------------------step2  验证参数完整性-----------------------------");
		if("".equals(body_id) || "".equals(hospital_id)) {
			resultmap.put("returnCode", "0002");
			resultmap.put("returnInfo", "请检查参数是否完整！");
			logger.info("resultmap = " + resultmap);
			
			return resultmap;
		}
		
		//接收客户端请求，查询二级部位
		Map<String,Object> detailmap = new HashMap<String,Object>();
		detailmap.put("whereStr", "BODY_ID");
		detailmap.put("whereStrdata", body_id);
		try {
			List<Map<String,Object>> bodydetailinfo = bodydetailservice.findByOne(detailmap);	//根据body_id查询身体详细部位信息
			resultmap.put("returnCode","0000");
			resultmap.put("data", bodydetailinfo);
			
			//根据sid查disease_info_instance，取出sick_name、sick_id，放在对应的每一个二级部位下
			for(int i = 0; i < bodydetailinfo.size();i++) {
				Map<String,Object> bodymap = bodydetailinfo.get(i);
				String SID = (String) bodymap.get("SID");
				Map<String,Object> diseasemap = new HashMap<String,Object>();
				diseasemap.put("SID", SID);
				diseasemap.put("HOSPITAL_ID", hospital_id);
				List<Map<String,Object>> diseaseinfo = diseaseservice.findBySID(diseasemap);	//根据sid查询身体详细部位信息

				for(int j = 0; j < diseaseinfo.size();j++) {
					Map<String,Object> diseaseinfomap = diseaseinfo.get(j);
					//去除不用的key
					diseaseinfomap.remove("_id");
					diseaseinfomap.remove("HOSPITAL_ID");
					diseaseinfomap.remove("DIAGNOSIS");
					diseaseinfomap.remove("SICK_REASON");
					diseaseinfomap.remove("SNAME");
					diseaseinfomap.remove("TREATMENT");
					diseaseinfomap.remove("EDITABLE");
					diseaseinfomap.remove("SICK_DESC");
					diseaseinfomap.remove("SID");
//					diseaseinfomap.remove("IS_CUSTOMIZE");
			
				}
				//将diseaseinfo加入到bodymap中
				bodymap.put("SICK_INFO", diseaseinfo);
				logger.info("bodymap = " + bodymap);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resultmap.put("returnCode", "0001");//数据库连接异常
			resultmap.put("returnInfo", "数据库连接异常");
			
		}
		logger.info("resultmap = " + resultmap);
		return resultmap;
		
	}
	/**
	 * 查询具体病症信息
	 * @param request
	 * @param response
	 * @param jsonreq
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/Disease_info", method = RequestMethod.POST)
	public Map<String,Object> Diseaseinfo(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String jsonreq) throws Exception {

		Map<String,Object> reqmap = new HashMap<String,Object>();	//客户端原始请求map
		Map<String,Object> datamap = new HashMap<String,Object>();	//客户端原始请求Data数据
		Map<String,Object> resultmap = new HashMap<String,Object>();	//返回结果
		//接收客户端请求数据并转换成map
		reqmap = JsonUtils.JsonToMapObj(jsonreq);
		logger.info("Disease_info reqmap = " + reqmap);
		
		datamap = (Map<String, Object>) reqmap.get("tradeParam");	//请求体数据
		logger.info("Disease_info datamap = " + datamap);
		String sid = datamap.get("sid").toString();					//二级部位ID
		String hospital_id = datamap.get("hospital_id").toString();	//医院id
		
		//---------------------step1  验证签名-----------------------------
		logger.info("---------------------step1  验证签名-----------------------------");
		String sign = (String) reqmap.get("sign");		//获取sign
		String key = MobileGuidConstants.GGMD5KEY;		//国光MD5密钥
		if (!PayUtil.verifySign(datamap,key,sign)) {
			resultmap.put("returnInfo", "签名错误");
			resultmap.put("returnCode", "0003");
			
			return resultmap;
		}
		
		//---------------------step2  验证参数完整性-----------------------------
		logger.info("---------------------step2  验证参数完整性-----------------------------");
		if("".equals(sid) || "".equals(hospital_id)) {
			resultmap.put("returnCode", "0002");
			resultmap.put("returnInfo", "请检查参数是否完整！");
			logger.info("resultmap = " + resultmap);
			return resultmap;
		}

			//根据sid和hospitail_id查询返回信息
			Map<String,Object> diseaseinfomap = new HashMap<String,Object>();
			diseaseinfomap.put("HOSPITAL_ID", hospital_id);
			diseaseinfomap.put("SID", sid);
			try {
				List<Map<String,Object>> diseaseinfo = diseaseservice.findBysidAndhospitalid(diseaseinfomap);
				resultmap.put("returnCode","0000");
				resultmap.put("data", diseaseinfo);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				resultmap.put("returnCode", "0001");//数据库连接异常
				resultmap.put("returnInfo", "数据库连接异常");
			}
		
		return resultmap;
	}
	
	/**
	 * 查询具体病症信息
	 * @param request
	 * @param response
	 * @param jsonreq
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/Query_Dept", method = RequestMethod.POST)
	public Map<String,Object> QueryDept(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String jsonreq) throws Exception {

		Map<String,Object> reqmap = new HashMap<String,Object>();	//客户端原始请求map
		Map<String,Object> datamap = new HashMap<String,Object>();	//客户端原始请求Data数据
		Map<String,Object> resultmap = new HashMap<String,Object>();	//返回结果
		//接收客户端请求数据并转换成map
		reqmap = JsonUtils.JsonToMapObj(jsonreq);
		logger.info("Query_Dept reqmap = " + reqmap);
		
		datamap = (Map<String, Object>) reqmap.get("tradeParam");	//请求体数据
		logger.info("Query_Dept datamap = " + datamap);
		String sick_id = datamap.get("sick_id").toString();			//病症标识id
		String hospital_id = datamap.get("hospital_id").toString();	//医院id
		
		//---------------------step1  验证签名-----------------------------
		logger.info("---------------------step1  验证签名-----------------------------");
		String sign = (String) reqmap.get("sign");		//获取sign
		String key = MobileGuidConstants.GGMD5KEY;		//国光MD5密钥
		if (!PayUtil.verifySign(datamap,key,sign)) {
			resultmap.put("returnInfo", "签名错误");
			resultmap.put("returnCode", "0003");
			
			return resultmap;
		}
		//---------------------step2  验证参数完整性-----------------------------
		logger.info("---------------------step2  验证参数完整性-----------------------------");
		if("".equals(sick_id) || "".equals(hospital_id)) {
			resultmap.put("returnCode", "0002");
			resultmap.put("returnInfo", "请检查参数是否完整！");
			logger.info("resultmap = " + resultmap);
			return resultmap;
		}
		//
		//先查recommend_dept
		Map<String,Object> departmap = new HashMap<String,Object>();
		departmap.put("HOSPITAL_ID", hospital_id);
		departmap.put("SICK_ID", sick_id);
		try {
			List<Map<String,Object>> departinfo = recommenddepartService.findBysickidAndhospitalid(departmap);	//根据sid查询身体详细部位信息
			
			if(departinfo.isEmpty()) {
				//当recommend_dept表为空时，只要根据SICK_ID查recommend_dept_dic表
				resultmap.put("Is_recomment","N");
				Map<String,Object> deptinfomap = new HashMap<String,Object>();
				deptinfomap.put("whereStr", "SICK_ID");
				deptinfomap.put("whereStrdata", sick_id);
				List<Map<String,Object>> departinfolist = recommenddepartdicService.findByOne(deptinfomap);	//根据sid查询身体详细部位信息
				resultmap.put("data", departinfolist);	
			}else {
				resultmap.put("Is_recomment", "Y");
				resultmap.put("data", departinfo);
			}

			resultmap.put("returnCode","0000");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resultmap.put("returnCode", "0001");//数据库连接异常
			resultmap.put("returnInfo", "数据库连接异常");
		}

		return resultmap;
	}
	
	@RequestMapping(value = "/QueryallDis", method = RequestMethod.POST)
	public Map<String,Object> QueryallDis(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String jsonreq) throws Exception {

		Map<String,Object> reqmap = new HashMap<String,Object>();	//客户端原始请求map
		Map<String,Object> datamap = new HashMap<String,Object>();	//客户端原始请求Data数据
		Map<String,Object> resultmap = new HashMap<String,Object>();	//返回结果
		//接收客户端请求数据并转换成map
		reqmap = JsonUtils.JsonToMapObj(jsonreq);
		logger.info("QueryallDis reqmap = " + reqmap);
		
		datamap = (Map<String, Object>) reqmap.get("tradeParam");	//请求体数据
		logger.info("QueryallDis datamap = " + datamap);
		String sick_id = datamap.get("sick_id").toString();					//二级部位ID
		String hospital_id = datamap.get("hospital_id").toString();
		
		//---------------------step1  验证签名-----------------------------
		logger.info("---------------------step1  验证签名-----------------------------");
		String sign = (String) reqmap.get("sign");		//获取sign
		String key = MobileGuidConstants.GGMD5KEY;		//国光MD5密钥
		if (!PayUtil.verifySign(datamap,key,sign)) {
			resultmap.put("returnInfo", "签名错误");
			resultmap.put("returnCode", "0003");
			
			return resultmap;
		}
		
		//---------------------step2  验证参数完整性-----------------------------
		logger.info("---------------------step2  验证参数完整性-----------------------------");
		if("".equals(sick_id) || "".equals(hospital_id)) {
			resultmap.put("returnCode", "0002");
			resultmap.put("returnInfo", "请检查参数是否完整！");
			logger.info("resultmap = " + resultmap);
			return resultmap;
		}

			//根据sick_id查询所有病症信息
			Map<String,Object> diseasemap = new HashMap<String,Object>();
			diseasemap.put("HOSPITAL_ID", hospital_id);
			diseasemap.put("SICK_ID", sick_id);
			try {
				List<Map<String,Object>> diseaseinfo = diseaseservice.findByOne(diseasemap);	//根据sid查询身体详细部位信息
				if(!diseaseinfo.isEmpty()) {
					resultmap.put("returnCode","0000");
					resultmap.put("data", diseaseinfo);
				}
				resultmap.put("returnCode","0000");
				resultmap.put("data", diseaseinfo);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				resultmap.put("returnCode", "0001");//数据库连接异常
				resultmap.put("returnInfo", "数据库连接异常");
			}	
		
		
		return resultmap;
	}
	
	
}
