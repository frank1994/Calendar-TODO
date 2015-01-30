package myTodoList.edit;

import java.awt.*; 
import java.awt.Point;

import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * 选择颜色的对话框
 * @author Frank
 */
public class BackColor extends JDialog {
	private static final long serialVersionUID = 1L;
	// backColor存放新颜色
	private Color backColor = myTodoList.bin.CalendarEntry.DEFAULT_BACKGROUND_COLOR; // 默认颜色
	public boolean change = false;

	private JColorChooser colorChooser; // JColorChooser的实例

	/**
	 * 默认构造函数
	 */
	public BackColor() {
		setTitle("选择颜色");
		setModalityType(ModalityType.APPLICATION_MODAL); // 运行模式，父窗口不能点击

		colorChooser = new JColorChooser();
		change = false;
		colorChooser.getSelectionModel().addChangeListener(new ChangeListener() { // 修改颜色监听
					@Override
					public void stateChanged(ChangeEvent e) { //选择了颜色，储存新颜色
						backColor = colorChooser.getColor();
						change = true;
					}
				});

		colorChooser.setBounds(0, 0, 694, 450); // 设置大小

		add(colorChooser); // 增加选择的颜色

		setLayout(null);
		Point location = new Point(150, 150);  // 设置出现的位置
		setLocation(location);
		setResizable(false);  // 不能改变大小
		//setSize(700, 500);
		getContentPane().setPreferredSize(new Dimension(700, 500));
		pack();
	}

	/**
	 * 获得背景颜色
	 * @return backColor 
	 */
	public Color getBackColor() {
		return backColor;
	}

	/**
	 * 设置背景颜色
	 * @param backColor 要设置的颜色
	 */
	public void setBackColor(Color backColor) {
		this.backColor = backColor;
	}
	
	public static void main(String args[]) {
		BackColor test = new BackColor();
		test.show();
	}
}
