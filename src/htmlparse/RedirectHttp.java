package htmlparse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;

/**
 * 重定向
 * 
 * @author wzl
 * 
 */
public class RedirectHttp {

	public static void main(String[] args) throws IOException, URISyntaxException {
		String url = "http://localhost:8181/spring/login/login.html";
		RequestConfig config = RequestConfig.custom().
				setSocketTimeout(5000).
				setConnectTimeout(5000).
				setConnectionRequestTimeout(5000).
				setStaleConnectionCheckEnabled(true)
				.build();		
		LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();
		
		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultRequestConfig(config)
				.setRedirectStrategy(redirectStrategy)
				.build();
		HttpPost post = new HttpPost(url);
		HttpClientContext context = HttpClientContext.create();
		try {
			//302的话是重定向
			CloseableHttpResponse response = httpclient.execute(post);
			System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			
			//输出最终访问地址
			HttpHost targetHost = context.getTargetHost();
			System.out.println(targetHost);
			List<URI> redirectLocations = context.getRedirectLocations();
			
			URI location = URIUtils.resolve(post.getURI(), targetHost, redirectLocations);
			System.out.println("fina http location: " + location.toASCIIString());
			
			if(entity != null){
				ContentType contentType = ContentType.getOrDefault(entity);
				Charset charset = contentType.getCharset();
				InputStream input = entity.getContent();
				BufferedReader bf = new BufferedReader(new InputStreamReader(input, charset));
				String line = null;
				while((line = bf.readLine()) != null){
					System.out.println(line);
				}
				input.close();
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (httpclient != null) {
				httpclient.close();
			}
		}
	}

}
