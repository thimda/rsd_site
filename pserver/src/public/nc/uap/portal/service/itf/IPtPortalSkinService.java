package nc.uap.portal.service.itf;

import nc.uap.portal.exception.PortalServerRuntimeException;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.vo.PtSkinVo;

/**
 * 样式增加、修改
 * 
 * @author dingrf
 * 
 */
public interface IPtPortalSkinService {
	/**
	 * 新增一个样式
	 * 
	 * @param vo
	 * @return
	 * @throws PortalServerRuntimeException
	 */
	public String add(PtSkinVo vo) throws PortalServiceException;
	/**
	 * 删除样式
	 * 
	 * @param vo
	 * @return
	 * @throws PortalServerRuntimeException
	 */
	public void delete(String module,String themeid,String type) throws PortalServiceException;

	/**
	 * 新增一组样式
	 * 
	 * @param vo
	 * @throws PortalServerRuntimeException
	 */
	public void add(PtSkinVo[] vo) throws PortalServiceException;
	
	/**
	 * 更新一组样式
	 * 
	 * @param vo
	 * @throws PortalServerRuntimeException
	 */
	public void update(PtSkinVo vo) throws PortalServiceException;
}
