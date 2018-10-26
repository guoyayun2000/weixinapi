package com.guosen.weixin.client;

import com.guosen.weixin.util.ConfigUtil;
import com.guosen.weixin.util.RandomRule;

/**
 * 微信发送客户端
 * 
 * 创建于2016年12月14日
 * 
 * @author guosen
 *
 */
public class WeiXinClient {
	
	/**
	 * 
	 * @param touser
	 * @param content
	 * @param agentid
	 * @return
	 * @throws Exception
	 */
	public String sendTextToUser(String touser, String content, int agentid) throws Exception{
		String data = "{\"touser\": \"#{touser}\",\"agentid\": " + agentid + ",\"safe\": 0,\"text\": { \"content\": \"#{content}\" }, \"msgtype\": \"text\"}";
		data = data.replace("#{touser}", touser).replace("#{content}", content);
		ConfigUtil configUtil = new ConfigUtil();
		RandomRule rr = new RandomRule(configUtil);
		return rr.postRuleUrl(data);
	}

	/**
	 * 
	 * @param toparty
	 * @param content
	 * @param agentid
	 * @return
	 * @throws Exception
	 */
	public String sendTextToParty(String toparty, String content, int agentid) throws Exception{
		String data = "{\"toparty\": \"#{toparty}\",\"agentid\": " + agentid + ",\"safe\": 0,\"text\": { \"content\": \"#{content}\" }, \"msgtype\": \"text\"}";
		data = data.replace("#{toparty}", toparty).replace("#{content}", content);
		ConfigUtil configUtil = new ConfigUtil();
		RandomRule rr = new RandomRule(configUtil);
		return rr.postRuleUrl(data);
	}
	
	/**
	 * 
	 * @param totag
	 * @param content
	 * @param agentid
	 * @return
	 * @throws Exception
	 */
	public String sendTextToTag(String totag, String content, int agentid) throws Exception{
		String data = "{\"totag\": \"#{totag}\",\"agentid\": " + agentid + ",\"safe\": 0,\"text\": { \"content\": \"#{content}\" }, \"msgtype\": \"text\"}";
		data = data.replace("#{totag}", totag).replace("#{content}", content);
		ConfigUtil configUtil = new ConfigUtil();
		RandomRule rr = new RandomRule(configUtil);
		return rr.postRuleUrl(data);
	}
}
