package com.smoc.cloud.filter.service;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.smoc.cloud.common.filters.utils.RedisConstant;
import com.smoc.cloud.common.filters.utils.RedisFilterConstant;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.filter.ExcelModel;
import com.smoc.cloud.common.smoc.filter.FilterBlackListValidator;
import com.smoc.cloud.common.smoc.filter.FilterWhiteListValidator;
import com.smoc.cloud.filter.entity.FilterBlackList;
import com.smoc.cloud.filter.repository.BlackRepository;
import com.smoc.cloud.redis.RedisModuleBloomFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BlackService {

    @Resource
    private BlackRepository blackRepository;

    @Autowired
    private RedisModuleBloomFilter redisModuleBloomFilter;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据群组id查询通讯录
     *
     * @param pageParams
     * @return
     */
    public PageList<FilterBlackListValidator> page(PageParams<FilterBlackListValidator> pageParams) {
        return blackRepository.page(pageParams);
    }

    /**
     * 根据id 查询
     *
     * @param id
     * @return
     */
    public ResponseData findById(String id) {

        Optional<FilterBlackList> data = blackRepository.findById(id);

        //使用了 Optional 的判断方式，判断优雅
        return ResponseDataUtil.buildSuccess(data.orElse(null));
    }

    /**
     * 保存或修改
     *
     * @param filterBlackListValidator
     * @param op                       操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData save(FilterBlackListValidator filterBlackListValidator, String op) {

        //转BaseUser存放对象
        FilterBlackList entity = new FilterBlackList();
        BeanUtils.copyProperties(filterBlackListValidator, entity);

        List<FilterBlackList> data = blackRepository.findByEnterpriseIdAndGroupIdAndMobileAndStatus(entity.getEnterpriseId(), entity.getGroupId(), entity.getMobile(), "1");

        //add查重
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError("组里已经存在此手机号码，请修改");
        }
        //edit查重orgName
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iter = data.iterator();
            while (iter.hasNext()) {
                FilterBlackList organization = (FilterBlackList) iter.next();
                if (!entity.getId().equals(organization.getId())) {
                    status = true;
                    break;
                }
            }
            if (status) {
                return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
            }
        }

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }
        //无论保存或修改，都同步成 未同步状态，系统自动同步黑名单
        entity.setIsSync("0");
        //记录日志
        log.info("[黑名单管理][{}]数据:{}", op, JSON.toJSONString(entity));

        blackRepository.saveAndFlush(entity);
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 根据id 删除
     *
     * @param id
     * @return
     */
    @Transactional
    public ResponseData deleteById(String id) {

        FilterBlackList data = blackRepository.findById(id).get();

        //记录日志
        log.info("[黑名单管理][delete]数据:{}", JSON.toJSONString(data));
        blackRepository.deleteById(id);

        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 批量保存
     *
     * @param filterBlackListValidator
     * @return
     */
    @Async
    public void bathSave(FilterBlackListValidator filterBlackListValidator) {
        log.info("[黑名单管理][导入]数据:{}", JSON.toJSONString(filterBlackListValidator));
        blackRepository.bathSave(filterBlackListValidator);
        //加载黑名单到黑名单过滤器
        if("SYSTEM".equals(filterBlackListValidator.getEnterpriseId())){
            this.loadBlackList();
        }

        if("INDUSTRY".equals(filterBlackListValidator.getEnterpriseId())){
            this.loadIndustryBlackList();
        }

    }

    /**
     * 查询导出数据
     *
     * @param pageParams
     * @return
     */
    public ResponseData<List<ExcelModel>> excelModel(PageParams<FilterBlackListValidator> pageParams) {
        List<ExcelModel> list = blackRepository.excelModel(pageParams);
        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 加载系统黑名单到系统黑名单过滤器
     */
    @Async
    public void loadBlackList() {
        //加载数据
        List<String> filterBlackListList = blackRepository.findSystemBlackList();
        if (null == filterBlackListList || filterBlackListList.size() < 1) {
            return;
        }

        String[] array = new String[filterBlackListList.size()];
        filterBlackListList.toArray(array);
        redisModuleBloomFilter.addFilter(RedisFilterConstant.REDIS_BLOOM_FILTERS_SYSTEM_BLACK_COMPLAINT, array);
        //更新黑名单状态
        blackRepository.bathUpdate(filterBlackListList);
        log.info("[黑名单管理][过滤器]数据:{}", JSON.toJSONString(filterBlackListList));

    }

    /**
     * 加载行业黑名单
     */
    public void loadIndustryBlackList() {
        List<FilterBlackListValidator> findIndustryBlackList = this.blackRepository.findIndustryBlackList();
        if (null == findIndustryBlackList || findIndustryBlackList.size() < 1) {
            return;
        }
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            connection.openPipeline();
            findIndustryBlackList.forEach((value) -> {
                connection.sAdd(RedisSerializer.string().serialize(RedisConstant.FILTERS_CONFIG_SYSTEM_INDUSTRY_BLACK + value.getGroupId()),
                        RedisSerializer.string().serialize(new Gson().toJson(value.getMobile())));
            });
            connection.close();
            return null;
        });

        //变更加载状态
        this.blackRepository.bathUpdateIndustry(findIndustryBlackList);
    }

    /**
     * 删除行业黑名单
     *
     * @param groupId
     * @param mobile
     */
    public void deleteIndustryBlackList(String groupId, String mobile) {
        redisTemplate.opsForSet().remove(RedisConstant.FILTERS_CONFIG_SYSTEM_INDUSTRY_BLACK + groupId, mobile);
    }

}
