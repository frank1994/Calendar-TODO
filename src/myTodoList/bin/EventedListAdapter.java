package myTodoList.bin;

/**
 * EventedList�¼���������
 * 
 * @author Frank
 */
public class EventedListAdapter<ListItem> implements EventedListListener<ListItem>{
	/**
	 * Item�޸ĵ�ʱ�����
	 * @author Frank
	 * @param Ҫ�޸ĵ�Item
	 */
	public void itemModified(ListItem item){}
	/**
	 * ����б��޸��ˣ�����
	 * @author Frank
	 * @param �µ�ListItem
	 */
	public void listModified(EventedList<ListItem> newVersion){}
}
