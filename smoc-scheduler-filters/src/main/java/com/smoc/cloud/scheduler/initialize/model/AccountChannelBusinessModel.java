package com.smoc.cloud.scheduler.initialize.model;

import com.smoc.cloud.scheduler.initialize.entity.AccountChannelInfo;

import java.util.*;

/**
 * 业务账号、通道业务模型
 */
public class AccountChannelBusinessModel {

    /**
     * 业务账号
     */
    private String accountId;

    /**
     * 移动
     */
    private Integer cmccPrivilegeWeight = 0;
    private Integer cmccNormalWeight = 0;
    private Integer cmccSuspendWeight = 0;
    Map<String, Map<Integer, AccountChannelInfo>> cmccProvincePrivilegeChannels = new HashMap<>();//特权通道PRIVILEGE
    Map<String, List<AccountChannelInfo>> cmccProvinceNormalChannels = new HashMap<>();//普通通道NORMAL
    Map<String, List<AccountChannelInfo>> cmccProvinceSuspendChannels = new HashMap<>(); //普通通道SUSPEND
    private List<AccountChannelInfo> cmccPrivilegeChannels = new ArrayList<>(); //特权通道PRIVILEGE
    private List<AccountChannelInfo> cmccNormalChannels = new ArrayList<>(); //普通通道NORMAL
    private List<AccountChannelInfo> cmccSuspendChannels = new ArrayList<>(); //普通通道SUSPEND

    /**
     * 电信
     */
    private Integer telcPrivilegeWeight = 0;
    private Integer telcNormalWeight = 0;
    private Integer telcSuspendWeight = 0;
    Map<String, List<AccountChannelInfo>> telcProvincePrivilegeChannels = new HashMap<>();//特权通道PRIVILEGE
    Map<String, List<AccountChannelInfo>> telcProvinceNormalChannels = new HashMap<>();//普通通道NORMAL
    Map<String, List<AccountChannelInfo>> telcProvinceSuspendChannels = new HashMap<>();//普通通道SUSPEND
    private List<AccountChannelInfo> telcPrivilegeChannels = new ArrayList<>(); //特权通道PRIVILEGE
    private List<AccountChannelInfo> telcNormalChannels = new ArrayList<>();//普通通道NORMAL
    private List<AccountChannelInfo> telcSuspendChannels = new ArrayList<>();//普通通道SUSPEND

    /**
     * 联通
     */
    private Integer unicPrivilegeWeight = 0;
    private Integer unicNormalWeight = 0;
    private Integer unicSuspendWeight = 0;
    Map<String, List<AccountChannelInfo>> unicProvincePrivilegeChannels = new HashMap<>();//特权通道PRIVILEGE
    Map<String, List<AccountChannelInfo>> unicProvinceNormalChannels = new HashMap<>();//普通通道NORMAL
    Map<String, List<AccountChannelInfo>> unicProvinceSuspendChannels = new HashMap<>();//普通通道SUSPEND
    private List<AccountChannelInfo> unicPrivilegeChannels = new ArrayList<>(); //特权通道PRIVILEGE
    private List<AccountChannelInfo> unicNormalChannels = new ArrayList<>();//普通通道NORMAL
    private List<AccountChannelInfo> unicSuspendChannels = new ArrayList<>();//普通通道SUSPEND

    /**
     * 国际
     */
    private Integer intlPrivilegeWeight = 0;
    private Integer intlNormalWeight = 0;
    private Integer intlSuspendWeight = 0;
    Map<String, List<AccountChannelInfo>> intlProvincePrivilegeChannels = new HashMap<>();//特权通道PRIVILEGE
    Map<String, List<AccountChannelInfo>> intlProvinceNormalChannels = new HashMap<>();//普通通道NORMAL
    Map<String, List<AccountChannelInfo>> intlProvinceSuspendChannels = new HashMap<>();//普通通道SUSPEND
    private List<AccountChannelInfo> intlPrivilegeChannels = new ArrayList<>(); //特权通道PRIVILEGE
    private List<AccountChannelInfo> intlNormalChannels = new ArrayList<>();//普通通道NORMAL
    private List<AccountChannelInfo> intlSuspendChannels = new ArrayList<>();//普通通道SUSPEND

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
    public void buildBusiness() {
        if (cmccPrivilegeChannels.size() > 0) {
            for (AccountChannelInfo accountChannelInfo : cmccPrivilegeChannels) {
                Set<String> areaCodes = accountChannelInfo.getAreaCodes();
                if (areaCodes.size() < 1) {
                    return;
                }
                for (String areaCode : areaCodes) {
                    String province = areaCode;
                    Map<Integer, AccountChannelInfo> provinceMap = cmccProvincePrivilegeChannels.get(province);
                    if (null == provinceMap) {
                        provinceMap = new HashMap<>();
                    }
                    handleChannelWeight(provinceMap, accountChannelInfo);
                    cmccProvincePrivilegeChannels.put(province, provinceMap);
                }
            }
        }
    }

    /**
     * 处理通道的权重
     *
     * @param provinceMap
     * @param accountChannelInfo
     */
    public void handleChannelWeight(Map<Integer, AccountChannelInfo> provinceMap, AccountChannelInfo accountChannelInfo) {
        Integer currentWeight = provinceMap.size();
        Integer channelWeight = accountChannelInfo.getChannelWeight() == 0 ? 1 : accountChannelInfo.getChannelWeight();
        Integer totalWeight = currentWeight + channelWeight;
        for (int i = currentWeight; i < totalWeight; i++) {
            provinceMap.put(i, accountChannelInfo);
        }
    }

}
