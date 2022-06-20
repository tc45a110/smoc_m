package com.base.common.manager;

import java.util.Properties;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import com.base.common.constant.FixedConstant;
import com.base.common.log.CategoryLog;
import com.base.common.worker.SuperMapWorker;

public class ResourceManager extends SuperMapWorker<String, String>{
	
	private static StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
	
	private static Properties prop = null;
	
	private static ResourceManager rm = new ResourceManager();
	
	

	private ResourceManager() {
		encryptor.setPassword(FixedConstant.ENCRYPT_KEY);
		loadData();
		this.start();
	}

	public int getIntValue(String key) {
		String value = prop.getProperty(key);
		if (value == null)
			return 0;
		try {
			return Integer.parseInt(value.trim());
		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(),e);
		}
		return 0;
	}

	public long getLongValue(String key) {
		String value = prop.getProperty(key);
		if (value == null)
			return 0L;
		try {
			return Long.parseLong(value.trim());
		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(),e);
		}
		return 0L;
	}

	public static ResourceManager getInstance() {
		return rm;
	}

	public String getValue(String key) {
		String value = prop.getProperty(key);
		if (value == null)
			return null;
		else
			return value.trim();
	}
	
	public String getDecryptionValue(String key) {
		String value = prop.getProperty(key);
		CategoryLog.commonLogger.info("获取解密数据{}",value);
		if (value == null)
			return null;
		else
			return encryptor.decrypt(value.trim());
	}

	@Override
	protected void doRun() throws Exception {
		Thread.sleep(INTERVAL);
		loadData();
	}
	
	private void loadData(){
		Properties properties = null;
		// 普通工程
		java.io.InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("resource.properties");

		// web工程需要/resource.properties定位文件
		if (is == null) {
			System.out.println("第一次未获取resource.properties");
			is = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("/resource.properties");
		}
		if (is == null) {
			System.out.println("第二次未获取resource.properties");
		}
		
		try {
			properties = new Properties();
			properties.load(is);
			prop = properties;
			is.close();
		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(),e);
		}
		
	}

}
