package myTodoList.gui.views;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 日期面板组件
 * @author Frank
 */
public class DayPanel extends JPanel implements MouseListener {
	private static final long serialVersionUID = 1L;

	private JLabel label;

	// 该日期组件所代表的日历时间
	private Calendar calendar;

	// 是否响应内部设置的鼠标事件，默认为不响应
	private boolean actionMoseEvent = false;
	// 是否为当前日期的标志
	private boolean isCurrentDay = false;

	/**
	 * 构造方法
	 * @param day
	 */
	public DayPanel(Calendar calendar) {
		this(calendar.get(Calendar.DAY_OF_MONTH) + "");
		this.calendar = Calendar.getInstance();
		this.calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
		this.calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
		this.calendar.set(Calendar.DATE, calendar.get(Calendar.DATE));
		if ((Calendar.SATURDAY == calendar.get(Calendar.DAY_OF_WEEK) || Calendar.SUNDAY == calendar
				.get(Calendar.DAY_OF_WEEK))) {
			label.setForeground(Color.red);
		}
	}

	/**
	 * 构造方法
	 * @param day
	 */
	public DayPanel(String day) {
		label = new JLabel();
		label.setText(day);
		label.setHorizontalAlignment(JLabel.CENTER);
		label.addMouseListener(this);
		setLayout(new GridBagLayout());
		setMinimumSize(new Dimension(20, 20));
		setPreferredSize(new Dimension(20, 20));
		add(label, new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
						0, 0, 0, 0), 0, 0));
	}

	/**
	 * 设置显示日期的颜色
	 * @param color
	 */
	public void setLabelForeground(Color color) {
		label.setForeground(color);
	}

	/**
	 * 是否为当前日期
	 * @return
	 */
	public boolean isCurrentDay() {
		return isCurrentDay;
	}

	/**
	 * 设置为当前日期的标记
	 * @param isCurrentDay
	 */
	public void setCurrentDay(boolean isCurrentDay) {
		this.isCurrentDay = isCurrentDay;
		setBackground(Color.cyan);
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	public JLabel getLabel() {
		return label;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (isCurrentDay() || !isActionMoseEvent()) {
			return;
		}
		setOpaque(true);
		setBackground(Color.gray);
		setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (isCurrentDay() || !isActionMoseEvent()) {
			return;
		}
		setOpaque(false);
		setBorder(null);

	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	/**
	 * 是否响应内部设置的鼠标动作事件
	 * @return
	 */
	public boolean isActionMoseEvent() {
		return actionMoseEvent;
	}

	/**
	 * 设置是否显示内部设置鼠标事件
	 * @param actionMoseEvent
	 */
	public void setActionMoseEvent(boolean actionMoseEvent) {
		this.actionMoseEvent = actionMoseEvent;
	}
}