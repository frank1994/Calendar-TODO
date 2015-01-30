package myTodoList.bin;

/**
 * 日历上点击事件的监听
 * 
 * @author Frank
 */
public interface CalendarSelectionChangedListener{
	/**
	 * @param selected 传入被点击的CalendarEntry，没有选择就是null
	 * @author Frank
	 */
	public void selectionChanged(CalendarEntry selected);
}
