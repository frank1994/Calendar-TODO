package myTodoList.bin;

import java.util.List; 
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 实现事件列表的调度
 * @author Frank
 */
public class EventedList<ListItem> implements Iterable<ListItem>{
	/**
	 * @param l 继承了java.util.List的链表
	 * @author Frank
	 */
	public EventedList(List<ListItem> l){
		list = l;
		addListeners = new LinkedList<EventedListListener<ListItem>>();
		removeListeners = new LinkedList<EventedListListener<ListItem>>();
		resetListeners = new LinkedList<EventedListListener<ListItem>>();
	}
	
	private List<ListItem> list;
	private List<EventedListListener<ListItem>> addListeners;
	private List<EventedListListener<ListItem>> removeListeners;
	private List<EventedListListener<ListItem>> resetListeners;
	
	
	// List modifiers
	
	/**
	 * 给链表增加结点
	 * 
	 * @author Frank
	 * @param item 增加的item
	 * @return Boolean as specified in List.
	 */
	public boolean add(ListItem item){
		boolean ret = list.add(item);
		dispatchItemAddedEvent(item);
		return ret;
	}
	
	/**
	 * 重置list的所有Item，清空
	 * 
	 * @author Frank
	 * @param l 需要重置的List<ListItem>
	 */
	public void resetList(List<ListItem> l){
		if (l == null) Thrower.Throw(new NullPointerException("Argument can't be null"));
		list = l;
		dispatchListResetEvent(this);
	}
	
	/**
	 * 从list中移除一个item
	 * 
	 * @author Frank
	 * @param item 需要移除的Item 
	 * @return Boolean as specified in List.
	 */
	public boolean remove(ListItem item){
		boolean ret = list.remove(item);
		dispatchItemRemovedEvent(item);
		return ret;
	}
	
	/**
	 * 根据index从list中移除item
	 * 
	 * @author Frank
	 * @param index 需要移除的index
	 * @return Removed list item.
	 */
	public ListItem remove(int index){
		ListItem ret = list.remove(index);
		dispatchItemRemovedEvent(ret);
		return ret;
	}
	
	/**
	 * 返回一个迭代器
	 * @author Frank
	 * @return list的迭代器
	 */
	public Iterator<ListItem> iterator(){
		return new Iterator<ListItem>(){
			private Iterator<ListItem> it = list.iterator();
			private ListItem current = null;
			
			public boolean hasNext(){
				return it.hasNext();
			}
			
			public ListItem next(){
				current = it.next();
				return current;
			}
			
			public void remove(){
				it.remove();
				dispatchItemRemovedEvent(current);
			}
		};
	}
	
	/**
	 * 返回list的大小
	 * @author Frank
	 * @return 返回list的大小
	 */
	public int size(){
		return list.size();
	}
	
	
	// Adders 
	
	/**
	 * 给ListItem增加监听
	 * @author Frank
	 * @param l 给Item增加监听
	 */
	public void addItemAddedListener(EventedListListener<ListItem> l){
		addListeners.add(l);
	}
	
	/**
	 * 给ListItem移除监听
	 * 
	 * @author Frank
	 * @param l 给ListItem移除监听
	 */
	public void addItemRemovedListener(EventedListListener<ListItem> l){
		removeListeners.add(l);
	}
	
	/**
	 * resetListener移除一个Item
	 * @author Frank
	 * @param l List 需要reset的ListItem
	 */
	public void addListResetListener(EventedListListener<ListItem> l){
		resetListeners.add(l);
	}
	
	// Removers 
	/**
	 * 移出的item增加监听 
	 * @author Frank
	 * @param l Item 需要移除该监听的有监听的item
	 * @return Boolean as specified in List.
	 */
	public boolean removeItemAddedListener(EventedListListener<ListItem> l){
		return addListeners.remove(l);
	}
	
	/**
	 * 移除的item移出监听
	 * @author Frank
	 * @param l Item 移除的item移除监听
	 * @return Boolean as specified in List.
	 */
	public boolean removeItemRemovedListener(EventedListListener<ListItem> l){
		return removeListeners.remove(l);
	}
	
	/**
	 * 移除的item重置监听
	 * @author Frank
	 * @param l List 需要移除的item
	 * @return Boolean as specified in List.
	 */
	public boolean removeListResetListener(EventedListListener<ListItem> l){
		return resetListeners.remove(l);
	}
	
	
	///////////调度 
	/**
	 * 增加事件链表
	 * @param item ListItem
	 */
	private void dispatchItemAddedEvent(ListItem item){
		for(EventedListListener<ListItem> l : addListeners){
			l.itemModified(item);
		}
	}
	
	/**
	 * 需要移除的事件链表
	 * @param item ListItem
	 */
	private void dispatchItemRemovedEvent(ListItem item){
		for(EventedListListener<ListItem> l : removeListeners){
			l.itemModified(item);
		}
	}

	/**
	 * 需要更新的事件链表
	 * @param newVersion
	 */
	private void dispatchListResetEvent(EventedList<ListItem> newVersion){
		for(EventedListListener<ListItem> l : resetListeners){
			l.listModified(newVersion);
		}
	}
	
}
