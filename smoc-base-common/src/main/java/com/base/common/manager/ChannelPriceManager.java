package com.base.common.manager;
import java.util.Map;
import java.util.Map.Entry;
import com.base.common.cache.CacheBaseService;
import com.base.common.constant.FixedConstant;
import com.base.common.dao.ChannellnfoDAO;
import com.base.common.util.DateUtil;
import com.base.common.worker.SuperMapWorker;
/**
 * @author liu
 *
 */

public class ChannelPriceManager extends SuperMapWorker<String, String> {
	private static ChannelPriceManager manager = new ChannelPriceManager();

	public static ChannelPriceManager getInstance() {
		return manager;
	}

	private ChannelPriceManager() {		
		loadData();
		this.start();
	}

	@Override
	protected void doRun() throws Exception {
		Thread.sleep(INTERVAL);			
		loadData();

	}

	/**
	 * 加载数据进行内存和redis中
	 */
	private void loadData() {
		Map<String, Map<String, Double>> channelPriceMaps = ChannellnfoDAO.loadChannelPrice();
		if (channelPriceMaps != null) {
			for (Entry<String, Map<String, Double>> channelPriceMap : channelPriceMaps.entrySet()) {
				String channelId = channelPriceMap.getKey();
				Map<String, Double> areaCodeMaps = channelPriceMap.getValue();
				for (Entry<String, Double> areaCodeMap : areaCodeMaps.entrySet()) {
					String areaCode = areaCodeMap.getKey();
					String channelPrice = String.valueOf(areaCodeMap.getValue());
					String key = new StringBuffer(channelId).append(FixedConstant.SPLICER).append(areaCode)
							.append(FixedConstant.SPLICER)
							.append(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_DAY)).toString();

					// 获取redis中通道价格
					String middleWarePrice = CacheBaseService.getAccountPriceFromMiddlewareCache(channelId, areaCode);
					// 获取map中通道价格
					String memoryPrice = super.get(key);
					// 如果redis,map中通道价格为null,则添加
					if (middleWarePrice == null && memoryPrice == null) {// memory

						CacheBaseService.saveChannelPriceToMiddlewareCache(channelId, areaCode, channelPrice);
						super.add(key, channelPrice);
						// 如果redis为null,map中通道价格不为null,则将map中通道价格添加到redis
					} else if (middleWarePrice == null && memoryPrice != null) {

						CacheBaseService.saveChannelPriceToMiddlewareCache(channelId, areaCode, memoryPrice);
						// 如果redis中通道价格不为null,map中通道价格为null,则将redis中通道价格添加到map
					} else if (memoryPrice == null && middleWarePrice != null) {

						super.add(key, middleWarePrice);
					}

				}
			}
		}
	}
   
	/**  获取通道价格
	 * @param channelID 通道id
	 * @param areaCode  业务区域：值为ALL表示全国
	 * @param time      当天日期
	 * @return
	 */
	private String getPrice(String channelID, String areaCode, String time) {
		String key = new StringBuffer(channelID).append(FixedConstant.SPLICER)
								.append(areaCode).append(FixedConstant.SPLICER).append(time).toString();	
		// 从map中获取通道的价格，如为null,则从redis中获取通道价格
		if (super.get(key) == null) {
			// 从redis中获取通道价格,不为null,则返回
			String middleWarePrice = CacheBaseService.getChannelPriceFromMiddlewareCache(channelID, areaCode);
			if (middleWarePrice != null) {
				return middleWarePrice;
			} else {			
				// 从redis中获取通道价格,为null,则从表中获取通道价格返回
				String channelPrice = ChannellnfoDAO.loadChannelPrice(channelID, areaCode);
				return channelPrice;
			}
		}
		return super.get(key);
	}

	
	/**对外调用
	 * @param channelID  通道id
	 * @param areaCode   业务区域：值为ALL表示全国
	 * @return
	 */
	public String getPrice(String channelID, String areaCode) {
		String time = DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_DAY);
		return getPrice(channelID, areaCode, time);
	}

}
