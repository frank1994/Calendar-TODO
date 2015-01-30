package myTodoList.data;

import java.util.ArrayList; 
import java.util.LinkedList;
import java.util.List;

import myTodoList.bin.CalendarEntry;
import myTodoList.bin.EventedList;


/**
 * �������ݵ���
 * @author Frank
 */
public class DataModel {
	private static EventedList<CalendarEntry> entryList = new EventedList<CalendarEntry>(new ArrayList<CalendarEntry>());
	private static List<String> typeList = new LinkedList<String>();
	private static String[] defaultTypeArray = new String[]{"�γ�", "����", "����", "����", "Other..."};
	
	public static final String newType = defaultTypeArray[4]; 

	/**
	 * @author Frank
	 * @return ���ӵ���Ŀ����
	 */
	public static EventedList<CalendarEntry> getEntryList() {
		return entryList;
	}
	
	/**
	 * @author Frank
	 * @return ��Ŀ����String����
	 */
	public static List<String> getTypeList() {
		return typeList;
	}
	
	/**
	 * @author Frank
	 * @return Ĭ�ϵ���Ŀ��������
	 */
	public static String[] getDefaultTypeArray() {
		return defaultTypeArray;
	}
}
