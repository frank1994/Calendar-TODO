package myTodoList.bin;

import myTodoList.gui.views.DayPanel;


/**
 * 日期组件创建事件
 * @author Frank 
 */
public interface DayCompCreateListener {

	/**
	 * 一个日期组件创建事件
	 * @param day
	 */
	public void dayCompCreated(DayPanel day);
}
