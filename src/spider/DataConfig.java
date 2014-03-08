package spider;

import java.io.File;

public class DataConfig {
	
	
	/*草榴的标签
	 * hostName = http://184.154.178.130/
	 * mainPage = thread0806.php?fid=8&search=&page=1
	 * mainPageStartFlag = <a href="
	 * mainPageEndFlag = " target="_blank"
	 * imagePageStartFlag = <input type="image" src="
	 * imagePageEndFlag = " onclick=
	 * 
	 * <a href="htm_data/8/1402/1043033.html" target="_blank" id="">山野妹子，乡村自拍，真是别样风情啊[15P]</a>
	<input type="image" src="http://www.astridsangelcash.com/tgp/jnstgp/033/01.jpg" 
 * onclick="window.open('http://www.viidii.info/?http://www______astridsangelcash______com/tgp/jnstgp/033/01______jpg&amp;z');return false;">
*/	
	public static String hostName = "http://184.154.178.130/";
	
	//写真http://184.154.178.130/thread0806.php?fid=8&search=&type=4&page=2
	//亚洲type=1
	public static String mainPage = "thread0806.php?fid=8&search=&type=4&page=";
	
	public static String mainPageStartFlag = "<a href=\"";
	
	public static String mainPageEndFlag = "\" target=\"_blank\"";
	
	public static String imagePageStartFlag = "<input type='image' src='";
	
	public static String imagePageEndFlag = "' onclick=";
	
	public static String picLocation = "D:\\spider\\3-2-xiez\\";//D:\\httpPic1\\"
	
	public static int imageSize = 24*1024;//20KB
	
	public static long contentLength = 20*1024*1024*8;//读取网页返回内容大小
	
	static{
		if(picLocation != null){
			//创建文件夹
			File file = new File(picLocation);
			if(!file.exists()){
				boolean creadok = file.mkdirs();
				if (creadok) {
                    System.out.println( " ok:创建文件夹成功！ " );
                } else {
                    System.out.println( " err:创建文件夹失败！ " );                    
                } 
			}
		}
	}
	
	public static void main(String[] args){
		System.out.println(DataConfig.picLocation);
	}
}
