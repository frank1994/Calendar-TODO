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
 * ������ӵ�CalendarEntry���¼�����
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

	private static final int heightCorrection = 75;      // �߶�
	private static final int widthCorrection = 210;     // ���

	/**
	 * @author Frank
	 * @return ��ѵ���ͼ���
	 */
	public static int getGUIWidth() {
		return WeekView.getViewWidth() + widthCorrection;
	}

	/**
	 * @author Frank
	 * @return ��ѵ���ͼ�߶�
	 */
	public static int getGUIHeight() {
		return WeekView.getViewHeight() + heightCorrection;
	}

	/**
	 * ��Container����GUI���
	 * 
	 * @param parent ���������Window
	 * @param container ����GUI�����container
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
					System.out.println("û��ѡ��");
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

		JButton btnNext = new JButton("��һ��");
		btnNext.setBounds(115, 50, 95, 30);
		btnNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				w.nextInterval();
				label.setText(getDateString(w.getDisplayedInterval()));
			}
		});
		JButton btnPrev = new JButton("��һ��");
		btnPrev.setBounds(10, 50, 95, 30);
		btnPrev.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				w.prevInterval();   // weekView�鿴��һ��
				label.setText(getDateString(w.getDisplayedInterval()));
				System.out.println("???" + label.getText());
			}
		});
		JButton btnJump = new JButton("�鿴ָ��ʱ��");
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
		
		btnCalenar = new JButton("�鿴����");
		btnCalenar.setBounds(10, 130, 200, 30);
		btnCalenar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				CalendarPop calendarPop  = new CalendarPop();
				calendarPop.addDayClickListener(ViewGUI.this);
				calendarPop.addDayCreateListener(ViewGUI.this);
				calendarPop.updateDate(Calendar.getInstance());
				calendarPop.show(btnCalenar, 20, 20);
				System.out.println("�鿴����");
			}
		});
		///////////////////////////////////////////
		// showLabelÿ��һ����ˢ��һ��
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
	 * ����¼�ʱ��������࣬��ʾ�¼������ݣ�
	 * 
	 * @param e Ҫ��ʾ��CalendarEntry
	 * @param currentMillis ��ǰ�ĺ�����
	 * @author Frank
	 */
	private static String generateSelectionText(CalendarEntry e, long currentMillis) {
		String ret = "<html><font size=+0>";
		String retEnd = "</font></html>";

		if (e == null) {
			ret += "<b>û����Ŀ��ѡ��</b>";
			return ret + retEnd;
		}

		ret += "<b>����:</b><br/>" + e.getType() + "<br/>";
		ret += "<br/>";
		ret += "<b>����:</b><br/>" + e.getTitle() + "<br/>";
		ret += "<br/>";

		Interval ival = e.getInterval();
		if (ival.contains(currentMillis)) {  // �����ǰʱ����ʱ�����ڣ��ú�ɫ��ʾ��ǰ���¼�
			ret += "<font size=+1 color=blue>�¼��� " + formatTime(currentMillis - ival.getStart())
					+ " ֮ǰ��ʼ</font><br/>";
		} else if (currentMillis < ival.getStart()) {
			ret += "ʱ�俪ʼ�ڣ� " + formatTime(ival.getStart() - currentMillis) + "��";
		} else {
			ret += "�¼������� " + formatTime(currentMillis - ival.getEnd()) + "��";
		}
		ret += "<br/><br/>";
		ret += "<b>�¼�����:</b><br/>" + e.getDescription().replaceAll("\n","<br/>");
		return ret + retEnd;
	}

	/**
	 * ������룬���ؾ����ʱ��
	 * @param m �Ժ���Ƶ�ʱ��
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
		String[] names = new String[] { "��", "��", "Сʱ", "����" };

		boolean last = false;
		for (int i = 0; i < times.length; i++) {
			if(last) {
				if (times[i] != 0) {
					ret += " �� ";
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
			ret += "����";
		}
		return ret;
	}

	/**
	 * ����ʱ�������ַ��� 
	 * @author Frank
	 * @param time ��ʼʱ�䵽����ʱ����ַ���
	 * @return ʱ���
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
	public void dayClicked(DayPanel day, MouseEvent e) {   // �����鿴ѡ��������
		Calendar getnowDay = day.getCalendar();
		w.toInterval(getnowDay.getTime());
		label.setText(getDateString(w.getDisplayedInterval()));
	}
}
