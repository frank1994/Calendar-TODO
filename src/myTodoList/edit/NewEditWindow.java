package myTodoList.edit;

import java.awt.Dimension; 
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import myTodoList.bin.CalendarEntry;
import myTodoList.bin.Interval;
import myTodoList.data.DataModel;
import myTodoList.gui.views.CalendarView;


/**
 * �������¼��Ĵ���
 * @author Frank
 * */
public class NewEditWindow extends JDialog {
	private static final long serialVersionUID = 1L;
	private JLabel hourFromLabel;
	private JLabel hourTillLabel;
	private JLabel typeLabel;
	private JLabel titleLabel;
	private JLabel eventLabel;
	private JLabel newTypeLabel;
	private JLabel letterColorLabel;
	private JLabel backColorLabel;
	/**
	 * �������ݵ�JTextFields 
	 */
	private JTextField titleField;
	private JTextArea eventField;
	private JTextField newTypeField;
	/**
	 *  JSpinners
	 */
	private JSpinner yearDateFrom;
	private JSpinner monthDateFrom;
	private JSpinner dayDateFrom;
	private JSpinner yearDateTill;
	private JSpinner monthDateTill;
	private JSpinner dayDateTill;
	private JSpinner hourFrom;
	private JSpinner minFrom;
	private JSpinner hourTill;
	private JSpinner minTill;
	/** 
	 * SpinnerModels 
	 */
	private SpinnerModel yearFromM;
	private SpinnerModel monthFromM;
	private SpinnerModel dayFromM;
	private SpinnerModel yearTillM;
	private SpinnerModel monthTillM;
	private SpinnerModel dayTillM;
	private SpinnerModel hourFromM;
	private SpinnerModel hourTillM;
	private SpinnerModel minFromM;
	private SpinnerModel minTillM;
	/** 
	 * JComboBox 
	 */
	private JComboBox<String> boxType;
	/**
	 * JButtons
	 */
	private JButton selectLetterColor;
	private JButton selectBackColor;
	private JButton okButton;
	private JButton cancelButton;
	/** 
	 * ʵ��������
	 */
	private LetterColor lc;
	private BackColor bc;
	private Date date;
	private JScrollPane eventScroll;
	
	private static int[] monthDays = new int[]{31,28,31,30,31,30,31,31,30,31,30,31};
	
	/**
	 * Ĭ�Ϲ��캯��
	 * @param usingCalendarView
	 * */
	public NewEditWindow(final CalendarView usingCalendarView) {
		setTitle("�½��¼�"); 
		setModalityType(ModalityType.APPLICATION_MODAL); // ����ģʽ���ͣ���Window����ѡ blog

		date = new Date();

		yearFromM = new SpinnerNumberModel(date.getYear() + 1900, date.getYear() + 1900, 5000, 1);
		monthFromM = new SpinnerNumberModel(date.getMonth() + 1, 1, 12, 1);
		dayFromM = new SpinnerNumberModel(date.getDate(), 1, 31, 1);
		yearTillM = new SpinnerNumberModel(date.getYear() + 1900, date.getYear() + 1900, 5000, 1);
		monthTillM = new SpinnerNumberModel(date.getMonth() + 1, 1, 12, 1);
		dayTillM = new SpinnerNumberModel(date.getDate(), 1, 31, 1);
		hourFromM = new SpinnerNumberModel(date.getHours(), 0, 23, 1);
		hourTillM = new SpinnerNumberModel(date.getHours(), 0, 23, 1);
		minFromM = new SpinnerNumberModel(date.getMinutes(), 0, 59, 1);
		minTillM = new SpinnerNumberModel(date.getMinutes(), 0, 59, 1);
		
		hourFromLabel = new JLabel("�¼���ʼʱ�䣺");
		hourTillLabel = new JLabel("�¼�����ʱ�䣺");
		typeLabel = new JLabel("�¼�����:");
		titleLabel = new JLabel("�¼����ƣ�");
		eventLabel = new JLabel("������");
		newTypeLabel = new JLabel("���¼���");
		letterColorLabel = new JLabel("������ɫ��");
		backColorLabel = new JLabel("������ɫ��");
		
		yearDateFrom = new JSpinner(yearFromM);
		monthDateFrom = new JSpinner(monthFromM);
		dayDateFrom = new JSpinner(dayFromM);
		yearDateTill = new JSpinner(yearTillM);
		monthDateTill = new JSpinner(monthTillM);
		dayDateTill = new JSpinner(dayTillM);
		hourFrom = new JSpinner(hourFromM);
		minFrom = new JSpinner(minFromM);
		hourTill = new JSpinner(hourTillM);
		minTill = new JSpinner(minTillM);
		
		titleField = new JTextField();
		eventField = new JTextArea();
		newTypeField = new JTextField();
		boxType = new JComboBox<String>(myTodoList.data.DataModel.getTypeList().toArray(new String[0]));
		selectLetterColor = new JButton();
		selectBackColor = new JButton();
//		selectLetterColor = new JButton("Select color of letters");
//		selectBackColor = new JButton("Select color of background");
		okButton = new JButton("ȷ��");
		cancelButton = new JButton("ȡ��");
		lc = new LetterColor();
		bc = new BackColor();

		hourFromLabel.setBounds(10, 10, 200, 20); // �������е�����Ĵ�С
		yearDateFrom.setBounds(10, 30, 80, 30);
		monthDateFrom.setBounds(90, 30, 50, 30);
		dayDateFrom.setBounds(140, 30, 50, 30);
		hourFrom.setBounds(215, 30, 50, 30);
		minFrom.setBounds(265, 30, 50, 30);
		hourTillLabel.setBounds(10, 70, 200, 20);
		yearDateTill.setBounds(10, 90, 80, 30);
		monthDateTill.setBounds(90, 90, 50, 30);
		dayDateTill.setBounds(140, 90, 50, 30);
		hourTill.setBounds(215, 90, 50, 30);
		minTill.setBounds(265, 90, 50, 30);
		typeLabel.setBounds(10, 130, 200, 20);
		boxType.setBounds(10, 150, 100, 30);
		newTypeLabel.setBounds(10, 130, 200, 20);
		newTypeField.setBounds(10, 150, 100, 30);
		titleLabel.setBounds(10, 190, 200, 20);
		titleField.setBounds(10, 210, 150, 30);
		eventLabel.setBounds(10, 250, 200, 20);
		//eventField.setBounds(10, 270, 365, 100);
		eventScroll = new JScrollPane(eventField,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED ,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		eventScroll.setBounds(10, 270, 365, 100);
		letterColorLabel.setBounds(10, 380, 140, 30);
		backColorLabel.setBounds(10, 410, 140, 30);
		selectLetterColor.setBounds(150, 380, 50, 30);
		selectBackColor.setBounds(150, 410, 50, 30);
		//selectLetterColor.setBounds(10, 310, 200, 30);
		//selectBackColor.setBounds(220, 310, 200, 30);
		okButton.setBounds(260, 435, 70, 30);
		cancelButton.setBounds(320, 435, 70, 30);

		selectLetterColor.setBackground(lc.getLetterColor());
		selectBackColor.setBackground(bc.getBackColor());
		
		eventField.setLineWrap(true);
		eventField.setWrapStyleWord(true);
		
		monthDateFrom.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
				int year = (int)yearDateFrom.getValue();
				int month = (int)monthDateFrom.getValue() - 1;
				int day = (int)dayDateFrom.getValue();
				
				int max;
				
				boolean isLeapYear = ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
				
				if (month == 1 && isLeapYear){
					max = 29;
				}else{
					max = monthDays[month];
				}
				
				if (max < day){
					dayDateFrom.setValue(max);
				}
			}
		});
		dayDateFrom.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
				int year = (int)yearDateFrom.getValue();
				int month = (int)monthDateFrom.getValue() - 1;
				int day = (int)dayDateFrom.getValue();
				
				int max;
				
				boolean isLeapYear = ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
				
				if (month == 1 && isLeapYear){
					max = 29;
				}else{
					max = monthDays[month];
				}
				
				if (max < day){
					dayDateFrom.setValue(max);
				}
			}
		});
		monthDateTill.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
				int year = (int)yearDateTill.getValue();
				int month = (int)monthDateTill.getValue() - 1;
				int day = (int)dayDateTill.getValue();
				
				int max;
				
				boolean isLeapYear = ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
				
				if (month == 1 && isLeapYear){
					max = 29;
				}else{
					max = monthDays[month];
				}
				
				if (max < day){
					dayDateTill.setValue(max);
				}
			}
		});
		dayDateTill.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
				int year = (int)yearDateTill.getValue();
				int month = (int)monthDateTill.getValue() - 1;
				int day = (int)dayDateTill.getValue();
				
				int max;
				
				boolean isLeapYear = ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
				
				if (month == 1 && isLeapYear){
					max = 29;
				}else{
					max = monthDays[month];
				}
				
				if (max < day){
					dayDateTill.setValue(max);
				}
			}
		});

		boxType.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (((String) boxType.getSelectedItem()).equals(DataModel.newType)) {
					// ���ѡ�����µ��¼������Դ����µ��¼�
					remove(typeLabel); // ����������µ��¼���ɾ��ԭ����boxType
					remove(boxType);
					add(newTypeLabel);
					add(newTypeField);
					repaint();
				}
			}
		});

		selectLetterColor.addActionListener(new ActionListener() { 
					@Override
					public void actionPerformed(ActionEvent e) { // �����ťѡ��������ɫ
						lc.setVisible(true);
						selectLetterColor.setBackground(lc.getLetterColor());
					}
				});

		selectBackColor.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) { // �����ťѡ�񱳾���ɫ
						bc.setVisible(true);
						selectBackColor.setBackground(bc.getBackColor());
					}
				});

		okButton.addActionListener(new ActionListener() { // ȷ����ť�ļ���
			@Override
			public void actionPerformed(ActionEvent e) {
				if(titleField.getText().equals("")){
					JOptionPane.showMessageDialog(getContentPane(),
							"�¼�����Ϊ�գ�", "����",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(eventField.getText().equals("")){
					JOptionPane.showMessageDialog(getContentPane(),
							"�¼�����Ϊ��", "����",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				
				Calendar fromDate = Calendar.getInstance();
				fromDate.set((int)yearDateFrom.getValue(), (int)monthDateFrom.getValue() - 1,
						(int)dayDateFrom.getValue(), (int)hourFrom.getValue(), (int)minFrom.getValue());
				fromDate.set(Calendar.SECOND, 0);
				fromDate.set(Calendar.MILLISECOND, 0);
				Calendar tillDate = Calendar.getInstance();
				tillDate.set((int) yearDateTill.getValue(), (int) monthDateTill.getValue() - 1,
						(int) dayDateTill.getValue(), (int) hourTill.getValue(), (int) minTill.getValue());
				tillDate.set(Calendar.SECOND, 0);
				tillDate.set(Calendar.MILLISECOND, 0);
				if ((int)yearDateFrom.getValue() == (int)yearDateTill.getValue()
						&& (int)monthDateFrom.getValue() == (int)monthDateTill.getValue()
						&& (int)dayDateFrom.getValue() == (int)dayDateTill.getValue()
						&& (int)hourFrom.getValue() == (int)hourTill.getValue()
						&& (int)minFrom.getValue() == (int)minTill.getValue()) { 
					JOptionPane.showMessageDialog(getContentPane(), "��ʼʱ�������ʱ����ͬ��",
							"����", JOptionPane.ERROR_MESSAGE);
				} else if (((int) yearDateFrom.getValue() > (int) yearDateTill.getValue())
						|| ((int) yearDateFrom.getValue() == (int) yearDateTill.getValue() && (int) monthDateFrom
								.getValue() > (int) monthDateTill.getValue())
						|| ((int) yearDateFrom.getValue() == (int) yearDateTill.getValue()
								&& (int) monthDateFrom.getValue() == (int) monthDateTill.getValue() && (int) dayDateFrom
								.getValue() > (int) dayDateTill.getValue())
						|| ((int) yearDateFrom.getValue() == (int) yearDateTill.getValue()
								&& (int) monthDateFrom.getValue() == (int) monthDateTill.getValue()
								&& (int) dayDateFrom.getValue() == (int) dayDateTill.getValue() && (int) hourFrom
								.getValue() > (int) hourTill.getValue())
						|| ((int) yearDateFrom.getValue() == (int) yearDateTill.getValue()
								&& (int) monthDateFrom.getValue() == (int) monthDateTill.getValue()
								&& (int) dayDateFrom.getValue() == (int) dayDateTill.getValue()
								&& (int) hourFrom.getValue() == (int) hourTill.getValue() && (int) minFrom.getValue() > (int) minTill
								.getValue())) {
					JOptionPane.showMessageDialog(getContentPane(),
							"����ʱ�����ڿ�ʼʱ��", "����",
							JOptionPane.ERROR_MESSAGE);
				} else if (newTypeField.getText().equals("")) { // ���û����д�µ��¼�
					if (boxType.getSelectedItem() == DataModel.newType){
						JOptionPane.showMessageDialog(getContentPane(),
								"û��ѡ���¼���", "����",
								JOptionPane.ERROR_MESSAGE);
						remove(newTypeLabel);
						remove(newTypeField);
						add(typeLabel);
						add(boxType);
						boxType.setSelectedItem(DataModel.getDefaultTypeArray()[0]);
						repaint();
						return;
					}
					CalendarEntry entry = new CalendarEntry(new Interval(fromDate.getTimeInMillis(), tillDate.getTimeInMillis()),
									(String) boxType.getSelectedItem(), titleField.getText(), lc.getLetterColor(), bc
											.getBackColor(), eventField.getText());
					myTodoList.data.DataModel.getEntryList().add(entry);
					usingCalendarView.setSelected(entry);
					dispose();
				} else { // �����д���µ��¼������ҿ����ύ
					CalendarEntry entry = new CalendarEntry();
					entry.setInterval(new Interval(fromDate.getTimeInMillis(), tillDate.getTimeInMillis()));
					entry.setType(newTypeField.getText());
					entry.setTitle(titleField.getText());
					entry.setForegroundColor((lc.change) ? lc.getLetterColor() : CalendarEntry.DEFAULT_FOREGROUND_COLOR);
					entry.setBackgroundColor((bc.change) ? bc.getBackColor() : CalendarEntry.DEFAULT_BACKGROUND_COLOR);
					entry.setDescription(eventField.getText());

					boolean faild = false;
					for(String str : DataModel.getTypeList()) {
						if(str.equals(newTypeField.getText())) {
							faild = true;
							entry.setType(str);
							break;
						}
					}
					if (!faild) {
						DataModel.getTypeList().remove(DataModel.newType);
						DataModel.getTypeList().add(newTypeField.getText());
						DataModel.getTypeList().add(DataModel.newType);
					}

					// �޸����
					DataModel.getEntryList().add(entry);
					usingCalendarView.setSelected(entry);
					dispose();
				}
			}
		});

		cancelButton.addActionListener(new ActionListener() { // cancel Button�ļ���
					@Override
					public void actionPerformed(ActionEvent e) { // �رմ���
						dispose();
					}
				});
		
		
		add(hourFromLabel); 
		add(yearDateFrom);
		add(monthDateFrom);
		add(dayDateFrom);
		add(hourFrom);
		add(minFrom);
		add(hourTillLabel);
		add(yearDateTill);
		add(monthDateTill);
		add(dayDateTill);
		add(hourTill);
		add(minTill);
		add(typeLabel);
		add(boxType);
		add(titleLabel);
		add(titleField);
		add(eventLabel);
		add(eventScroll);
		add(letterColorLabel);
		add(backColorLabel);
		add(selectLetterColor);
		add(selectBackColor);
		add(okButton);
		add(cancelButton);

		setLayout(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Point location = new Point(200, 200);
		setLocation(location);
		setResizable(false);
		//setSize(450, 500);
		getContentPane().setPreferredSize(new Dimension(450, 500));
		pack();
	}
}
