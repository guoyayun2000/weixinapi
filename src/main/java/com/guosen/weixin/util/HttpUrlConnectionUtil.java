package com.guosen.weixin.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * http请求工具类
 * 
 * 创建于2016年12月14日
 * @author guosen
 *
 */
public class HttpUrlConnectionUtil {
	private static int connectionTimeout = 5000;
	private static int readTimeout = 5000;

	public HttpUrlConnectionUtil(ConfigUtil configUtil) {
		if (configUtil != null) {
			try {
				String cvalue = configUtil.getValue(ConfigUtil.CONNECTION_TIMEOUT);
				if (cvalue != null && !"".equals(cvalue)) {
					connectionTimeout = Integer.parseInt(cvalue);
				}
				String rvalue = configUtil.getValue(ConfigUtil.READ_TIMEOUT);
				if (rvalue != null && !"".equals(rvalue)) {
					readTimeout = Integer.parseInt(rvalue);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * POST请求
	 * 
	 * @param url
	 *            请求地址
	 * @param data
	 *            请求数据
	 * @return 响应数据
	 * @throws Exception
	 */
	public String post(String url, String data) throws Exception {
		HttpURLConnection http = null;
		URL finalUrl = null;
		String result = "";
		OutputStream dos = null;
		BufferedReader responseReader = null;
		try {
			finalUrl = new URL(url);
			http = (HttpURLConnection) finalUrl.openConnection();
			http.setDoInput(true);
			http.setDoOutput(true);
			http.setUseCaches(false);
			http.setConnectTimeout(connectionTimeout);
			http.setReadTimeout(readTimeout);
			http.setRequestMethod("POST");
			http.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			http.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
			http.setRequestProperty("Charset", "UTF-8");
			http.connect();
			String param = data;
			dos = http.getOutputStream();
		    dos.write(param.getBytes("UTF-8"));
		    dos.flush();
		    //获得响应状态
		    int retCode = http.getResponseCode();
			if (HttpURLConnection.HTTP_OK == retCode) {
				StringBuffer sb=new StringBuffer();
			    String readLine=new String();
			    responseReader = new BufferedReader(new InputStreamReader(http.getInputStream(),"UTF-8"));
			    while((readLine=responseReader.readLine())!=null){
			    	sb.append(readLine).append("\n");
			    }
				result = sb.toString();
			} else {
				throw new Exception("请求失败,状态码为[" + retCode + "]");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (dos != null) {
				dos.close();
			}
			if (responseReader != null) {
				responseReader.close();
			}
		}
		return result;
	}
}
