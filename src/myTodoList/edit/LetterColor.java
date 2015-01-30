package myTodoList.edit;

import java.awt.*; 
import java.awt.Point;

import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * ������ɫѡ��Ի���
 * @author Frank
 */
public class LetterColor extends JDialog {
	private static final long serialVersionUID = 1L;
	// ��ʼ��ΪĬ�ϵ���ɫ
	private Color letterColor = myTodoList.bin.CalendarEntry.DEFAULT_FOREGROUND_COLOR;
	public boolean change = false;

	/** 
	 * JColorChooser ѡ����ɫ 
	 */
	private JColorChooser colorChooser;

	/**
	 * Ĭ�Ϲ��캯��
	 */
	public LetterColor() {
		setTitle("ѡ���������ɫ"); 
		setModalityType(ModalityType.APPLICATION_MODAL); // ����ģʽ������ѡ�񸸴���

		colorChooser = new JColorChooser(); // JColorChooserʵ��
		change = false;
		// Change listener to color chooser
		colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
					@Override
					public void stateChanged(ChangeEvent e) { 
						// ���ѡ������ɫ���洢ѡ�������ɫ
						letterColor = colorChooser.getColor();
						change = true;
					}
				});

		colorChooser.setBounds(0, 0, 694, 450); // ���ô�С

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
