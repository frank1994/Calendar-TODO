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
 * 主界面
 * @author Frank
 */
public class Window extends JFrame implements DayClickListener,DayCompCreateListener, MouseListener{
	int ifCalendarPop = 0;
	private static final long serialVersionUID = 1L;
	private String openPath;   	// 文件的路径
	private String savePath;
	
	private CalendarView usingCalendarView;  // 现在使用的 calendar view
	
	private JMenuBar menu;
	private JMenu fileMenu;
	private JMenu editMenu;
	private JMenu funMenu;   // 功能：查看日历，今天天气
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
	private JFileChooser fileChooser;	// JFileChooser 打开文件 保存文件 
	private FileNameExtensionFilter fileFilter;

	public Dimension size;

	/**
	 * 默认构造函数
	 * @param path 该程序运行时的路径
	 */
	public Window(String path) {
		super("Calendar&&TODO");  // 确定主窗口的标题
//		setBackground(new Color(76, 157, 160));    // Nimbus的背景颜色默认为白色  
		final ViewGUI wgui = new ViewGUI();
		openPath = path;          // 在传入的路径下寻找需要打开的Calendar文件
		savePath = path;          // 在传入的路径下保存Calendar类型文件

		gui = null;
		GUIPanel = getContentPane();

		// 文件类型
		fileFilter = new FileNameExtensionFilter("Calendar file", DataHandler.calendarExtension); // 加上.cal后缀

		menu = new JMenuBar();
		fileMenu = new JMenu("  文件  ");
		fileMenu.setMinimumSize(new Dimension(120, 1));
		openItem = new JMenuItem("打开");    // 打开文件的openItem选项
		openItem.addActionListener(new ActionListener() { // openItem的监听
			@Override
			public void actionPerformed(ActionEvent e) { 
				fileChooser = new JFileChooser(openPath);// 如果选择了打开，就可以根据路径打开文件，并且保存路径
				fileChooser.removeChoosableFileFilter(fileChooser.getChoosableFileFilters()[0]);
				fileChooser.addChoosableFileFilter(fileFilter);
				fileChooser.showSaveDialog(null);
				if(fileChooser.getSelectedFile() == null) {   // 如果没有选择文件
					return;
				}
				openPath = fileChooser.getSelectedFile().getPath(); // 打开文件的路径
				System.out.println("打开文件的openPath:" + openPath);
				List<CalendarEntry> list = new LinkedList<CalendarEntry>();
				try{
					DataHandler.readData(openPath, list);
					DataModel.getEntryList().resetList(list);   // 根据CalendarEntry的list更新EntryList,已经实现了监听。
				}catch(IOException exception) {
					exception.printStackTrace();
					JOptionPane.showMessageDialog(null, "无法打开文件", "出错了！",
							JOptionPane.ERROR_MESSAGE);
				}catch(DataFormatException exception) {
					exception.printStackTrace();
					JOptionPane.showMessageDialog(getContentPane(), "文件已损坏", "出错了！",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		saveItem = new JMenuItem("保存");
		saveItem.addActionListener(new ActionListener() { // 保存Item的监听
			@Override
			public void actionPerformed(ActionEvent e) { // 如果选择保存Item，打开文件选择对话框，并且保存打开的路径
				if(savePath == null || !new File(savePath).exists()) {  // 如果没有保存过
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
					JOptionPane.showMessageDialog(getContentPane(), "保存文件时发生错误\n文件:\n"
							+ savePath, "错误！", JOptionPane.ERROR_MESSAGE);
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
		saveAsItem = new JMenuItem("另存为");
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
							"保存文件的时候发生错误:\n文件:\n" + savePath, "错误",
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
		exitItem = new JMenuItem("退出");
		exitItem.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		editMenu = new JMenu("  编辑  ");
		newItem = new JMenuItem("新建");
		newItem.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) { // 弹出新建事件的窗口
				NewEditWindow n = new NewEditWindow(usingCalendarView);
				n.setVisible(true);
			}
		});
		reWriteItem = new JMenuItem("修改");
		reWriteItem.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) { 
				if(wgui.selectedEntry == null) {
					JOptionPane.showMessageDialog(getContentPane(), "没有选择事件！", "警告",
							JOptionPane.ERROR_MESSAGE);
				}else {
					RewriteEditWindow r = new RewriteEditWindow(wgui.selectedEntry, usingCalendarView);
					r.setVisible(true);
				}
			}
		});
		deleteItem = new JMenuItem("删除");
		deleteItem.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) { // 弹出删除事件的对话框
				if (wgui.selectedEntry == null) {
					JOptionPane.showMessageDialog(getContentPane(), "没有选择事件！", "警告",
							JOptionPane.ERROR_MESSAGE);
				} else {
					int res = JOptionPane.showConfirmDialog(getContentPane(), "确定要删除吗？",
							"警告！", JOptionPane.YES_NO_OPTION);
					if (res == JOptionPane.YES_OPTION) {
						myTodoList.data.DataModel.getEntryList().
						remove(myTodoList.gui.ViewGUI.selectedEntry);
						usingCalendarView.repaint();
					}
				}
			}
		});
		
		funMenu = new JMenu("  功能  ");
		calendarItem = new JMenuItem("查看老黄历");
		calendarItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				myTodoList.gui.ViewGUI.calendarClicked();
				JOptionPane.showConfirmDialog(null, 
						"抱歉，还在开发中", "information", 
						JOptionPane.YES_NO_OPTION); 
			}
		});
		weatherItem = new JMenuItem("查看天气");
		weatherItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WeatherFrame wf = null;
				try {
					wf = new WeatherFrame();
				} catch (IOException e1) {
					System.out.println("网络连接出现了问题");
					e1.printStackTrace();
				}
				wf.show();
			}
		});
		
		currentDateItem = new JButton("当前周");
		currentDateItem.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) { // 更新Week View
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

		setResizable(false);    // 不可以改变大小！！

		// 根据WeekWiew的大小，设置Window的大小
		int reqWidth = ViewGUI.getGUIWidth();
		int reqHeight = ViewGUI.getGUIHeight();
		int extraW = 50;   // 这个宽度给最左边
		int extraH = 0;

		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int height = gd.getDisplayMode().getHeight() - 100;
		if (height < reqHeight + extraH) {    // 如果屏幕的高度要小于需要的高度，会有滚动条
			size = new Dimension(reqWidth + extraW, height);
			// setSize(reqWidth + extraW, height - 50);
		} else {
			size = new Dimension(reqWidth + extraW, reqHeight + extraH);
			// setSize(reqWidth + extraW, reqHeight + extraH);
		}
		setSize(size);

		refreshGUI(new ViewGUI()); // 载入最开始的GUI
		setVisible(true);
	}

	private GUIGenerator gui;
	private Container GUIPanel; // 可改变的GUI容器

	/**
	 * 更新GUIPanel的内容
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
//		System.out.println("每次打开都刷新？");
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
//    JMenuItem的鼠标监听，下面的内容可以
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
