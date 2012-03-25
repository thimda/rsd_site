package nc.uap.portal.service.itf;

import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.vo.PtVacationVO;
import nc.uap.portal.vo.PtWeekendVO;

/**
 * 工作日查询服务
 * @author licza
 *
 */
public interface IPtWorkDayQryService { 
	
	/**
	 * 获得假期
	 * @return
	 */
	public PtVacationVO[] getHolidays();
	
	/**
	 * 获得特殊工作日
	 * @return
	 */
	public PtVacationVO[] getSpecialWorkDay();
	
	/**
	 * 获得周末
	 * @return
	 */
	public Integer[] getWeekend();
	
	/**
	 * 获得周末的配置信息
	 * @return
	 * @throws PortalServiceException
	 */
	public PtWeekendVO getWeekendProp() throws PortalServiceException;
	
	/**
	 * 初始化缓存
	 */
	public void initCache();
}
