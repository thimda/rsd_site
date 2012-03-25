package nc.uap.portal.service.impl;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cache.ILfwCache;
import nc.uap.lfw.core.cache.LfwCacheManager;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.constant.CacheKeys;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.persist.dao.PtBaseDAO;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.service.itf.IPtWorkDayQryService;
import nc.uap.portal.service.itf.IPtWorkDayService;
import nc.uap.portal.util.PtUtil;
import nc.uap.portal.vo.PtVacationVO;
import nc.uap.portal.vo.PtWeekendVO;

/**
 * 工作日查询服务实现
 * @author licza
 *
 */
public class PtWorkDayQryServiceImpl implements IPtWorkDayQryService {
	 
	@SuppressWarnings("unchecked")
	public PtVacationVO[] _getHolidays() {
		PtVacationVO[] vs = new PtVacationVO[0];
		PtBaseDAO d = new PtBaseDAO();
		try {
			List l = (List)d.retrieveByClause(PtVacationVO.class, " type = 0");
			if(!PtUtil.isNull(l)){
				vs = (PtVacationVO[])l.toArray( new PtVacationVO[0]);
			}
		} catch (DAOException e) {
			LfwLogger.error("获得假期时出现异常",e);
		}
		return vs;
	}

	 
	@SuppressWarnings("unchecked")
	public PtVacationVO[] _getSpecialWorkDay() {
		PtVacationVO[] vs = new PtVacationVO[0];
		PtBaseDAO d = new PtBaseDAO();
		try {
			List l = (List)d.retrieveByClause(PtVacationVO.class, " type = 1");
			if(!PtUtil.isNull(l)){
				vs = (PtVacationVO[])l.toArray( new PtVacationVO[0]);
			}
		} catch (DAOException e) {
			LfwLogger.error("获得假期时出现异常",e);
		}
		return vs;
	}

	 
	public Integer[] _getWeekend() {
		PtWeekendVO cfg = null;
		List<Integer> wklist = new ArrayList<Integer>();
		try {
			cfg = getWeekendProp();
			String[] days = cfg.getWeekendday().split(",");
			if(!PtUtil.isNull(days)){
				for(String day : days){
					 if(PtUtil.isNumbic(day))
						 wklist.add(Integer.parseInt(day));
				}
			}
		} catch (Exception e) {
			LfwLogger.error("获得周末的配置出错!",e);
		}
		return wklist.toArray(new Integer[0]);
	}
	
	@Override
	public PtWeekendVO getWeekendProp() throws PortalServiceException{
		try {
			PtBaseDAO dao = new PtBaseDAO();
			Collection coll = dao.retrieveAll(PtWeekendVO.class);
			if(coll != null && !coll.isEmpty()){
				PtWeekendVO vo = (PtWeekendVO)coll.toArray()[0];
				return vo;
			}else{
			IPtWorkDayService wds = PortalServiceUtil.getWorkDayService();//	NCLocator.getInstance().lookup(IPtWorkDayService.class);
				PtWeekendVO vo = new PtWeekendVO();
				vo.setWeekendday("6,7");
				return wds.add(vo);
			}
		} catch (Exception e) {
			throw new PortalServiceException(e.getMessage(),e);
		}
	}
	
	/**
	 * 初始化缓存
	 */
	public void initCache(){
		String dsName = LfwRuntimeEnvironment.getDatasource();
		ILfwCache cache = LfwCacheManager.getStrongCache(CacheKeys.PORTAL_WORK_DAY_CACHE, dsName);
		cache.put(PtVacationVO.HOLIDAYS,_getHolidays());
		cache.put(PtVacationVO.SPECIALWORKDAY, _getSpecialWorkDay());
		cache.put(PtVacationVO.WEEKEND, _getWeekend());
	}


	@Override
	public PtVacationVO[] getHolidays() {
		String dsName = LfwRuntimeEnvironment.getDatasource();
		ILfwCache cache = LfwCacheManager.getStrongCache(CacheKeys.PORTAL_WORK_DAY_CACHE, dsName);
		return (PtVacationVO[]) cache.get(PtVacationVO.HOLIDAYS);
	}


	@Override
	public PtVacationVO[] getSpecialWorkDay() {
		String dsName = LfwRuntimeEnvironment.getDatasource();
		ILfwCache cache = LfwCacheManager.getStrongCache(CacheKeys.PORTAL_WORK_DAY_CACHE, dsName);
		return (PtVacationVO[]) cache.get(PtVacationVO.SPECIALWORKDAY);
	}


	@Override
	public Integer[] getWeekend() {
		String dsName = LfwRuntimeEnvironment.getDatasource();
		ILfwCache cache = LfwCacheManager.getStrongCache(CacheKeys.PORTAL_WORK_DAY_CACHE, dsName);
		return (Integer[]) cache.get(PtVacationVO.WEEKEND);
	}
}
