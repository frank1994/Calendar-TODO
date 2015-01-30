package myTodoList.bin;

/**
 * EventedList�¼��ļ���
 * @author Frank
 */
public interface EventedListListener<ListItem>{
	/**
	 * Item�޸ĵ�ʱ�����
	 * @author Frank
	 * @param Ҫ�޸ĵ�Item
	 */
	public void itemModified(ListItem item);
	
	/**
	 * ����б��޸��ˣ�����
	 * @author Frank
	 * @param �µ�ListItem
	 */
	public void listModified(EventedList<ListItem> newVersion);
}
