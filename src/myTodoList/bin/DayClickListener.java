package myTodoList.bin;

import java.awt.event.MouseEvent;
import myTodoList.gui.views.DayPanel;

/**
 * ��������¼�
 * @author Frank 
 *
 */
public interface  DayClickListener {
	/**
	 * һ���������������¼�
	 * @param day
	 */
	public  void dayClicked(DayPanel day,MouseEvent e);

}
