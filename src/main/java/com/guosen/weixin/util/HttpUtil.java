package com.guosen.weixin.util;

import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
/**
 * http请求工具类
 * 
 * 创建于2016年12月14日
 * @author guosen
 *
 */
public class HttpUtil {
	private static int connectionTimeout = 50;
	
	
	public HttpUtil(ConfigUtil configUtil) {
		if (configUtil != null) {
			try {
				String value = configUtil.getValue(ConfigUtil.CONNECTION_TIMEOUT);
				if (value != null && !"".equals(value)) {
					connectionTimeout = Integer.parseInt(value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * get方式请求,返回响应码
	 * @param url 请求地址
	 * @return 响应码
	 */
	public int ping(String url){
		HttpClient httpClient = null;
		HttpGet httpGet = null;
		HttpResponse response = null;
		try {
			httpClient = HttpClients.createDefault();
			httpGet = new HttpGet(url);
			response = httpClient.execute(httpGet);
			return response.getStatusLine().getStatusCode();
		} catch (Exception e) {
			return 9999;
		} finally {
			if (httpGet != null) {
				httpGet.releaseConnection();
			}
		}
	}
	
	/**
	 * POST请求
	 * @param url 请求地址
	 * @param data 请求数据
	 * @return 响应数据
	 * @throws Exception
	 */
	public String post(String url, String data) throws Exception{
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		HttpResponse response = null;
		String result = "";
		try {
			httpClient = HttpClients.createDefault();
			httpPost = new HttpPost(url);
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(connectionTimeout)
					.setConnectTimeout(connectionTimeout)
					.setSocketTimeout(connectionTimeout)
					.build();
			httpPost.setConfig(requestConfig);
			
			StringEntity dataEntity = new StringEntity(data, Charset.forName("UTF-8"));
			httpPost.setEntity(dataEntity);
			response = httpClient.execute(httpPost);
			int retCode = response.getStatusLine().getStatusCode();
			if (200 == retCode){
				HttpEntity entity = response.getEntity();
				InputStream instream = entity.getContent();
				// 不超过2048M的内容可以转
				int total = (int) entity.getContentLength();
				byte[] bytes = new byte[total];
				int readCount = 0;
				int countTemp = 0;
				if ((countTemp = instream.read(bytes, readCount, total)) != -1) {
					readCount += countTemp;
				}
				result = new String(bytes, "UTF-8");
			} else {
				throw new Exception("请求失败,状态码为[" + retCode + "]");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (httpPost != null) {
				httpPost.releaseConnection();
			}
		}
		return result;
	}
}
