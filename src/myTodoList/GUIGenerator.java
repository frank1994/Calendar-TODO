package myTodoList;


import java.awt.Container; 

import myTodoList.gui.views.CalendarView;


/**
 * 产生、维持Window的content的变化
 * 
 * @author Frank
 */
public interface GUIGenerator {
	/**
	 * 产生Window的容器
	 * @param parent 父窗口
	 * @param container 窗口的container
	 * @return 默认值
	 */
	public CalendarView show(Window parent, Container container);

	/**
	 * 从Window中移除GUI
	 */
	public void destroy();
}
