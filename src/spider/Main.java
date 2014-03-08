package spider;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.http.client.ClientProtocolException;

public class Main {

	/**
	 * @param args
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static void main(String[] args) throws ClientProtocolException,
			IOException {
		//mimi
		/*String hostName = "http://www.mimilike.com/";
		String loginUrl = "forumdisplay.php?fid=81&page=";
		for (int i = 9; i < 10; i++) {
			//ImagePageRun imr = ;
			Executor ex = Executors.newFixedThreadPool(5);
			ex.execute(new ImagePageRun(new MimiHttp(hostName, loginUrl + i)));
			
			//Thread.currentThread().
		}*/
		
		//草榴
		
		//String hostName = "http://184.154.178.130/";
		//String loginUrl = "thread0806.php?fid=8&search=&page=4";
		// http = new MimiHttp(hostName, loginUrl);
		//http.listUrl();
		//index.php">草榴社區</a> 
		//ImagePage imagePage = new ImagePage("http://184.154.178.130/htm_data/8/1402/1042310.html");
		//imagePage.listImageUrl();
		//String hostName = DataConfig.hostName;
		//String loginUrl = DataConfig.mainPage;
		Executor ex = Executors.newFixedThreadPool(2);
		for (int i = 1; i < 2; i++) {			
			ex.execute(new MainPageThread(DataConfig.hostName, DataConfig.mainPage + i));
		}
		
		//测试
		/*String hostName = "";
		String loginUrl = "http://184.154.178.130/thread0806.php?fid=8&search=&type=1&page=5";
		MainPage m = new MainPage(hostName, loginUrl);
		m.listUrl();*/		
		
	}

}
