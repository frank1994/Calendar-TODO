package myTodoList;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.DataFormatException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import myTodoList.bin.CalendarEntry;
import myTodoList.bin.DayClickListener;
import myTodoList.bin.DayCompCreateListener;
import myTodoList.data.DataHandler;
import myTodoList.data.DataModel;
import myTodoList.edit.CalendarPop;
import myTodoList.edit.NewEditWindow;
import myTodoList.edit.RewriteEditWindow;
import myTodoList.edit.WeatherFrame;
import myTodoList.gui.ViewGUI;
import myTodoList.gui.views.CalendarView;
import myTodoList.gui.views.DayPanel;

/**
 * ������
 * @author Frank
 */
public class Window extends JFrame implements DayClickListener,DayCompCreateListener, MouseListener{
	int ifCalendarPop = 0;
	private static final long serialVersionUID = 1L;
	private String openPath;   	// �ļ���·��
	private String savePath;
	
	private CalendarView usingCalendarView;  // ����ʹ�õ� calendar view
	
	private JMenuBar menu;
	private JMenu fileMenu;
	private JMenu editMenu;
	private JMenu funMenu;   // ���ܣ��鿴��������������
	private JMenuItem openItem;
	private JMenuItem saveItem;
	private JMenuItem saveAsItem;
	private JMenuItem exitItem;
	private JMenuItem reWriteItem;
	private JMenuItem deleteItem;
	private JMenuItem newItem;
	private JMenuItem calendarItem;
	private JMenuItem weatherItem;
//	private JMenuItem weekItem;  // TODO
//	private JMenuItem monthItem; // TODO
	private JButton currentDateItem;
	private JFileChooser fileChooser;	// JFileChooser ���ļ� �����ļ� 
	private FileNameExtensionFilter fileFilter;

	public Dimension size;

	/**
	 * Ĭ�Ϲ��캯��
	 * @param path �ó�������ʱ��·��
	 */
	public Window(String path) {
		super("Calendar&&TODO");  // ȷ�������ڵı���
//		setBackground(new Color(76, 157, 160));    // Nimbus�ı�����ɫĬ��Ϊ��ɫ  
		final ViewGUI wgui = new ViewGUI();
		openPath = path;          // �ڴ����·����Ѱ����Ҫ�򿪵�Calendar�ļ�
		savePath = path;          // �ڴ����·���±���Calendar�����ļ�

		gui = null;
		GUIPanel = getContentPane();

		// �ļ�����
		fileFilter = new FileNameExtensionFilter("Calendar file", DataHandler.calendarExtension); // ����.cal��׺

		menu = new JMenuBar();
		fileMenu = new JMenu("  �ļ�  ");
		fileMenu.setMinimumSize(new Dimension(120, 1));
		openItem = new JMenuItem("��");    // ���ļ���openItemѡ��
		openItem.addActionListener(new ActionListener() { // openItem�ļ���
			@Override
			public void actionPerformed(ActionEvent e) { 
				fileChooser = new JFileChooser(openPath);// ���ѡ���˴򿪣��Ϳ��Ը���·�����ļ������ұ���·��
				fileChooser.removeChoosableFileFilter(fileChooser.getChoosableFileFilters()[0]);
				fileChooser.addChoosableFileFilter(fileFilter);
				fileChooser.showSaveDialog(null);
				if(fileChooser.getSelectedFile() == null) {   // ���û��ѡ���ļ�
					return;
				}
				openPath = fileChooser.getSelectedFile().getPath(); // ���ļ���·��
				System.out.println("���ļ���openPath:" + openPath);
				List<CalendarEntry> list = new LinkedList<CalendarEntry>();
				try{
					DataHandler.readData(openPath, list);
					DataModel.getEntryList().resetList(list);   // ����CalendarEntry��list����EntryList,�Ѿ�ʵ���˼�����
				}catch(IOException exception) {
					exception.printStackTrace();
					JOptionPane.showMessageDialog(null, "�޷����ļ�", "�����ˣ�",
							JOptionPane.ERROR_MESSAGE);
				}catch(DataFormatException exception) {
					exception.printStackTrace();
					JOptionPane.showMessageDialog(getContentPane(), "�ļ�����", "�����ˣ�",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		saveItem = new JMenuItem("����");
		saveItem.addActionListener(new ActionListener() { // ����Item�ļ���
			@Override
			public void actionPerformed(ActionEvent e) { // ���ѡ�񱣴�Item�����ļ�ѡ��Ի��򣬲��ұ���򿪵�·��
				if(savePath == null || !new File(savePath).exists()) {  // ���û�б����
					fileChooser = new JFileChooser(openPath);
					fileChooser.removeChoosableFileFilter(fileChooser.getChoosableFileFilters()[0]);
					fileChooser.addChoosableFileFilter(fileFilter);
					fileChooser.showSaveDialog(null);
					if (fileChooser.getSelectedFile() == null) {
						return;
					}
					savePath = fileChooser.getSelectedFile().getPath() + "." + DataHandler.calendarExtension;
				}

				try{
					DataHandler.writeData(savePath, DataModel.getEntryList());
				}catch (IOException exception) {
					exception.printStackTrace();
					JOptionPane.showMessageDialog(getContentPane(), "�����ļ�ʱ��������\n�ļ�:\n"
							+ savePath, "����", JOptionPane.ERROR_MESSAGE);
				}

				try{
					Writer write = new FileWriter("Config.txt");
					write.write(savePath);
					write.close();
				}catch(IOException exception) {
					exception.printStackTrace();
				}
			}
		});
		saveAsItem = new JMenuItem("���Ϊ");
		saveAsItem.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) { 
				fileChooser = new JFileChooser(openPath);
				fileChooser.removeChoosableFileFilter(fileChooser.getChoosableFileFilters()[0]);
				fileChooser.addChoosableFileFilter(fileFilter);
				fileChooser.showSaveDialog(null);
				if (fileChooser.getSelectedFile() == null) {
					return;
				}
				savePath = fileChooser.getSelectedFile().getPath() + "." + DataHandler.calendarExtension;
				try {
					DataHandler.writeData(savePath, DataModel.getEntryList());
				} catch (IOException exception) {
					exception.printStackTrace();
					JOptionPane.showMessageDialog(getContentPane(),
							"�����ļ���ʱ��������:\n�ļ�:\n" + savePath, "����",
							JOptionPane.ERROR_MESSAGE);
				}

				try {
					Writer write = new FileWriter("Config.txt");
					write.write(savePath);
					write.close();
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
		});
		exitItem = new JMenuItem("�˳�");
		exitItem.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		editMenu = new JMenu("  �༭  ");
		newItem = new JMenuItem("�½�");
		newItem.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) { // �����½��¼��Ĵ���
				NewEditWindow n = new NewEditWindow(usingCalendarView);
				n.setVisible(true);
			}
		});
		reWriteItem = new JMenuItem("�޸�");
		reWriteItem.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) { 
				if(wgui.selectedEntry == null) {
					JOptionPane.showMessageDialog(getContentPane(), "û��ѡ���¼���", "����",
							JOptionPane.ERROR_MESSAGE);
				}else {
					RewriteEditWindow r = new RewriteEditWindow(wgui.selectedEntry, usingCalendarView);
					r.setVisible(true);
				}
			}
		});
		deleteItem = new JMenuItem("ɾ��");
		deleteItem.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) { // ����ɾ���¼��ĶԻ���
				if (wgui.selectedEntry == null) {
					JOptionPane.showMessageDialog(getContentPane(), "û��ѡ���¼���", "����",
							JOptionPane.ERROR_MESSAGE);
				} else {
					int res = JOptionPane.showConfirmDialog(getContentPane(), "ȷ��Ҫɾ����",
							"���棡", JOptionPane.YES_NO_OPTION);
					if (res == JOptionPane.YES_OPTION) {
						myTodoList.data.DataModel.getEntryList().
						remove(myTodoList.gui.ViewGUI.selectedEntry);
						usingCalendarView.repaint();
					}
				}
			}
		});
		
		funMenu = new JMenu("  ����  ");
		calendarItem = new JMenuItem("�鿴�ϻ���");
		calendarItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				myTodoList.gui.ViewGUI.calendarClicked();
				JOptionPane.showConfirmDialog(null, 
						"��Ǹ�����ڿ�����", "information", 
						JOptionPane.YES_NO_OPTION); 
			}
		});
		weatherItem = new JMenuItem("�鿴����");
		weatherItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WeatherFrame wf = null;
				try {
					wf = new WeatherFrame();
				} catch (IOException e1) {
					System.out.println("�������ӳ���������");
					e1.printStackTrace();
				}
				wf.show();
			}
		});
		
		currentDateItem = new JButton("��ǰ��");
		currentDateItem.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) { // ����Week View
				usingCalendarView.toInterval(new Date(System.currentTimeMillis()));
				myTodoList.gui.ViewGUI.label.setText(myTodoList.gui.ViewGUI.getDateString(usingCalendarView
						.getDisplayedInterval()));
			}
		});
		/*
		 * viewMenu = new JMenu("View"); weekItem = new JMenuItem("Week"); weekItem.addActionListener(new
		 * ActionListener() { // Action listener to weekItem
		 * 
		 * @Override public void actionPerformed(ActionEvent e) { // If select week item refresh gui to week view
		 * refreshGUI(wgui); } }); monthItem = new JMenuItem("Month"); // monthItem.addActionListener(new
		 * ActionListener() { // Action listener to monthItem // @Override // public void actionPerformed(ActionEvent e)
		 * { // If select month item refresh gui to month view // refreshGUI(new ViewGUI()); // } // });
		 */

		setJMenuBar(menu); 
		menu.add(fileMenu); 
		menu.add(editMenu);
		menu.add(funMenu);
		// menu.add(viewMenu);
		menu.add(currentDateItem);
		fileMenu.add(openItem); 
		fileMenu.addSeparator();
		fileMenu.add(saveItem);
		fileMenu.addSeparator();
		fileMenu.add(saveAsItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);
		editMenu.add(newItem); 
		editMenu.addSeparator();
		editMenu.add(reWriteItem);
		editMenu.addSeparator();
		editMenu.add(deleteItem);
		funMenu.add(calendarItem);
		funMenu.add(weatherItem);
		// viewMenu.add(weekItem); 
		// v/ewMenu.addSeparator();
		// viewMenu.add(monthItem);

		setLayout(new FlowLayout());
		pack();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Point location = new Point(75, 50);
		setLocation(location);

		setResizable(false);    // �����Ըı��С����

		// ����WeekWiew�Ĵ�С������Window�Ĵ�С
		int reqWidth = ViewGUI.getGUIWidth();
		int reqHeight = ViewGUI.getGUIHeight();
		int extraW = 50;   // �����ȸ������
		int extraH = 0;

		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int height = gd.getDisplayMode().getHeight() - 100;
		if (height < reqHeight + extraH) {    // �����Ļ�ĸ߶�ҪС����Ҫ�ĸ߶ȣ����й�����
			size = new Dimension(reqWidth + extraW, height);
			// setSize(reqWidth + extraW, height - 50);
		} else {
			size = new Dimension(reqWidth + extraW, reqHeight + extraH);
			// setSize(reqWidth + extraW, reqHeight + extraH);
		}
		setSize(size);

		refreshGUI(new ViewGUI()); // �����ʼ��GUI
		setVisible(true);
	}

	private GUIGenerator gui;
	private Container GUIPanel; // �ɸı��GUI����

	/**
	 * ����GUIPanel������
	 * 
	 * @author Frank
	 * @param gui GUIGenerator
	 */
	public void refreshGUI(GUIGenerator gui) {
		if (this.gui != null) {
			this.gui.destroy();
		}
		GUIPanel.removeAll();

		this.gui = gui;
		usingCalendarView = gui.show(this, GUIPanel);

		validate();
		GUIPanel.repaint();
//		System.out.println("ÿ�δ򿪶�ˢ�£�");
	}
	
	public static void main(String args[]) {
		Window test = new Window("C:/Users/Administrator/Desktop");
		test.show();
	}

	@Override
	public void dayCompCreated(DayPanel day) {
		// TODO Auto-generated method stub
	}

	@Override
	public void dayClicked(DayPanel day, MouseEvent e) {
		Calendar getnowDay = day.getCalendar();
		usingCalendarView.toInterval(getnowDay.getTime());
		myTodoList.gui.ViewGUI.label.setText(myTodoList.gui.ViewGUI.getDateString(usingCalendarView
				.getDisplayedInterval()));
	}
//    JMenuItem������������������ݿ���
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("test");
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}
}
