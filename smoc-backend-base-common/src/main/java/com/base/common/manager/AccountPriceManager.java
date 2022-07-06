package com.base.common.manager;

import java.util.Map;
import java.util.Map.Entry;
import com.base.common.cache.CacheBaseService;
import com.base.common.constant.FixedConstant;
import com.base.common.dao.AccountInfoDAO;
import com.base.common.util.DateUtil;
import com.base.common.worker.SuperMapWorker;

public class AccountPriceManager extends SuperMapWorker<String, String> {

	private static AccountPriceManager manager = new AccountPriceManager();

	public static AccountPriceManager getInstance() {
		return manager;
	}

	private AccountPriceManager() {
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
		Map<String, Map<String, Double>> accountPriceMaps = AccountInfoDAO.loadAccountPrice();

		if (accountPriceMaps != null) {

			for (Entry<String, Map<String, Double>> accountPriceMap : accountPriceMaps.entrySet()) {
				String accountId = accountPriceMap.getKey();
				Map<String, Double> carrierPriceMaps = accountPriceMap.getValue();
				for (Entry<String, Double> carrierPriceMap : carrierPriceMaps.entrySet()) {
					String carrier = carrierPriceMap.getKey();
					String carrierPrice = String.valueOf(carrierPriceMap.getValue());
					String key = accountId.concat(FixedConstant.SPLICER).concat(carrier).concat(FixedConstant.SPLICER)
							.concat(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_DAY));
					// 获取redis中账号价格
					String middleWarePrice = CacheBaseService.getAccountPriceFromMiddlewareCache(accountId, carrier);
					// 获取map中账号价格
					String memoryPrice = super.get(key);
					// 如果redis,map中账号价格为null,则添加
					if (middleWarePrice == null && memoryPrice == null) {
						CacheBaseService.saveAccountPriceToMiddlewareCache(accountId, carrier, carrierPrice);
						super.add(key, carrierPrice);
						// 如果redis中为null,map中账号价格不为null,则将map中账号价格添加到redis
					} else if (middleWarePrice == null && memoryPrice != null) {
						CacheBaseService.saveAccountPriceToMiddlewareCache(accountId, carrier, memoryPrice);
						// 如果redis中不为null,map中账号价格为null,则将redis中账号价格添加到map
					} else if (memoryPrice == null && middleWarePrice != null) {
						super.add(key, middleWarePrice);
					}

				}

			}
		}
	}

	
	/**获取账号价格
	 * @param accountId  账号id
	 * @param carrier    运营商
	 * @param time       当天日期
	 * @return
	 */
	private String getPrice(String accountID, String carrier, String time) {
		String key =accountID.concat(FixedConstant.SPLICER).concat(carrier).concat(FixedConstant.SPLICER).concat(time);

		// 从map中获取账号的价格，如为null,则从redis中获取账号价格
		if (super.get(key) == null) {
			// 从redis中获取账号价格,不为null,则返回
			String middleWarePrice = CacheBaseService.getAccountPriceFromMiddlewareCache(accountID, carrier);
			if (middleWarePrice != null) {
				return middleWarePrice;
			} else {
				// 从redis中获取账号价格,为null,则从表中获取价格返回
				String carrierPrice = AccountInfoDAO.loadAccountPrice(accountID, carrier);
				return carrierPrice;
			}
		}

		return super.get(key);
	}

	
	/**对外调用
	 * @param accountID  账号id
	 * @param carrier    运营商：国内运营商      国际运营商
	 * @return
	 */
	public String getPrice(String accountID, String carrier) {
		String time = DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_DAY);
		return getPrice(accountID, carrier, time);
	}

}
