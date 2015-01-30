package myTodoList.data;

import java.util.ArrayList; 
import java.util.LinkedList;
import java.util.List;

import myTodoList.bin.CalendarEntry;
import myTodoList.bin.EventedList;


/**
 * 处理数据的类
 * @author Frank
 */
public class DataModel {
	private static EventedList<CalendarEntry> entryList = new EventedList<CalendarEntry>(new ArrayList<CalendarEntry>());
	private static List<String> typeList = new LinkedList<String>();
	private static String[] defaultTypeArray = new String[]{"课程", "会议", "生日", "工作", "Other..."};
	
	public static final String newType = defaultTypeArray[4]; 

	/**
	 * @author Frank
	 * @return 增加的条目链表
	 */
	public static EventedList<CalendarEntry> getEntryList() {
		return entryList;
	}
	
	/**
	 * @author Frank
	 * @return 条目种类String链表
	 */
	public static List<String> getTypeList() {
		return typeList;
	}
	
	/**
	 * @author Frank
	 * @return 默认的条目种类链表
	 */
	public static String[] getDefaultTypeArray() {
		return defaultTypeArray;
	}
}
