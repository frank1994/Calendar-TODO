package myTodoList.gui.views;

import java.awt.Graphics; 
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

import myTodoList.bin.CalendarEntry;
import myTodoList.bin.CalendarEventGenerator;
import myTodoList.bin.CalendarSelectionChangedListener;
import myTodoList.bin.Interval;


/**
 * 日历的抽象类
 * @author Frank
 */
public abstract class CalendarView extends JPanel implements CalendarEventGenerator{
	private static final long serialVersionUID = 1L;

	public CalendarView(){
		super();
		
		toInterval(new Timestamp(System.currentTimeMillis()));
		selectionChangedListeners = new ArrayList<CalendarSelectionChangedListener>();
		
		addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				clickPerformed(e);
			}
		});
		
		setVisible(true);
	}
	
	protected Interval viewInterval;
	private List<CalendarSelectionChangedListener> selectionChangedListeners;
	
	/**
	 * @author Frank
	 * @return 需要表现的间隔
	 */
	public Interval getDisplayedInterval(){
		return viewInterval.clone();
	}
	
	/**
	 * Adds SelectionChangedListener to view.
	 * 给视图增加SelectionChanged的监听
	 * @author Frank
	 * @param listener 需要增加监听的值
	 */
	public void addSelectionChangedListener(CalendarSelectionChangedListener listener){
		for (CalendarSelectionChangedListener l : selectionChangedListeners){
			if (l == listener) {
				return;
			}
		}
		selectionChangedListeners.add(listener);
	}
	
	/**
	 * 移出监听
	 * 
	 * @author Frank
	 * @param listener 需要移除的监听的值
	 * @return False 如果没有发现listener
	 */
	public boolean removeSelectionChangedListener(CalendarSelectionChangedListener listener){
		return selectionChangedListeners.remove(listener);
	}
	
	/**
	 * 移出所有的SelectionChangedListeners
	 * 
	 * @author Frank
	 * @param selected Selected value to dispatch.
	 */
	public void dispatchSelectionChanged(CalendarEntry selected){
		for (CalendarSelectionChangedListener l : selectionChangedListeners){
			l.selectionChanged(selected);
		}
	}
	
	/**
	 * JPanel画组件的内部方法
	 * @author Frank
	 * @param g Graphics
	 */
	protected void abstractPaintComponent(Graphics g){
		super.paintComponent(g);
	}
	
	/**
	 * 返回在每周的第几天，周一是0
	 * 
	 * @param timestamp 时间戳
	 * @author Frank
	 * @return 每周的第几天
	 */
	protected int dayOfWeek(long timestamp){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(timestamp));
		if (1 == c.get(Calendar.DAY_OF_WEEK)){
			return 6;
		}
		return c.get(Calendar.DAY_OF_WEEK) - 2;
	}
	
	/**
	 * @param timestamp 时间戳
	 * @author Frank
	 * @return 以小时为单位的时间
	 */
	protected int hourOfDay(long timestamp){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(timestamp));
		return c.get(Calendar.HOUR_OF_DAY);
	}
	
	// 抽象方法
	/**
	 * 展示下一个时间间隔
	 */
	public abstract void nextInterval();
	/**
	 * 前一个时间间隔
	 */
	public abstract void prevInterval();
	/**
	 * 给定时间的时间间隔
	 * @param t 给定的时间
	 */
	public abstract void toInterval(Date t);
	/**
	 * 设置CalendarEntry为已选择
	 * @param e Selected entry.
	 * @return Default 
	 */
	public abstract boolean setSelected(CalendarEntry e);
	/**
	 * 画出内容
	 * @param g Drawable content.
	 */
	public abstract void paintComponent(Graphics g);
	
	/**
	 * 处理鼠标事件
	 * @author Frank
	 * @param e MouseEvent 鼠标点击
	 */
	protected abstract void clickPerformed(MouseEvent e);
	
	public static void main(String args[]) {
		
	}
}
