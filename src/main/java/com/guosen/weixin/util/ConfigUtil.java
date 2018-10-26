package com.guosen.weixin.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 配置类
 * 
 * 创建于2016年12月14日
 * 
 * @author guosen
 *
 */
public class ConfigUtil {
	private static final Logger logger = LoggerFactory.getLogger(ConfigUtil.class);
	public static final String DEFAULT_SERVER = "weixin.client.defaultServer";
	/**
	 * 服务器列key
	 */
	public static final String LIST_OF_SERVERS = "weixin.client.listOfServers";
	/**
	 * 连接超时key
	 */
	public static final String CONNECTION_TIMEOUT = "weixin.client.connectionTimeout";
	/**
	 * 读超时key
	 */
	public static final String READ_TIMEOUT = "weixin.client.readTimeout";
	private Properties prop = null;

	public ConfigUtil() {
		prop = new Properties();
		InputStream is = ConfigUtil.class.getClassLoader().getResourceAsStream("weixinclient.properties");
		try {
			prop.load(is);
		} catch (Exception e) {
			logger.error("加载配置文件时出现异常", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}
	
	/**
	 * 根据key获取配置的值
	 * @param key
	 * @return key对应的value或者null
	 * @throws Exception
	 */
	public String getValue(String key) throws Exception{
		if (null == prop) {
			throw new Exception("没有找到配置文件");
		}
		return prop.getProperty(key);
	}
}
