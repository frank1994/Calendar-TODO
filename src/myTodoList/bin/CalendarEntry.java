package myTodoList.bin;

import java.awt.Color;

/**
 * �����ϵ��¼�
 * @author Frank
 */
public class CalendarEntry implements Comparable<CalendarEntry> {
	public CalendarEntry(){
		
		interval = null;
		this.type = null;
		this.title = null;
		foregroundColor = null;
		backgroundColor = null;
		description = null;
	}
	
	/**
	 * ���������Ĺ��캯��
	 * 
	 * @param ival Interval �¼���ʱ����
	 * @param type �¼�������
	 * @param title �¼��ı���
	 * @param foreColor �¼��������ɫ
	 * @param backColor ��������ɫ
	 * @param desc �¼�������
	 * @author Frank
	 */
	public CalendarEntry(Interval ival, String type, String title, Color foreColor, Color backColor, String desc) {
		if (ival == null || type == null || foreColor == null || backColor == null || desc == null) {
			Thrower.Throw(new NullPointerException("Null found in constructor"));
		}

		interval = ival;
		this.type = type;
		this.title = title;
		foregroundColor = foreColor;
		backgroundColor = backColor;
		description = desc;
	}
	
	/**
	 * ����ʱ�������Ƚ�������¼
	 * 
	 * @param o ��Ҫ�Ƚϵ�CalendarEntry
	 * 
	 * @author Frank
	 */
	public int compareTo(CalendarEntry o) {
		long l = interval.getLength() - o.interval.getLength();   // ����ĳ���
		if(0 == l) {
			return (getObjectString().compareTo(o.getObjectString()));
		}
		if(l > Integer.MAX_VALUE) {
			return (int)(l / (((long)(Integer.MAX_VALUE) + 1) * 2));
		}
		return (int)l;
	}

	// ����
	public static final Color DEFAULT_FOREGROUND_COLOR = new Color(0, 0, 0);       // ��ɫ��Ĭ��ǰ��ɫ
	public static final Color DEFAULT_BACKGROUND_COLOR = new Color(0, 227, 217);   // Ĭ�ϱ���ɫ

	// ����
	private Interval interval;
	private String type;
	private String title;
	private String description;
	private Color foregroundColor;
	private Color backgroundColor;

	
	
	// getters and setter
	public Interval getInterval() {
		return interval;
	}

	public String getType() {
		return type;
	}

	public String getTitle() {
		return title;
	}

	public String getObjectString() {
		return super.toString();
	}

	public String getDescription() {
		return description;
	}

	public Color getForegroundColor() {
		return foregroundColor;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}
	
	public void setInterval(Interval interval) {
		this.interval = interval;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
}
