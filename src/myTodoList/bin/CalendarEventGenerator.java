package myTodoList.bin;
/**
 * 事件产生的接口
 * @author Frank
 */
public interface CalendarEventGenerator{
	public void addSelectionChangedListener(CalendarSelectionChangedListener listener);
	/**
	 * @author Frank
	 * @param listener 要移除的监听
	 * @return 如果监听被移除了返回True
	 */
	public boolean removeSelectionChangedListener(CalendarSelectionChangedListener listener);
	/**
	 * @param 改变选择的CalendarEntry
	 * @author Frank
	 */
	public void dispatchSelectionChanged(CalendarEntry selected);
}
