package htmlparse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class TestLogin {
	
	public static void main(String[] args) throws IOException, ParserException {
		RequestConfig config = RequestConfig.custom()
				.setSocketTimeout(5000)
				.setConnectTimeout(5000)
				.setConnectionRequestTimeout(5000)
				.setStaleConnectionCheckEnabled(true)
				.build();
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(config).build();
		
		String loginUrl = "http://www.mayalike.com:8000/logging.php?action=login";
		
		Map<String, String> nameValuesPair = new HashMap<String, String>();
		//nameValuesPair.put("username", "sony");
		//nameValuesPair.put("password", "sony");
		nameValuesPair.put("username", "piaoyang");
		nameValuesPair.put("password", "5131579417");
		nameValuesPair.put("uid", "");
		
		nameValuesPair.put("formhash", "ac7af991");
		nameValuesPair.put("referer", "index.php");
		nameValuesPair.put("questionid", "0");
		nameValuesPair.put("answer", "");
		nameValuesPair.put("cookietime", "0");
		nameValuesPair.put("loginmode", "");
		nameValuesPair.put("styleid", "");
		nameValuesPair.put("action", "login");
		//nameValuesPair.put("loginsubmit", "");
		PostLogin postLogin = new PostLogin(httpclient, loginUrl, nameValuesPair);
		System.out.println(postLogin.getLoginStatusLine());
		postLogin.printLoginReturnContent();
		
		//String content = postLogin.getSessionPage("http://www.mayalike.com:8000/index.php");
		
		
		/*Parser parser = new Parser(content);
		NodeFilter textFilter = new NodeClassFilter(TextNode.class);
		NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
		NodeFilter titleFilter = new NodeClassFilter(TitleTag.class);		
		OrFilter lastFilter = new OrFilter(new NodeFilter[]{
				linkFilter});
		
		
		NodeList nodelist = parser.parse(lastFilter);
		Node[] nodes = nodelist.toNodeArray();
		
		String line = "";
		for(int i = 0; i < nodes.length; i++){
			Node n = nodes[i];
			if(n instanceof TextNode){
				TextNode tn = (TextNode)n;
				line = tn.getText();
			}else if(n instanceof TitleTag){
				TitleTag titleN = (TitleTag)n;
				line = titleN.getTitle();
			}else if(n instanceof LinkTag){
				LinkTag lt = (LinkTag)n;
				line = lt.getLinkText();
			}
			
			if(!"".equals(line)){
				System.out.println(line);
			}
		}*/
		
		
		//System.out.println(content);
		
		httpclient.close();
		
	}

}
