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
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * 打开标题的页面
 * @author hp
 *
 */
public class ImagePageThread implements Runnable {
	
	private String imgPageUrl;
	
	private String mainPageThread;
	
	public ImagePageThread(String imgPageUrl, String mainPageThread){
		this.imgPageUrl = imgPageUrl;
		this.mainPageThread = mainPageThread;
	}
	
	public List<String> listImageUrl() {
		//List<String> listString = new ArrayList<String>();
		List<String> listString = Collections.synchronizedList(new ArrayList<String>(50));
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(imgPageUrl);
		
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000*2).build();//设置请求和传输超时时间
		httpGet.setConfig(requestConfig);
		
		CloseableHttpResponse response = null;		
		try{
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream in = entity.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(in,  "GBK"));
				String s;
				while ((s = br.readLine()) != null) {
					try{
						String[] line = s.split("<br>");
						for(String temp : line){
							if(temp.contains(DataConfig.imagePageStartFlag) && temp.contains(DataConfig.imagePageEndFlag)){
								String temp2 = temp.substring(temp.indexOf(DataConfig.imagePageStartFlag) + DataConfig.imagePageStartFlag.length(), temp.indexOf(DataConfig.imagePageEndFlag));
								listString.add(temp2);;
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try {
				if(response != null){
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
		List<String> listImage = listImageUrl();
		synchronized(listImage){
			if(listImage != null && listImage.size() > 0){
				Executor ex3 = Executors.newFixedThreadPool(3);
				for(String img : listImage){
					//每张图片对于一个线程				
					//System.out.println("write img: " + img + " " +  Thread.currentThread().getName());
					ex3.execute(new WriteImageThread(img, mainPageThread, Thread.currentThread().getName()));
				}
			}
		}
	}
	
	
	 
	/*public static void main(String[] args) throws IOException{
		(new ImagePage("http://www.mimilike.com/viewthread.php?tid=1099284")).listImageUrl();
	}*/
}
