package nc.uap.portal.service.itf;

import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.vo.PtWeekendVO;

/**
 * 工作日设置服务接口
 * @author licza
 *
 */
public interface IPtWorkDayService {
	/**
	 * 插入一个周末设置VO
	 * @param vo
	 * @return
	 * @throws PortalServiceException
	 */
	public PtWeekendVO add(PtWeekendVO vo) throws PortalServiceException;
	/**
	 * 更新一个周末设置VO
	 * @param vo
	 * @throws PortalServiceException
	 */
	public void update(PtWeekendVO vo) throws PortalServiceException;
}
