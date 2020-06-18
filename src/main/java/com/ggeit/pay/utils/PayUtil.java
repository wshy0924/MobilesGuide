package com.ggeit.pay.utils;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.ggeit.pay.config.MobileGuidConstants;

@Service
public class PayUtil {

	private final static Logger logger = LoggerFactory.getLogger(PayUtil.class);

	
	
	public static String alicreateOrderID() {
		return aligetTime() + aligetRandom(6);
	}
	
	public static String aligetTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(System.currentTimeMillis());
	}
	
	public static String aligetRandom(int len) {
		StringBuffer flag = new StringBuffer();
		String sources = "0123456789";
		Random rand = new Random();
		for (int j = 0; j < len; j++) {
			flag.append(sources.charAt(rand.nextInt(9)) + "");
		}
		return flag.toString();
	}
	/**
	 * 生成待签名数据
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	   public static String builderSignStr(Map<String, Object> params,String md5key) throws Exception {
	        Set<String> keySet = params.keySet();
	        List<String>keyList = new ArrayList<String>(keySet);
	        Collections.sort(keyList);
	        StringBuilder sb = new StringBuilder();
	        for (String key : keyList) {
	            sb.append(key);
	            sb.append("=");
	            sb.append(params.get(key));
	            sb.append("&");
	        }
	        sb.deleteCharAt(sb.length() - 1); //去掉最后一个&
	        sb.append(md5key);
	        logger.info("builderSignStr= " + sb.toString());
	        logger.info("验证sign:" + MD5(sb.toString()).toUpperCase());
	        return MD5(sb.toString()).toUpperCase();
	    }
	   
	   public static String YZbuilderSignStr(Map<String, Object> params,String md5key) throws Exception {
	      
		 String paramsStr = JsonUtils.MapToJson(params);
	        StringBuffer sb = new StringBuffer();
	        sb.append(paramsStr);
	        sb.append(md5key);
	        logger.info("YZbuilderSignStr= " + sb.toString());
	        logger.info("验证sign:" + MD5(sb.toString()).toUpperCase());
	        return MD5(sb.toString()).toUpperCase();
	    }
	   
		/**
		 * 微信签名
		 * 
		 * @param data
		 * @param key
		 * @return
		 */
		public static String generateSignature(Map<String, String> data, String key) throws Exception {
			Set<String> keySet = data.keySet();
			String[] keyArray = keySet.toArray(new String[keySet.size()]);
			Arrays.sort(keyArray);
			StringBuilder sb = new StringBuilder();
			for (String k : keyArray) {
				if (k.equals(MobileGuidConstants.FIELD_SIGN)) {
					continue;
				}
				// if (data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
				// sb.append(k).append("=").append(data.get(k).trim()).append("&");
				sb.append(k).append("=").append(data.get(k).trim()).append("&");
			}
			sb.append("key=").append(key);
			logger.info("sb = " + sb.toString());
			return MD5(sb.toString()).toUpperCase();
		}
		
		/**
		 * 生成 MD5
		 *
		 * @param data
		 *            待处理数据
		 * @return MD5结果
		 */
		public static String MD5(String data) throws Exception {
			java.security.MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] array = md.digest(data.getBytes("UTF-8"));
			StringBuilder sb = new StringBuilder();
			for (byte item : array) {
				sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString().toUpperCase();
		}
	
		/**
		 * 签名校验
		 * @param data
		 * @param key
		 * @param sign
		 * @return
		 * @throws Exception
		 */
		public static boolean verifySign(Map<String, Object> data, String key, String sign) throws Exception {
				//调用加签方法，看加签后的签名是否和接收到的一致        
				String encryptData = YZbuilderSignStr(data, key);        
				if (encryptData.equals(sign)) {
					return true;   
					}else{	
					return false; 
					
					}
			}

		/**
		 * 验证公共参数完整性
		 * 
		 * @param requestmap
		 * @return
		 */
		public  static boolean verifyParameter(Map<String, Object> requestmap) {
			if(!requestmap.get("mid").equals("") && !requestmap.get("tid").equals("") 
					&& !requestmap.get("instMid").equals("") && !requestmap.get("msgSrc").equals("")) {
				return true;
			}
			return false;
		}
//		
//		public  static Map<String,Object> Parameter(Map<String, Object> requestmap) {
//			Map<String,Object> resultmap = new HashMap<String,Object>();
//			Map<String,Object> resultdatamap = new HashMap<String,Object>();
//			
//			if(!requestmap.get("merOrderId").equals("") && !(requestmap.get("merOrderId") ==null)) {
//				resultmap.put("merOrderId", requestmap.get("merOrderId"));
//			}
//			if(!requestmap.get("totalAmount").equals("") && !(requestmap.get("totalAmount") ==null)) {
//				resultmap.put("totalAmount", requestmap.get("totalAmount"));
//			}
//			if(!requestmap.get("msgType").equals("") && !(requestmap.get("msgType") ==null)) {
//				resultmap.put("msgType", requestmap.get("msgType"));
//			}
//			if(!requestmap.get("notifyUrl").equals("") && !(requestmap.get("notifyUrl") ==null)) {
//				resultmap.put("notifyUrl", requestmap.get("notifyUrl"));
//			}
//			return resultdatamap;
//		}
		
		/**
		 * 元转分 
		 * 1.23-->123
		 * 银联商务微信支付中，"分"为无小数点类型
		 * @param amount
		 * @return
		 */
		public static String changeY2F(String amount) {
			BigDecimal price = BigDecimal.valueOf(new Double(amount)).multiply(new BigDecimal(100));
			DecimalFormat df2 =new DecimalFormat("#");  //定义分格式
		    String str2 =df2.format(price); 
	    return str2;
		}
		
		/**
		 * 元转分 
		 * 1.23-->123.00
		 * 转化后保留小数点后两位
		 * @param amount
		 * @return
		 */
		public static String wxchangeY2F(String amount) {
		
	    return BigDecimal.valueOf(new Double(amount)).multiply(new BigDecimal(100)).toString() ;
		}
		
		
		/**
		 * 金额分转元
		 */
		/**
		 * 将字符串"分"转换成"元"（长格式），如：100分被转换为1.00元。
		 * @param s
		 * @return
		 */
		public static String convertCent2Dollar(String s) {
		    if("".equals(s) || s ==null){
		        return "";
		    }
		    long l;
		    if(s.length() != 0) {
		        if(s.charAt(0) == '+') {
		            s = s.substring(1);
		        }
		        l = Long.parseLong(s);
		    } else {
		        return "";
		    }
		    boolean negative = false;
		    if(l < 0) {
		        negative = true;
		        l = Math.abs(l);
		    }
		    s = Long.toString(l);
		    if(s.length() == 1) {
		        return (negative ? ("-0.0" + s) : ("0.0" + s));
		    }
		    if(s.length() == 2) {
		        return (negative ? ("-0." + s) : ("0." + s));
		    }else {
		        return (negative ? ("-" + s.substring(0, s.length() - 2) + "." + s
		                .substring(s.length() - 2)) : (s.substring(0,
		                s.length() - 2)
		                + "." + s.substring(s.length() - 2)));
		    }
		}

		/**
		 * 将字符串"分"转换成"元"（短格式），如：100分被转换为1元。
		 * @param s
		 * @return
		 */
		public static String changeF2Y(String s) {
		    String ss = convertCent2Dollar(s);
		    ss = "" + Double.parseDouble(ss);
		    if(ss.endsWith(".0")) {
		        return ss.substring(0, ss.length() - 2);
		    }
		    if(ss.endsWith(".00")) {
		        return ss.substring(0, ss.length() - 3);
		    }else {
		        return ss;
		    }
		}

		

}
