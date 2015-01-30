package myTodoList.bin;

import java.sql.Timestamp;

/**
 * ����ʱ����
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
	 * �ж�����Interval�Ƿ��н�������
	 * @author Frank
	 * @param i Interval
	 * @return ����н�����������True
	 */
	public boolean intersect(Interval i){
		return (contains(i.start) || contains(i.end - 1)) || (i.contains(start) || i.contains(end - 1));
	}
	
	/**
	 * @author Frank
	 * @param i Intersection Interval value.
	 * @return ���û�в��뷵��NULL�����򷵻�Interval
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
	 * @return �����ʱ����ڼ���ڣ�����true
	 */
	public boolean contains(long i){
		return (start <= i && i < end);
	}
	
	/**
	 * �ж�һ��Interval�Ƿ����i(Interval)
	 * @author Frank
	 * @param i Container Interval value.
	 * @return 
	 */
	public boolean contains(Interval i){
		return (contains(i.start) && contains(i.end));
	}
	
	/**
	 * ������Interval�ϲ�
	 * @author Frank
	 * @param i Union Interval value.
	 * @return Union of the intervals or null if not possible
	 */
	public Interval union(Interval i){
		if(contains(i)) {
			return clone();   // �����Interval����i�����ظ�Interval
		}
		if(i.contains(this)) {   // ���i������Interval������i
			return i.clone();
		}
		
		if(intersect(i)){    // ��������
			if (start <= i.start){
				return new Interval(start, (end < i.end ? i.end : end));
			}
			return new Interval(i.start,end);
		}
		if(joinable(i)){     // ����������ģ�������
			if (start < i.start) {
				return new Interval(start, i.end);
			}
			return new Interval(i.start, end);
		}
		return null;
	}
	
	/**
	 * @author Frank
	 * @return ����һ��Interval
	 */
	public Interval clone(){
		return new Interval(start, end);
	}
	
	/**
	 * @author Frank
	 * @param i Interval
	 * @return �������ʱ��֮��û�м������������������true
	 */
	public boolean joinable(Interval i){
		return (start == i.end || end == i.start);
	}
	
	/**
	 * @author Frank
	 * @return ��ʼʱ��
	 */
	public long getStart(){
		return start;
	}
	
	/**
	 * @author Frank
	 * @return ����ʱ��
	 */
	public long getEnd(){
		return end;
	}
	
	/**
	 * @author Frank
	 * @return ʱ��ĳ���
	 */
	public long getLength(){
		return end-start;
	}
	
	/**
	 * @author Frank
	 * @return ��ʼ ʱ���
	 */
	public Timestamp getStartTimestamp(){
		return new Timestamp(start);
	}
	
	/**
	 * @author Frank
	 * @return ���� ʱ���
	 */
	public Timestamp getEndTimestamp(){
		return new Timestamp(end);
	}
	/**
	 * @author Frank
	 * @return ����ʱ�������ַ���
	 */
	public String toString(){
		return "[ " + start + " - " + end + " )";
	}
}
