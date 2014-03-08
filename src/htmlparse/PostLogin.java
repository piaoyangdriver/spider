package htmlparse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/*
 * 登录成功后返回的
 */
public class PostLogin {
	
	private CloseableHttpClient httpClient;
	
	private String loginUrl;
	
	private Map<String, String> nameValuesPair;
	
	public PostLogin(CloseableHttpClient httpClient, String loginUrl, Map<String, String> nameValuesPair) {
		super();
		this.httpClient = httpClient;
		this.loginUrl = loginUrl;
		this.nameValuesPair = nameValuesPair;
	}
	
	public String getLoginStatusLine(){
		String statusLine = "";
		HttpPost post = new HttpPost(loginUrl);
		//ajax请求，接受的是ajax返回结果
		/*post.addHeader(HttpHeaders.ACCEPT, "application/json");
		post.addHeader("X-Requested-With", "XMLHttpRequest");*/
		//post.addHeader(HttpHeaders.ACCEPT, "application/json");
		//post.addHeader("X-Requested-With", "XMLHttpRequest");
		post.addHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		post.addHeader("Connection", "keep-alive");
		post.addHeader("DNT", "1");
		post.setHeader("Cookie", "is_use_cookiex=yes; cdb_cookietime=315360000; cdb_sid=LW46Lc; is_use_cookied=yes; AJSTAT_ok_pages=22; AJSTAT_ok_times=12");
		post.addHeader("Content-Type", "application/x-www-form-urlencoded");
		post.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		Iterator<Entry<String, String>> it = nameValuesPair.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, String> entry = it.next();
			String key = entry.getKey();
			String value = entry.getValue();
			nvps.add(new BasicNameValuePair(key, value));
		}		
		try {
			post.setEntity(new UrlEncodedFormEntity(nvps));
			CloseableHttpResponse response = httpClient.execute(post);
			statusLine = response.getStatusLine().toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return statusLine;		
	}

	/**
	 * 打印登录成功后的页面
	 */
	public void printLoginReturnContent(){
		HttpPost post = new HttpPost(loginUrl);
		//post.addHeader(HttpHeaders.ACCEPT, "application/json");
		//post.addHeader("X-Requested-With", "XMLHttpRequest");
		//post.addHeader(HttpHeaders.ACCEPT, "text/html");
		post.addHeader("Content-Type", "application/x-www-form-urlencoded");
		post.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		Iterator<Entry<String, String>> it = nameValuesPair.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, String> entry = it.next();
			String key = entry.getKey();
			String value = entry.getValue();
			nvps.add(new BasicNameValuePair(key, value));
		}		
		try {
			post.setEntity(new UrlEncodedFormEntity(nvps));
			CloseableHttpResponse response = httpClient.execute(post);
			HttpEntity entity = response.getEntity();
			if(entity != null){
				//ContentType contentType = ContentType.getOrDefault(entity);
				//Charset charset = contentType.getCharset();
				InputStream inputStream = entity.getContent();
				BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
				String line = null;
				while((line = bf.readLine()) != null){
					System.out.println(line);
				}
				EntityUtils.consume(entity);				
				bf.close();
				inputStream.close();
			}
			response.close();
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 需要在登录后操作
	 * @return 
	 */
	public InputStream printSessionPage(String sessionUrl){
		InputStream inputStream = null;
		//登录成功后
		HttpGet httpget = new HttpGet(sessionUrl);
		CloseableHttpResponse response;
		try {
			response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if(entity != null){
				inputStream = entity.getContent();
				//sessionPageContentStream = inputStream;
				/*BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
				String line = null;
				while((line = bf.readLine()) != null){
					//System.out.println(line);
				}*/
				EntityUtils.consume(entity);				
				//bf.close();
				//inputStream.close();
			}
			//response.close();			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inputStream;
	}
	
	public String getSessionPage(String sessionUrl){
		StringBuffer sb = new StringBuffer();
		InputStream inputStream = null;
		HttpGet httpget = new HttpGet(sessionUrl);
		CloseableHttpResponse response;
		try {
			response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if(entity != null){
				inputStream = entity.getContent();
				BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
				String line = null;
				while((line = bf.readLine()) != null){
					sb.append(line);
				}
				EntityUtils.consume(entity);				
				bf.close();
				inputStream.close();
			}
			response.close();			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	
	public static void main(String[] args) {
		String url = "http://localhost:8181/mbt/login/login.html";
		RequestConfig config = RequestConfig.custom()
				.setSocketTimeout(5000)
				.setConnectTimeout(5000)
				.setConnectionRequestTimeout(5000)
				.setStaleConnectionCheckEnabled(true)
				.build();
		
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(config).build();
		
		
		HttpPost post = new HttpPost(url);
		//ajax请求，接受的是ajax返回结果
		post.addHeader(HttpHeaders.ACCEPT, "application/json");
		post.addHeader("X-Requested-With", "XMLHttpRequest");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("username", "sony"));
		nvps.add(new BasicNameValuePair("password", "sony"));
		
		try {
			//添加post参数
			post.setEntity(new UrlEncodedFormEntity(nvps));
			CloseableHttpResponse response = httpclient.execute(post);
			//状态302的话,重定向,无法获取相应消息实体
			System.out.println(response.getStatusLine());
			
			HttpEntity entity = response.getEntity();
			if(entity != null){
				//ContentType contentType = ContentType.getOrDefault(entity);
				//Charset charset = contentType.getCharset();
				
				InputStream inputStream = entity.getContent();
				BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
				String line = null;
				while((line = bf.readLine()) != null){
					System.out.println(line);
				}
				EntityUtils.consume(entity);				
				bf.close();
				inputStream.close();
			}
			response.close();
			
			//登录成功后
			HttpGet httpget = new HttpGet("http://localhost:8181/mbt/mbtoncloud.html");
			CloseableHttpResponse response2 = httpclient.execute(httpget);
			
			HttpEntity entity2 = response2.getEntity();
			if(entity2 != null){
				InputStream inputStream2 = entity2.getContent();
				BufferedReader bf2 = new BufferedReader(new InputStreamReader(inputStream2));
				String line = null;
				while((line = bf2.readLine()) != null){
					System.out.println(line);
				}
				EntityUtils.consume(entity);				
				bf2.close();
				inputStream2.close();
			}
			response2.close();
			
			
			//返回状态是302的话
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{			
			if(httpclient != null){
				try {
					httpclient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
