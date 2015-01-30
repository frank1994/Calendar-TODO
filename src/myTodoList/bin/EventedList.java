package myTodoList.bin;

import java.util.List; 
import java.util.Iterator;
import java.util.LinkedList;

/**
 * ʵ���¼��б�ĵ���
 * @author Frank
 */
public class EventedList<ListItem> implements Iterable<ListItem>{
	/**
	 * @param l �̳���java.util.List������
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
	 * ���������ӽ��
	 * 
	 * @author Frank
	 * @param item ���ӵ�item
	 * @return Boolean as specified in List.
	 */
	public boolean add(ListItem item){
		boolean ret = list.add(item);
		dispatchItemAddedEvent(item);
		return ret;
	}
	
	/**
	 * ����list������Item�����
	 * 
	 * @author Frank
	 * @param l ��Ҫ���õ�List<ListItem>
	 */
	public void resetList(List<ListItem> l){
		if (l == null) Thrower.Throw(new NullPointerException("Argument can't be null"));
		list = l;
		dispatchListResetEvent(this);
	}
	
	/**
	 * ��list���Ƴ�һ��item
	 * 
	 * @author Frank
	 * @param item ��Ҫ�Ƴ���Item 
	 * @return Boolean as specified in List.
	 */
	public boolean remove(ListItem item){
		boolean ret = list.remove(item);
		dispatchItemRemovedEvent(item);
		return ret;
	}
	
	/**
	 * ����index��list���Ƴ�item
	 * 
	 * @author Frank
	 * @param index ��Ҫ�Ƴ���index
	 * @return Removed list item.
	 */
	public ListItem remove(int index){
		ListItem ret = list.remove(index);
		dispatchItemRemovedEvent(ret);
		return ret;
	}
	
	/**
	 * ����һ��������
	 * @author Frank
	 * @return list�ĵ�����
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
	 * ����list�Ĵ�С
	 * @author Frank
	 * @return ����list�Ĵ�С
	 */
	public int size(){
		return list.size();
	}
	
	
	// Adders 
	
	/**
	 * ��ListItem���Ӽ���
	 * @author Frank
	 * @param l ��Item���Ӽ���
	 */
	public void addItemAddedListener(EventedListListener<ListItem> l){
		addListeners.add(l);
	}
	
	/**
	 * ��ListItem�Ƴ�����
	 * 
	 * @author Frank
	 * @param l ��ListItem�Ƴ�����
	 */
	public void addItemRemovedListener(EventedListListener<ListItem> l){
		removeListeners.add(l);
	}
	
	/**
	 * resetListener�Ƴ�һ��Item
	 * @author Frank
	 * @param l List ��Ҫreset��ListItem
	 */
	public void addListResetListener(EventedListListener<ListItem> l){
		resetListeners.add(l);
	}
	
	// Removers 
	/**
	 * �Ƴ���item���Ӽ��� 
	 * @author Frank
	 * @param l Item ��Ҫ�Ƴ��ü������м�����item
	 * @return Boolean as specified in List.
	 */
	public boolean removeItemAddedListener(EventedListListener<ListItem> l){
		return addListeners.remove(l);
	}
	
	/**
	 * �Ƴ���item�Ƴ�����
	 * @author Frank
	 * @param l Item �Ƴ���item�Ƴ�����
	 * @return Boolean as specified in List.
	 */
	public boolean removeItemRemovedListener(EventedListListener<ListItem> l){
		return removeListeners.remove(l);
	}
	
	/**
	 * �Ƴ���item���ü���
	 * @author Frank
	 * @param l List ��Ҫ�Ƴ���item
	 * @return Boolean as specified in List.
	 */
	public boolean removeListResetListener(EventedListListener<ListItem> l){
		return resetListeners.remove(l);
	}
	
	
	///////////���� 
	/**
	 * �����¼�����
	 * @param item ListItem
	 */
	private void dispatchItemAddedEvent(ListItem item){
		for(EventedListListener<ListItem> l : addListeners){
			l.itemModified(item);
		}
	}
	
	/**
	 * ��Ҫ�Ƴ����¼�����
	 * @param item ListItem
	 */
	private void dispatchItemRemovedEvent(ListItem item){
		for(EventedListListener<ListItem> l : removeListeners){
			l.itemModified(item);
		}
	}

	/**
	 * ��Ҫ���µ��¼�����
	 * @param newVersion
	 */
	private void dispatchListResetEvent(EventedList<ListItem> newVersion){
		for(EventedListListener<ListItem> l : resetListeners){
			l.listModified(newVersion);
		}
	}
	
}
