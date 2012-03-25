package nc.uap.portal.service.itf;

import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.integrate.message.vo.PtMessageVO;

/**
 * 信息中心查询服务接口
 * 
 * @author licza
 * @version NC 6.0
 * @since 2010-11-9 20:23:46
 */
public interface IPtMessageCenterQryService {
	/**
	 * 根据消息的PK查询的消息
	 * @param pk_message
	 * @return
	 * @throws PortalServiceException
	 */
	public PtMessageVO getMessageByPK(String pk_message) throws PortalServiceException;
	
	
	/**
	 * 获得新信息数量
	 * 
	 * @param pk_user 用户编码
	 * @param systemCodes 系统编码
	 * 
	 * @return 系统编码 新信息量
	 * @throws PortalServiceException
	 */
	public Integer  getNewMessageCounts(String pk_user, String[] systemCodes) throws PortalServiceException;

	/**
	 * 查询用户的信息
	 * 
	 * @param pk_user 用户编码
	 * @return
	 * @throws PortalServiceException
	 */
	public PtMessageVO[] getMessage(String pk_user) throws PortalServiceException;

	/**
	 * 获得新信息
	 * 
	 * @param pk_user 用户编码
	 * @param systemCodes 系统编码
	 * @return
	 * @throws PortalServiceException
	 */
	public PtMessageVO[] getMessage(String pk_user, String[] systemCodes) throws PortalServiceException;

	/**
	 * 获得新信息
	 * 
	 * @param pk_user 用户编码
	 * @param systemCodes 系统编码
	 * @param timespan 时间段(1= 一周 ,2= 30天内 ,3=三个月内)
	 * @return
	 * @throws PortalServiceException
	 */
	public PtMessageVO[] getMessage(String pk_user, String[] systemCodes, Integer timespan) throws PortalServiceException;

	
	/**
	 * 获得信息
	 * 
	 * @param pk_user 用户编码
	 * @param systemCodes 系统编码
	 * @param timespan 时间段(1= 一周 ,2= 30天内 ,3=三个月内)
	 * @param states 信息状态 (0 =未读,1=已读,-1=已删除,-2=彻底删除)
	 * @return
	 * @throws PortalServiceException
	 */
	public PtMessageVO[] getMessage(String pk_user, String[] systemCodes, Integer timespan, String states[]) throws PortalServiceException;
	
	/**
	 * 获得信息
	 * @param pk_user 用户编码 
	 * @param systemCodes 系统编码
	 * @param timespan 时间段(1= 一周 ,2= 30天内 ,3=三个月内)
	 * @param states 信息状态 (0 =未读,1=已读,-1=已删除,-2=彻底删除)
	 * @param pinfo 分页信息
	 * @return
	 * @throws PortalServiceException
	 */
	public PtMessageVO[] getMessage(String pk_user, String[] systemCodes, Integer timespan, String states[], PaginationInfo pinfo) throws PortalServiceException;

	
	/**
	 * 获得用户垃圾箱里的信息
	 * @param pk_user
	 * @return
	 * @throws PortalServiceException
	 */
	public PtMessageVO[] getTrashMessage(String pk_user, PaginationInfo pinfo) throws PortalServiceException;
	
	/**
	 * 获得发件箱信息 
	 * @param pk_user
	 * @return
	 * @throws PortalServiceException
	 */
	public  PtMessageVO[] getSentMessage(String pk_user, PaginationInfo pinfo) throws PortalServiceException;
	/**
	 * 获得用户垃圾箱里的信息
	 * @param pk_user
	 * @param type 时间段(1= 一周 ,2= 30天内 ,3=三个月内)
	 * @return
	 * @throws PortalServiceException
	 */
	public PtMessageVO[] getTrashMessage(String pk_user,Integer type, PaginationInfo pinfo) throws PortalServiceException;
	/**
	 * 获得发件箱信息 
	 * @param pk_user
	 * @param type 时间段(1= 一周 ,2= 30天内 ,3=三个月内)
	 * @return
	 * @throws PortalServiceException
	 */
	public  PtMessageVO[] getSentMessage(String pk_user,Integer type, PaginationInfo pinfo) throws PortalServiceException;
	
	/**
	 * 获得已发送消息的数量
	 * @param pk_user
	 * @return
	 * @throws PortalServiceException
	 */
	public  Integer getSentMessageCount(String pk_user) throws PortalServiceException;
	
	/**
	 * 获得垃圾箱的信息数量
	 * @param pk_user
	 * @return
	 * @throws PortalServiceException
	 */
	public Integer getTrashMessageCount(String pk_user) throws PortalServiceException;
}
