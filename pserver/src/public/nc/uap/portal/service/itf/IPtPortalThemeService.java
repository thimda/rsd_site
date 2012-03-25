package nc.uap.portal.service.itf;

import nc.uap.portal.exception.PortalServerRuntimeException;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.vo.PtThemeVO;

/**
 * 主题增加、修改
 * 
 * @author licza
 * 
 */
public interface IPtPortalThemeService {
	/**
	 * 新增一个主题
	 * 
	 * @param vo
	 * @return
	 * @throws PortalServerRuntimeException
	 */
	public String add(PtThemeVO vo) throws PortalServiceException;

	/**
	 * 新增一组主题
	 * 
	 * @param vo
	 * @throws PortalServerRuntimeException
	 */
	public void add(PtThemeVO[] vo) throws PortalServiceException;

	/**
	 * 更新一组主题
	 * 
	 * @param vo
	 * @throws PortalServerRuntimeException
	 */
	public void update(PtThemeVO vo) throws PortalServiceException;
}
