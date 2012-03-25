package nc.uap.portal.service.itf;
import nc.uap.portal.container.om.PortletDefinition;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.vo.PtPortletVO;
import nc.uap.portal.vo.PtPreferenceVO;

/**
 * Portlet信息查询接口
 * 
 * @author licza
 * 
 */
public interface IPtPortletQryService {

	/**
	 * 查询用户的portlet配置
	 * @param pk_user
	 * @param pk_group
	 * @param portletname
	 * @param pagename
	 * @return
	 * @throws PortalServiceException
	 */
	public PtPreferenceVO getUserPortletPreference(String pk_user,String pk_group,String portletname , String pagename) throws PortalServiceException;
	/**
	 * 查询集团Portlet配置
	 * @param pk_group
	 * @param portletname.
	 * @param pagename
	 * @return
	 * @throws PortalServiceException
	 */
	public PtPreferenceVO getGroupPortletPreference(String pk_group,String portletname , String pagename) throws PortalServiceException;
	/**
	 * 从缓存中获得一个有自定义设置的Portlet配置
	 * 
	 * @param pk_user 用户编码
	 * @param pk_group 集团编码
	 * @param portletName Portlet名称
	 * @param portletModule Portlet模块
	 * @param pageModule 页面模块
	 * @param pageName 页面名称
	 * @return
	 */
	public PortletDefinition findPortlet(String pk_user, String pk_group, String portletName, String portletModule, String pageModule, String pageName);

	/**
	 * 根据主键获得Portlet实体
	 * 
	 * @param pk_portlet Portlet主键
	 * @return PortletVO
	 * @throws PortalServiceException
	 */
	public PtPortletVO findPortletByPK(String pk_portlet) throws PortalServiceException;

	 
	/**
	 * 获得系统级PortletVO
	 * 
	 * @param module 模块
	 * @return 系统级PtPortletVO
	 */
	public PtPortletVO[] getSystemPortlet(String module) throws PortalServiceException;

	/**
	 * 获得集团级PortletVO
	 * 
	 * @param module 模块名
	 * @param parentid 原型Portlet编码
	 * @return 集团级PortletVO
	 * @throws PortalServiceException
	 */
	public PtPortletVO[] getGroupPortlets(String module, String parentid) throws PortalServiceException;

	/**
	 * 获得系统级Portlet
	 * 
	 * @return 系统级PtPortletVO
	 * @throws PortalServiceException
	 */
	public PtPortletVO[] getSystemPortlet() throws PortalServiceException;

	/**
	 * 获得集团级PortletVOs
	 * 
	 * @return 集团级PortletVOs
	 * @throws PortalServiceException
	 */
	public PtPortletVO[] getGroupPortlets() throws PortalServiceException;

	/**
	 * 获得用户私有的Portlet
	 * 
	 * @return
	 * @throws PortalServiceException
	 */
	public PtPortletVO[] getUserDiyPortlets() throws PortalServiceException;

	/**
	 * 获得集团的Portlet
	 * 
	 * @param pk_group 集团编码
	 * @param portletNames Portlet名称(模块名,Portlet名)
	 * @return PortletVOs
	 * @throws PortalServiceException
	 */
	public PtPortletVO[] getGroupPortlets(String pk_group, String[][] portletNames) throws PortalServiceException;
	/**
	 * 
	 * 从缓存中选择集团级Portlet
	 * 
	 * @param pk_group 集团编码
	 * @param portletName Portlet名称
	 * @param module 模块
	 * @return 集团级Portlet定义
	 */
	public PortletDefinition findPortletFromGroupCache(String pk_group, String portletName, String module);
	
	/**
	 * 从缓存中选择系统级Portlet
	 * 
	 * @param portletId Portlet名称
	 * @param module 模块
	 * @return 系统级Portlet定义
	 */
	public PortletDefinition findPortletFromSystemCache(String portletId, String module) ;
	
	/**
	 * 获得集团的Portlet
	 * @param pk_group
	 * @return
	 * @throws PortalServiceException
	 */
	public PtPortletVO[] getGroupPortlets(String pk_group) throws PortalServiceException;
	
	/**
	 * 根据类型查询Portlet
	 * @param clause
	 * @return
	 */
	PtPortletVO[] qryPortletByClause(String clause);
	
}
