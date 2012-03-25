package nc.uap.portal.service.itf;

import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.integrate.message.vo.PtMessageVO;

public interface IPtMessageCenterService {
	/**
	 * 新增一个信息
	 * 
	 * @param vo
	 * @return
	 * @throws PortalServiceException
	 */
	public String add(PtMessageVO vo) throws PortalServiceException;

	/**
	 * 新增一组信息
	 * 
	 * @param vo
	 * @return
	 * @throws PortalServiceException
	 */
	public String[] add(PtMessageVO[] vo) throws PortalServiceException;
	/**
	 * 读信息
	 * @param pk
	 * @throws PortalServiceException
	 */
	public void read(String pk) throws PortalServiceException;
	/**
	 * 删除信息
	 * @param pk
	 * @throws PortalServiceException
	 */
	public void delete(String[] pk ,boolean onwer) throws PortalServiceException;
	
	/**
	 * 将信息删除到"已删除"文件夹
	 * @param pk
	 * @throws PortalServiceException
	 */
	public void trash(String[] pk ,boolean onwer) throws PortalServiceException;
	

}
