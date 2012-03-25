package nc.uap.portal.service.itf;

import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.om.Skin;
import nc.uap.portal.vo.PtSkinVo;;

/**
 * 样式查询
 * 
 * @author dingrf
 * 
 */
public interface IPtPortalSkinQryService {
	/**
	 * 查找一个样式
	 * 
	 * @param skinId
	 * @return
	 * @throws PortalServerException
	 */
	public PtSkinVo find(String module ,String themeId , String type ,String skinId) throws PortalServiceException;

	/**
	 * 按类别查找某一组样式
	 * 
	 * @param type
	 * @return
	 * @throws PortalServiceException
	 */
	public PtSkinVo[] findByType(String type) throws PortalServiceException;

	/**
	 * 取样式缓存
	 * 
	 * @param type
	 * @return
	 */
	public Skin[] getSkinCache(String type);
	
}
