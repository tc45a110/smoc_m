package com.smoc.cloud.common.smoc.utils;

import com.smoc.cloud.common.auth.qo.Dict;
import com.smoc.cloud.common.auth.qo.DictType;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 通道
 *
 */
public class ChannelUtils {


    /**
     *  获取省份
     * @param dictMap 字典
     * @param type 字典类型
     * @param areaCode 区域代码
     * @return
     */
    public static String getAreaProvince(Map<String, DictType> dictMap, String type, String areaCode) {

        DictType dictType = dictMap.get(type);
        List<Dict> dictList = dictType.getDict();
        if (dictList.size() < 1) {
            return "数据为空或无效";
        }

        String[] areaCodes = areaCode.split(",");

        //封装区域数据
        StringBuilder areaName = new StringBuilder();
        if (areaCodes.length > 1) {
            for (int a = 0; a < areaCodes.length; a++) {
                String name = "";
                for (int i = 0; i < dictList.size(); i++) {
                    Dict dict = dictList.get(i);
                    if (areaCodes[a].equals(dict.getFieldCode())) {
                        name += dict.getFieldName();
                        break;
                    }
                }
                areaName.append(name + " ");
            }
        } else {
            String name = "";
            for (Dict dict : dictList) {
                if (areaCode.equals(dict.getFieldCode())) {
                    name += dict.getFieldName();
                    break;
                }
            }
            areaName.append(name);
        }

        return areaName.toString();
    }

    /**
     * 截取码号
     * 码号的匹配规则：
     * 1068、1069开头的被举报号码，则码号匹配前8位；
     * 1065开头的被举报号码，则码号匹配11位；
     * 对于特殊的106550240018/106550240786开头的，则匹配12位；
     * @param numberCode
     * @return
     */
    public static String getNumbeCode(String numberCode) {

        if(StringUtils.isEmpty(numberCode)){
            return "";
        }

        String number = numberCode.substring(0,4);

        //第一种情况
        if("1068".equals(number) || "1069".equals(number) ){
            if(numberCode.length()>8){
                return numberCode.substring(0,8);
            }
            return numberCode;
        }

        //第二种
        if("1065".equals(number)){

            if(numberCode.contains("106550240018") || numberCode.contains("106550240786")){

                if(numberCode.length()>12){
                    return numberCode.substring(0,12);
                }
                return numberCode;
            }

            if(numberCode.length()>11){
                return numberCode.substring(0,11);
            }
            return numberCode;

        }

        return numberCode;
    }


    public static void  main(String[] args) {
        //测试
        System.out.println(ChannelUtils.getNumbeCode("106550240786822"));
    }

}
