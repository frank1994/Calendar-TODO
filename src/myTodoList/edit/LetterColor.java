package myTodoList.edit;

import java.awt.*; 
import java.awt.Point;

import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * 字体颜色选择对话框
 * @author Frank
 */
public class LetterColor extends JDialog {
	private static final long serialVersionUID = 1L;
	// 初始化为默认的颜色
	private Color letterColor = myTodoList.bin.CalendarEntry.DEFAULT_FOREGROUND_COLOR;
	public boolean change = false;

	/** 
	 * JColorChooser 选择颜色 
	 */
	private JColorChooser colorChooser;

	/**
	 * 默认构造函数
	 */
	public LetterColor() {
		setTitle("选择字体的颜色"); 
		setModalityType(ModalityType.APPLICATION_MODAL); // 设置模式，不能选择父窗口

		colorChooser = new JColorChooser(); // JColorChooser实例
		change = false;
		// Change listener to color chooser
		colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
					@Override
					public void stateChanged(ChangeEvent e) { 
						// 如果选择了颜色，存储选择的新颜色
						letterColor = colorChooser.getColor();
						change = true;
					}
				});

		colorChooser.setBounds(0, 0, 694, 450); // 设置大小

		add(colorChooser); // Ad color chooser to JDialog

		setLayout(null);
		Point location = new Point(400, 300);
		setLocation(location);
		setResizable(false);
		//setSize(700, 500);
		getContentPane().setPreferredSize(new Dimension(700, 500));
		pack();
	}

	// getter and setter
	public Color getLetterColor() {
		return letterColor;
	}

	public void setLetterColor(Color letterColor) {
		this.letterColor = letterColor;
	}
}
