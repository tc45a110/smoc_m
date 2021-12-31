package com.smoc.cloud.common.smoc.utils;

import com.smoc.cloud.common.auth.qo.Dict;
import com.smoc.cloud.common.auth.qo.DictType;

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
}
