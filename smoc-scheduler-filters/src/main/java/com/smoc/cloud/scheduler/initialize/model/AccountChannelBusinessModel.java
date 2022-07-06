package com.smoc.cloud.scheduler.initialize.model;

import com.smoc.cloud.scheduler.initialize.entity.AccountChannelInfo;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 账号通道路由业务模型
 * 1、移动、电信、联通、国际
 * 2、特权通道、普通通道、挂起通道
 * 3、省份通道支持
 * 4、权重分布
 */
public class AccountChannelBusinessModel {

    /**
     * 移动
     */
    // Map<areaCode, Map<Integer, channelId>>
    private Map<String, Map<Integer, String>> cmccProvincePrivilegeChannels = new HashMap<>();//特权通道PRIVILEGE
    private Map<String, Map<Integer, String>> cmccProvinceNormalChannels = new HashMap<>();//普通通道NORMAL
    private Map<String, Map<Integer, String>> cmccProvinceSuspendChannels = new HashMap<>(); //普通通道SUSPEND
    //中间数据移动通道列表
    private List<AccountChannelInfo> cmccPrivilegeChannels = new ArrayList<>(); //特权通道PRIVILEGE
    private List<AccountChannelInfo> cmccNormalChannels = new ArrayList<>(); //普通通道NORMAL
    private List<AccountChannelInfo> cmccSuspendChannels = new ArrayList<>(); //普通通道SUSPEND

    /**
     * 电信
     */
    // Map<areaCode, Map<Integer, channelId>>
    private Map<String, Map<Integer, String>> telcProvincePrivilegeChannels = new HashMap<>();//特权通道PRIVILEGE
    private Map<String, Map<Integer, String>> telcProvinceNormalChannels = new HashMap<>();//普通通道NORMAL
    private Map<String, Map<Integer, String>> telcProvinceSuspendChannels = new HashMap<>();//普通通道SUSPEND
    //中间数据电信通道列表
    private List<AccountChannelInfo> telcPrivilegeChannels = new ArrayList<>(); //特权通道PRIVILEGE
    private List<AccountChannelInfo> telcNormalChannels = new ArrayList<>();//普通通道NORMAL
    private List<AccountChannelInfo> telcSuspendChannels = new ArrayList<>();//普通通道SUSPEND

    /**
     * 联通
     */
    // Map<areaCode, Map<Integer, channelId>>
    private Map<String, Map<Integer, String>> unicProvincePrivilegeChannels = new HashMap<>();//特权通道PRIVILEGE
    private Map<String, Map<Integer, String>> unicProvinceNormalChannels = new HashMap<>();//普通通道NORMAL
    private Map<String, Map<Integer, String>> unicProvinceSuspendChannels = new HashMap<>();//普通通道SUSPEND
    //中间数据电信通道列表
    private List<AccountChannelInfo> unicPrivilegeChannels = new ArrayList<>(); //特权通道PRIVILEGE
    private List<AccountChannelInfo> unicNormalChannels = new ArrayList<>();//普通通道NORMAL
    private List<AccountChannelInfo> unicSuspendChannels = new ArrayList<>();//普通通道SUSPEND

    /**
     * 国际
     */
    // Map<areaCode, Map<Integer, channelId>>
    private Map<String, Map<Integer, String>> intlProvincePrivilegeChannels = new HashMap<>();//特权通道PRIVILEGE
    private Map<String, Map<Integer, String>> intlProvinceNormalChannels = new HashMap<>();//普通通道NORMAL
    private Map<String, Map<Integer, String>> intlProvinceSuspendChannels = new HashMap<>();//普通通道SUSPEND
    //中间数据国际通道列表
    private List<AccountChannelInfo> intlPrivilegeChannels = new ArrayList<>(); //特权通道PRIVILEGE
    private List<AccountChannelInfo> intlNormalChannels = new ArrayList<>();//普通通道NORMAL
    private List<AccountChannelInfo> intlSuspendChannels = new ArrayList<>();//普通通道SUSPEND

    /**
     * 根据运营商、区域编码获取通道
     *
     * @param carrier
     * @param areaCode
     * @return
     */
    public String getChannelId(String carrier, String areaCode) {
        String channelId = null;
        //运营商为移动
        if ("CMCC".equals(carrier)) {
            //先处理特权通道
            if (cmccPrivilegeChannels.size() > 0) {
                Map<Integer, String> channels = unicProvincePrivilegeChannels.get(areaCode);
                if (null != channels && channels.size() > 0) {
                    Integer channelSize = channels.size();
                    if (1 == channelSize) {
                        channelId = channels.get(0);
                    } else {
                        channelId = channels.get(new Random().nextInt(channels.size()));
                    }
                }

                if (!StringUtils.isEmpty(channelId)) {
                    return channelId;
                }
            }
            //再处理普通通道
            if (cmccNormalChannels.size() > 0) {
                Map<Integer, String> channels = cmccProvinceNormalChannels.get(areaCode);
                if (null != channels && channels.size() > 0) {
                    Integer channelSize = channels.size();
                    if (1 == channelSize) {
                        channelId = channels.get(0);
                    } else {
                        channelId = channels.get(new Random().nextInt(channels.size()));
                    }
                }

                if (!StringUtils.isEmpty(channelId)) {
                    return channelId;
                }
            }
        }

        //运营商为电信
        if ("TELC".equals(carrier)) {
            //先处理特权通道
            if (telcPrivilegeChannels.size() > 0) {
                Map<Integer, String> channels = telcProvincePrivilegeChannels.get(areaCode);
                if (null != channels && channels.size() > 0) {
                    Integer channelSize = channels.size();
                    if (1 == channelSize) {
                        channelId = channels.get(0);
                    } else {
                        channelId = channels.get(new Random().nextInt(channels.size()));
                    }
                }

                if (!StringUtils.isEmpty(channelId)) {
                    return channelId;
                }
            }
            //再处理普通通道
            if (telcNormalChannels.size() > 0) {
                Map<Integer, String> channels = telcProvinceNormalChannels.get(areaCode);
                if (null != channels && channels.size() > 0) {
                    Integer channelSize = channels.size();
                    if (1 == channelSize) {
                        channelId = channels.get(0);
                    } else {
                        channelId = channels.get(new Random().nextInt(channels.size()));
                    }
                }

                if (!StringUtils.isEmpty(channelId)) {
                    return channelId;
                }
            }
        }

        //运营商为联通
        if ("UNIC".equals(carrier)) {
            //先处理特权通道
            if (unicPrivilegeChannels.size() > 0) {
                Map<Integer, String> channels = unicProvincePrivilegeChannels.get(areaCode);
                if (null != channels && channels.size() > 0) {
                    Integer channelSize = channels.size();
                    if (1 == channelSize) {
                        channelId = channels.get(0);
                    } else {
                        channelId = channels.get(new Random().nextInt(channels.size()));
                    }
                }

                if (!StringUtils.isEmpty(channelId)) {
                    return channelId;
                }
            }
            //再处理普通通道
            if (unicNormalChannels.size() > 0) {
                Map<Integer, String> channels = unicProvinceNormalChannels.get(areaCode);
                if (null != channels && channels.size() > 0) {
                    Integer channelSize = channels.size();
                    if (1 == channelSize) {
                        channelId = channels.get(0);
                    } else {
                        channelId = channels.get(new Random().nextInt(channels.size()));
                    }
                }

                if (!StringUtils.isEmpty(channelId)) {
                    return channelId;
                }
            }
        }

        //运营商为国际
        if ("INTL".equals(carrier)) {
            //先处理特权通道
            if (intlPrivilegeChannels.size() > 0) {
                Map<Integer, String> channels = intlProvincePrivilegeChannels.get(areaCode);
                if (null != channels && channels.size() > 0) {
                    Integer channelSize = channels.size();
                    if (1 == channelSize) {
                        channelId = channels.get(0);
                    } else {
                        channelId = channels.get(new Random().nextInt(channels.size()));
                    }
                }

                if (!StringUtils.isEmpty(channelId)) {
                    return channelId;
                }
            }
            //再处理普通通道
            if (intlNormalChannels.size() > 0) {
                Map<Integer, String> channels = intlProvinceNormalChannels.get(areaCode);
                if (null != channels && channels.size() > 0) {
                    Integer channelSize = channels.size();
                    if (1 == channelSize) {
                        channelId = channels.get(0);
                    } else {
                        channelId = channels.get(new Random().nextInt(channels.size()));
                    }
                }

                if (!StringUtils.isEmpty(channelId)) {
                    return channelId;
                }
            }
        }

        return channelId;
    }

    /**
     * 业务账号通道数据初始化
     *
     * @param accountChannelInfo
     */
    public void add(AccountChannelInfo accountChannelInfo) {
        if ("CMCC".equals(accountChannelInfo.getCarrier())) {
            //特权通道
            if ("PRIVILEGE".equals(accountChannelInfo.getChannelPriority())) {
                cmccPrivilegeChannels.add(accountChannelInfo);
            }

            //普通通道
            if ("NORMAL".equals(accountChannelInfo.getChannelPriority())) {
                cmccNormalChannels.add(accountChannelInfo);
            }

            //挂起通道
            if ("SUSPEND".equals(accountChannelInfo.getChannelPriority())) {
                cmccSuspendChannels.add(accountChannelInfo);
            }
        }
        if ("TELC".equals(accountChannelInfo.getCarrier())) {
            //特权通道
            if ("PRIVILEGE".equals(accountChannelInfo.getChannelPriority())) {
                telcPrivilegeChannels.add(accountChannelInfo);
            }

            //普通通道
            if ("NORMAL".equals(accountChannelInfo.getChannelPriority())) {
                telcNormalChannels.add(accountChannelInfo);
            }

            //挂起通道
            if ("SUSPEND".equals(accountChannelInfo.getChannelPriority())) {
                telcSuspendChannels.add(accountChannelInfo);
            }
        }
        if ("UNIC".equals(accountChannelInfo.getCarrier())) {
            //特权通道
            if ("PRIVILEGE".equals(accountChannelInfo.getChannelPriority())) {
                unicPrivilegeChannels.add(accountChannelInfo);
            }

            //普通通道
            if ("NORMAL".equals(accountChannelInfo.getChannelPriority())) {
                unicNormalChannels.add(accountChannelInfo);

            }

            //挂起通道
            if ("SUSPEND".equals(accountChannelInfo.getChannelPriority())) {
                unicSuspendChannels.add(accountChannelInfo);
            }
        }
        if ("INTL".equals(accountChannelInfo.getCarrier())) {
            //特权通道
            if ("PRIVILEGE".equals(accountChannelInfo.getChannelPriority())) {
                intlPrivilegeChannels.add(accountChannelInfo);
            }

            //普通通道
            if ("NORMAL".equals(accountChannelInfo.getChannelPriority())) {
                intlNormalChannels.add(accountChannelInfo);
            }

            //挂起通道
            if ("SUSPEND".equals(accountChannelInfo.getChannelPriority())) {
                intlSuspendChannels.add(accountChannelInfo);
            }
        }
    }

    /**
     * 构建业务模型
     */
    public void builder() {
        /**
         * 构建移动通道
         */
        //移动特权通道
        if (cmccPrivilegeChannels.size() > 0) {
            for (AccountChannelInfo accountChannelInfo : cmccPrivilegeChannels) {
                Set<String> areaCodes = accountChannelInfo.getAreaCodes();
                if (areaCodes.size() < 1) {
                    return;
                }
                for (String areaCode : areaCodes) {
                    String province = areaCode;
                    Map<Integer, String> provinceMap = cmccProvincePrivilegeChannels.get(province);
                    if (null == provinceMap) {
                        provinceMap = new HashMap<>();
                    }
                    handleChannelWeight(provinceMap, accountChannelInfo);
                    cmccProvincePrivilegeChannels.put(province, provinceMap);
                }
            }
        }

        //移动普通通道
        if (cmccNormalChannels.size() > 0) {
            for (AccountChannelInfo accountChannelInfo : cmccNormalChannels) {
                Set<String> areaCodes = accountChannelInfo.getAreaCodes();
                if (areaCodes.size() < 1) {
                    return;
                }
                for (String areaCode : areaCodes) {
                    String province = areaCode;
                    Map<Integer, String> provinceMap = cmccProvinceNormalChannels.get(province);
                    if (null == provinceMap) {
                        provinceMap = new HashMap<>();
                    }
                    handleChannelWeight(provinceMap, accountChannelInfo);
                    cmccProvinceNormalChannels.put(province, provinceMap);
                }
            }
        }
        //移动挂起通道
//        if (cmccSuspendChannels.size() > 0) {
//            for (AccountChannelInfo accountChannelInfo : cmccSuspendChannels) {
//                Set<String> areaCodes = accountChannelInfo.getAreaCodes();
//                if (areaCodes.size() < 1) {
//                    return;
//                }
//                for (String areaCode : areaCodes) {
//                    String province = areaCode;
//                    Map<Integer, String> provinceMap = cmccProvincePrivilegeChannels.get(province);
//                    if (null == provinceMap) {
//                        provinceMap = new HashMap<>();
//                    }
//                    handleChannelWeight(provinceMap, accountChannelInfo);
//                    cmccProvinceSuspendChannels.put(province, provinceMap);
//                }
//            }
//        }

        /**
         * 构建联通通道
         */
        //联通特权通道
        if (unicPrivilegeChannels.size() > 0) {
            for (AccountChannelInfo accountChannelInfo : unicPrivilegeChannels) {
                Set<String> areaCodes = accountChannelInfo.getAreaCodes();
                if (areaCodes.size() < 1) {
                    return;
                }
                for (String areaCode : areaCodes) {
                    String province = areaCode;
                    Map<Integer, String> provinceMap = unicProvincePrivilegeChannels.get(province);
                    if (null == provinceMap) {
                        provinceMap = new HashMap<>();
                    }
                    handleChannelWeight(provinceMap, accountChannelInfo);
                    unicProvincePrivilegeChannels.put(province, provinceMap);
                }
            }
        }

        //联通普通通道
        if (unicNormalChannels.size() > 0) {
            for (AccountChannelInfo accountChannelInfo : unicNormalChannels) {
                Set<String> areaCodes = accountChannelInfo.getAreaCodes();
                if (areaCodes.size() < 1) {
                    return;
                }
                for (String areaCode : areaCodes) {
                    String province = areaCode;
                    Map<Integer, String> provinceMap = unicProvinceNormalChannels.get(province);
                    if (null == provinceMap) {
                        provinceMap = new HashMap<>();
                    }
                    handleChannelWeight(provinceMap, accountChannelInfo);
                    unicProvinceNormalChannels.put(province, provinceMap);
                }
            }
        }
        //联通挂起通道
//        if (unicSuspendChannels.size() > 0) {
//            for (AccountChannelInfo accountChannelInfo : unicSuspendChannels) {
//                Set<String> areaCodes = accountChannelInfo.getAreaCodes();
//                if (areaCodes.size() < 1) {
//                    return;
//                }
//                for (String areaCode : areaCodes) {
//                    String province = areaCode;
//                    Map<Integer, String> provinceMap = unicProvincePrivilegeChannels.get(province);
//                    if (null == provinceMap) {
//                        provinceMap = new HashMap<>();
//                    }
//                    handleChannelWeight(provinceMap, accountChannelInfo);
//                    unicProvinceSuspendChannels.put(province, provinceMap);
//                }
//            }
//        }

        /**
         * 构建电信通道
         */
        //联通特权通道
        if (telcPrivilegeChannels.size() > 0) {
            for (AccountChannelInfo accountChannelInfo : telcPrivilegeChannels) {
                Set<String> areaCodes = accountChannelInfo.getAreaCodes();
                if (areaCodes.size() < 1) {
                    return;
                }
                for (String areaCode : areaCodes) {
                    String province = areaCode;
                    Map<Integer, String> provinceMap = telcProvincePrivilegeChannels.get(province);
                    if (null == provinceMap) {
                        provinceMap = new HashMap<>();
                    }
                    handleChannelWeight(provinceMap, accountChannelInfo);
                    telcProvincePrivilegeChannels.put(province, provinceMap);
                }
            }
        }

        //联通普通通道
        if (telcNormalChannels.size() > 0) {
            for (AccountChannelInfo accountChannelInfo : telcNormalChannels) {
                Set<String> areaCodes = accountChannelInfo.getAreaCodes();
                if (areaCodes.size() < 1) {
                    return;
                }
                for (String areaCode : areaCodes) {
                    String province = areaCode;
                    Map<Integer, String> provinceMap = telcProvinceNormalChannels.get(province);
                    if (null == provinceMap) {
                        provinceMap = new HashMap<>();
                    }
                    handleChannelWeight(provinceMap, accountChannelInfo);
                    telcProvinceNormalChannels.put(province, provinceMap);
                }
            }
        }
        //联通挂起通道
//        if (telcSuspendChannels.size() > 0) {
//            for (AccountChannelInfo accountChannelInfo : telcSuspendChannels) {
//                Set<String> areaCodes = accountChannelInfo.getAreaCodes();
//                if (areaCodes.size() < 1) {
//                    return;
//                }
//                for (String areaCode : areaCodes) {
//                    String province = areaCode;
//                    Map<Integer, String> provinceMap = telcProvincePrivilegeChannels.get(province);
//                    if (null == provinceMap) {
//                        provinceMap = new HashMap<>();
//                    }
//                    handleChannelWeight(provinceMap, accountChannelInfo);
//                    telcProvinceSuspendChannels.put(province, provinceMap);
//                }
//            }
//        }

        /**
         * 构建国际通道
         */
        //国际特权通道
        if (intlPrivilegeChannels.size() > 0) {
            for (AccountChannelInfo accountChannelInfo : intlPrivilegeChannels) {
                Set<String> areaCodes = accountChannelInfo.getAreaCodes();
                if (areaCodes.size() < 1) {
                    return;
                }
                for (String areaCode : areaCodes) {
                    String province = areaCode;
                    Map<Integer, String> provinceMap = intlProvincePrivilegeChannels.get(province);
                    if (null == provinceMap) {
                        provinceMap = new HashMap<>();
                    }
                    handleChannelWeight(provinceMap, accountChannelInfo);
                    intlProvincePrivilegeChannels.put(province, provinceMap);
                }
            }
        }

        //国际普通通道
        if (intlNormalChannels.size() > 0) {
            for (AccountChannelInfo accountChannelInfo : intlNormalChannels) {
                Set<String> areaCodes = accountChannelInfo.getAreaCodes();
                if (areaCodes.size() < 1) {
                    return;
                }
                for (String areaCode : areaCodes) {
                    String province = areaCode;
                    Map<Integer, String> provinceMap = intlProvinceNormalChannels.get(province);
                    if (null == provinceMap) {
                        provinceMap = new HashMap<>();
                    }
                    handleChannelWeight(provinceMap, accountChannelInfo);
                    intlProvinceNormalChannels.put(province, provinceMap);
                }
            }
        }
        //国际挂起通道
//        if (intlSuspendChannels.size() > 0) {
//            for (AccountChannelInfo accountChannelInfo : intlSuspendChannels) {
//                Set<String> areaCodes = accountChannelInfo.getAreaCodes();
//                if (areaCodes.size() < 1) {
//                    return;
//                }
//                for (String areaCode : areaCodes) {
//                    String province = areaCode;
//                    Map<Integer, String> provinceMap = intlProvincePrivilegeChannels.get(province);
//                    if (null == provinceMap) {
//                        provinceMap = new HashMap<>();
//                    }
//                    handleChannelWeight(provinceMap, accountChannelInfo);
//                    intlProvinceSuspendChannels.put(province, provinceMap);
//                }
//            }
//        }
    }

    /**
     * 处理通道的权重
     *
     * @param provinceMap
     * @param accountChannelInfo
     */
    public void handleChannelWeight(Map<Integer, String> provinceMap, AccountChannelInfo accountChannelInfo) {
        Integer currentWeight = provinceMap.size();
        Integer channelWeight = accountChannelInfo.getChannelWeight() == 0 ? 1 : accountChannelInfo.getChannelWeight();
        Integer totalWeight = currentWeight + channelWeight;
        for (int i = currentWeight; i < totalWeight; i++) {
            provinceMap.put(i, accountChannelInfo.getChannelId());
        }
    }

}
