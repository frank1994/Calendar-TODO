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
	Font font1 = new Font("楷体_GB2312", Font.BOLD, 13);// 字体
	Font font2 = new Font("楷体_GB2312", Font.BOLD, 13);
	Font font3 = new Font("楷体_GB2312", Font.PLAIN, 12);

	String time;// 日历时间
	String Cityid;// 城市的代码

	JTabbedPane tabbedPane; // 分2页显示
	JSplitPane Todaypanel;// 分开今日天气面板为2部分
	JPanel Aboutpanel;
	JPanel left = new JPanel();// 今天天气左边面板
	JPanel right = new JPanel();// 今日天气右边面板

	// 设置面板的内容
	JLabel chooseCity;// 设置城市
	JComboBox<Object> jcb;
	JButton chooseButton;
	JLabel share;		//分享天气
	JButton shareButton;
	JTextArea area;
	String message = null;
    
	static JLabel source = new JLabel("天气来源：新浪天气接口");
	static JLabel author = new JLabel("作者： Frank");
	// 显示在面板中的内容
	CommonsWeatherUtils wea;  
	JLabel cityLabel = new JLabel(); // 城市
	JLabel fchh = new JLabel();     //发布时间
	JLabel Image1 = new JLabel();// today右边的图标

	JLabel weather1 = new JLabel();// 天气情况
	// 显示在面板中的内容
	JLabel city = new JLabel();// 城市
	JLabel city1 = new JLabel();
	JLabel week = new JLabel();// 星期
	JLabel week1 = new JLabel();
	JLabel date_y = new JLabel();// 今日时间
	JLabel wind1 = new JLabel();// 风况

	JLabel fl1 = new JLabel();			// 风速
	JLabel fl2 = new JLabel();
	JLabel fl3 = new JLabel();
	JLabel fl4 = new JLabel();
	JLabel fl5 = new JLabel();
	JLabel fl6 = new JLabel();

	JLabel temp1 = new JLabel();// 气温
	JLabel temp2 = new JLabel();
	JLabel temp3 = new JLabel();
	JLabel temp4 = new JLabel();
	JLabel temp5 = new JLabel();
	JLabel temp6 = new JLabel();

	JLabel index = new JLabel();// 穿衣指数
	JLabel index_uv = new JLabel();// 紫外线
	JLabel index_tr = new JLabel();// 旅游
	JLabel index_co = new JLabel();// 舒适指数
	JLabel index_cl = new JLabel();// 运动指数
	JLabel index_xc = new JLabel();// 洗车指数

	

	public WeatherFrame() throws IOException {
		this.setTitle("查看天气");
		setIconImage(getToolkit().getImage("src/image/Calendar.png"));    // 设置图标
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX = screenSize.width / 2;				// 屏幕中央x 坐标
		int centerY = screenSize.height / 2; 			// 屏幕中央y 坐标
		int w = 800;		// 本窗体宽度
		int h = 365;		// 本窗体高度
		this.setBounds(centerX - w / 2, centerY - h / 2, w, h);// 设置窗体出现在屏幕中央
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
		tabbedPane.addTab(" 今天天气 ", icon1, Todaypanel, "这是今天的天气");

		Aboutpanel = new JPanel();
		Aboutpanel.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		tabbedPane.addTab(" 设置 ", icon5, Aboutpanel, "设置");
		Aboutpanel.setLayout(null);
		
		chooseCity = new JLabel("设置城市:");
		jcb = new JComboBox<Object>();
		String[] citys = {"兰州", "岳阳", "北京", "上海", "广州"};
		jcb.setModel(new DefaultComboBoxModel<>(citys));
		jcb.setEditable(false);			//不能输入
		chooseButton = new JButton("确定");
		
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
		String CityItem = "兰州";
        Map<String, String> weatherInfoMap;
        System.out.println("CityItem:" + CityItem);
		weatherInfoMap = CommonsWeatherUtils.weatherInfo(CityItem);// 设置新城市的天气
		System.out.println("weatherInfoMap:" + weatherInfoMap);
		show(weatherInfoMap);
		tabbedPane.setSelectedIndex(0);
		
		
		chooseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String CityItem = "兰州";
				Map<String, String> weatherInfoMap;
				CityItem = (String)jcb.getSelectedItem();	// 获取当前选中的城市
				try {
					System.out.println("CityItem:" + CityItem);
					weatherInfoMap = CommonsWeatherUtils.weatherInfo(CityItem);// 设置新城市的天气
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

		
		Calendar c = Calendar.getInstance();   // 解析当前的年月日 并生成日历类
		time t = new time(c);

		// 设置今日天气的内容
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
		week.setText("星期：" + t.weekOfDay);
		left.add(week);

		date_y.setFont(font1);
		date_y.setBounds(15, 125, 235, 20);
		date_y.setText("日期：" + t.month + "月" + t.day + "日");
		left.add(date_y);

		JLabel seperate = new JLabel(
				"------------------------------------------------");
		seperate.setBounds(15, 145, 235, 20);
		left.add(seperate);

		temp1.setFont(font1);
		temp1.setBounds(15, 165, 235, 20);
		temp1.setText("气温:" +  infoMap.get(nodes[4]) + "℃ ");
		left.add(temp1);

		wind1.setFont(font1);
		wind1.setBounds(15, 185, 235, 20);
		wind1.setText("风向:" + infoMap.get(nodes[2]));
		left.add(wind1);

		fl1.setFont(font1);
		fl1.setBounds(15, 205, 235, 20);
		fl1.setText("风级:" + infoMap.get(nodes[3]));
		left.add(fl1);

		// 今日面板右边的天气设置
		right.setLayout(null);
		Image1.setBounds(35, 5, 235, 60);
		Image1.setFont(font2);
		Image1.setText("更多天气信息");
		Image1.setIcon(new ImageIcon("img/index.png"));
		right.add(Image1);

		index.setBounds(15, 65, 700, 35);
		index.setFont(font2);
		index.setText("穿衣说明：" + infoMap.get(nodes[8]));
		index.setIcon(new ImageIcon("src/img/cy.png"));
		right.add(index);

		index_uv.setBounds(15, 100, 700, 35);
		index_uv.setFont(font2);
		index_uv.setText("紫外线说明：" + infoMap.get(nodes[10]));
		index_uv.setIcon(new ImageIcon("src/img/uv.png"));
		right.add(index_uv);

		index_tr.setBounds(15, 135, 700, 35);
		index_tr.setFont(font2);
		if(infoMap.get(nodes[6]) != null) {
			index_tr.setText("污染说明：" + infoMap.get(nodes[6]));
		}else {
			index_tr.setText("污染说明：" + "无");
		}
		
		index_tr.setIcon(new ImageIcon("src/img/tr.png"));
		right.add(index_tr);

		index_co.setBounds(15, 170, 700, 35);
		index_co.setFont(font2);
		index_co.setText("预防感冒：" + infoMap.get(nodes[7]));
		index_co.setIcon(new ImageIcon("src/img/co.png"));
		right.add(index_co);

		index_cl.setBounds(15, 205, 700, 35);
		index_cl.setFont(font2);
		index_cl.setText("运动说明：" + infoMap.get(nodes[9]));
		index_cl.setIcon(new ImageIcon("src/img/cl.png"));
		right.add(index_cl);

		Aboutpanel.repaint();
		Todaypanel.repaint();
		left.repaint();
		right.repaint();
	}

	// 设置时间类（内部类）
	class time {

		int year;
		int month;
		int weekDay;
		int day;
		String weekOfDay;
		Calendar c;

		// 解析当前的年月日 并生成日历
		public time(Calendar c) {

			// 解析当前的年月日 并生成日历
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH) + 1;
			day = c.get(Calendar.DAY_OF_MONTH);
			weekDay = c.get(Calendar.DAY_OF_WEEK);

			switch (weekDay) {
			case 1:
				weekOfDay = "星期天";
				break;
			case 2:
				weekOfDay = "星期一";
				break;
			case 3:
				weekOfDay = "星期二";
				break;
			case 4:
				weekOfDay = "星期三";
				break;
			case 5:
				weekOfDay = "星期四";
				break;
			case 6:
				weekOfDay = "星期五";
				break;
			case 7:
				weekOfDay = "星期六";
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
	 * 设定天气情况的图片
	 * @param weather
	 * @return
	 */
	public  String setimage (String  weather) {
		switch (weather) {
		case "晴":
			return "src/image/0.png";
		case "多云":
			return "src/image/1.png";
		case "阴":
			return "src/image/2.png";
		case "小雨":
			return "src/image/3.png";
		case "中雨":
			return "src/image/4.png";
		case "大雨":
			return "src/image/5.png";
		case "暴雨":
			return "src/image/6.png";
		case "雷阵雨":
			return "src/image/7.png";
		case "阵雨":
			return "src/image/8.png";
		case "雨夹雪":
			return "src/image/9.png";
		case "小雪":
			return "src/image/10.png";
		case "中雪":
			return "src/image/11.png";
		case "大雪":
			return "src/image/12.png";
		case "暴雪":
			return "src/image/13.png";
		case "小到中雨":
			return "src/image/15.png";
		case "中到大雨":
			return "src/image/16.png";
		case "大到暴雨":
			return "src/image/17.png";
		case "雾":
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
