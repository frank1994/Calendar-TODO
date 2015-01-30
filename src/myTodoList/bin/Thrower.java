package myTodoList.bin;

import java.util.*;

/**
 * �쳣���׳�ʱ����׷�ٵ�������ĵط�
 * @author Frank
 */
public class Thrower{
	/**
	 * �׳��쳣
	 * 
	 * @param e ��ͳ������ʱ�쳣
	 * @author Frank
	 */
	public static void Throw(RuntimeException e){
		e.setStackTrace(getStackTrace());
		throw e;
	}
	
	private static StackTraceElement[] getStackTrace(){
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		return new ArrayList<StackTraceElement>(Arrays.asList(stack)).subList(3,stack.length).toArray(new StackTraceElement[0]);
	}
}
