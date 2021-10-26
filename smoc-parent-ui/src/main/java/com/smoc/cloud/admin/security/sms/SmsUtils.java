package com.smoc.cloud.admin.security.sms;

import com.google.gson.Gson;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.utils.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import java.net.URLEncoder;
import java.util.*;

/**
 * 发送短信
 * 2019/5/14 11:37
 **/
@Slf4j
@Controller
public class SmsUtils {

    /**
     * 短信方式发送
     * @param user
     * @throws Exception
     */
    public static void sendShortMessage(SecurityUser user,String sysName,MessageConfig messageConfig) {
        try {
            String url = messageConfig.getUrl();
            String timestamp = DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmss");
            String username = messageConfig.getUsername();
            String password = messageConfig.getPassword();
            String templateid = messageConfig.getTemplateid();

            StringBuffer signStr = new StringBuffer("timestamp=");
            signStr.append(timestamp);
            signStr.append("&username=");
            signStr.append(username);
            signStr.append("&password=");
            signStr.append(password);
            String sign = MD5.md5(signStr.toString());

            List<String> content = new ArrayList<>();
            if("meip".equals(sysName)){
                content.add(user.getPhone()+"|"+"您好，用户"+user.getCorporation()+user.getCode()+"已于"+DateTimeUtils.getDateTimeFormat(new Date())+"登录自服务平台，如非本人操作请及时联系管理员，谢谢！");
            }
            if("meip-admin".equals(sysName)){
                content.add(user.getPhone()+"|"+"您好，用户"+user.getRealName()+"已于"+ DateTimeUtils.getDateTimeFormat(new Date())+"登录管理中心，如非本人操作请及时联系管理员，谢谢！");
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("username", username);
            map.put("templateid", templateid);
            map.put("content", content);
            map.put("timestamp", timestamp);
            map.put("sign", sign);

            String sentJson = new Gson().toJson(map);
            String formatJson = URLEncoder.encode(sentJson,"UTF-8");
            log.info("[短信发送-登录][短信格式化内容]数据：{}",sentJson);

            String result = Okhttp3Utils.postJson(url,formatJson);
            log.info("[短信发送-登录][接口响应]数据：{}",result);
        } catch (Exception e) {
            log.info("[短信发送-登录][接口响应]数据：{}",e.getMessage());
        }
    }
}
