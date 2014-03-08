package htmlparse;

import java.net.HttpURLConnection;
import java.net.URL;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

public class HtmlParser {

	public static void main(String[] args) throws Exception {
		//String url = "http://localhost:8181/spring/redirectPage.jsp";
		URL url = new URL("http://localhost:8181/spring/redirectPage.jsp");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		Parser parser = new Parser(conn);
		NodeIterator nodeItrator = parser.elements();
		/*while(nodeItrator.hasMoreNodes()){
			Node node = nodeItrator.nextNode();
			System.out.println(node.toHtml());
			System.out.println();
		}*/
		
		//
		/*NodeFilter filter = new TagNameFilter("div");
		NodeList nodes = parser.extractAllNodesThatMatch(filter);
		if(nodes != null){
			for(int i = 0; i < nodes.size(); i++){
				Node textNode = (Node)nodes.elementAt(i);
				System.out.println(textNode.getText());
			}
		}*/
		
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
		}
	}

}
