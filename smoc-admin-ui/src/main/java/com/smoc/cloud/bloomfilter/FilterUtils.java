package com.smoc.cloud.bloomfilter;

import io.rebloom.client.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 数据过滤工具类
 * 描述：判断是否存在
 */
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class FilterUtils {

    @Autowired
    private BloomFilterRedis bloomFilterRedis;

    /**
     * 名单过滤 注意：白名单过滤与黑名单过滤结果表达处理，即存在即为true
     *
     * @param filterFlag 过滤器标记，通过该字段可以分出用那个过滤器  现在可以是企业CODE过滤  系统过滤 为 "system"
     * @param fiterType  过滤类型，结合filterFlag 来决定用那个过滤器 比如白名单、黑名单  1：白名单 2：黑名单 3：系统黑名单 4、已删除企业黑名单 5、已删除系统黑名单
     * @param targets    要过滤的数据
     * @return boolean[] 对应于 targets;当返回 null 时：表示参数不符合规则、过滤器不存在
     */
    public boolean[] filter(String filterFlag, String fiterType, String[] targets) {

        if (null == targets || targets.length < 1) {
            return null;
        }

        if (targets.length > 1000000) {
            return null;
        }

        //根据 过滤类型 组织过滤器名称
        String filterName = buildFilterName(filterFlag, fiterType);
        if (StringUtils.isEmpty(filterName)) {
            return null;
        }

        boolean[] booleans = null;
        try {
            Client client = bloomFilterRedis.getClient();

            //判断过滤器是否存在
            boolean isExist = client.exists("filterList", filterName);
            if (!isExist) {
                log.error("[名单过滤]数据： 过滤器{}不存在", filterName);
                return null;
            }

            log.info("[名单过滤]数据：应用过滤器：{}，开始：{}", filterName, System.currentTimeMillis());
            booleans = client.existsMulti(filterName, targets);
            log.info("[名单过滤]数据：应用过滤器：{}，结束：{}", filterName, System.currentTimeMillis());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return booleans;
    }

    /**
     * 数据添加到过滤器
     *
     * @param filterFlag 过滤器标记，通过该字段可以分出用那个过滤器  现在可以是企业CODE过滤  系统过滤 为 "system"
     * @param fiterType  过滤类型，结合filterFlag 来决定用那个过滤器 比如白名单、黑名单  1：白名单 2：黑名单 3：系统黑名单
     * @param targets    要过滤的数据
     * @return
     */
    public boolean[] addItemsFilter(String filterFlag, String fiterType, String[] targets) {

        if (null == targets || targets.length < 1) {
            return null;
        }

        //根据 过滤类型 组织过滤器名称
        String filterName = buildFilterName(filterFlag, fiterType);
        if (StringUtils.isEmpty(filterName)) {
            return null;
        }

        boolean[] booleans = null;

        try {

            Client client = bloomFilterRedis.getClient();

            //创建过滤器 或 判断过滤器是否存在，逻辑是如果过滤器不存在则创建过滤器，如果存在则返回
            createFilter(filterName, fiterType);

            log.info("[过滤器添加数据]数据：应用过滤器：{}，开始：{}", filterName, System.currentTimeMillis());
            booleans = client.addMulti(filterName, targets);
            log.info("[过滤器添加数据]数据：应用过滤器：{}，结束：{}", filterName, System.currentTimeMillis());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return booleans;
    }

    /**
     * 创建过滤器 或 判断过滤器是否存在，逻辑是如果过滤器不存在则创建过滤器，如果存在则返回
     * 描述：用filterList 保存已创建的过滤器
     * 把已创建过的过滤器放到 filterList 里
     * 向过滤器添加数据的时候，判断过滤器是否存在，如不存在创建过滤器
     *
     * @param filterName 过滤器名称
     * @return
     */
    public void createFilter(String filterName, String fiterType) {

        try {

            Client client = bloomFilterRedis.getClient();
            //如果不存在 则创建过滤器
            boolean isExist = client.exists("filterList", filterName);
            log.info("[创建或判断过滤器是否存在]数据：应用过滤器：{}-{}，开始：{}", filterName,isExist, System.currentTimeMillis());
            if (!isExist) {
                if ("1".equals(fiterType)) { //表示创建企业白名单  5千万量
                    client.add("filterList", filterName);
                    client.createFilter(filterName, 50000000, 0.0001);
                }
                if ("2".equals(fiterType)) { //表示创建企业黑名单  100万量
                    client.add("filterList", filterName);
                    client.createFilter(filterName, 1000000, 0.0001);
                }
                if ("3".equals(fiterType)) { //表示创建系统黑名单 5千万量
                    client.add("filterList", filterName);
                    client.createFilter(filterName, 50000000, 0.0001);
                }

                if ("4".equals(fiterType)) { //企业已删除黑名单过滤  10万量
                    client.add("filterList", filterName);
                    client.createFilter(filterName, 100000, 0.0001);
                }
                if ("5".equals(fiterType)) { //系统已删除黑名单过滤 10万量
                    client.add("filterList", filterName);
                    client.createFilter(filterName, 100000, 0.0001);
                }
            }

            //log.info("[创建或判断过滤器是否存在]数据：应用过滤器：{}，结束：{}", filterName, System.currentTimeMillis());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化过滤器
     */
    public void initFilter() {

        try {
            Client client = bloomFilterRedis.getClient();

            log.info("[初始化过滤器]数据：应用过滤器：{}，开始：{}", "filterList", System.currentTimeMillis());
            //初始化过滤器 filterList
            client.createFilter("filterList", 50000, 0.000001);
            log.info("[初始化过滤器]数据：应用过滤器：{}，结束：{}", "filterList", System.currentTimeMillis());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 构建过滤器名称
     *
     * @param filterFlag 过滤器标记，通过该字段可以分出用那个过滤器  现在可以是企业CODE过滤  系统过滤 为 "system"
     * @param fiterType  过滤类型，结合filterFlag 来决定用那个过滤器 比如白名单、黑名单  1：白名单 2：黑名单 3：系统黑名单
     * @return 过滤器名称，如果为null 则表示fiterType 不符合规则
     */
    public String buildFilterName(String filterFlag, String fiterType) {

        String filterName = null;

        //白名单过滤
        if ("1".equals(fiterType)) {
            filterName = filterFlag.trim() + "WhiteFilter";
        }

        //企业黑名单过滤
        if ("2".equals(fiterType)) {
            filterName = filterFlag.trim() + "BlackFilter";
        }

        //系统黑名单过滤
        if ("3".equals(fiterType)) {
            filterName = filterFlag.trim() + "BlackFilter";
        }

        //企业已删除黑名单过滤
        if ("4".equals(fiterType)) {
            filterName = filterFlag.trim() + "DeleteFilter";
        }

        //系统已删除黑名单过滤
        if ("5".equals(fiterType)) {
            filterName = filterFlag.trim() + "DeleteFilter";
        }

        return filterName;
    }
}
