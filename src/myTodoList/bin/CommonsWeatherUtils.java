package myTodoList.bin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 解析xml文档，包括本地文档和url都可以解析
 */
public class CommonsWeatherUtils {
	InputStream inStream;
	Element root;

    public InputStream getInStream() {
		return inStream;
	}

	public void setInStream(InputStream inStream) {
		this.inStream = inStream;
	}

	public Element getRoot() {
		return root;
	}

	public void setRoot(Element root) {
		this.root = root;
	}

	public CommonsWeatherUtils() {
	}

	/**
	 * 通过输入流来获取新浪接口信息
	 * @param inStream
	 */
	public CommonsWeatherUtils(InputStream inStream) {
		if (inStream != null) {
			this.inStream = inStream;
			DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder domBuilder = domfac.newDocumentBuilder();
				Document doc = domBuilder.parse(inStream);
				root = doc.getDocumentElement();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public CommonsWeatherUtils(String path) {
		InputStream inStream = null;
		try {
			inStream = new FileInputStream(path);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		if (inStream != null) {
			this.inStream = inStream;
			DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder domBuilder = domfac.newDocumentBuilder();
				Document doc = domBuilder.parse(inStream);
				root = doc.getDocumentElement();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public CommonsWeatherUtils(URL url) {
		InputStream inStream = null;
		try {
			inStream = url.openStream();
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, 
					"资源无法访问，请查看网络是否连接", "alert", JOptionPane.ERROR_MESSAGE); 
			e1.printStackTrace();
		}
		
		if (inStream != null) {
			this.inStream = inStream;
			DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder domBuilder = domfac.newDocumentBuilder();
				Document doc = domBuilder.parse(inStream);
				root = doc.getDocumentElement();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param nodes
	 * @return 单个节点多个值以分号分隔
	 */
	public Map<String, String> getValue(String[] nodes) {
		if (inStream == null || root==null) {
			return null;
		}
		
		Map<String, String> map = new HashMap<String, String>();
		
		// 初始化每个节点的值为null
		for (int i = 0; i < nodes.length; i++) {
			map.put(nodes[i], null);
		}

		// 遍历第一节点
		NodeList topNodes = root.getChildNodes();
		if (topNodes != null) {
			for (int i = 0; i < topNodes.getLength(); i++) {
				Node book = topNodes.item(i);
				if (book.getNodeType() == Node.ELEMENT_NODE) {
					for (int j = 0; j < nodes.length; j++) {
						for (Node node = book.getFirstChild(); node != null; node = node.getNextSibling()) {
							if (node.getNodeType() == Node.ELEMENT_NODE) {
								if (node.getNodeName().equals(nodes[j])) {
									String val = node.getTextContent();
									String temp = map.get(nodes[j]);
									if (temp != null && !temp.equals("")) {
										temp = temp + ";" + val;
									} else {
										temp = val;
									}
									map.put(nodes[j], temp);
								}
							}
						}
					}
				}
			}
		}
		return map;
	}
	
	/**
	 * 通过城市名，返回天气信息Map
	 * @param city
	 * @return
	 */
	public static Map<String, String> weatherInfo(String city) throws MalformedURLException {
		String citycode = new String();
		try{
			citycode = URLEncoder.encode(city, "GBK");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String link="http://php.weather.sina.com.cn/xml.php?city=" + citycode + "&password=DJOYnieT8234jlsK&day=0";
		URL url;
		url = new URL(link);
        CommonsWeatherUtils parser = new CommonsWeatherUtils(url);
        /**
         * city->城市名             // 标签1代表白天，2代表夜间
         * status1->晴，雨等。
         * direction1->风向
         * power1->风级
         * temperature1->温度
         *           ssd->体感指数系数
         * pollution->污染指数数值
         * pullution_l->污染指数描述
         * 
         */
        String[] nodes = {"city", "status1", "direction1", 
        		"power1", "temperature1", "pullution", 
        		"pullution_s", "gm_s", "chy_shuoming" , 
        		"yd_s", "zwx_s"};
		Map<String, String> map = parser.getValue(nodes);
		return map;
	}
	
	public static void main(String args[]) throws MalformedURLException {
		Map<String, String> map;
		String[] nodes = {"city", "status1", "direction1", 
        		"power1", "temperature1", "pullution", 
        		"pullution_s", "gm_s", "chy_shuoming", 
        		"yd_s", "zwx_s"};
		map = weatherInfo("兰州");
		
		System.out.println("城市：" + map.get(nodes[0]));
		System.out.println("今天天气：" + map.get(nodes[1]));
		System.out.println("风向：" + map.get(nodes[2]));
		System.out.println("风级：" + map.get(nodes[3]));
		System.out.println("温度：" + map.get(nodes[4]) + "℃ ");
		System.out.println("污染指数：" + map.get(nodes[5]) + "级污染");
		System.out.println("污染指数说明：" + map.get(nodes[6]));
		System.out.println("感冒指数说明：" + map.get(nodes[7]));
		System.out.println("穿衣指数说明：" + map.get(nodes[8]));
		System.out.println("运动指数说明：" + map.get(nodes[9]));
		System.out.println("紫外线说明：" + map.get(nodes[10]));
	}
}
