package nc.uap.portal.testcase;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import nc.bs.framework.test.AbstractTestCase;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.comm.WorkDayHelper;
import nc.vo.pub.lang.UFDate;
import nc.vo.uap.distribution.DateUtils;

import org.apache.commons.lang.ArrayUtils;

/**
 * 工作日筛选测试用例
 * 
 * @author licza
 * 
 */
public class WorkDayTestCase extends AbstractTestCase {
	public void testNextDay(){
		UFDate s1 = new UFDate();
		Date d1 = WorkDayHelper.newIns().getNextDate(s1.getMillis(), 10);
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
		assertEquals("11-03-14",sdf.format(d1));
	}
	
	
	
	
	
	
	
	
	
	
	/**
	 * 工作日过滤器链
	 */
	private static final IWorkDayFilterChain[] chains = new IWorkDayFilterChain[] 
    {
		new SpecialWorkDayFilterChain(),
		new HolidayFilterChain(),
		new WeekendFilterChain()
	};
	
	
	
	public void _testFilterWeeken() {
		final int n = 2;
		long c = System.currentTimeMillis();
		ProvingDate pd = new ProvingDate();
		int j = 1;
		while (pd.getIdentifier() <= n) {
			pd.setTimestamp(DateUtils.addDays(c, j));
			rock(pd);
			j++;
		}
		Date td = new Date(pd.getTtimestamp());
		System.out.println(DateUtils.getDate(td));
	}

	/**
	 * 处理过滤链
	 * @param pd
	 */
	public void rock(ProvingDate pd) {
		WorkDayFilterChain chainBase = new WorkDayFilterChain();
		for (int k = 0; k < chains.length; k++) {
			/**
			 * 主动断开责任链
			 */
			if(!chainBase.isChainBreak())
				chains[k].doFilter(pd, chainBase);
		}
	}

	/**
	 * 返回时间
	 * 
	 * @param startDay
	 * @param stopDay
	 * @param t
	 * @return
	 */
	public static boolean among(long startDay, long stopDay, long t) {
		return t >= DateUtils.startOfDayInMillis(startDay) && t <= DateUtils.endOfDayInMillis(stopDay);
	}

	/**
	 * 得一组测试数据
	 * 
	 * @return
	 */
	public List<long[]> mockDate() {
		try {
			parseDate("", new String[] { "yyyy-MM-dd" });
		} catch (ParseException e) {
			LfwLogger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 根据多条规则解析日期字符串
	 * 
	 * @param str
	 * @param parsePatterns
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String str, String[] parsePatterns)
			throws ParseException {
		if (str == null || parsePatterns == null) {
			throw new IllegalArgumentException("日期解析规则为空!");
		}
		SimpleDateFormat parser = null;
		ParsePosition pos = new ParsePosition(0);
		for (int i = 0; i < parsePatterns.length; i++) {
			if (i == 0) {
				parser = new SimpleDateFormat(parsePatterns[0]);
			} else {
				parser.applyPattern(parsePatterns[i]);
			}
			pos.setIndex(0);
			Date date = parser.parse(str, pos);
			if (date != null && pos.getIndex() == str.length()) {
				return date;
			}
		}
		throw new ParseException("Unable to parse the date: " + str, -1);
	}
}

/**
 * 要检验的时间
 * 
 * @author licza
 * 
 */
class ProvingDate {
	/**
	 * 当前步进数
	 */
	private int identifier = 1;
	/**
	 * 日期时间戳
	 */
	private Long timestamp;

	public int getIdentifier() {
		return identifier;
	}

	public void setIdentifier(int i) {
		this.identifier = i;
	}

	public Long getTtimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long t) {
		this.timestamp = t;
	}
}

/**
 * 工作日过滤链接口
 * 
 * @author licza
 * 
 */
interface IWorkDayFilterChain {
	/**
	 * 日期是否符合规则
	 * 
	 * @param t
	 *            传入的日期
	 * @param i
	 *            期限
	 * @param filterChain
	 *            下一个链
	 */
	public void doFilter(ProvingDate pd, WorkDayFilterChain filterChain);
}

/**
 * 工作日过滤器
 * 
 * @author licza
 * 
 */
class WorkDayFilterChain {
	/** 链是否断开 **/
	private boolean breakChain = false;
	public void doFilter(ProvingDate pd) {
		pd.setIdentifier(pd.getIdentifier() + 1);
		breakChain();
	}
	/**
	 * 返回链是否断开
	 * @return
	 */
	public boolean isChainBreak(){
		return breakChain;
	}
	/**
	 * 断开链 
	 **/
	public void breakChain(){
		breakChain = true;
	}
}

/**
 * 周末过滤器
 * 
 * @author licza
 * 
 */
class WeekendFilterChain implements IWorkDayFilterChain {

	@Override
	public void doFilter(ProvingDate pd, WorkDayFilterChain filterChain) {
		int[] weekend = new int[] { 1, 7 };
		int wk = DateUtils.getDayOfWeek(pd.getTtimestamp());
		if (!ArrayUtils.contains(weekend, wk))
			filterChain.doFilter(pd);
	}
}

/**
 * 特殊工作日过滤器
 * 
 * @author licza
 * 
 */
class SpecialWorkDayFilterChain implements IWorkDayFilterChain {
	@Override
	public void doFilter(ProvingDate pd, WorkDayFilterChain filterChain) {
		try {
			long startDay = WorkDayTestCase.parseDate("2011-02-27", new String[] { "yyyy-MM-dd" }).getTime();
			long stopDay = WorkDayTestCase.parseDate("2011-02-27", new String[] { "yyyy-MM-dd" }).getTime();
			boolean b = WorkDayTestCase.among(startDay, stopDay, pd.getTtimestamp());
			if(b)
				filterChain.doFilter(pd);
		} catch (Exception e) {
			
		}
	}
}

/**
 * 假期过滤器
 * 
 * @author licza
 * 
 */
class HolidayFilterChain implements IWorkDayFilterChain {
	@Override
	public void doFilter(ProvingDate pd, WorkDayFilterChain filterChain) {
		try {
			long startDay = WorkDayTestCase.parseDate("2011-02-28", new String[] { "yyyy-MM-dd" }).getTime();
			long stopDay = WorkDayTestCase.parseDate("2011-03-11", new String[] { "yyyy-MM-dd" }).getTime();
			boolean b = WorkDayTestCase.among(startDay, stopDay, pd.getTtimestamp());
			/**
			 * 如果是假期 主动结束链
			 */
			if(b)
				filterChain.breakChain();
		} catch (Exception e) {
			
		}
	}
}
