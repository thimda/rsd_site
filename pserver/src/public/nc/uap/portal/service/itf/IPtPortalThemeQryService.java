package nc.uap.portal.service.itf;

import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.vo.PtThemeVO;

/**
 * 主题查询
 * 
 * @author licza
 * 
 */
public interface IPtPortalThemeQryService {
	/**
	 * 查找一个主题
	 * 
	 * @param themeId
	 * @return
	 * @throws PortalServerException
	 */
	public PtThemeVO find(String themeId) throws PortalServiceException;
	
	/**
	 * 获得集团的主题
	 * @return
	 * @throws PortalServiceException
	 */
	public PtThemeVO[] getThemeByGroup() throws PortalServiceException;
	
}
