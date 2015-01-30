package myTodoList.edit;

import java.awt.*; 
import java.awt.Point;

import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * ѡ����ɫ�ĶԻ���
 * @author Frank
 */
public class BackColor extends JDialog {
	private static final long serialVersionUID = 1L;
	// backColor�������ɫ
	private Color backColor = myTodoList.bin.CalendarEntry.DEFAULT_BACKGROUND_COLOR; // Ĭ����ɫ
	public boolean change = false;

	private JColorChooser colorChooser; // JColorChooser��ʵ��

	/**
	 * Ĭ�Ϲ��캯��
	 */
	public BackColor() {
		setTitle("ѡ����ɫ");
		setModalityType(ModalityType.APPLICATION_MODAL); // ����ģʽ�������ڲ��ܵ��

		colorChooser = new JColorChooser();
		change = false;
		colorChooser.getSelectionModel().addChangeListener(new ChangeListener() { // �޸���ɫ����
					@Override
					public void stateChanged(ChangeEvent e) { //ѡ������ɫ����������ɫ
						backColor = colorChooser.getColor();
						change = true;
					}
				});

		colorChooser.setBounds(0, 0, 694, 450); // ���ô�С

		add(colorChooser); // ����ѡ�����ɫ

		setLayout(null);
		Point location = new Point(150, 150);  // ���ó��ֵ�λ��
		setLocation(location);
		setResizable(false);  // ���ܸı��С
		//setSize(700, 500);
		getContentPane().setPreferredSize(new Dimension(700, 500));
		pack();
	}

	/**
	 * ��ñ�����ɫ
	 * @return backColor 
	 */
	public Color getBackColor() {
		return backColor;
	}

	/**
	 * ���ñ�����ɫ
	 * @param backColor Ҫ���õ���ɫ
	 */
	public void setBackColor(Color backColor) {
		this.backColor = backColor;
	}
	
	public static void main(String args[]) {
		BackColor test = new BackColor();
		test.show();
	}
}
