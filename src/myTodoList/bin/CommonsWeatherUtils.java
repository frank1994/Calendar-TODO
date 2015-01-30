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
 * ����xml�ĵ������������ĵ���url�����Խ���
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
	 * ͨ������������ȡ���˽ӿ���Ϣ
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
					"��Դ�޷����ʣ���鿴�����Ƿ�����", "alert", JOptionPane.ERROR_MESSAGE); 
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
	 * @return �����ڵ���ֵ�Էֺŷָ�
	 */
	public Map<String, String> getValue(String[] nodes) {
		if (inStream == null || root==null) {
			return null;
		}
		
		Map<String, String> map = new HashMap<String, String>();
		
		// ��ʼ��ÿ���ڵ��ֵΪnull
		for (int i = 0; i < nodes.length; i++) {
			map.put(nodes[i], null);
		}

		// ������һ�ڵ�
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
	 * ͨ��������������������ϢMap
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
         * city->������             // ��ǩ1������죬2����ҹ��
         * status1->�磬��ȡ�
         * direction1->����
         * power1->�缶
         * temperature1->�¶�
         *           ssd->���ָ��ϵ��
         * pollution->��Ⱦָ����ֵ
         * pullution_l->��Ⱦָ������
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
		map = weatherInfo("����");
		
		System.out.println("���У�" + map.get(nodes[0]));
		System.out.println("����������" + map.get(nodes[1]));
		System.out.println("����" + map.get(nodes[2]));
		System.out.println("�缶��" + map.get(nodes[3]));
		System.out.println("�¶ȣ�" + map.get(nodes[4]) + "�� ");
		System.out.println("��Ⱦָ����" + map.get(nodes[5]) + "����Ⱦ");
		System.out.println("��Ⱦָ��˵����" + map.get(nodes[6]));
		System.out.println("��ðָ��˵����" + map.get(nodes[7]));
		System.out.println("����ָ��˵����" + map.get(nodes[8]));
		System.out.println("�˶�ָ��˵����" + map.get(nodes[9]));
		System.out.println("������˵����" + map.get(nodes[10]));
	}
}
