package nc.uap.portal.service.itf;

import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBException;

import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.om.PortletDisplayCategory;
import nc.uap.portal.vo.PtPortletVO;

/**
 * Portlet注册服务
 * @author licza
 *
 */
public interface IPtPortletRegistryService {

	/**
	 * 将portlet及其配置加载到缓存中
	 * @throws PortalServiceException 
	 * @throws JAXBException 
	 */
	public void loadPortlets() throws PortalServiceException;

	/**
	 * 缓存欲更新的Portlet
	 * @param pageVOs
	 */
	public void cachePrepareUpdatePages(Set<PtPortletVO> portlets);

	/**
	 * 从缓存中找到模块的实际路径
	 * @param moduleName
	 * @return
	 */
	public String findModuleFolder(String moduleName);

	/**
	 * 将模块的实际路径放入缓存中 
	 * @param moduleName
	 * @return
	 */
	public void addModuleFolder(String moduleName, String moduleFolder);

	/**
	 * 获得Master的Module初始化状态
	 * @param moduleName
	 * @return
	 */
	public Boolean getModuleInitializeStatus();

	/**
	 * 标记Master的Module初始化完成
	 * @param moduleName
	 * @return
	 */
	public void setModuleInitializeStatus();

	/**
	 * 从缓存中找到模块的实际路径
	 * @param moduleName
	 * @return
	 */
	public Map<String, String> findModuleFolders();
	
	/**
	 * 更新集团的Portal缓存
	 * @param pk
	 */
	public void updateGroupCache(String pk,Boolean fire);
	
	/**
	 * 获得Portlet分组缓存
	 * @return
	 */
	public Map<String, PortletDisplayCategory>  getPortletDisplayCache();
 }
