package myTodoList.edit;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import myTodoList.bin.CommonsWeatherUtils;

public class WeatherFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	Font font1 = new Font("����_GB2312", Font.BOLD, 13);// ����
	Font font2 = new Font("����_GB2312", Font.BOLD, 13);
	Font font3 = new Font("����_GB2312", Font.PLAIN, 12);

	String time;// ����ʱ��
	String Cityid;// ���еĴ���

	JTabbedPane tabbedPane; // ��2ҳ��ʾ
	JSplitPane Todaypanel;// �ֿ������������Ϊ2����
	JPanel Aboutpanel;
	JPanel left = new JPanel();// ��������������
	JPanel right = new JPanel();// ���������ұ����

	// ������������
	JLabel chooseCity;// ���ó���
	JComboBox<Object> jcb;
	JButton chooseButton;
	JLabel share;		//��������
	JButton shareButton;
	JTextArea area;
	String message = null;
    
	static JLabel source = new JLabel("������Դ�����������ӿ�");
	static JLabel author = new JLabel("���ߣ� Frank");
	// ��ʾ������е�����
	CommonsWeatherUtils wea;  
	JLabel cityLabel = new JLabel(); // ����
	JLabel fchh = new JLabel();     //����ʱ��
	JLabel Image1 = new JLabel();// today�ұߵ�ͼ��

	JLabel weather1 = new JLabel();// �������
	// ��ʾ������е�����
	JLabel city = new JLabel();// ����
	JLabel city1 = new JLabel();
	JLabel week = new JLabel();// ����
	JLabel week1 = new JLabel();
	JLabel date_y = new JLabel();// ����ʱ��
	JLabel wind1 = new JLabel();// ���

	JLabel fl1 = new JLabel();			// ����
	JLabel fl2 = new JLabel();
	JLabel fl3 = new JLabel();
	JLabel fl4 = new JLabel();
	JLabel fl5 = new JLabel();
	JLabel fl6 = new JLabel();

	JLabel temp1 = new JLabel();// ����
	JLabel temp2 = new JLabel();
	JLabel temp3 = new JLabel();
	JLabel temp4 = new JLabel();
	JLabel temp5 = new JLabel();
	JLabel temp6 = new JLabel();

	JLabel index = new JLabel();// ����ָ��
	JLabel index_uv = new JLabel();// ������
	JLabel index_tr = new JLabel();// ����
	JLabel index_co = new JLabel();// ����ָ��
	JLabel index_cl = new JLabel();// �˶�ָ��
	JLabel index_xc = new JLabel();// ϴ��ָ��

	

	public WeatherFrame() throws IOException {
		this.setTitle("�鿴����");
		setIconImage(getToolkit().getImage("src/image/Calendar.png"));    // ����ͼ��
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX = screenSize.width / 2;				// ��Ļ����x ����
		int centerY = screenSize.height / 2; 			// ��Ļ����y ����
		int w = 800;		// ��������
		int h = 365;		// ������߶�
		this.setBounds(centerX - w / 2, centerY - h / 2, w, h);// ���ô����������Ļ����
		this.setVisible(true);
		this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		tabbedPane = new JTabbedPane();
		ImageIcon icon1 = new ImageIcon("src/img/today.png");
		ImageIcon icon5 = new ImageIcon("src/img/about.png");

		Todaypanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
		Todaypanel.setDividerLocation(225);
		Todaypanel.setDividerSize(0);
		left.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		right.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		tabbedPane.addTab(" �������� ", icon1, Todaypanel, "���ǽ��������");

		Aboutpanel = new JPanel();
		Aboutpanel.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		tabbedPane.addTab(" ���� ", icon5, Aboutpanel, "����");
		Aboutpanel.setLayout(null);
		
		chooseCity = new JLabel("���ó���:");
		jcb = new JComboBox<Object>();
		String[] citys = {"����", "����", "����", "�Ϻ�", "����"};
		jcb.setModel(new DefaultComboBoxModel<>(citys));
		jcb.setEditable(false);			//��������
		chooseButton = new JButton("ȷ��");
		
	    chooseCity.setBounds(250, 30, 70,30);
	    jcb.setBounds(325, 30, 110, 30);
	    chooseButton.setBounds(440, 30, 70, 30);
		author.setBounds(650, 230, 100, 20);
		author.setFont(font3);
		
		Aboutpanel.add(chooseCity);
		Aboutpanel.add(jcb);
		Aboutpanel.add(chooseButton);
		Aboutpanel.add(author);

        this.add(tabbedPane);
		String CityItem = "����";
        Map<String, String> weatherInfoMap;
        System.out.println("CityItem:" + CityItem);
		weatherInfoMap = CommonsWeatherUtils.weatherInfo(CityItem);// �����³��е�����
		System.out.println("weatherInfoMap:" + weatherInfoMap);
		show(weatherInfoMap);
		tabbedPane.setSelectedIndex(0);
		
		
		chooseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String CityItem = "����";
				Map<String, String> weatherInfoMap;
				CityItem = (String)jcb.getSelectedItem();	// ��ȡ��ǰѡ�еĳ���
				try {
					System.out.println("CityItem:" + CityItem);
					weatherInfoMap = CommonsWeatherUtils.weatherInfo(CityItem);// �����³��е�����
					System.out.println("weatherInfoMap:" + weatherInfoMap);
					show(weatherInfoMap);
					tabbedPane.setSelectedIndex(0);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}


	public void show(Map<String, String> infoMap) {
		String[] nodes = {"city", "status1", "direction1", 
        		"power1", "temperature1", "pullution", 
        		"pullution_s", "gm_s", "chy_shuoming", 
        		"yd_s", "zwx_s"};
		left.setLayout(null);

		
		Calendar c = Calendar.getInstance();   // ������ǰ�������� ������������
		time t = new time(c);

		// ���ý�������������
		weather1.setBounds(40, 5, 200, 80);
		weather1.setFont(font1);
		weather1.setText(infoMap.get(nodes[1]));
		weather1.setIcon(new ImageIcon(setimage(infoMap.get(nodes[1]))));
		left.add(weather1);

		city.setFont(font1);
		city.setBounds(15, 85, 235, 20);
		city.setText(infoMap.get(nodes[0]));
		left.add(city);

		week.setFont(font1);
		week.setBounds(15, 105, 235, 20);
		week.setText("���ڣ�" + t.weekOfDay);
		left.add(week);

		date_y.setFont(font1);
		date_y.setBounds(15, 125, 235, 20);
		date_y.setText("���ڣ�" + t.month + "��" + t.day + "��");
		left.add(date_y);

		JLabel seperate = new JLabel(
				"------------------------------------------------");
		seperate.setBounds(15, 145, 235, 20);
		left.add(seperate);

		temp1.setFont(font1);
		temp1.setBounds(15, 165, 235, 20);
		temp1.setText("����:" +  infoMap.get(nodes[4]) + "�� ");
		left.add(temp1);

		wind1.setFont(font1);
		wind1.setBounds(15, 185, 235, 20);
		wind1.setText("����:" + infoMap.get(nodes[2]));
		left.add(wind1);

		fl1.setFont(font1);
		fl1.setBounds(15, 205, 235, 20);
		fl1.setText("�缶:" + infoMap.get(nodes[3]));
		left.add(fl1);

		// ��������ұߵ���������
		right.setLayout(null);
		Image1.setBounds(35, 5, 235, 60);
		Image1.setFont(font2);
		Image1.setText("����������Ϣ");
		Image1.setIcon(new ImageIcon("img/index.png"));
		right.add(Image1);

		index.setBounds(15, 65, 700, 35);
		index.setFont(font2);
		index.setText("����˵����" + infoMap.get(nodes[8]));
		index.setIcon(new ImageIcon("src/img/cy.png"));
		right.add(index);

		index_uv.setBounds(15, 100, 700, 35);
		index_uv.setFont(font2);
		index_uv.setText("������˵����" + infoMap.get(nodes[10]));
		index_uv.setIcon(new ImageIcon("src/img/uv.png"));
		right.add(index_uv);

		index_tr.setBounds(15, 135, 700, 35);
		index_tr.setFont(font2);
		if(infoMap.get(nodes[6]) != null) {
			index_tr.setText("��Ⱦ˵����" + infoMap.get(nodes[6]));
		}else {
			index_tr.setText("��Ⱦ˵����" + "��");
		}
		
		index_tr.setIcon(new ImageIcon("src/img/tr.png"));
		right.add(index_tr);

		index_co.setBounds(15, 170, 700, 35);
		index_co.setFont(font2);
		index_co.setText("Ԥ����ð��" + infoMap.get(nodes[7]));
		index_co.setIcon(new ImageIcon("src/img/co.png"));
		right.add(index_co);

		index_cl.setBounds(15, 205, 700, 35);
		index_cl.setFont(font2);
		index_cl.setText("�˶�˵����" + infoMap.get(nodes[9]));
		index_cl.setIcon(new ImageIcon("src/img/cl.png"));
		right.add(index_cl);

		Aboutpanel.repaint();
		Todaypanel.repaint();
		left.repaint();
		right.repaint();
	}

	// ����ʱ���ࣨ�ڲ��ࣩ
	class time {

		int year;
		int month;
		int weekDay;
		int day;
		String weekOfDay;
		Calendar c;

		// ������ǰ�������� ����������
		public time(Calendar c) {

			// ������ǰ�������� ����������
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH) + 1;
			day = c.get(Calendar.DAY_OF_MONTH);
			weekDay = c.get(Calendar.DAY_OF_WEEK);

			switch (weekDay) {
			case 1:
				weekOfDay = "������";
				break;
			case 2:
				weekOfDay = "����һ";
				break;
			case 3:
				weekOfDay = "���ڶ�";
				break;
			case 4:
				weekOfDay = "������";
				break;
			case 5:
				weekOfDay = "������";
				break;
			case 6:
				weekOfDay = "������";
				break;
			case 7:
				weekOfDay = "������";
			default:
				break;
			}

		}

		public int getYear() {
			return year;
		}

		public int getMonth() {
			return month;
		}

		public int getDay() {
			return day;
		}

		public String getweekOfDay() {
			return weekOfDay;
		}

	}
	
	/**
	 * �趨���������ͼƬ
	 * @param weather
	 * @return
	 */
	public  String setimage (String  weather) {
		switch (weather) {
		case "��":
			return "src/image/0.png";
		case "����":
			return "src/image/1.png";
		case "��":
			return "src/image/2.png";
		case "С��":
			return "src/image/3.png";
		case "����":
			return "src/image/4.png";
		case "����":
			return "src/image/5.png";
		case "����":
			return "src/image/6.png";
		case "������":
			return "src/image/7.png";
		case "����":
			return "src/image/8.png";
		case "���ѩ":
			return "src/image/9.png";
		case "Сѩ":
			return "src/image/10.png";
		case "��ѩ":
			return "src/image/11.png";
		case "��ѩ":
			return "src/image/12.png";
		case "��ѩ":
			return "src/image/13.png";
		case "С������":
			return "src/image/15.png";
		case "�е�����":
			return "src/image/16.png";
		case "�󵽱���":
			return "src/image/17.png";
		case "��":
			return "src/image/18.png";
		default:
			return "src/image/6.png";
		}
	}

	public static void main(String[] args) throws IOException {
		WeatherFrame test = new WeatherFrame();
		test.show();
    }
}
