package spider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * 读取登陆页下面的标题页
 * 
 * @author hp
 * 
 */
@SuppressWarnings("deprecation")
public class MainPage {

	private String hostName;

	private String loginUrl;

	private HttpClient httpClient;

	public MainPage(String hostName, String loginUrl) {
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
	public List<String> listUrl() {
		List<String> listString = new ArrayList<String>();

		httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(hostName + loginUrl);
		HttpResponse response;
		try {
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			
			if (entity != null) {
				InputStream in = entity.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(in,
						"GBK"));
				String s = "";
				while ( (s = br.readLine()) != null) {
					try {
						if (s.contains(DataConfig.mainPageStartFlag)
								&& s.contains(DataConfig.mainPageEndFlag)) {
							s = s.substring(s.indexOf(DataConfig.mainPageStartFlag)
									+ DataConfig.mainPageStartFlag.length(),
									s.indexOf(DataConfig.mainPageEndFlag));
							if (!s.contains("index.php\">")) {
								listString.add(hostName + s);
							}
						}
						//s = br.readLine();
						//System.out.println(s);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		httpClient.getConnectionManager().shutdown();
		return listString;
	}

	public List<String> listUrlByHtmlparse(String path_url)
			throws ClientProtocolException, IOException {

		List<String> listString = new ArrayList<String>();

		httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(hostName + loginUrl);
		HttpResponse response;
		response = httpClient.execute(httpGet);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			InputStream in = entity.getContent();
			BufferedReader br = new BufferedReader(new InputStreamReader(in,
					"GBK"));
			// StringBuffer sb = new StringBuffer();
			String s = "";
			while (s != null) {
				System.out.println(s);
				if (s.contains(DataConfig.mainPageStartFlag)
						&& s.contains(DataConfig.mainPageEndFlag)) {
					s = s.substring(s.indexOf(DataConfig.mainPageStartFlag)
							+ DataConfig.mainPageStartFlag.length(),
							s.indexOf(DataConfig.mainPageEndFlag));
					if (!s.contains("index.php\">")) {
						listString.add(hostName + s);
					}
				}
				
				s = br.readLine();
			}

			// System.out.println(sb.toString());

		}
		httpClient.getConnectionManager().shutdown();
		return listString;
	}
}
