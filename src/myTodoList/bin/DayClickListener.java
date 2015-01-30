package myTodoList.bin;

import java.awt.event.MouseEvent;
import myTodoList.gui.views.DayPanel;

/**
 * 日期组件事件
 * @author Frank 
 *
 */
public interface  DayClickListener {
	/**
	 * 一个日期组件被点击事件
	 * @param day
	 */
	public  void dayClicked(DayPanel day,MouseEvent e);

}
