package myTodoList.bin;

/**
 * EventedList事件的适配器
 * 
 * @author Frank
 */
public class EventedListAdapter<ListItem> implements EventedListListener<ListItem>{
	/**
	 * Item修改的时候调用
	 * @author Frank
	 * @param 要修改的Item
	 */
	public void itemModified(ListItem item){}
	/**
	 * 这个列表都修改了，调用
	 * @author Frank
	 * @param 新的ListItem
	 */
	public void listModified(EventedList<ListItem> newVersion){}
}
