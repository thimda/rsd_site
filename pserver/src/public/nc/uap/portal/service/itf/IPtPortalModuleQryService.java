package nc.uap.portal.service.itf;

import nc.uap.portal.container.om.ModuleApplication;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.vo.PtModuleVO;

/**
 * Portal模块服务
 * 
 * @author licza
 * 
 */
public interface IPtPortalModuleQryService {
	/**
	 * 查询
	 * 
	 * @param moduleId
	 * @return PtModuleVO||NULL
	 * @throws PortalServiceException
	 */
	public PtModuleVO find(String moduleId) throws PortalServiceException;

	/**
	 * 获得所有ModuleVO
	 * 
	 * @return
	 * @throws PortalServiceException
	 */
	public PtModuleVO[] findALl() throws PortalServiceException;
	/**
	 * 根据模块名获得Portal模块应用
	 * @param module
	 * @return
	 */
	public ModuleApplication getModuleAppByModuleName(String module);
	/**
	 * 根据namespace获得Portal模块应用
	 * @param ns
	 * @return
	 */
	public ModuleApplication[] getModuleAppByNs(String ns);
}
