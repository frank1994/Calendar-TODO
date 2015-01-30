package myTodoList.bin;

import java.awt.event.ActionEvent;
import javax.swing.JButton;

/**
 * 鼠标点击事件接口
 * @author Frank 
 */
public interface ActionButtonListener {

	/**
	 * 动作按钮鼠标点击事件
	 * @param button
	 */
	public void actionButtonactionPerformed(JButton button ,ActionEvent e);
}
