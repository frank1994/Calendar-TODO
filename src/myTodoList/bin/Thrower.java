package myTodoList.bin;

import java.util.*;

/**
 * 异常被抛出时可以追踪到出问题的地方
 * @author Frank
 */
public class Thrower{
	/**
	 * 抛出异常
	 * 
	 * @param e 传统的运行时异常
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
