package myTodoList.edit;

import java.util.*;  
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

/**
 * 选择日期的对话框
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
	 * 参数为方向的构造函数
	 * 
	 * @param 如果是查看下个星期
	 * @author Frank
	 */
	public DateChooserWindow(boolean forward) {
		setVisible(false);
		setTitle("选择日期"); 
		setModalityType(ModalityType.APPLICATION_MODAL); // Set modality of window
		
		if(forward){
			direction = 1;  // 向前  
			monthNamesBackwards = new String[]{"一月", "二月", "三月",
					"四月", "五月", "六月", "七月", "八月",
					"九月", "十月", "十一月", "十二月"};
			monthDaysBackwards = new int[]{31, 28, 31, 30, 31, 31, 30, 31, 30, 31, 30, 31};
		}else{
			direction = -1;
			monthNamesBackwards = new String[]{"十二月", "十一月", "十月",
					"九月", "八月", "七月", "六月", "五月",
					"四月", "三月", "二月", "一月"};
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
	 * 出现对话框，如果选择了按钮，触发ActionListener
	 * @param l 回触的信号
	 * @author Frank
	 */
	@SuppressWarnings("deprecation")
	public void show(final ActionListener l){
		years = new JSpinner(new SpinnerNumberModel(new    // 选择
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
				
				if(monthBackward == 11 - 1 && isLeapYear){   // 二月，因为传入的false
					max = 29;
				}else{   // 当前选择的月的天数
					max = monthDaysBackwards[monthBackward];
				}
				
				if (max < day){   // 如果当前选择月的天数小于当前day，days（JSpinner）的最大值需要改变
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
		
		JButton btnOK = new JButton("确定");
		btnOK.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				l.actionPerformed(null);
				dispose();
			}
		});
		JButton btnCancel = new JButton("取消");
		btnCancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		years.setBounds(10, 10, 80, 30);
		months.setBounds(100, 10, 140, 30);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(System.currentTimeMillis()));
		months.setValue(monthNamesBackwards[11 * ((1 - direction) / 2) +   // 取得当前的月份
		                                    direction * (c.get(Calendar.MONTH))]);
		days.setBounds(250,10,80,30);                  
		days.setValue(c.get(Calendar.DAY_OF_MONTH));   // 取得当前的日期
		
		btnOK.setBounds(70, 50, 90, 30);
		btnCancel.setBounds(170, 50, 90, 30);   // 取消按钮的大小
		
		add(years);
		add(months);
		add(days);
		add(btnOK);
		add(btnCancel);
		
		setVisible(true);
	}
	
	/**
	 * @author Frank
	 * @return 返回选择的日期
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

