package myTodoList.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.Timer;

import myTodoList.GUIGenerator;
import myTodoList.Window;
import myTodoList.bin.CalendarEntry;
import myTodoList.bin.CalendarSelectionChangedListener;
import myTodoList.bin.DayClickListener;
import myTodoList.bin.DayCompCreateListener;
import myTodoList.bin.EventedList;
import myTodoList.bin.Interval;
import myTodoList.data.DataModel;
import myTodoList.edit.CalendarPop;
import myTodoList.edit.DateChooserWindow;
import myTodoList.gui.views.CalendarView;
import myTodoList.gui.views.DayPanel;
import myTodoList.gui.views.WeekView;


/**
 * 设置添加到CalendarEntry的事件内容
 * @author Frank
 */
public class ViewGUI implements GUIGenerator, DayClickListener,DayCompCreateListener  {

	private Window parent;
	private WeekView w;
	public static JLabel label;
	private JScrollPane scroll;
	private JLabel showLabel;
	private Timer timer;
	private static JButton btnCalenar ;
	public static CalendarEntry selectedEntry;

	private static final int heightCorrection = 75;      // 高度
	private static final int widthCorrection = 210;     // 宽度

	/**
	 * @author Frank
	 * @return 最佳的视图宽度
	 */
	public static int getGUIWidth() {
		return WeekView.getViewWidth() + widthCorrection;
	}

	/**
	 * @author Frank
	 * @return 最佳的视图高度
	 */
	public static int getGUIHeight() {
		return WeekView.getViewHeight() + heightCorrection;
	}

	/**
	 * 给Container增加GUI组件
	 * 
	 * @param parent 放置组件的Window
	 * @param container 增加GUI组件的container
	 * @author Frank
	 * @return CalendarView
	 */
	@Override
	public CalendarView show(final Window parent, Container container) {
		this.parent = parent;

		container.setLayout(null);

		Date now = new Date(System.currentTimeMillis());
		EventedList<CalendarEntry> elist = DataModel.getEntryList();
		w = new WeekView(elist);

		w.toInterval(now);
		w.addSelectionChangedListener(new CalendarSelectionChangedListener() {
			@Override
			public void selectionChanged(CalendarEntry e) {
				selectedEntry = e;
				showLabel.setText(generateSelectionText(selectedEntry, System.currentTimeMillis()));
				if (selectedEntry == null) {
					System.out.println("没有选择");
					return;
				}
				System.out.println(e.getType() + " | " + e.getInterval().getStartTimestamp().toString() + " --- "
						+ e.getInterval().getEndTimestamp().toString());
			}
		});

		showLabel = new JLabel(generateSelectionText(null, 0));
		showLabel.setVerticalAlignment(JLabel.TOP);
		showLabel.setVerticalTextPosition(JLabel.TOP);
		showLabel.setOpaque(true);
		showLabel.setBackground(new Color(232, 198, 152));

		scroll = new JScrollPane(w);
		int barWidth = 15;
		scroll.getVerticalScrollBar().setPreferredSize(new Dimension(barWidth, 0));
		if ((int) parent.size.getHeight() < getGUIHeight()) {
			scroll.setBounds(widthCorrection + 10, 10, WeekView.getViewWidth() + barWidth + 6,
					(int) parent.size.getHeight() - heightCorrection);
			showLabel.setBounds(10, 170, widthCorrection - 10, (int) parent.size.getHeight() - heightCorrection - 130);
		} else {
			scroll.setBounds(widthCorrection + 10, 10, WeekView.getViewWidth() + barWidth + 6,
					WeekView.getViewHeight() + 6);
			showLabel.setBounds(10, 170, widthCorrection - 10, WeekView.getViewHeight() + 6 - 130 + 10);
		}

		label = new JLabel(getDateString(w.getDisplayedInterval()), JLabel.CENTER);
		label.setBounds(10, 10, 200, 30);

		JButton btnNext = new JButton("下一周");
		btnNext.setBounds(115, 50, 95, 30);
		btnNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				w.nextInterval();
				label.setText(getDateString(w.getDisplayedInterval()));
			}
		});
		JButton btnPrev = new JButton("上一周");
		btnPrev.setBounds(10, 50, 95, 30);
		btnPrev.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				w.prevInterval();   // weekView查看上一周
				label.setText(getDateString(w.getDisplayedInterval()));
				System.out.println("???" + label.getText());
			}
		});
		JButton btnJump = new JButton("查看指定时间");
		btnJump.setBounds(10, 90, 200, 30);
		btnJump.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {

				final DateChooserWindow dateChooser = new DateChooserWindow(false);
				dateChooser.show(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println(dateChooser.getDate());
						if (dateChooser.getDate() == null) {
							return;
						}
						w.toInterval(dateChooser.getDate());
						label.setText(getDateString(w.getDisplayedInterval()));
					}
				});
			}
		});
		///////////////////////////////////////////
		
		btnCalenar = new JButton("查看日历");
		btnCalenar.setBounds(10, 130, 200, 30);
		btnCalenar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				CalendarPop calendarPop  = new CalendarPop();
				calendarPop.addDayClickListener(ViewGUI.this);
				calendarPop.addDayCreateListener(ViewGUI.this);
				calendarPop.updateDate(Calendar.getInstance());
				calendarPop.show(btnCalenar, 20, 20);
				System.out.println("查看日历");
			}
		});
		///////////////////////////////////////////
		// showLabel每过一分钟刷新一次
		timer = new Timer((int) (WeekView.HOUR_MILLIS / 60), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				showLabel.setText(generateSelectionText(selectedEntry, System.currentTimeMillis()));
			}
		});
		timer.start();

		container.add(btnPrev);
		container.add(label);
		container.add(btnNext);
		container.add(btnJump);
		container.add(btnCalenar);
		container.add(showLabel);
		container.add(scroll);
		return w;
	}

	/**
	 * 点击事件时，在最左侧，显示事件的内容，
	 * 
	 * @param e 要显示的CalendarEntry
	 * @param currentMillis 当前的毫秒数
	 * @author Frank
	 */
	private static String generateSelectionText(CalendarEntry e, long currentMillis) {
		String ret = "<html><font size=+0>";
		String retEnd = "</font></html>";

		if (e == null) {
			ret += "<b>没有条目被选择</b>";
			return ret + retEnd;
		}

		ret += "<b>类型:</b><br/>" + e.getType() + "<br/>";
		ret += "<br/>";
		ret += "<b>标题:</b><br/>" + e.getTitle() + "<br/>";
		ret += "<br/>";

		Interval ival = e.getInterval();
		if (ival.contains(currentMillis)) {  // 如果当前时间在时间间隔内，用红色显示当前的事件
			ret += "<font size=+1 color=blue>事件在 " + formatTime(currentMillis - ival.getStart())
					+ " 之前开始</font><br/>";
		} else if (currentMillis < ival.getStart()) {
			ret += "时间开始于： " + formatTime(ival.getStart() - currentMillis) + "后";
		} else {
			ret += "事件结束于 " + formatTime(currentMillis - ival.getEnd()) + "后";
		}
		ret += "<br/><br/>";
		ret += "<b>事件描述:</b><br/>" + e.getDescription().replaceAll("\n","<br/>");
		return ret + retEnd;
	}

	/**
	 * 传入毫秒，返回具体的时间
	 * @param m 以毫秒计的时间
	 * @author Frank
	 */
	private static String formatTime(long m) {
		int[] times = new int[4];
		int years = times[0] = (int) (m / (WeekView.HOUR_MILLIS * 24 * 365));
		m = (m % (WeekView.HOUR_MILLIS * 24 * 365));
		int days = times[1] = (int) (m / (WeekView.HOUR_MILLIS * 24));
		m = (m % (WeekView.HOUR_MILLIS * 24));
		int hours = times[2] = (int) (m / (WeekView.HOUR_MILLIS));
		m = (m % (WeekView.HOUR_MILLIS));
		int minutes = times[3] = (int) (m / (WeekView.HOUR_MILLIS / 60));
		m = (m % (WeekView.HOUR_MILLIS / 60));

		String ret = "";
		String[] names = new String[] { "年", "天", "小时", "分钟" };

		boolean last = false;
		for (int i = 0; i < times.length; i++) {
			if(last) {
				if (times[i] != 0) {
					ret += " ， ";
				} else {
					break;
				}
			}
			if(times[i] != 0) {
				ret += times[i] + " " + names[i];
				if (times[i] != 1) {
					ret += " ";
				}

				if (last) {
					break;
				}
				last = true;
			}
		}
		if (!last) {
			ret += "几秒";
		}
		return ret;
	}

	/**
	 * 生成时间间隔的字符串 
	 * @author Frank
	 * @param time 开始时间到结束时间的字符串
	 * @return 时间戳
	 */
	public static String getDateString(Interval time) {
		Calendar c = Calendar.getInstance();
		c.setTime(time.getEndTimestamp());
		c.add(Calendar.DAY_OF_WEEK, -1);
		return time.getStartTimestamp().toString().split(" ")[0] + " --- "
				+ new Timestamp(c.getTimeInMillis()).toString().split(" ")[0];
	}

	@Override
	public void destroy() {
	}

	public ViewGUI() {
		super();
		this.btnCalenar = btnCalenar;
	}

	@Override
	public void dayCompCreated(DayPanel day) {
		
	}
	
//	public static void calendarClicked() {
//		btnCalenar.doClick();
//	}

	@Override
	public void dayClicked(DayPanel day, MouseEvent e) {   // 日历查看选择了日期
		Calendar getnowDay = day.getCalendar();
		w.toInterval(getnowDay.getTime());
		label.setText(getDateString(w.getDisplayedInterval()));
	}
}
