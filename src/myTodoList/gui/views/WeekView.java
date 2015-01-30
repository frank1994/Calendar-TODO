package myTodoList.gui.views;


import java.awt.Color;  
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import myTodoList.bin.CalendarEntry;
import myTodoList.bin.CalendarSelectionChangedListener;
import myTodoList.bin.EventedList;
import myTodoList.bin.EventedListAdapter;
import myTodoList.bin.Interval;
import myTodoList.bin.Thrower;


/**
 * 展示添加到week的CalendarEntry的组件
 * @author Frank
 */
public class WeekView extends CalendarView{
	private static final long serialVersionUID = 1L;


	/**
	 * 构造函数
	 * @param e 有着原本条目链表的EventedList
	 * @author Frank
	 */
	public WeekView(EventedList<CalendarEntry> elist){
		super();
		if (elist == null){
			Thrower.Throw(new NullPointerException("参数不能为null"));
		}
		
		groups = new ArrayList<IntersectionGroup>();
		gfxEntries = new ArrayList<EntryGFX>();
		selected = new ArrayList<EntryGFX>();
		
		setCalendarEntries(elist);
		
		elist.addItemAddedListener(new EventedListAdapter<CalendarEntry>(){
			public void itemModified(CalendarEntry e){
				addCalendarEntry(e);
				repaint();
			}
		});
		
		elist.addItemRemovedListener(new EventedListAdapter<CalendarEntry>(){
			public void itemModified(CalendarEntry e){
				removeCalendarEntry(e);
				if (selected.size() != 0 && selected.get(0).entry == e){
					selected.clear();
					dispatchSelectionChanged(null);
				}
				repaint();
			}
		});
		
		elist.addListResetListener(new EventedListAdapter<CalendarEntry>(){
			public void listModified(EventedList<CalendarEntry> newVersion){
				setCalendarEntries(newVersion);
				setSelected(null);
				dispatchSelectionChanged(null);
				repaint();
			}
		});
		
		finalVerticalGap = 0;
		finalHorizontalGap = 0;
		
		setBackground(new Color(87, 255, 231));    // 背景颜色

		entryFont = new Font("微软雅黑", Font.BOLD,13);
		hourFont = new Font("微软雅黑", Font.PLAIN,13);
		dayFont = new Font("微软雅黑", Font.BOLD,16);
		
		rowColors = new Color[]{new Color(255, 236, 85), new Color(147, 232, 135)};  // 黄， 绿
		rowLightColors = new Color[]{new Color(255, 245, 134), new Color(185, 245, 177)};
		selectionColor = new Color(56,179,179,128);
		currentTimeColor = new Color(255, 141, 132);    // 当前时间的小三角形的颜色
		lineColor = new Color(100,100,100);    // 绘制日历的线条的颜色
		
		//  设置环境
		
		setPreferredSize(new Dimension(getViewWidth(),getViewHeight()));
		
		// 当有选择的时候调用repaint重绘
		addSelectionChangedListener(new CalendarSelectionChangedListener(){
			public void selectionChanged(CalendarEntry e){
				repaint();
			}
		});
		
		// 每分钟重画一次, 确定时间标志的位置
		timer = new javax.swing.Timer((int)(HOUR_MILLIS / 60), new ActionListener(){
			public void actionPerformed(ActionEvent event){
				repaint();
			}
		});
		timer.start();
	}
	
	//    IntersectionGroup   
	/**
	 * 处理条目绘制信息的内部类
	 * @author Frank
	 */
	class IntersectionGroup{
		public IntersectionGroup(CalendarEntry e){
			entries = new TreeSet<CalendarEntry>();
			rects = new ArrayList<EntryGFX>();
			entries.add(e);
			
			ival = e.getInterval();
			
			layers = new ArrayList<Set<CalendarEntry>>();
			layers.add(new TreeSet<CalendarEntry>());
			layers.get(0).add(e);
			
			changePerformed = true;
		}
		
		private Interval ival;         						// 组中的事件
		private Set<CalendarEntry> entries;					// 唯一的条目链表
		private java.util.List<Set<CalendarEntry>> layers;	// layers 放置好的条目
		private java.util.List<EntryGFX> rects;				// 产生条目需要的元素
		private boolean changePerformed;    //  组件需要刷新
		
		/**
		 * @author Frank
		 * @return Groups full interval.
		 */
		public Interval getInterval(){
			return ival;
		}
				
		/**
		 * @author Frank
		 * @return 分组的事件
		 */
		public Set<CalendarEntry> getEntries(){
			return entries;
		}
		
		/**
		 * 合并两个group
		 * 
		 * @author Frank
		 * @return True if changes performed correctly
		 */
		public boolean merge(IntersectionGroup g){
			Interval tmp = ival.union(g.ival);
			if(tmp == null) {
				return false;
			}
			
			boolean change = entries.addAll(g.entries);
			if(change){
				ival = ival.union(g.ival);
				for(CalendarEntry e : g.entries){
					addToLayers(e);
				}
			}
			return change;
		}
		
		/**
		 * 增加一个事件到group
		 * 
		 * @author Frank
		 * @return True if changes performed correctly
		 */
		public boolean addEntry(CalendarEntry e){
			Interval tmp = ival.union(e.getInterval());
			if (tmp == null) {
				return false;
			}
			
			boolean change = entries.add(e);
			if (change){
				ival = tmp;
				addToLayers(e);
			}
			return change;
		}
		
		/**
		 * 从group中移出一个事件
		 * 
		 * @author Frank
		 * @return Intersection group(s)  
		 */
		public java.util.List<IntersectionGroup> removeEntry(CalendarEntry e){
			java.util.List<IntersectionGroup> ret = new ArrayList<IntersectionGroup>();
			boolean change = entries.remove(e);
			if (change){
				removeFromLayers(e);
				Iterator<CalendarEntry> it = entries.iterator();
				for (CalendarEntry entr : entries){
					addToGroups(ret,new IntersectionGroup(entr));
				}
			}
			if (ret.size() == 0) return null;
			return ret;
		}
		
		/**
		 * 返回最近更新的graphics.
		 * 
		 * @author Frank
		 * @return 可绘制的长方形的Array
		 */
		public EntryGFX[] getRectangles(){
			if (changePerformed) {
				gfx();
			}
			return rects.toArray(new EntryGFX[0]);
		}
		
		/**
		 * 增加条目
		 * @author Frank
		 * @return True 如果成功返回true
		 */
		private boolean addToLayers(CalendarEntry e){
			sets:
			for(Set<CalendarEntry> set : layers){
				for(CalendarEntry entry : set){
					if(e.getInterval().intersect(entry.getInterval())) {
						continue sets;
					}
				}
				set.add(e);
				changePerformed = true;
				return true;
			}
			Set<CalendarEntry> ret = new TreeSet<CalendarEntry>();
			ret.add(e);
			layers.add(ret);
			changePerformed = true;
			return true;
		}
		
		/**
		 * 从layers中移出一个事件
		 * @author Frank
		 * @return True 成功返回true
		 */
		@SuppressWarnings("unused")
		private boolean removeFromLayers(CalendarEntry e){
			sets:
			for (Set<CalendarEntry> set : layers){
				if (set.remove(e)){
					if (set.size() == 0) layers.remove(set);
					changePerformed = true;
					return true;
				}
			}
			return false;
		}
		
		/**
		 * Calculates graphics.
		 * 
		 * @author Frank
		 */
		private void gfx(){
			changePerformed = false;
			rects.clear();
			
			if (layers.size() == 0) return;
			int w = xstep / layers.size();
			int wcorr = xstep % layers.size();
			
			Calendar c = Calendar.getInstance();
			
			int layer = 0;
			for (Set<CalendarEntry> set : layers){
				for (CalendarEntry e : set){
					Interval o = e.getInterval();
					Interval i = o.clone();
					
					c.setTime(i.getStartTimestamp());
					int start = c.get(Calendar.DAY_OF_YEAR);
					int startYear = c.get(Calendar.YEAR);
					c.setTime(i.getEndTimestamp());
					int end = c.get(Calendar.DAY_OF_YEAR);
					int endYear = c.get(Calendar.YEAR);
					
					//first fragment of shattered
					if (start != end || startYear != endYear){
						c.setTime(i.getStartTimestamp());
						int x = firstVerticalLine + dayOfWeek(i.getStart()) * xstep + layer * w;
						int y = firstHorizontalLine + (int)((c.get(Calendar.HOUR_OF_DAY) * HOUR_MILLIS + 
								c.get(Calendar.MINUTE) * HOUR_MILLIS / 60) * (double)(ystep / 60.0) / 60000.0);
						int h = firstHorizontalLine + ystep * 24 - y;
						
						
						c.add(Calendar.DAY_OF_YEAR, 1);
						c.set(Calendar.AM_PM, Calendar.AM);
						c.set(Calendar.HOUR, 0);
						c.set(Calendar.MINUTE, 0);
						c.set(Calendar.SECOND, 0);
						c.set(Calendar.MILLISECOND, 0);
						
						rects.add(new EntryGFX(e, new Interval(i.getStart(), c.getTimeInMillis()), x + 
								((layer < wcorr) ? layer : wcorr), y, w + ((layer < wcorr) ? 1 : 0), h));
						
						i = new Interval(c.getTimeInMillis(),i.getEnd());
						start = c.get(Calendar.DAY_OF_YEAR);
						startYear = c.get(Calendar.YEAR);
						
					}
					
					//full days
					while(start != end || startYear != endYear){
						
						c.setTime(i.getStartTimestamp());
						int x = firstVerticalLine + dayOfWeek(i.getStart()) * xstep + layer * w;
						int y = firstHorizontalLine + (int)((c.get(Calendar.HOUR_OF_DAY) * HOUR_MILLIS + 
								c.get(Calendar.MINUTE) * HOUR_MILLIS / 60) * (double)(ystep / 60.0) / 60000.0);
						int h = ystep * 24;
						
						c.add(Calendar.DAY_OF_YEAR, 1);
						c.set(Calendar.AM_PM, Calendar.AM);
						c.set(Calendar.HOUR, 0);
						c.set(Calendar.MINUTE, 0);
						c.set(Calendar.SECOND, 0);
						c.set(Calendar.MILLISECOND, 0);
						
						rects.add(new EntryGFX(e, new Interval(i.getStart(), c.getTimeInMillis()),x + ((layer < wcorr) ? layer : wcorr),y,w + ((layer < wcorr) ? 1 : 0),h));
						
						i = new Interval(c.getTimeInMillis(),i.getEnd());
						start = c.get(Calendar.DAY_OF_YEAR);
						startYear = c.get(Calendar.YEAR);
						
					}
					
					//last fragment or full if not shattered
					if (i.getLength() != 0){
						c.setTime(i.getStartTimestamp());
						int x = firstVerticalLine + dayOfWeek(i.getStart()) * xstep + layer * w;
						int y = firstHorizontalLine + (int)((c.get(Calendar.HOUR_OF_DAY) * HOUR_MILLIS + 
								c.get(Calendar.MINUTE) * HOUR_MILLIS / 60) * (double)(ystep / 60.0) / 60000.0);
						int h = (int)((i.getLength() * ystep) / (double)(HOUR_MILLIS));
						
						rects.add(new EntryGFX(e, i, x + ((layer < wcorr) ? layer : wcorr), y, 
								w + ((layer < wcorr) ? 1 : 0),h));
					}
				}
				
				layer++;
			}
		}
	}

	/**
	 * 存放绘出事件信息的内部类
	 * @author Frank
	 */
	class EntryGFX{
		CalendarEntry entry;
		Interval time;
		int x, y, height, width;
		
		public EntryGFX(CalendarEntry entry, Interval time, int x, int y, int width, int height){
			this.entry = entry;
			this.time = time;
			this.x = x;
			this.y = y;
			this.height = height;
			this.width = width;
		}
		/**
		 * @author Frank
		 * @return 如果给定的点在条目的长方形范围内，返回True
		 */
		public boolean intersect(int x, int y){
			return (this.x <= x && x <= this.x + width && this.y <= y && y <= this.y + height);
		}
	}
	
	
	
	private static int firstVerticalLine = 56;
	private static int firstHorizontalLine = 48;
	private static int finalVerticalGap = 0;  //gap between final vertical line and width
	private static int finalHorizontalGap = 0;//gap between final horizontal line and height
	private int xstep;
	private int ystep;
	private int finalVerticalLine;
	private int finalHorizontalLine;
	private java.util.List<EntryGFX> gfxEntries;
	private java.util.List<EntryGFX> selected;
	private java.util.List<IntersectionGroup> groups;
	
	private Font hourFont;
	private Font dayFont;
	private Font entryFont;
	private FontMetrics entryMetrics;
	private FontMetrics hourMetrics;
	private FontMetrics dayMetrics;
		
	private Color[] rowColors;
	private Color[] rowLightColors;
	private Color selectionColor;
	private Color currentTimeColor;
	private Color lineColor;
	
	private javax.swing.Timer timer;
		
	public static final String[] days = new String[]{"星期一", "星期二", "星期三",
		"星期四", "星期五","星期六","星期天"};
	public static final long HOUR_MILLIS = (long)3600000;
	public static final long WEEK_MILLIS = 7*24*HOUR_MILLIS;
	
	
	// Public方法
	/**
	 * @author Frank
	 * @return views的最佳宽度
	 */
	public static int getViewWidth(){
		return firstVerticalLine + 7 * 130 + finalVerticalGap;
	}
	
	/**
	 * @author Frank
	 * @return views的最佳高度
	 */
	public static int getViewHeight(){
		return firstHorizontalLine + 24 * 30 + finalHorizontalGap;
	}
	
	/**
	 * 跳到下一个间隔
	 * @author Frank
	 */
	public void nextInterval(){
		toInterval(new Date(viewInterval.getEnd() + 1));
	}
	
	/**
	 * 跳到前一个间隔
	 * @author Frank
	 */
	public void prevInterval(){
		if(viewInterval.getStart() < WEEK_MILLIS){
			return;
		}
		toInterval(new Date(viewInterval.getStart() - HOUR_MILLIS * 24 - 1));
	}
	
	/**
	 * 产生视图
	 * @author Frank
	 */
	public void paintComponent(Graphics g){
		abstractPaintComponent(g);
		// 设置环境
		
		int width = getWidth();  	// panel的宽度
		int height = getHeight();	// panel的高度
		
		xstep = (width - firstVerticalLine - finalVerticalGap) / 7;
		ystep = (height - firstHorizontalLine - finalHorizontalGap) / 24;
		
		entryMetrics = g.getFontMetrics(entryFont);
		hourMetrics = g.getFontMetrics(hourFont);
		dayMetrics = g.getFontMetrics(dayFont);
		
		finalVerticalLine = firstVerticalLine + xstep * 7;
		finalHorizontalLine = firstHorizontalLine + ystep * 24;
		
		int rowColor = 0;	// 确定深色和浅色的区别
		g.setFont(dayFont);
		g.setColor(lineColor);
		
		//    绘制静态的信息（星期，小时）
		
		for(int i = 0, x = firstVerticalLine + xstep / 2; i < days.length; i++, x += xstep){
			g.drawString(days[i], x - dayMetrics.stringWidth(days[i]) / 2,
					(firstHorizontalLine + dayMetrics.getHeight()) / 2);
		}
		
		g.setFont(hourFont);
		
		for(int y = firstHorizontalLine, i = 0; y <= getHeight(); y += ystep, i++){
			if (i < 24){
				g.drawString(createHour(i), firstVerticalLine / 2 - hourMetrics.stringWidth(createHour(i)) / 2,
						(int)(y + ystep / 2.0 + (hourMetrics.getHeight() - 3) / 2.0));
				g.setColor(rowColors[rowColor]);
				
				g.fillRect(firstVerticalLine, y, finalVerticalLine - firstVerticalLine, ystep);
				
				g.setColor(rowLightColors[rowColor]);
				for (int j = firstVerticalLine + xstep;j + xstep < width;j+=xstep*2){
					g.fillRect(j,y,xstep,ystep);
				}
				
				rowColor++;
				if (rowColor == rowColors.length){
					rowColor = 0;
				}
				g.setColor(lineColor);
			}
			g.drawLine(0, y, finalVerticalLine, y);
		}
		
		for (int x = firstVerticalLine;x <= getWidth();x+=xstep){
			g.drawLine(x,0,x,finalHorizontalLine);
		}
		
		g.drawString("小时", 6, firstHorizontalLine - 3);
		g.drawString("星期", firstVerticalLine - hourMetrics.stringWidth("Day") - 3, hourMetrics.getHeight() + 3);
		
		g.drawLine(0,0,firstVerticalLine,firstHorizontalLine);
		
		// 绘制事件
		gfxEntries.clear();
		g.setFont(entryFont);
		for(IntersectionGroup group : groups){
			EntryGFX[] rects = group.getRectangles();
			for(int i = 0; i < rects.length; i++){
				if (!rects[i].time.intersect(viewInterval)){
					continue;
				}
				gfxEntries.add(rects[i]);
				g.setColor(rects[i].entry.getBackgroundColor());
				g.fillRect(rects[i].x, rects[i].y, rects[i].width, rects[i].height);
				g.setColor(Color.BLACK);
				g.drawRect(rects[i].x, rects[i].y, rects[i].width, rects[i].height);
				
				int mheight = entryMetrics.getHeight();
				
				String[] str = breakToLines(rects[i].entry.getTitle(), entryMetrics,rects[i].width - 6, 
						rects[i].height / (mheight + 6));
				g.setColor(rects[i].entry.getForegroundColor());
				for (int j = 0;j < str.length;j++){
					g.drawString(str[j], rects[i].x + 3, 
							rects[i].y + mheight / 2 + (j == 0 ? 6 : 0) + j * (mheight + 6));
				}
			}
		}
		
		
		//    绘制选择的事件的边框
		if(selected.size() != 0 && selected.get(0).entry.getInterval().intersect(viewInterval)){
			int i = -1, size = selected.size();
			for(EntryGFX entry : selected){
				i++;
				if (!viewInterval.intersect(entry.time)){
					continue;
				}
				
				g.setColor(selectionColor);
				g.fillRect(entry.x, entry.y, entry.width, entry.height);
				g.setColor(new Color(selectionColor.getRGB()));
				if(i == 0){
					g.fillRoundRect(entry.x, entry.y, entry.width, 3, 3, 3);	//above
				}
				if(i == size - 1) {
					g.fillRoundRect(entry.x, entry.y + entry.height - 3 + 1, entry.width, 3, 3, 3);//below
				}
				g.fillRoundRect(entry.x, entry.y, 3, entry.height, 3, 3);     //left
				g.fillRoundRect(entry.x + entry.width - 3 + 1, entry.y, 3, entry.height, 3, 3);//right
			}
		}
		
		// 绘制当前时间的标志
		long currentTime = System.currentTimeMillis();
		if(viewInterval.contains(currentTime)){
			g.setColor(currentTimeColor);
			Calendar c = Calendar.getInstance();
			c.setTime(new Date(currentTime));
			int x = firstVerticalLine + dayOfWeek(currentTime) * xstep;
			int y = firstHorizontalLine + (int)((c.get(Calendar.HOUR_OF_DAY) * HOUR_MILLIS + 
					c.get(Calendar.MINUTE) * HOUR_MILLIS / 60) * (double)(ystep / 60.0) / 60000.0);
			
			y -= 4;
			
			// 在当天
			g.fillPolygon(
				new int[]{x, x, x+2, x+8, x+2},
				new int[]{y, y+8, y+8, y+4, y},
				5
				);
			x += xstep;
			g.fillPolygon(
				new int[]{x, x, x-2, x-8, x-2},
				new int[]{y, y+8, y+8, y+4, y},
				5
				);
			
			// 在y轴边上
			x = 0;
			g.fillPolygon(
				new int[]{x,x,x+2,x+8,x+2},
				new int[]{y,y+8,y+8,y+4,y},
				5
				);
			x = firstVerticalLine;
			g.fillPolygon(
				new int[]{x,x,x-2,x-8,x-2},
				new int[]{y,y+8,y+8,y+4,y},
				5
				);
			
		}
	}
	
	/**
	 * 查看指定的时间
	 * @author Frank
	 */
	public void toInterval(Date week){
		if(week == null){
			Thrower.Throw(new NullPointerException("Error: Argument can't be null"));
		}
		Calendar c = Calendar.getInstance();
		c.setTime(week);
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		c.set(Calendar.AM_PM, Calendar.AM);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		long start = c.getTimeInMillis();
		
		c.add(Calendar.DAY_OF_YEAR, 7);
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		c.set(Calendar.AM_PM, Calendar.AM);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		viewInterval = new Interval(start, c.getTimeInMillis());
		repaint();
	}
	
	
	/**
	 * Adds group to group list.
	 */
	private boolean addToGroups(java.util.List<IntersectionGroup> glist, IntersectionGroup g){
		
		boolean changePerformed = true;
		while(changePerformed){
			changePerformed = false;
			
			for(Iterator<IntersectionGroup> it = glist.iterator();it.hasNext();){
				IntersectionGroup igrp = it.next();
				if (igrp == g) {
					continue;
				}
				
				if (g.getInterval().intersect(igrp.getInterval())){
					g.merge(igrp);
					it.remove();
					changePerformed = true;
				}
			}
		}
		glist.add(g);
		return true;
	}
	
	protected void clickPerformed(MouseEvent e){
		if(selected.size() != 0){// 有选定的事件
			for(EntryGFX g : gfxEntries){
				if(g.intersect(e.getX(),e.getY())){
					for(EntryGFX entry : selected){
						if(entry.entry == g.entry){
							return;
						}
					}
					setSelected(g.entry);
					dispatchSelectionChanged(g.entry);
					return;
				}
			}
			selected.clear();
			dispatchSelectionChanged(null);
		}else{// 没有选定的事件
			for(EntryGFX g : gfxEntries){
				if(g.intersect(e.getX(), e.getY())){
					setSelected(g.entry);
					dispatchSelectionChanged(g.entry);
					return;
				}
			}
		}
	}
	
	/**
	 * Selects the given entry, don't repaint view but dispatches event if necessery.
	 * @return True if selection made.
	 */
	public boolean setSelected(CalendarEntry e){
		boolean added = false;
		CalendarEntry prevSelected = null;
		if (selected.size() != 0) {
			prevSelected = selected.get(0).entry;
		}
		
		selected.clear();
		if (e != null){
			for (IntersectionGroup group : groups){
				EntryGFX[] entries = group.getRectangles();
				for (int i = 0;i < entries.length;i++){
					if (entries[i].entry == e){
						added = true;
						selected.add(entries[i]);
					}
				}
				if (added){
					if (prevSelected != selected.get(0).entry){
						dispatchSelectionChanged(selected.get(0).entry);
					}
					return true;
				}
			}
		}
		if (prevSelected != null){
			dispatchSelectionChanged(null);
		}
		return false;
	}
	
	/**
	 * Sets the calendar entries list.
	 * 设置日历事件链表
	 */
	private boolean setCalendarEntries(EventedList<CalendarEntry> elist){
		groups.clear();
		
		for(CalendarEntry e : elist){
			addToGroups(groups, new IntersectionGroup(e));
		}
		return true;
	}
	
	/**
	 * 增加一个新事件到链表中
	 */
	private boolean addCalendarEntry(CalendarEntry e){
		return addToGroups(groups, new IntersectionGroup(e));
	}
	
	/**
	 * 从链表中移出一个事件
	 */
	private boolean removeCalendarEntry(CalendarEntry e){
		for(IntersectionGroup g : groups){
			java.util.List<IntersectionGroup> grps = g.removeEntry(e);
			if (grps != null){
				groups.remove(g);
				groups.addAll(grps);
				if(selected.size() != 0 && e == selected.get(0).entry){
					selected.clear();
					dispatchSelectionChanged(null);
				}
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 根据字符串得到宽度，用于绘制文本
	 */
	private static String[] breakToLines(String str, FontMetrics fm, int maxWidth, int maxLines){
		java.util.List<String> ret = new ArrayList<String>();
		
		int start = 0, end, line = 0;
		while(start != str.length() && line < maxLines){
			end = str.length();
			while(fm.stringWidth(str.substring(start,end)) > maxWidth){
				end--;
			}
			
			ret.add(str.substring(start,end));
			line++;
			start = end;
		}
		
		if(0 == ret.size()) {
			return new String[0];
		}
		
		int size = ret.get(ret.size() - 1).length();
		if (start != str.length() && size >= 3) {
			ret.set(ret.size() - 1,ret.get(ret.size() - 1).substring(0,size - 3) + "...");
		}
		
		return ret.toArray(new String[0]);
	}
	
	/**
	 * 产生合适大小的，展示小时字符串，用于绘制Y轴
	 */
	private static String createHour(int i){
		if (i < 0 || i >= 100) return "??:??";
		if (i < 10) return ("0" + i + ":00");
		return (i + ":00");
	}
	public static void main(String args[]) {
		WeekView test = new WeekView(null);
		test.show();
	}
}
