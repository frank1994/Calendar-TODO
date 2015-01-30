package myTodoList.data;

import java.awt.Color; 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.DataFormatException;

import myTodoList.bin.CalendarEntry;
import myTodoList.bin.EventedList;
import myTodoList.bin.Interval;


/**
 * �����ַ���������
 *
 * @author Frank
 */

public class DataHandler{
	
	/**
	 * Calendar�ļ��ĺ�׺
	 */
	public static final String calendarExtension = "cal";
	
	/**
	 * ���������ַ�
	 * @param str ��Ҫת�����ַ���
	 * @author Frank
	 */
	private static String escape(String str){
		return str.replaceAll("/","//").replaceAll(",","/c").replaceAll("\n","/n");
	}
	/**
	 * ���������ַ�������
	 * @param str ��Ҫ��ת�����ַ���
	 * @author Frank
	 */
	private static String deescape(String str){
		char[] ch = str.toCharArray();
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < ch.length; i++){
			if(ch[i] == '/'){
				i++;
				switch(ch[i]){
					case 'c':sb.append(','); break;
					case 'n':sb.append('\n'); break;  
					case '/':sb.append('/'); break;
				}
			}else {
				sb.append(ch[i]);
			}
		}
		return sb.toString();
	}
	/**
	 * ת���ļ��е��ַ���������һ���ַ���������
	 * @param sampleText ��Ҫ������ַ���
	 * @throws DataFormatException Exception ��ʽ�����׳��쳣
	 * @author Frank
	 */
	private static String[] getFields(String sampleText) throws DataFormatException{
		String separates = ",";
		StringTokenizer st = new StringTokenizer(sampleText, separates);
		
		if(st.countTokens() != 7){    // �������7�����ݣ��׳��쳣
			throw new DataFormatException();
		}
		String[] ret = new String[7];
		for(int i = 0; i < ret.length; i++){
			ret[i] = st.nextToken();
		}
		return ret;
	}
	/**
	 * �Ӹ������ļ��ж������ݣ�ת����Ϊ���Դ����CalendarEntry����
	 * @param fileName �������ļ�����
	 * @param data ���ݵ�����
	 * @throws IOException Exception �ļ��޷�����
	 * @throws DataFormatException ��������ض���ʽ���ļ����׳��쳣
	 * @author Frank
	 */
	public static void readData(String fileName, List<CalendarEntry> data) throws IOException, DataFormatException{
		BufferedReader inPut = null;
		try{
			inPut = new BufferedReader(new FileReader(fileName));
			Interval ival = null;
			String type = null;
			String title = null;
			Color foreColor = null;
			Color backColor = null;
			String desc = null;
			
			String line;
			List<String> typeList = new LinkedList<String>();
			String[] defaultsType = DataModel.getDefaultTypeArray();
			for(int i = 0; i < defaultsType.length; i++){
				typeList.add(defaultsType[i]);       // �ȼ���Ĭ���е��¼���Ŀ
				System.out.println(defaultsType[i]);
			}
			
			while((line = inPut.readLine()) != null){
				String[] fields = getFields(line);
				try{
					long start = Long.parseLong(fields[0]);
					long end = Long.parseLong(fields[1]);
					ival = new Interval(start, end);
					
					type = deescape(fields[2]);
					String typeCopy = type;
					System.out.println(typeCopy);
					for(String str : typeList){
						System.out.println("typeList�еģ�"+str);
						if(str.equals(type)){
							type = str;
							break;
						}
					}
					if(type == typeCopy){ 
						// �����ַ�ı��ˣ�˵��typeList����type��������µ�����
						System.out.println("add?");
						typeList.add(type);    // ��ѡ����¼�����������һ���µ�ѡ��
					}
					
					title = deescape(fields[3]);
					foreColor = new Color(Integer.parseInt(fields[4]));
					backColor = new Color(Integer.parseInt(fields[5]));
//					System.out.println("readData�����е�foreColor��backColor��");
//					System.out.println(foreColor + "" + backColor);
					desc = deescape(fields[6]);
				}catch(NumberFormatException e){
					throw new DataFormatException();
				}
				data.add(new CalendarEntry(ival, type, title, foreColor, backColor, desc));
			}
			DataModel.getTypeList().clear();
			DataModel.getTypeList().addAll(typeList);
		}catch(IOException e){
			throw e;
		}finally{
			if (inPut != null){
				inPut.close();
			}
		}
	}
	/**
	 * �����ض���ģʽ������д��������ļ���
	 * @param fileName �ļ���
	 * @param data �¼�������
	 * @throws IOException Exception �ļ��޷���д���׳��쳣
	 * @author Frank
	 */
	public static void writeData(String fileName,EventedList<CalendarEntry> data) throws IOException{
		
		File f=new File(fileName);
		if(!f.exists()){
			f.createNewFile();
		}
		
		PrintWriter out = new PrintWriter(new FileWriter(fileName));
		
		for(CalendarEntry entry : data){
			Interval ival = entry.getInterval();
			out.print(ival.getStart() + ",");
			out.print(ival.getEnd() + ",");
			out.print(escape(entry.getType()) + ",");
			out.print(escape(entry.getTitle()) + ",");
			out.print((entry.getForegroundColor()).getRGB() + ",");
			out.print((entry.getBackgroundColor()).getRGB() + ",");
			out.print(escape(entry.getDescription()));
			out.println();
		}
		out.close();
	}
}
