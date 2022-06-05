/**
 * @desc
 * 
 */
package com.base.common.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.base.common.constant.DictionariesTypeConstant;
import com.base.common.constant.FixedConstant;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.log.CategoryLog;
import com.base.common.worker.SuperMapWorker;

public class DictionaryDataManager extends SuperMapWorker<String, String>{
	
	private static DictionaryDataManager manager = new DictionaryDataManager();
			
	/**
	 * 根据分类和字典名称获取对应的数据
	 * @param dictionariesType
	 * @param dictionariesName
	 * @return
	 */
	public String getDictionariesCode(String dictionariesType,String dictionariesName){
		String key = new StringBuilder()
		.append(dictionariesType)
		.append(FixedConstant.SPLICER)
		.append(dictionariesName).toString();
		CategoryLog.commonLogger.debug("{}",key);
		return get(key);
	}
	
	/**
	 * 根据分类和字典值获取对应的字典名称
	 * @param dictionariesType
	 * @param dictionariesCode
	 * @return
	 */
	public String getDictionariesName(String dictionariesType,String dictionariesCode) {
		String key = new StringBuilder()
		.append(dictionariesType)
		.append(FixedConstant.SPLICER)
		.append(dictionariesCode).toString();
		CategoryLog.commonLogger.debug("{}",key);
		return get(key);
	}
	
	private DictionaryDataManager(){
		loadData();
		this.start();
	}
	
	public static DictionaryDataManager getInstance(){
		return manager;
	}
	
	@Override
	public void doRun() throws Exception {
			Thread.sleep(INTERVAL);
			loadData();
	}
	
	/**
	 * 加载数据
	 */
	private void loadData(){
		long startTime = System.currentTimeMillis();
		Map<String, String> resultMap = loadDictionariesParameterValue();
		
		if(resultMap != null){
			//将新获取的数据集赋值
			superMap = resultMap;
		}
		
		long endTime = System.currentTimeMillis();
		CategoryLog.commonLogger.info("size={},耗时={}",size(),(endTime-startTime));
	}
	
	/**
	 * 获取parameter_extend_business_param_value、parameter_extend_filters_value、parameter_extend_system_param_value的配置数据
	 * 数据库异常时返回null
	 * @return
	 */
	private Map<String, String> loadDictionariesParameterValue() {
		Map<String, String> resultMap = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		sql.append("SELECT DICT_TYPE,DICT_NAME,DICT_CODE FROM smoc_oauth.base_comm_dict WHERE ACTIVE = 1");
		Map<String, Set<String>> carrierSegmentMap = new HashMap<String, Set<String>>();
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String dictType = rs.getString("DICT_TYPE");
				String dictName = rs.getString("DICT_NAME");
				String dictCode = rs.getString("DICT_CODE");
				// 运营商号段处理
				if (DictionariesTypeConstant.CARRIER_SEGMENT.equals(dictType)) {
					Set<String> segmentSet = carrierSegmentMap.get(dictName);
					if (segmentSet == null || segmentSet.size() == 0) {
						segmentSet = new HashSet<String>();
						carrierSegmentMap.put(dictName, segmentSet);
					}
					segmentSet.add(dictCode);
				} else {
					String namekey = new StringBuilder().append(rs.getString("DICT_TYPE")).append(FixedConstant.SPLICER)
							.append(rs.getString("DICT_NAME")).toString();

					String codeKey = new StringBuilder().append(rs.getString("DICT_TYPE")).append(FixedConstant.SPLICER)
							.append(rs.getString("DICT_CODE")).toString();

					resultMap.put(namekey.toString(), rs.getString("DICT_CODE"));
					resultMap.put(codeKey.toString(), rs.getString("DICT_NAME"));
				}
			}
			// 运营商号段特殊处理，获取正则表达式
			for (String carrier : carrierSegmentMap.keySet()) {
				String carrierKey = new StringBuffer().append(DictionariesTypeConstant.CARRIER_SEGMENT)
						.append(FixedConstant.SPLICER).append(carrier).toString();
				String regExCarrier = getCarrierPattern(carrierSegmentMap.get(carrier));
				resultMap.put(carrierKey, regExCarrier);
			}

		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(), e);
			return null;
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
		}
		return resultMap;
	}
	
	/**
	 * 通过号段拼接成正则表达式
	 * @param segmentSet
	 * @return
	 */
	private String getCarrierPattern(Set<String> segmentSet) {
		StringBuffer pattern = new StringBuffer();
		StringBuffer threeSegment = new StringBuffer();
		StringBuffer fourSegment = new StringBuffer();
		StringBuffer fiveSegment = new StringBuffer();
		for (String segment : segmentSet) {
			if (segment.length() == 3) {
				if (threeSegment.length() > 0) {
					threeSegment.append("|");
				} else {
					threeSegment.append("(^(");
				}
				threeSegment.append(segment);
			} else if (segment.length() == 4) {
				if (fourSegment.length() > 0) {
					fourSegment.append("|");
				} else {
					fourSegment.append("(^(");
				}
				fourSegment.append(segment);
			} else if (segment.length() == 5) {
				if (fiveSegment.length() > 0) {
					fiveSegment.append("|");
				} else {
					fiveSegment.append("(^(");
				}
				fiveSegment.append(segment);
			}
		}
		// 三位
		if (threeSegment.length() > 0) {
			threeSegment.append(")\\d{8}$)");
			pattern.append(threeSegment);
		}
		// 四位
		if (fourSegment.length() > 0) {
			fourSegment.append(")\\d{7}$)");
			pattern.append("|").append(fourSegment);
		}
		// 五位 多一位保证后续的添加
		if (fiveSegment.length() > 0) {
			fiveSegment.append(")\\d{6}$)");
			pattern.append("|").append(fiveSegment);
		}
		return pattern.toString();
	}
}


