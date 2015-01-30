package myTodoList.bin;

import java.sql.Timestamp;

/**
 * 处理时间间隔
 * @author Frank
 */
public class Interval{
	private long start;
	private long end;
	
	public Interval(){
		start = 0;
		end = 0;
	}
	
	public Interval(long s, long e){
		start = s;
		end = e;
	}
	
	/**
	 * 判断两个Interval是否有交叉的情况
	 * @author Frank
	 * @param i Interval
	 * @return 如果有交叉的情况返回True
	 */
	public boolean intersect(Interval i){
		return (contains(i.start) || contains(i.end - 1)) || (i.contains(start) || i.contains(end - 1));
	}
	
	/**
	 * @author Frank
	 * @param i Intersection Interval value.
	 * @return 如果没有插入返回NULL，否则返回Interval
	 */
	public Interval intersection(Interval i){
		if (!intersect(i)){
			return null;
		}
		return new Interval(((start > i.start) ? start : i.start), ((end > i.end) ? i.end : end));
	}
	
	/**
	 * @author Frank
	 * @param i Containter value.
	 * @return 如果该时间点在间隔内，返回true
	 */
	public boolean contains(long i){
		return (start <= i && i < end);
	}
	
	/**
	 * 判断一个Interval是否包含i(Interval)
	 * @author Frank
	 * @param i Container Interval value.
	 * @return 
	 */
	public boolean contains(Interval i){
		return (contains(i.start) && contains(i.end));
	}
	
	/**
	 * 把两个Interval合并
	 * @author Frank
	 * @param i Union Interval value.
	 * @return Union of the intervals or null if not possible
	 */
	public Interval union(Interval i){
		if(contains(i)) {
			return clone();   // 如果该Interval包含i，返回该Interval
		}
		if(i.contains(this)) {   // 如果i包含该Interval，返回i
			return i.clone();
		}
		
		if(intersect(i)){    // 交叉的情况
			if (start <= i.start){
				return new Interval(start, (end < i.end ? i.end : end));
			}
			return new Interval(i.start,end);
		}
		if(joinable(i)){     // 如果是相连的，连起来
			if (start < i.start) {
				return new Interval(start, i.end);
			}
			return new Interval(i.start, end);
		}
		return null;
	}
	
	/**
	 * @author Frank
	 * @return 返回一个Interval
	 */
	public Interval clone(){
		return new Interval(start, end);
	}
	
	/**
	 * @author Frank
	 * @param i Interval
	 * @return 如果两个时间之间没有间隔，可以相连，返回true
	 */
	public boolean joinable(Interval i){
		return (start == i.end || end == i.start);
	}
	
	/**
	 * @author Frank
	 * @return 开始时间
	 */
	public long getStart(){
		return start;
	}
	
	/**
	 * @author Frank
	 * @return 结束时间
	 */
	public long getEnd(){
		return end;
	}
	
	/**
	 * @author Frank
	 * @return 时间的长度
	 */
	public long getLength(){
		return end-start;
	}
	
	/**
	 * @author Frank
	 * @return 开始 时间戳
	 */
	public Timestamp getStartTimestamp(){
		return new Timestamp(start);
	}
	
	/**
	 * @author Frank
	 * @return 结束 时间戳
	 */
	public Timestamp getEndTimestamp(){
		return new Timestamp(end);
	}
	/**
	 * @author Frank
	 * @return 返回时间间隔的字符串
	 */
	public String toString(){
		return "[ " + start + " - " + end + " )";
	}
}
