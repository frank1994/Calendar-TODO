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
 * �����ĳ�����
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
	 * @return ��Ҫ���ֵļ��
	 */
	public Interval getDisplayedInterval(){
		return viewInterval.clone();
	}
	
	/**
	 * Adds SelectionChangedListener to view.
	 * ����ͼ����SelectionChanged�ļ���
	 * @author Frank
	 * @param listener ��Ҫ���Ӽ�����ֵ
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
	 * �Ƴ�����
	 * 
	 * @author Frank
	 * @param listener ��Ҫ�Ƴ��ļ�����ֵ
	 * @return False ���û�з���listener
	 */
	public boolean removeSelectionChangedListener(CalendarSelectionChangedListener listener){
		return selectionChangedListeners.remove(listener);
	}
	
	/**
	 * �Ƴ����е�SelectionChangedListeners
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
	 * JPanel��������ڲ�����
	 * @author Frank
	 * @param g Graphics
	 */
	protected void abstractPaintComponent(Graphics g){
		super.paintComponent(g);
	}
	
	/**
	 * ������ÿ�ܵĵڼ��죬��һ��0
	 * 
	 * @param timestamp ʱ���
	 * @author Frank
	 * @return ÿ�ܵĵڼ���
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
	 * @param timestamp ʱ���
	 * @author Frank
	 * @return ��СʱΪ��λ��ʱ��
	 */
	protected int hourOfDay(long timestamp){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(timestamp));
		return c.get(Calendar.HOUR_OF_DAY);
	}
	
	// ���󷽷�
	/**
	 * չʾ��һ��ʱ����
	 */
	public abstract void nextInterval();
	/**
	 * ǰһ��ʱ����
	 */
	public abstract void prevInterval();
	/**
	 * ����ʱ���ʱ����
	 * @param t ������ʱ��
	 */
	public abstract void toInterval(Date t);
	/**
	 * ����CalendarEntryΪ��ѡ��
	 * @param e Selected entry.
	 * @return Default 
	 */
	public abstract boolean setSelected(CalendarEntry e);
	/**
	 * ��������
	 * @param g Drawable content.
	 */
	public abstract void paintComponent(Graphics g);
	
	/**
	 * ��������¼�
	 * @author Frank
	 * @param e MouseEvent �����
	 */
	protected abstract void clickPerformed(MouseEvent e);
	
	public static void main(String args[]) {
		
	}
}
