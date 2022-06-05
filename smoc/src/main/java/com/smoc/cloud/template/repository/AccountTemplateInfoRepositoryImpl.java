package com.smoc.cloud.template.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.template.AccountTemplateInfoValidator;
import com.smoc.cloud.template.entity.AccountTemplateContent;
import com.smoc.cloud.template.rowmapper.AccountTemplateContentRowMapper;
import com.smoc.cloud.template.rowmapper.AccountTemplateInfoRowMapper;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountTemplateInfoRepositoryImpl extends BasePageRepository {


    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public PageList<AccountTemplateInfoValidator> page(PageParams<AccountTemplateInfoValidator> pageParams) {
        //查询条件
        AccountTemplateInfoValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");

        sqlBuffer.append(" t.TEMPLATE_ID,");
        sqlBuffer.append(" e.ENTERPRISE_NAME,");
        sqlBuffer.append(" a.ACCOUNT_NAME,");
        sqlBuffer.append(" t.TEMPLATE_TYPE as BUSINESS_TYPE,");
        sqlBuffer.append(" t.BUSINESS_ACCOUNT,");
        sqlBuffer.append(" t.TEMPLATE_TYPE,");
        sqlBuffer.append(" t.TEMPLATE_FLAG,");
        sqlBuffer.append(" t.SIGN_NAME,");
        sqlBuffer.append(" t.TEMPLATE_TITLE,");
        sqlBuffer.append(" t.TEMPLATE_CONTENT,");
        sqlBuffer.append(" t.FORBIDDEN_CONTENT,");
        sqlBuffer.append(" DATE_FORMAT(t.CHECK_DATE, '%Y-%m-%d %H:%i:%S')CHECK_DATE, ");
        sqlBuffer.append(" t.CHECK_BY,");
        sqlBuffer.append(" t.CHECK_OPINIONS,");
        sqlBuffer.append(" t.CHECK_STATUS,");
        sqlBuffer.append(" t.TEMPLATE_STATUS,");
        sqlBuffer.append(" t.TEMPLATE_AGREEMENT_TYPE,");
        sqlBuffer.append(" t.CREATED_BY,");
        sqlBuffer.append(" t.INFO_TYPE,");
        sqlBuffer.append(" t.TEMPLATE_CLASSIFY,");
        sqlBuffer.append(" t.IS_FILTER,");
        sqlBuffer.append(" DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME ");
        sqlBuffer.append(" from account_template_info t left join enterprise_basic_info e on t.ENTERPRISE_ID = e.ENTERPRISE_ID left join account_base_info a on t.BUSINESS_ACCOUNT = a.ACCOUNT_ID ");
        sqlBuffer.append(" where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        //企业名称
        if (!StringUtils.isEmpty(qo.getEnterpriseName())) {
            sqlBuffer.append(" and e.ENTERPRISE_NAME like ? ");
            paramsList.add("%" + qo.getEnterpriseName().trim() + "%");
        }

        //企业ID
        if (!StringUtils.isEmpty(qo.getEnterpriseId())) {
            sqlBuffer.append(" and t.ENTERPRISE_ID = ? ");
            paramsList.add(qo.getEnterpriseId().trim());
        }

        //业务账号
        if (!StringUtils.isEmpty(qo.getBusinessAccount())) {
            sqlBuffer.append(" and t.BUSINESS_ACCOUNT like ?");
            paramsList.add("%" + qo.getBusinessAccount().trim() + "%");
        }

        //业务类型
        if (!StringUtils.isEmpty(qo.getTemplateType())) {
            sqlBuffer.append(" and t.TEMPLATE_TYPE =?");
            paramsList.add(qo.getTemplateType().trim());
        }

        //信息分类
        if (!StringUtils.isEmpty(qo.getInfoType())) {
            sqlBuffer.append(" and t.INFO_TYPE =?");
            paramsList.add(qo.getInfoType().trim());
        }

        //业务账号名称
        if (!StringUtils.isEmpty(qo.getAccountName())) {
            sqlBuffer.append(" and a.ACCOUNT_NAME like ? ");
            paramsList.add("%" + qo.getAccountName().trim() + "%");
        }

        //业务类型
        if (!StringUtils.isEmpty(qo.getTemplateStatus())) {
            sqlBuffer.append(" and t.TEMPLATE_STATUS =?");
            paramsList.add(qo.getTemplateStatus().trim());
        }

        //协议类型 SERVICE_WEB:标识是自服务平台
        if (!StringUtils.isEmpty(qo.getTemplateAgreementType())) {
            if ("WEB".equals(qo.getTemplateAgreementType())) {
                sqlBuffer.append(" and ( t.TEMPLATE_AGREEMENT_TYPE ='WEB' or t.TEMPLATE_AGREEMENT_TYPE ='HTTP')");
            } else if ("SERVICE_WEB".equals(qo.getTemplateAgreementType())) {
                sqlBuffer.append(" and t.TEMPLATE_AGREEMENT_TYPE ='WEB' ");
            } else {
                sqlBuffer.append(" and t.TEMPLATE_AGREEMENT_TYPE =? ");
                paramsList.add(qo.getTemplateAgreementType().trim());
            }
        }

        //模板标识
        if (!StringUtils.isEmpty(qo.getTemplateFlag())) {
            sqlBuffer.append(" and t.TEMPLATE_FLAG = ? ");
            paramsList.add(qo.getTemplateFlag().trim());
        }

        //模板模版类型
        if (!StringUtils.isEmpty(qo.getTemplateClassify())) {
            sqlBuffer.append(" and t.TEMPLATE_CLASSIFY = ? ");
            paramsList.add(qo.getTemplateClassify().trim());
        }

        //模板内容
        if (!StringUtils.isEmpty(qo.getTemplateContent())) {
            sqlBuffer.append(" and t.TEMPLATE_CONTENT like ? ");
            paramsList.add("%" + qo.getTemplateContent().trim() + "%");
        }

        //签名
        if (!StringUtils.isEmpty(qo.getSignName())) {
            sqlBuffer.append(" and t.SIGN_NAME like ? ");
            paramsList.add("%" + qo.getSignName().trim() + "%");
        }

        //时间起
        if (!StringUtils.isEmpty(qo.getStartDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.MESSAGE_DATE,'%Y-%m-%d') >=? ");
            paramsList.add(qo.getStartDate().trim());
        }
        //时间止
        if (!StringUtils.isEmpty(qo.getEndDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.MESSAGE_DATE,'%Y-%m-%d') <=? ");
            paramsList.add(qo.getEndDate().trim());
        }

        if ("SERVICE_WEB".equals(qo.getTemplateAgreementType())) {
            sqlBuffer.append(" order by t.CREATED_TIME desc,t.TEMPLATE_STATUS desc");
        } else {
            sqlBuffer.append(" order by t.TEMPLATE_STATUS desc,t.CREATED_TIME desc");
        }


        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<AccountTemplateInfoValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new AccountTemplateInfoRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }

    /**
     * 查询绑定账号的固定模版，包括CMPP、HTTP的普通模版
     *
     * @return
     */
//    public List<AccountTemplateContent> findFixedTemplate() {
//        //查询sql
//        StringBuilder sqlBuffer = new StringBuilder("select ");
//        sqlBuffer.append(" t.BUSINESS_ACCOUNT,");
//        sqlBuffer.append(" t.IS_FILTER,");
//        sqlBuffer.append(" t.TEMPLATE_CONTENT ");
//        sqlBuffer.append(" from account_template_info t ");
//        sqlBuffer.append(" where t.TEMPLATE_FLAG='1' and (TEMPLATE_AGREEMENT_TYPE = 'CMPP' or TEMPLATE_AGREEMENT_TYPE = 'HTTP' ) ");
//
//        List<AccountTemplateContent> result = this.jdbcTemplate.query(sqlBuffer.toString(), new AccountTemplateContentRowMapper());
//        //对内容进行md5加密
//        if (null != result && result.size() > 0) {
//            for (AccountTemplateContent obj : result) {
//                String md5Content = DigestUtils.md5Hex(obj.getContent());
//                obj.setContent(md5Content);
//            }
//        }
//        return result;
//    }

    /**
     * 查询绑定账号的固定模版，包括CMPP类型
     *
     * @return
     */
    public Map<String, String> findFixedTemplate() {
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.TEMPLATE_ID,");
        sqlBuffer.append(" t.BUSINESS_ACCOUNT,");
        sqlBuffer.append(" t.IS_FILTER,");
        sqlBuffer.append(" t.TEMPLATE_CONTENT ");
        sqlBuffer.append(" from account_template_info t ");
        sqlBuffer.append(" where  t.TEMPLATE_STATUS='2' and t.TEMPLATE_FLAG='1' and (TEMPLATE_AGREEMENT_TYPE = 'CMPP' or TEMPLATE_AGREEMENT_TYPE = 'HTTP' ) ");

        List<AccountTemplateContent> result = this.jdbcTemplate.query(sqlBuffer.toString(), new AccountTemplateContentRowMapper());
        Map<String, String> resultMap = new HashMap<>();
        //对模版进行加工处理
        if (null != result && result.size() > 0) {
            for (AccountTemplateContent obj : result) {
                String messageContent = obj.getContent();
                if (null == resultMap.get(obj.getAccount())) {
                    resultMap.put(obj.getAccount(), messageContent);
                } else {
                    String content = resultMap.get(obj.getAccount());
                    resultMap.put(obj.getAccount(), content + "|" + messageContent);
                }
            }
        }
        return resultMap;
    }

    /**
     * 查询绑定账号的固定模版，包括CMPP类型
     *
     * @return
     */
    public List<AccountTemplateContent> findFixedTemplate(String account) {
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.TEMPLATE_ID,");
        sqlBuffer.append(" t.BUSINESS_ACCOUNT,");
        sqlBuffer.append(" t.IS_FILTER,");
        sqlBuffer.append(" t.TEMPLATE_CONTENT ");
        sqlBuffer.append(" from account_template_info t ");
        sqlBuffer.append(" where  t.TEMPLATE_STATUS='2' and t.TEMPLATE_FLAG='1' and (TEMPLATE_AGREEMENT_TYPE = 'CMPP' or TEMPLATE_AGREEMENT_TYPE = 'HTTP' ) and t.BUSINESS_ACCOUNT ='" + account + "'");

        List<AccountTemplateContent> result = this.jdbcTemplate.query(sqlBuffer.toString(), new AccountTemplateContentRowMapper());

        return result;
    }

    /**
     * 查询绑定企业的固定模版，包括HTTP、WEB类型
     *
     * @return
     */
    public Map<String, String> findHttpFixedTemplate() {
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.TEMPLATE_ID,");
        sqlBuffer.append(" t.BUSINESS_ACCOUNT,");
        sqlBuffer.append(" t.IS_FILTER,");
        sqlBuffer.append(" t.TEMPLATE_CONTENT ");
        sqlBuffer.append(" from account_template_info t ");
        sqlBuffer.append(" where  t.TEMPLATE_STATUS='2' and t.TEMPLATE_FLAG='1' and (TEMPLATE_AGREEMENT_TYPE = 'WEB' or TEMPLATE_AGREEMENT_TYPE = 'HTTP' ) ");

        List<AccountTemplateContent> result = this.jdbcTemplate.query(sqlBuffer.toString(), new AccountTemplateContentRowMapper());
        Map<String, String> resultMap = new HashMap<>();
        if (null != result && result.size() > 0) {
            for (AccountTemplateContent obj : result) {
                resultMap.put(obj.getTemplateId(), obj.getContent());
            }
        }
        return resultMap;
    }

    /**
     * 查询绑定的固定模版，包括HTTP、WEB类型
     *
     * @return
     */
    public Map<String, String> findHttpVariableTemplate() {
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.TEMPLATE_ID,");
        sqlBuffer.append(" t.BUSINESS_ACCOUNT,");
        sqlBuffer.append(" t.IS_FILTER,");
        sqlBuffer.append(" t.TEMPLATE_CONTENT ");
        sqlBuffer.append(" from account_template_info t ");
        sqlBuffer.append(" where  t.TEMPLATE_STATUS='2' and t.TEMPLATE_FLAG='2' and (TEMPLATE_AGREEMENT_TYPE = 'WEB' or TEMPLATE_AGREEMENT_TYPE = 'HTTP' ) ");

        List<AccountTemplateContent> result = this.jdbcTemplate.query(sqlBuffer.toString(), new AccountTemplateContentRowMapper());
        Map<String, String> resultMap = new HashMap<>();
        //对内HTTP的变量模版进行加工处理  HTTP变量结构是 ${1}、${2}、${3}、${4}、${5}、占位符。
        if (null != result && result.size() > 0) {
            for (AccountTemplateContent obj : result) {
                //替换占位符
                Map<String, String> paramMap = new HashMap<>();
                for (int j = 1; j < 11; j++) {
                    paramMap.put(j + "", ".*");
                }
                StrSubstitutor strSubstitutor = new StrSubstitutor(paramMap);
                String messageContent = strSubstitutor.replace(obj.getContent());
                resultMap.put(obj.getTemplateId(), messageContent);
            }
        }
        return resultMap;
    }

    /**
     * 查询绑定账号的变量模版，而且该变量模版 匹配后，不在进行后续过滤
     *
     * @return
     */
    public Map<String, String> findNoFilterVariableTemplate() {
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.TEMPLATE_ID,");
        sqlBuffer.append(" t.BUSINESS_ACCOUNT,");
        sqlBuffer.append(" t.IS_FILTER,");
        sqlBuffer.append(" t.TEMPLATE_CONTENT ");
        sqlBuffer.append(" from account_template_info t ");
        sqlBuffer.append(" where  t.TEMPLATE_STATUS='2' and t.TEMPLATE_FLAG='2' and (TEMPLATE_AGREEMENT_TYPE = 'CMPP' and IS_FILTER='NO_FILTER')");

        List<AccountTemplateContent> result = this.jdbcTemplate.query(sqlBuffer.toString(), new AccountTemplateContentRowMapper());
        Map<String, String> resultMap = new HashMap<>();
        //对内HTTP的变量模版进行加工处理  HTTP变量结构是 ${1}、${2}、${3}、${4}、${5}、占位符。
        if (null != result && result.size() > 0) {
            for (AccountTemplateContent obj : result) {
                //替换占位符
                Map<String, String> paramMap = new HashMap<>();
                for (int j = 1; j < 11; j++) {
                    paramMap.put(j + "", ".*");
                }
                StrSubstitutor strSubstitutor = new StrSubstitutor(paramMap);
                String messageContent = strSubstitutor.replace(obj.getContent());
                if (null == resultMap.get(obj.getAccount())) {
                    resultMap.put(obj.getAccount(), messageContent);
                } else {
                    String content = resultMap.get(obj.getAccount());
                    resultMap.put(obj.getAccount(), content + "|" + messageContent);
                }
            }
        }
        return resultMap;
    }

    /**
     * 查询绑定账号的变量模版，而且该变量模版 匹配后，不在进行后续过滤
     *
     * @return
     */
    public List<AccountTemplateContent> findNoFilterVariableTemplate(String account) {
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.TEMPLATE_ID,");
        sqlBuffer.append(" t.BUSINESS_ACCOUNT,");
        sqlBuffer.append(" t.IS_FILTER,");
        sqlBuffer.append(" t.TEMPLATE_CONTENT ");
        sqlBuffer.append(" from account_template_info t ");
        sqlBuffer.append(" where t.TEMPLATE_STATUS='2' and t.TEMPLATE_FLAG='2' and (TEMPLATE_AGREEMENT_TYPE = 'CMPP' and IS_FILTER='NO_FILTER') and t.BUSINESS_ACCOUNT ='" + account + "'");

        List<AccountTemplateContent> result = this.jdbcTemplate.query(sqlBuffer.toString(), new AccountTemplateContentRowMapper());
        return result;
    }

    /**
     * 查询绑定账号的CMPP变量模版，该部分只查询匹配上模版后，需要继续过滤的模版
     *
     * @return
     */
    public Map<String, String> findCMPPVariableTemplate() {
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.TEMPLATE_ID,");
        sqlBuffer.append(" t.BUSINESS_ACCOUNT,");
        sqlBuffer.append(" t.IS_FILTER,");
        sqlBuffer.append(" t.TEMPLATE_CONTENT ");
        sqlBuffer.append(" from account_template_info t ");
        sqlBuffer.append(" where  t.TEMPLATE_STATUS='2' and t.TEMPLATE_FLAG='2' and TEMPLATE_AGREEMENT_TYPE = 'CMPP' and IS_FILTER='FILTER' ");

        List<AccountTemplateContent> result = this.jdbcTemplate.query(sqlBuffer.toString(), new AccountTemplateContentRowMapper());
        Map<String, String> resultMap = new HashMap<>();
        //对内CMPP的变量模版进行加工处理  HTTP变量结构是 ${1}、${2}、${3}、${4}、${5}、占位符。
        if (null != result && result.size() > 0) {
            for (AccountTemplateContent obj : result) {
                //替换占位符
                Map<String, String> paramMap = new HashMap<>();
                for (int j = 1; j < 11; j++) {
                    paramMap.put(j + "", ".*");
                }
                StrSubstitutor strSubstitutor = new StrSubstitutor(paramMap);
                String messageContent = strSubstitutor.replace(obj.getContent());
                if (null == resultMap.get(obj.getAccount())) {
                    resultMap.put(obj.getAccount(), messageContent);
                } else {
                    String content = resultMap.get(obj.getAccount());
                    resultMap.put(obj.getAccount(), content + "|" + messageContent);
                }
            }
        }
        return resultMap;
    }

    /**
     * 查询绑定账号的CMPP变量模版，该部分只查询匹配上模版后，需要继续过滤的模版
     *
     * @return
     */
    public List<AccountTemplateContent> findCMPPVariableTemplate(String account) {
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.TEMPLATE_ID,");
        sqlBuffer.append(" t.BUSINESS_ACCOUNT,");
        sqlBuffer.append(" t.IS_FILTER,");
        sqlBuffer.append(" t.TEMPLATE_CONTENT ");
        sqlBuffer.append(" from account_template_info t ");
        sqlBuffer.append(" where  t.TEMPLATE_STATUS='2' and t.TEMPLATE_FLAG='2' and TEMPLATE_AGREEMENT_TYPE = 'CMPP' and IS_FILTER='FILTER' and t.BUSINESS_ACCOUNT ='" + account + "'");

        List<AccountTemplateContent> result = this.jdbcTemplate.query(sqlBuffer.toString(), new AccountTemplateContentRowMapper());
        return result;
    }

    /**
     * 查询绑定账号的CMPP签名模版
     *
     * @return
     */
    public Map<String, String> findCMPPSignTemplate() {
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.TEMPLATE_ID,");
        sqlBuffer.append(" t.BUSINESS_ACCOUNT,");
        sqlBuffer.append(" t.IS_FILTER,");
        sqlBuffer.append(" t.TEMPLATE_CONTENT ");
        sqlBuffer.append(" from account_template_info t ");
        sqlBuffer.append(" where  t.TEMPLATE_STATUS='2' and t.TEMPLATE_FLAG='3' and TEMPLATE_AGREEMENT_TYPE = 'CMPP' ");

        List<AccountTemplateContent> result = this.jdbcTemplate.query(sqlBuffer.toString(), new AccountTemplateContentRowMapper());
        Map<String, String> resultMap = new HashMap<>();
        //对签名模版进行加工处理
        if (null != result && result.size() > 0) {
            for (AccountTemplateContent obj : result) {
                String messageContent = obj.getContent();
                if (null == resultMap.get(obj.getAccount())) {
                    resultMap.put(obj.getAccount(), messageContent);
                } else {
                    String content = resultMap.get(obj.getAccount());
                    resultMap.put(obj.getAccount(), content + "|" + messageContent);
                }
            }
        }
        return resultMap;
    }

    /**
     * 查询绑定账号的CMPP签名模版
     *
     * @return
     */
    public List<AccountTemplateContent> findCMPPSignTemplate(String account) {
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.TEMPLATE_ID,");
        sqlBuffer.append(" t.BUSINESS_ACCOUNT,");
        sqlBuffer.append(" t.IS_FILTER,");
        sqlBuffer.append(" t.TEMPLATE_CONTENT ");
        sqlBuffer.append(" from account_template_info t ");
        sqlBuffer.append(" where  t.TEMPLATE_STATUS='2' and t.TEMPLATE_FLAG='3' and TEMPLATE_AGREEMENT_TYPE = 'CMPP' and t.BUSINESS_ACCOUNT ='" + account + "'");

        List<AccountTemplateContent> result = this.jdbcTemplate.query(sqlBuffer.toString(), new AccountTemplateContentRowMapper());
        return result;
    }
}
