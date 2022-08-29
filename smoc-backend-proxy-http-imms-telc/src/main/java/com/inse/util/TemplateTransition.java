package com.inse.util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.base.common.util.DateUtil;

public class TemplateTransition {
	private static Logger logger = Logger.getLogger(TemplateTransition.class);
	/**
	 * 将平台变量符替换通道的变量符	
	 * @param s
	 * @param index
	 * @return
	 */
	static private String replace(String s, int index) {
		return s.replaceAll(new StringBuffer().append("\\$\\{").append(String.valueOf(index)).append("\\}").toString(),
				"\\$" + (index - 1) + "\\$");
	}

	public static Map<String, String> getTemplate(String MmAttchnent,String channelID,String TemplateTitle,String extend){
	
		
		// 获取通道接口扩展参数
		List<JSONObject> contentList = new ArrayList<JSONObject>();
		Map<String, String> resultMap = ChannelInterfaceUtil.getArgMap(channelID);
	    String loginname= resultMap.get("login-name");
	    String loginpass= resultMap.get("login-pass");
		
	    String data=DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_SECONDE);
	    String authenticator=DigestUtils.md5Hex(loginname+data+loginpass).toUpperCase();

	   
	    
		Map<String, String> map = new HashMap<String, String>();
		JSONObject jsonobject = new JSONObject();
		List<String> list = new ArrayList<String>();
		List<String> urlList = new ArrayList<>();
		try {
			JSONArray array = JSONObject.parseArray(MmAttchnent);
			for (int i = 0; i < array.size(); i++) {
				//总第几帧
				int tindex = i+1;
				//每帧资源
				int s = 1;
				
				String str = array.get(i) + "";
				JSONObject object = JSONObject.parseObject(str);
								
				String frameTxt = object.getString("frameTxt");			
				if (StringUtils.isNotEmpty(frameTxt)) {
					JSONObject textJsonObject = new JSONObject();
					JSONObject optionJsonObject = new JSONObject();
					List<Integer> listflag = new ArrayList<Integer>();	
					// 通过递归来将平台变量符替换通道的变量符
					boolean flag = MyStringUtils.checkTemplate(frameTxt);
					logger.debug("text:" + frameTxt + ",flag=" + flag);
					int index = 1;
					// 从1开始递归增加，将所有变量替换成通道的变量符
					while (flag) {	
						if (frameTxt.contains("${" + index + "}")) {
							frameTxt = replace(frameTxt, index);
							listflag.add(index - 1);
							
						}
						index++;
						flag = MyStringUtils.checkTemplate(frameTxt);
						logger.debug("text:" + frameTxt + ",flag=" + flag);
					}
					
					optionJsonObject.put("Frame",tindex+"-"+s);
					optionJsonObject.put("param",listflag.toString());	
					list.add(optionJsonObject.toString());
					
					textJsonObject.put("Frame",tindex+"-"+s);
					textJsonObject.put("Text",frameTxt);
					contentList.add(textJsonObject);	
					s++;	
				}
				
				String resUrl = object.getString("resUrl");
				if(StringUtils.isNotEmpty(resUrl)) {
					urlList.add(resUrl);
				JSONObject frameJsonObject = new JSONObject();
				frameJsonObject.put("Frame",tindex+"-"+s);
				frameJsonObject.put("FileName",resUrl.substring(resUrl.lastIndexOf("/")+1));
				contentList.add(frameJsonObject);
				s++;	
				}	
			}
			
			jsonobject.put("SiID", loginname);
			jsonobject.put("Authenticator",authenticator);
			jsonobject.put("Date", data);
			jsonobject.put("Method","material");
			jsonobject.put("ExtNum", extend);
			jsonobject.put("Subject", TemplateTitle);
			jsonobject.put("Content",contentList.toString());
			
			map.put("mmdl", jsonobject.toString());
			map.put("options", list.toString());
			map.put("urlpath", String.join(",", urlList));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return map;
	}

	public static void main(String[] args) {
		String sa ="[{\"index\":0,\"resSize\":\"1867\",\"resUrl\":\"/20220531/38a23c7c6b2d4c6eb03e0e0ccb66b264/ec73039362cf432d93a645fa2228e997.png\",\"resType\":\"PIC\",\"resPostfix\":\"png\",\"stayTimes\":\"2\",\"frameTxt\":\"说几句急急急${1}，急急急${2}罗瑟科什\"},{\"index\":1,\"resSize\":\"6\",\"resUrl\":\"/20220422/38a23c7c6b2d4c6eb03e0e0ccb66b264/23086d60027049bb89725cc27f0899f1.png\",\"resType\":\"PIC\",\"resPostfix\":\"png\",\"stayTimes\":\"2\",\"frameTxt\":\"顶顶顶${3}顶顶顶顶顶\"}]";
		//System.out.println(getTemplate(sa).toString());	
		//Map<String, String> map = getTemplate(sa);
		//System.out.println(map.get("mmdl"));

	}

}
