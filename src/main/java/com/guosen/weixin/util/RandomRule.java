package com.guosen.weixin.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 随机选择一个server
 * 
 * 创建于2016年12月14日
 * @author guosen
 *
 */
public class RandomRule {
	private static final Logger logger = LoggerFactory.getLogger(RandomRule.class);
	private ConfigUtil configUtil;

	public RandomRule(ConfigUtil configUtil) {
		super();
		this.configUtil = configUtil;
	}
	
	/**
	 * post请求,负载发送数据
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String postRuleUrl(String data) throws Exception {
		// 创建http请求工具类
		HttpUtil httpUtil = new HttpUtil(configUtil);
		// 获取默认服务器地址
		String defaultServer = configUtil.getValue(ConfigUtil.DEFAULT_SERVER);
		String retMsg = null;
		if (null != defaultServer && !"".equals(defaultServer)) {
			retMsg = post(httpUtil, defaultServer, data);
			if (null != retMsg) {
				return retMsg;
			}
		}
		
		// 获取服务器列表
		String server = configUtil.getValue(ConfigUtil.LIST_OF_SERVERS);
		if (server == null || "".equals(server)) {
			throw new Exception("没有从配置文件中找到配置的服务器地址列表");
		}

		String[] servers = server.split(",");
		int count = 0;
		int randomSize = servers.length;
		List<Integer> countList = new ArrayList<Integer>();
		while (count < randomSize) {
			int random = (int) (Math.random() * randomSize);
			while (countList.contains(random)) {
				random = (int) (Math.random() * randomSize);
			}
			retMsg = post(httpUtil, servers[random], data);
			if (retMsg != null) {
				break;
			}
			countList.add(random);
			count++;
		}
		if (count == randomSize && retMsg == null) {
			throw new Exception("没有可用的服务器地址");
		}
		return retMsg;
	}
	
	private String post(HttpUtil httpUtil, String url, String data) {
		String retMsg = null;
		try {
			String finalUrl = "http://".concat(url).concat("/sendText");
			retMsg = httpUtil.post(finalUrl, data);
		} catch (Exception e) {
			logger.error("请求服务器[" + url + "]时出现异常", e);
		}
		return retMsg;
	}
	
	/**
	 * 获取可用的地址
	 * @return 可用地址
	 * @throws Exception
	 */
	public String getRuleUrl() throws Exception{
		// 创建http请求工具类
		HttpUtil httpUtil = new HttpUtil(configUtil);
		// 获取默认服务器地址
		String defaultServer = configUtil.getValue(ConfigUtil.DEFAULT_SERVER);
		if (defaultServer != null && !"".equals(defaultServer)) {
			int retCode = httpUtil.ping(defaultServer);
			if (retCode == 200) {
				return defaultServer;
			}
		}
		// 获取服务器列表
		String server = configUtil.getValue(ConfigUtil.LIST_OF_SERVERS);
		if (server == null || "".equals(server)) {
			throw new Exception("没有从配置文件中找到配置的服务器地址列表");
		}
		
		String[] servers = server.split(",");
		int count = 0;
		int randomSize = servers.length;
		List<Integer> countList = new ArrayList<Integer>();
		String rule = null;
		while (count < randomSize) {
			int random = (int) (Math.random() * randomSize);
			while (countList.contains(random)) {
				random = (int) (Math.random() * randomSize);
			}
			int retCode = httpUtil.ping(servers[random]);
			if (retCode == 200) {
				rule = servers[random];
				break;
			}
			countList.add(random);
			count++;
		}
		if (count == randomSize && rule == null) {
			throw new Exception("没有可用的服务器地址");
		}
		return rule;
	}
	
}
