package com.smoc.cloud.http.filters.service.account;

import com.smoc.cloud.http.filters.utils.FilterResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 屏蔽省份过滤
 */
@Slf4j
@Component
public class MaskProvinceFilter {

    /**
     * 屏蔽省份过滤
     *
     * @param provinceCode     省份编码
     * @param carrier          运营商
     * @param cmccMaskProvince 移动屏蔽省份
     * @param unicMaskProvince 联通屏蔽省份
     * @param telcMaskProvince 电信屏蔽省份
     * @return
     */
    public Map<String, String> filter(String provinceCode, String carrier, Object cmccMaskProvince, Object unicMaskProvince, Object telcMaskProvince) {
        Map<String, String> result = new HashMap<>();
        if (StringUtils.isEmpty(provinceCode) || StringUtils.isEmpty(carrier)) {
            result.put("result", "false");
            return result;
        }

        //移动省份屏蔽
        if (!StringUtils.isEmpty(cmccMaskProvince) && "CMCC".equals(carrier)) {
            Pattern pattern = Pattern.compile(provinceCode);
            Matcher matcher = pattern.matcher(cmccMaskProvince.toString());
            if (matcher.find()) {
                result.put("result", "true");
                result.put("code", FilterResponseCode.LIMIT_MASK_PROVINCE.getCode());
                result.put("message", FilterResponseCode.LIMIT_MASK_PROVINCE.getMessage());
                return result;
            }
            result.put("result", "false");
            return result;
        }

        //联通省份屏蔽
        if (!StringUtils.isEmpty(unicMaskProvince) && "UNIC".equals(carrier)) {
            Pattern pattern = Pattern.compile(provinceCode);
            Matcher matcher = pattern.matcher(unicMaskProvince.toString());
            if (matcher.find()) {
                result.put("result", "true");
                result.put("code", FilterResponseCode.LIMIT_MASK_PROVINCE.getCode());
                result.put("message", FilterResponseCode.LIMIT_MASK_PROVINCE.getMessage());
                return result;
            }
            result.put("result", "false");
            return result;
        }

        //电信省份屏蔽
        if (!StringUtils.isEmpty(telcMaskProvince) && "TELC".equals(carrier)) {
            Pattern pattern = Pattern.compile(provinceCode);
            Matcher matcher = pattern.matcher(telcMaskProvince.toString());
            if (matcher.find()) {
                result.put("result", "true");
                result.put("code", FilterResponseCode.LIMIT_MASK_PROVINCE.getCode());
                result.put("message", FilterResponseCode.LIMIT_MASK_PROVINCE.getMessage());
                return result;
            }
            result.put("result", "false");
            return result;
        }

        result.put("result", "false");
        return result;
    }

}
