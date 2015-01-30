package myTodoList;


import java.awt.Container; 

import myTodoList.gui.views.CalendarView;


/**
 * ������ά��Window��content�ı仯
 * 
 * @author Frank
 */
public interface GUIGenerator {
	/**
	 * ����Window������
	 * @param parent ������
	 * @param container ���ڵ�container
	 * @return Ĭ��ֵ
	 */
	public CalendarView show(Window parent, Container container);

	/**
	 * ��Window���Ƴ�GUI
	 */
	public void destroy();
}
