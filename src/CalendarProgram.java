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
 * ��������
 * 
 * @author Frank
 */
public class CalendarProgram {
	public static String path;
	
	public static final String configPath = "Config.txt";   // ���Դ�ŵ�һ�δ���ļ���Path
	
	/**
	 * ��������
	 * ���ڿ���ʹ�����з��
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

		// ��ʼ�� BoxList�������Ŀ������
		DataModel.getTypeList().addAll(Arrays.asList(DataModel.getDefaultTypeArray()));

		// ���Ӧ�ô򿪹�
		path = null;
		if(new File(configPath).exists()){   // ��������ļ�����
			BufferedReader read = new BufferedReader(new FileReader(configPath));
			String line;
			try{
				while((line = read.readLine()) != null) {
					path = line;
				}
			}catch(IOException exception) {
				System.out.println("�ļ��޷�����");
			}
			read.close();
		}
		
		Window w = new Window(path);
		
		w.setIconImage(w.getToolkit().getImage("src/image/Calendar.png"));    // ����ͼ��
		
		if(path != null) {    // ����������ļ�
			boolean rmConfig = false;
			List<CalendarEntry> list = new LinkedList<CalendarEntry>();
			try{
				DataHandler.readData(path, list);
				DataModel.getEntryList().resetList(list);
			}catch(IOException exception) {
				exception.printStackTrace();
				JOptionPane.showMessageDialog(null, "�޷����ļ�", "�������", JOptionPane.ERROR_MESSAGE);
			}catch(DataFormatException exception) {   // ������ݵĸ�ʽ����
				exception.printStackTrace();
				JOptionPane.showMessageDialog(null, "�ļ�����", "�������", JOptionPane.ERROR_MESSAGE);
			}
			if(rmConfig){
				new File(configPath).delete();
			}
		}

	}
}
