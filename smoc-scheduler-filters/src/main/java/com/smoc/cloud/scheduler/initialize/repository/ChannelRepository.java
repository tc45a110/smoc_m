package com.smoc.cloud.scheduler.initialize.repository;

import com.smoc.cloud.scheduler.initialize.entity.AccountBaseInfo;
import com.smoc.cloud.scheduler.initialize.entity.AccountChannelInfo;
import com.smoc.cloud.scheduler.initialize.entity.AccountContentRoute;
import com.smoc.cloud.scheduler.initialize.model.AccountChannelBusinessModel;
import com.smoc.cloud.scheduler.initialize.model.ContentRouteBusinessModel;
import com.smoc.cloud.scheduler.initialize.rowmapper.AccountBaseInfoRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChannelRepository {

    @Autowired
    public DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 加载业务账号内容路由配置信息
     *
     * @return
     */
    public Map<String, ContentRouteBusinessModel> getContentRoutesBusinessModels() {
        Map<String, ContentRouteBusinessModel> resultMap = new HashMap<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer("SELECT ");
        sql.append(" route.ACCOUNT_ID,");
        sql.append(" route.CARRIER,");
        sql.append(" route.AREA_CODES,");
        sql.append(" channel.SUPPORT_AREA_CODES CHANNEL_AREA_CODES,");
        sql.append(" route.ROUTE_CONTENT,");
        sql.append(" route.ROUTE_REVERSE_CONTENT,");
        sql.append(" route.MOBILE_NUM,");
        sql.append(" route.MIN_CONTENT,");
        sql.append(" route.MAX_CONTENT,");
        sql.append(" route.CHANNEL_ID");
        sql.append(" FROM smoc.config_route_content_rule route,smoc.config_channel_basic_info channel ");
        sql.append(" where route.CHANNEL_ID = channel.CHANNEL_ID and ROUTE_STATUS='1' and channel.CHANNEL_STATUS = '001'");
        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                AccountContentRoute qo = new AccountContentRoute();
                qo.setAccountId(rs.getString("ACCOUNT_ID"));
                qo.setCarrier(rs.getString("CARRIER"));
                qo.setAreaCodes(rs.getString("AREA_CODES"));
                qo.setChannelAreaCodes(rs.getString("CHANNEL_AREA_CODES"));
                qo.setSupportAreaCodes(convertIntersection(qo.getAreaCodes(), qo.getChannelAreaCodes()));
                qo.setRouteContent(rs.getString("ROUTE_CONTENT"));
                qo.setRouteReverseContent(rs.getString("ROUTE_REVERSE_CONTENT"));
                qo.setMobileNum(rs.getString("MOBILE_NUM"));
                qo.setMinContent(rs.getInt("MIN_CONTENT"));
                qo.setMaxContent(rs.getInt("MAX_CONTENT"));
                qo.setRouteChannelId(rs.getString("CHANNEL_ID"));
                ContentRouteBusinessModel contentRouteBusinessModel = resultMap.get(qo.getAccountId());
                if (null == contentRouteBusinessModel) {
                    contentRouteBusinessModel = new ContentRouteBusinessModel();
                }
                contentRouteBusinessModel.add(qo);
                resultMap.put(qo.getAccountId(), contentRouteBusinessModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != conn) {
                    conn.close();
                }
                if (null != pstmt) {
                    pstmt.close();
                }
                if (null != rs) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultMap;
    }


    /**
     * 加载业务账号对应通道信息
     *
     * @return
     */
    public Map<String, AccountChannelBusinessModel> getAccountChannels() {
        Map<String, AccountChannelBusinessModel> resultMap = new HashMap<>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer("SELECT ");
        sql.append(" base.ACCOUNT_ID,");
        sql.append(" base.CONFIG_TYPE,");
        sql.append(" base.CARRIER,");
        sql.append(" channel.BUSINESS_AREA_TYPE,");
        sql.append(" channel.SUPPORT_AREA_CODES,");
        sql.append(" base.CHANNEL_GROUP_ID,");
        sql.append(" base.CHANNEL_ID,");
        sql.append(" base.CHANNEL_PRIORITY,");
        sql.append(" base.CHANNEL_WEIGHT,");
        sql.append(" base.CHANNEL_STATUS");
        sql.append(" from smoc.account_channel_info base,smoc.config_channel_basic_info channel ");
        sql.append(" where base.CHANNEL_ID = channel.CHANNEL_ID and channel.CHANNEL_STATUS = '001' ");
        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                AccountChannelInfo qo = new AccountChannelInfo();
                qo.setAccountId(rs.getString("ACCOUNT_ID"));
                qo.setConfigType(rs.getString("CONFIG_TYPE"));
                qo.setCarrier(rs.getString("CARRIER"));
                qo.setBusinessAreaType(rs.getString("BUSINESS_AREA_TYPE"));
                qo.setSupportAreaCodes(rs.getString("SUPPORT_AREA_CODES"));
                qo.setAreaCodes(this.convertToSet(qo.getSupportAreaCodes()));
                qo.setChannelGroupId(rs.getString("CHANNEL_GROUP_ID"));
                qo.setChannelId(rs.getString("CHANNEL_ID"));
                qo.setChannelPriority(rs.getString("CHANNEL_PRIORITY"));
                qo.setChannelWeight(rs.getInt("CHANNEL_WEIGHT"));
                qo.setChannelStatus(rs.getString("CHANNEL_STATUS"));

                AccountChannelBusinessModel businessModel = resultMap.get(qo.getAccountId());
                if (null == businessModel) {
                    businessModel = new AccountChannelBusinessModel();
                }
                businessModel.add(qo);
                resultMap.put(qo.getAccountId(), businessModel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != conn) {
                    conn.close();
                }
                if (null != pstmt) {
                    pstmt.close();
                }
                if (null != rs) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return resultMap;
    }


    /**
     * 加载业务账号基本信息
     *
     * @return
     */
    public Map<String, AccountBaseInfo> getChannelInfoInfo() {
        Map<String, AccountBaseInfo> resultMap = new HashMap<>();
        StringBuffer sql = new StringBuffer("SELECT ");
        sql.append(" a.CHANNEL_ID,");
        sql.append(" a.CHANNEL_NAME,");
        sql.append(" a.CARRIER,");
        sql.append(" a.BUSINESS_TYPE,");
        sql.append(" a.MAX_COMPLAINT_RATE");
        sql.append(" a.ACCESS_PROVINCE,");
        sql.append(" a.CHANNEL_PROVDER,");
        sql.append(" a.INFO_TYPE,");
        sql.append(" a.BUSINESS_AREA_TYPE,");
        sql.append(" a.SUPPORT_AREA_CODES,");
        sql.append(" a.CHANNEL_STATUS,");
        sql.append(" a.CHANNEL_RUN_STATUS,");
        sql.append(" a.PRICE_STYLE,");
        sql.append(",b.CHANNEL_ACCESS_ACCOUNT,b.CHANNEL_ACCESS_PASSWORD,b.CHANNEL_SERVICE_URL,b.SP_ID,b.SRC_ID");
        sql.append(",b.BUSINESS_CODE,b.CONNECT_NUMBER,b.MAX_SEND_SECOND,b.HEARTBEAT_INTERVAL,b.PROTOCOL,b.VERSION ");
        sql.append("FROM smoc.config_channel_basic_info a join smoc.config_channel_interface b on a.CHANNEL_ID=b.CHANNEL_ID");

        List<AccountBaseInfo> result = this.jdbcTemplate.query(sql.toString(), new AccountBaseInfoRowMapper());
        if (null == result || result.size() < 1) {
            return resultMap;
        }
        for (AccountBaseInfo baseInfo : result) {
            resultMap.put(baseInfo.getAccountId(), baseInfo);
        }
        return resultMap;
    }


    /**
     * 分割的字符串转set
     *
     * @param areaCodes
     * @return
     */
    private Set<String> convertToSet(String areaCodes) {
        Set<String> areaCodeSet = new HashSet<>();
        if (StringUtils.isEmpty(areaCodes)) {
            return areaCodeSet;
        }
        areaCodeSet = Arrays.stream(areaCodes.split(",")).collect(Collectors.toSet());
        return areaCodeSet;
    }

    /**
     * 计算  两个数组的交集
     *
     * @return
     */
    public Set<String> convertIntersection(String areaCodes, String channelAreaCodes) {
        Set<String> channelAreaCodeSet = convertToSet(channelAreaCodes);
        if (StringUtils.isEmpty(areaCodes)) {
            return channelAreaCodeSet;
        }
        Set<String> areaCodeSet = convertToSet(areaCodes);
        Set<String> intersection = new HashSet<>();
        for (String areaCode : areaCodeSet) {
            if (channelAreaCodeSet.contains(areaCode)) {
                intersection.add(areaCode);
            }
        }
        return intersection;
    }
}
