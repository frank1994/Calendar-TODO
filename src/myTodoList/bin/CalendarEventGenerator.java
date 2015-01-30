package myTodoList.bin;
/**
 * �¼������Ľӿ�
 * @author Frank
 */
public interface CalendarEventGenerator{
	public void addSelectionChangedListener(CalendarSelectionChangedListener listener);
	/**
	 * @author Frank
	 * @param listener Ҫ�Ƴ��ļ���
	 * @return ����������Ƴ��˷���True
	 */
	public boolean removeSelectionChangedListener(CalendarSelectionChangedListener listener);
	/**
	 * @param �ı�ѡ���CalendarEntry
	 * @author Frank
	 */
	public void dispatchSelectionChanged(CalendarEntry selected);
}
