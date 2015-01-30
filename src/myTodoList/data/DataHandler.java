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
 * 处理字符串的数据
 *
 * @author Frank
 */

public class DataHandler{
	
	/**
	 * Calendar文件的后缀
	 */
	public static final String calendarExtension = "cal";
	
	/**
	 * 处理特殊字符
	 * @param str 需要转换的字符串
	 * @author Frank
	 */
	private static String escape(String str){
		return str.replaceAll("/","//").replaceAll(",","/c").replaceAll("\n","/n");
	}
	/**
	 * 处理特殊字符，解码
	 * @param str 需要被转换的字符串
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
	 * 转换文件中的字符串，返回一个字符串的数组
	 * @param sampleText 需要处理的字符串
	 * @throws DataFormatException Exception 格式错误抛出异常
	 * @author Frank
	 */
	private static String[] getFields(String sampleText) throws DataFormatException{
		String separates = ",";
		StringTokenizer st = new StringTokenizer(sampleText, separates);
		
		if(st.countTokens() != 7){    // 如果不是7段数据，抛出异常
			throw new DataFormatException();
		}
		String[] ret = new String[7];
		for(int i = 0; i < ret.length; i++){
			ret[i] = st.nextToken();
		}
		return ret;
	}
	/**
	 * 从给定的文件中读入数据，转换成为可以处理的CalendarEntry链表
	 * @param fileName 给定的文件名称
	 * @param data 数据的链表
	 * @throws IOException Exception 文件无法读入
	 * @throws DataFormatException 如果不是特定格式的文件，抛出异常
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
				typeList.add(defaultsType[i]);       // 先加入默认有的事件条目
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
						System.out.println("typeList中的："+str);
						if(str.equals(type)){
							type = str;
							break;
						}
					}
					if(type == typeCopy){ 
						// 如果地址改变了，说明typeList中有type，不添加新的类型
						System.out.println("add?");
						typeList.add(type);    // 供选择的事件类型里增加一个新的选择
					}
					
					title = deescape(fields[3]);
					foreColor = new Color(Integer.parseInt(fields[4]));
					backColor = new Color(Integer.parseInt(fields[5]));
//					System.out.println("readData方法中的foreColor和backColor：");
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
	 * 按照特定的模式将数据写入给定的文件中
	 * @param fileName 文件名
	 * @param data 事件的链表
	 * @throws IOException Exception 文件无法读写，抛出异常
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
