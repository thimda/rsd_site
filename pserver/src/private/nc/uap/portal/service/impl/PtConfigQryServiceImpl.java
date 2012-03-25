package nc.uap.portal.service.impl;

import java.util.List;

import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.persist.dao.PtBaseDAO;
import nc.uap.portal.service.itf.IPtConfigQryService;
import nc.uap.portal.util.PtUtil;
import nc.uap.portal.vo.PtAnnoyVO;
import nc.uap.portal.vo.PtProtalConfigVO;
import nc.vo.pub.lang.UFBoolean;
/**
 * Portal配置信息查询服务实现
 * @author licza
 *
 */
public class PtConfigQryServiceImpl implements IPtConfigQryService {

	@SuppressWarnings("unchecked")
	@Override
	public PtProtalConfigVO[] getAllPortalConfig() throws PortalServiceException {
		PtBaseDAO baseDAO = new PtBaseDAO();
		try {
			List list = (List)baseDAO.retrieveAll(PtProtalConfigVO.class);
			if(!PtUtil.isNull(list))
				return (PtProtalConfigVO[]) list.toArray(new PtProtalConfigVO[0]);
			else
				return new PtProtalConfigVO[]{};
		} 
		catch (DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new PortalServiceException(e);
		}
	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public PtSysConfigVO[] getAllSysConfig() throws PortalServiceException {
//		PtBaseDAO baseDAO = new PtBaseDAO();
//		try {
//			List list = (List)baseDAO.retrieveAll(PtSysConfigVO.class);
//			if(!PtUtil.isNull(list))
//				return (PtSysConfigVO[]) list.toArray(new PtSysConfigVO[0]);
//			else
//				return null;
//		} 
//		catch (DAOException e) {
//			Logger.error(e.getMessage(), e);
//			throw new PortalServiceException(e);
//		}
//	}

	@SuppressWarnings("unchecked")
	@Override
	public PtProtalConfigVO getPortalConfig(String key)	throws PortalServiceException {
		PtBaseDAO baseDAO = new PtBaseDAO();
		try {
			List list = (List)baseDAO.retrieveByClause(PtProtalConfigVO.class, "config_key='"+key+"'");
			if(!PtUtil.isNull(list))
				return (PtProtalConfigVO) list.get(0);
			else
				return null;
		} 
		catch (DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new PortalServiceException(e);
		}
	}

	@Override
	public PtAnnoyVO getAnnoymousUser() throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			List<PtAnnoyVO> list = (List<PtAnnoyVO>)dao.retrieveAll(PtAnnoyVO.class);
			if(list != null && !list.isEmpty()){
				PtAnnoyVO annoy = list.get(0);
				UFBoolean isActive = annoy.getIsactive();
				if(isActive != null && isActive.booleanValue()){
					 return annoy;
				}
			}
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(),e);
			throw new PortalServiceException("获取\"是否支持匿名用户\"配置信息时发生错误!");
		}
		return null;
	}

	@Override
	public Boolean isSupportAnnoyMousUser() throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			List<PtAnnoyVO> list = (List<PtAnnoyVO>)dao.retrieveAll(PtAnnoyVO.class);
			if(list != null && !list.isEmpty()){
				UFBoolean isActive = list.get(0).getIsactive();
				return isActive == null ? false : isActive.booleanValue();
			}
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(),e);
			throw new PortalServiceException("获取\"是否支持匿名用户\"配置信息时发生错误!");
		}
		return false;
	}

//	@Override
//	public PtSysConfigVO getSysConfig(String key) throws PortalServiceException {
//		PtBaseDAO baseDAO = new PtBaseDAO();
//		try {
//			List list = (List)baseDAO.retrieveByClause(PtSysConfigVO.class, "config_key='"+key+"'");
//			if(!PtUtil.isNull(list))
//				return (PtSysConfigVO) list.get(0);
//			else
//				return null;
//		} 
//		catch (DAOException e) {
//			Logger.error(e.getMessage(), e);
//			throw new PortalServiceException(e);
//		}
//	}

}
