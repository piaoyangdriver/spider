package spider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class MainPageThread implements Runnable {

	private String hostName;

	private String loginUrl;

	public MainPageThread(String hostName, String loginUrl) {
		this.hostName = hostName;
		this.loginUrl = loginUrl;
	}

	/**
	 * 字符串拼串
	 * 
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private List<String> listUrl() {
		//List<String> listString = new CopyOnWriteArrayList<String>();
		List<String> listString = Collections.synchronizedList(new ArrayList<String>(110));
		
		// HttpClient httpClient = new DefaultHttpClient();
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(hostName + loginUrl);
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(60000).setConnectTimeout(60000 * 2).build();// 设置请求和传输超时时间
		httpGet.setConfig(requestConfig);

		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream in = entity.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						in, "GBK"));
				String s = "";
				while ((s = br.readLine()) != null) {
					try {
						if (s.contains(DataConfig.mainPageStartFlag)
								&& s.contains(DataConfig.mainPageEndFlag)) {
							s = s.substring(
									s.indexOf(DataConfig.mainPageStartFlag)
											+ DataConfig.mainPageStartFlag
													.length(),
									s.indexOf(DataConfig.mainPageEndFlag));
							if (!s.contains("index.php\">")) {
								listString.add(hostName + s);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return listString;
	}

	@Override
	public void run() {

		List<String> list = listUrl();
		synchronized (list) {
			if (list != null && list.size() > 0) {
				Executor ex2 = Executors.newFixedThreadPool(6);
				// 6个线程,每个线程处理主页的图片网页链接
				for (String s : list) {
					/*
					 * ImagePage imagePage = new ImagePage(s); List<String>
					 * listImage = imagePage.listImageUrl(); if(listImage !=
					 * null && listImage.size() > 0){ for(String img :
					 * listImage){ System.out.println("write img: " + img + " "
					 * + Thread.currentThread().getName()); writeImage(img); } }
					 */
					ex2.execute(new ImagePageThread(s, Thread.currentThread()
							.getName()));
				}
			}
		}

	}
}
