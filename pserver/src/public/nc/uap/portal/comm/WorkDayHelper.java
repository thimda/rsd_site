package nc.uap.portal.comm;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.util.PtUtil;
import nc.uap.portal.vo.PtVacationVO;
import nc.vo.uap.distribution.DateUtils;

import org.apache.commons.lang.ArrayUtils;

/**
 * 工作日辅助类
 * 
 * @author licza
 * 
 */
public class WorkDayHelper {
	/**
	 * 获得指定日期之后的指定个工作日
	 * 
	 * @param start
	 *            开始日期
	 * @param howLong
	 *            工作日数量
	 * @return
	 */
	public Date getNextDate(long startTimeSpan, int howLong) {
		ProvingDate pd = new ProvingDate();
		int j = 1;
		while (pd.getIdentifier() <= howLong) {
			pd.setTimestamp(DateUtils.addDays(startTimeSpan, j));
			rock(pd);
			j++;
		}
		return new Date(pd.getTtimestamp());
	}

	/**
	 * 获得工作时间差
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public long getWorkTimeDifference(long start, long end) {
		long wt = end - start;
		if (wt < 0)
			throw new IllegalArgumentException("开始时间必须小于结束时间!");
		/**
		 * 剪去周末
		 */
		Integer[] weekend = PortalServiceUtil.getWorkDayQryService()
				.getWeekend();
		long t = 0L;
		int k = 1;
		boolean isEndDay = false;
		while (!isEndDay) {
			t = DateUtils.addDays(start, k);
			Integer dow = DateUtils.getDayOfWeek(t);
			if (ArrayUtils.contains(weekend, dow))
				wt = wt - DAY_MMS;
			isEndDay = isSameDay(t, end);
		}
		/**
		 * 剪假期
		 */

		try {
			PtVacationVO[] vos = PortalServiceUtil.getWorkDayQryService().getHolidays();
			if (!PtUtil.isNull(vos)) {
				for (PtVacationVO vo : vos) {
					long s2 = vo.getStartday().getMillis();
					long e2 = vo.getEndday().getMillis();
					wt = wt - getMixTime(start, end, s2, e2);
				}
			}
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
		}
		/**
		 * 增加特殊工作日
		 */
		try {
			PtVacationVO[] vos = PortalServiceUtil.getWorkDayQryService().getSpecialWorkDay();
			if (!PtUtil.isNull(vos)) {
				for (PtVacationVO vo : vos) {
					long s2 = vo.getStartday().getMillis();
					long e2 = vo.getEndday().getMillis();
					wt = wt + getMixTime(start, end, s2, e2);
				}
			}
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
		}
		return 0L;
	}
	
	/**
	 * 一天的毫秒数
	 */
	private static final long DAY_MMS = 60*60*24L;

	/**
	 * 求两个时间段交集
	 * 
	 * @param s1
	 *            时间段1开始时间
	 * @param e1
	 *            时间段1结束时间
	 * @param s2
	 *            时间段2开始时间
	 * @param e2
	 *            时间段2结束时间
	 * @return
	 */
	public static long getMixTime(long s1, long e1, long s2, long e2) {
		/**
		 * 传入参数校验
		 */
		if (s1 > e1 || s2 > e2)
			throw new IllegalArgumentException("开始时间必须小于结束时间!");

		/**
		 * 判断是否有交集
		 */
		if (s2 > e1 || e2 < s1)
			return 0L;
		/**
		 * 2∈1
		 */
		if (s1 > s2 && e1 > e2)
			return e2 - s2;
		/**
		 * 1∈2
		 */
		if (s2 > s1 && e2 > e1)
			return e1 - s1;

		if (s2 < e1)
			return e1 - s2;

		if (s1 < e2)
			return e2 - s1;
		return 0L;
	}

	private static boolean isSameDay(long d1, long d2) {
		Date date1 = new Date(d1);
		Date date2 = new Date(d2);
		return org.apache.commons.lang.time.DateUtils.isSameDay(date1, date2);
	}

	/**
	 * 单例
	 */
	private static WorkDayHelper wdh = null;

	/**
	 * 初始化一个工作日辅助类
	 * 
	 * @return
	 */
	public static WorkDayHelper newIns() {
		if (wdh == null)
			wdh = new WorkDayHelper();
		return wdh;
	}

	/**
	 * 匿名构造函数
	 */
	private WorkDayHelper() {
		PortalServiceUtil.getWorkDayQryService().initCache();
	}

	/**
	 * 工作日过滤器链
	 */
	private static final IWorkDayFilterChain[] chains = new IWorkDayFilterChain[] {
			new SpecialWorkDayFilterChain(), new HolidayFilterChain(),
			new WeekendFilterChain() };

	/**
	 * 处理过滤链
	 * 
	 * @param pd
	 */
	private void rock(ProvingDate pd) {
		WorkDayFilterChain chainBase = new WorkDayFilterChain();
		for (int k = 0; k < chains.length; k++) {
			/**
			 * 主动断开责任链
			 */
			if (!chainBase.isChainBreak())
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
		// return t >= DateUtils.startOfDayInMillis(startDay) && t <=
		// DateUtils.endOfDayInMillis(stopDay);
		// 使用绝对时间比较
		return t >= startDay && t <= stopDay;
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
		fire();
	}

	/**
	 * 返回链是否断开
	 * 
	 * @return
	 */
	public boolean isChainBreak() {
		return breakChain;
	}

	/**
	 * 断开链
	 **/
	public void fire() {
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
		Integer[] weekend = PortalServiceUtil.getWorkDayQryService()
				.getWeekend();
		int wk = DateUtils.getDayOfWeek(pd.getTtimestamp());
		if (!ArrayUtils.contains(weekend, new Integer(wk)))
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
			PtVacationVO[] vos = PortalServiceUtil.getWorkDayQryService()
					.getSpecialWorkDay();
			if (!PtUtil.isNull(vos)) {
				for (PtVacationVO vo : vos) {
					long start = vo.getStartday().getMillis();
					long stop = vo.getEndday().getMillis();
					boolean b = WorkDayHelper.among(start, stop, pd
							.getTtimestamp());
					if (b)
						filterChain.doFilter(pd);
					return;
				}
			}
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
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
			PtVacationVO[] vos = PortalServiceUtil.getWorkDayQryService()
					.getHolidays();
			if (!PtUtil.isNull(vos)) {
				for (PtVacationVO vo : vos) {
					long start = vo.getStartday().getMillis();
					long stop = vo.getEndday().getMillis();
					boolean b = WorkDayHelper.among(start, stop, pd
							.getTtimestamp());
					/**
					 * 如果是假期 终止链
					 */
					if (b)
						filterChain.fire();
					return;
				}
			}
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
		}
	}

}
