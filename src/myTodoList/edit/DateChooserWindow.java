package myTodoList.edit;

import java.util.*;  
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

/**
 * ѡ�����ڵĶԻ���
 * 
 * @author Frank
 */
public class DateChooserWindow extends JDialog{
	private static final long serialVersionUID = 1L;
	private JSpinner years;
	private JSpinner months;
	private JSpinner days;
	
	private static String[] monthNamesBackwards;
	private static int[] monthDaysBackwards;
	private int direction;
	
	/**
	 * ����Ϊ����Ĺ��캯��
	 * 
	 * @param ����ǲ鿴�¸�����
	 * @author Frank
	 */
	public DateChooserWindow(boolean forward) {
		setVisible(false);
		setTitle("ѡ������"); 
		setModalityType(ModalityType.APPLICATION_MODAL); // Set modality of window
		
		if(forward){
			direction = 1;  // ��ǰ  
			monthNamesBackwards = new String[]{"һ��", "����", "����",
					"����", "����", "����", "����", "����",
					"����", "ʮ��", "ʮһ��", "ʮ����"};
			monthDaysBackwards = new int[]{31, 28, 31, 30, 31, 31, 30, 31, 30, 31, 30, 31};
		}else{
			direction = -1;
			monthNamesBackwards = new String[]{"ʮ����", "ʮһ��", "ʮ��",
					"����", "����", "����", "����", "����",
					"����", "����", "����", "һ��"};
			monthDaysBackwards = new int[]{31, 30, 31, 30, 31, 31, 30, 31, 30, 31, 28, 31};
		}
		
		setLayout(null);
		Point location = new Point(400, 300);
		setLocation(location);
		setResizable(false);
		getContentPane().setPreferredSize(new Dimension(340, 90));
		pack();
	}
	
	/**
	 * ���ֶԻ������ѡ���˰�ť������ActionListener
	 * @param l �ش����ź�
	 * @author Frank
	 */
	@SuppressWarnings("deprecation")
	public void show(final ActionListener l){
		years = new JSpinner(new SpinnerNumberModel(new    // ѡ��
				Date(System.currentTimeMillis()).getYear() + 1900, 1970, 5000, direction));
		months = new JSpinner(new SpinnerListModel(monthNamesBackwards));
		days = new JSpinner(new SpinnerNumberModel(1, 1, 31, direction));
		
		months.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
				int year = (int)years.getValue();
				int monthBackward = Arrays.asList(monthNamesBackwards).indexOf(months.getValue());
				int day = (int)days.getValue();
				
				int max;
				
				boolean isLeapYear = ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
				
				if(monthBackward == 11 - 1 && isLeapYear){   // ���£���Ϊ�����false
					max = 29;
				}else{   // ��ǰѡ����µ�����
					max = monthDaysBackwards[monthBackward];
				}
				
				if (max < day){   // �����ǰѡ���µ�����С�ڵ�ǰday��days��JSpinner�������ֵ��Ҫ�ı�
					days.setValue(max);
				}
			}
		});
		days.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
				int year = (int)years.getValue();
				int monthBackward = Arrays.asList(monthNamesBackwards).indexOf(months.getValue());
				int day = (int)days.getValue();
				
				int max;
				
				boolean isLeapYear = ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
				
				if (monthBackward == 11 - 1 && isLeapYear){
					max = 29;
				}else{
					max = monthDaysBackwards[monthBackward];
				}
				
				if (max < day){
					days.setValue(max);
				}
			}
		});
		
		JButton btnOK = new JButton("ȷ��");
		btnOK.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				l.actionPerformed(null);
				dispose();
			}
		});
		JButton btnCancel = new JButton("ȡ��");
		btnCancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		years.setBounds(10, 10, 80, 30);
		months.setBounds(100, 10, 140, 30);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(System.currentTimeMillis()));
		months.setValue(monthNamesBackwards[11 * ((1 - direction) / 2) +   // ȡ�õ�ǰ���·�
		                                    direction * (c.get(Calendar.MONTH))]);
		days.setBounds(250,10,80,30);                  
		days.setValue(c.get(Calendar.DAY_OF_MONTH));   // ȡ�õ�ǰ������
		
		btnOK.setBounds(70, 50, 90, 30);
		btnCancel.setBounds(170, 50, 90, 30);   // ȡ����ť�Ĵ�С
		
		add(years);
		add(months);
		add(days);
		add(btnOK);
		add(btnCancel);
		
		setVisible(true);
	}
	
	/**
	 * @author Frank
	 * @return ����ѡ�������
	 */
	public Date getDate(){
		Calendar c = Calendar.getInstance();
		
		c.set(Calendar.YEAR, (int)years.getValue());
		c.set(Calendar.MONTH, 11 * ((1 - direction) / 2) + direction * Arrays.asList(monthNamesBackwards).indexOf(months.getValue()));
		c.set(Calendar.DAY_OF_MONTH, (int)days.getValue());
		c.set(Calendar.AM_PM, Calendar.AM);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	
	public static void main(String args[]) {
		DateChooserWindow test = new DateChooserWindow(false);
		test.show();
	}
}

