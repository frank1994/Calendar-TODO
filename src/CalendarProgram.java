import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.DataFormatException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import myTodoList.Window;
import myTodoList.bin.CalendarEntry;
import myTodoList.data.DataHandler;
import myTodoList.data.DataModel;


/**
 * 程序的入口
 * 
 * @author Frank
 */
public class CalendarProgram {
	public static String path;
	
	public static final String configPath = "Config.txt";   // 可以存放第一次存放文件的Path
	
	/**
	 * 程序的入口
	 * 窗口可以使用下列风格：
	 * Metal
	 * Nimbus
	 * CDE/Motif
     * Windows
     * Windows Classic
	 * @author Frank
	 * @param args 
	 * @throws IOException Nimbus LAF not available
	 */
	public static void main(String[] args) throws IOException {
		try{
			
			for(LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		}catch(Exception e) {
			System.err.println("Error: Nimbus LAF not available");
			System.exit(1);
		}

		// 初始化 BoxList，获得条目的种类
		DataModel.getTypeList().addAll(Arrays.asList(DataModel.getDefaultTypeArray()));

		// 如果应用打开过
		path = null;
		if(new File(configPath).exists()){   // 如果配置文件存在
			BufferedReader read = new BufferedReader(new FileReader(configPath));
			String line;
			try{
				while((line = read.readLine()) != null) {
					path = line;
				}
			}catch(IOException exception) {
				System.out.println("文件无法读入");
			}
			read.close();
		}
		
		Window w = new Window(path);
		
		w.setIconImage(w.getToolkit().getImage("src/image/Calendar.png"));    // 设置图标
		
		if(path != null) {    // 如果有配置文件
			boolean rmConfig = false;
			List<CalendarEntry> list = new LinkedList<CalendarEntry>();
			try{
				DataHandler.readData(path, list);
				DataModel.getEntryList().resetList(list);
			}catch(IOException exception) {
				exception.printStackTrace();
				JOptionPane.showMessageDialog(null, "无法打开文件", "读入错误", JOptionPane.ERROR_MESSAGE);
			}catch(DataFormatException exception) {   // 如果数据的格式不对
				exception.printStackTrace();
				JOptionPane.showMessageDialog(null, "文件损坏了", "读入错误", JOptionPane.ERROR_MESSAGE);
			}
			if(rmConfig){
				new File(configPath).delete();
			}
		}

	}
}
