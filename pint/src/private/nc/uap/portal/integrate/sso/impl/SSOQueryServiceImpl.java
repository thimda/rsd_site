package nc.uap.portal.integrate.sso.impl;
 

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.exception.DbException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.constant.WebKeys;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.integrate.credential.CredentialReferenceSerializer;
import nc.uap.portal.integrate.credential.CredentialWrapper;
import nc.uap.portal.integrate.credential.PtCredentialVO;
import nc.uap.portal.integrate.sso.itf.ISSOQueryService;
import nc.uap.portal.persist.dao.PtBaseDAO;
import nc.uap.portal.service.itf.IEncodeService;
import nc.uap.portal.vo.PtSlotVO;
import nc.vo.pub.BusinessException;
 
/**
 * SSO凭证操作接口的实现类
 * 
 * @author lkp
 *
 */
public class SSOQueryServiceImpl implements ISSOQueryService {
	
	private IEncodeService iEncodeService;

	public IEncodeService getIEncodeService() {
		return this.iEncodeService;
	}
 
	public void setIEncodeService(IEncodeService iEncodeService) {
		this.iEncodeService = iEncodeService;
	}

	/**
	 * 获取凭证VO
	 */
	public PtCredentialVO getCredentials(String userId, String portletId,
			String className, Integer sharelevel) throws PortalServiceException {
		
		PtBaseDAO dao = new PtBaseDAO();
		try {
			// 获取凭证槽
			PtSlotVO[] slots = getSlots(userId, portletId, className, sharelevel);
			if(slots == null || slots.length == 0)
				return null;
			//用户放弃集成
			if(slots[0].getActive() == null || slots[0].getActive().intValue() != 1)
				return new PtCredentialVO();
			
		    String credentialWhere = " fk_slot = '" + slots[0].getPk_slot() + "'";
		    Collection crecon = dao.retrieveByClause(PtCredentialVO.class, credentialWhere);
		    if(crecon.size() == 0)
		    	return null;
		    
		    PtCredentialVO credential = (PtCredentialVO)crecon.iterator().next();
		    credential.setPassword(iEncodeService.decode(credential.getPassword()));
		    credential.setCredentialReference(CredentialReferenceSerializer.
		    		                         fromXML(credential.getReference()));
		    return credential;
		}
		catch (BusinessException e) {
			throw new PortalServiceException(e);
		}
	}

	public PtSlotVO[] getSlots(String userId, String portletId, String className,
			Integer sharelevel) throws PortalServiceException {
		if(sharelevel == null)
			sharelevel = Integer.valueOf(1);
		StringBuffer slotWhere = new StringBuffer();
		slotWhere.append(" sharelevel = " );
		slotWhere.append(sharelevel == null ?1 :sharelevel.intValue());
		if(sharelevel.intValue() == WebKeys.PORTLET_SHARE_PRIVATE)
		{
			if(portletId != null && !portletId.trim().equals(""))
				slotWhere.append(" and portletid = '" + portletId + "' ");
			if(userId != null && !userId.trim().equals(""))
				slotWhere.append(" and userid = '" + userId + "' ");
		}
		else if(sharelevel.intValue() == WebKeys.PORTLET_SHARE_APPLICATION)
		{
			if(className != null && !className.trim().equals(""))
				slotWhere.append(" and classname = '" + className + "' ");
			if(userId != null && !userId.trim().equals(""))
				slotWhere.append(" and userid = '" + userId + "' ");
		}
		else
		{
			if(className != null && !className.trim().equals(""))
				slotWhere.append(" and classname = '" + className + "'");
		}		
		
	    PersistenceManager persist = null;
	    
	    Collection con = null;
	    try {	    	
			persist = PersistenceManager.getInstance();
			con = persist.retrieveByClause(PtSlotVO.class, slotWhere.toString());
		} catch (DbException e) {
			Logger.error(e.getMessage());
		} finally{
			if(persist != null) persist.release();
		}
		
	    if(con == null || con.size() == 0)
	    	return null;
	    else
	    	return (PtSlotVO[])con.toArray(new PtSlotVO[con.size()]);
	}

	public CredentialWrapper[] getPortletCredentials(String portletId, String className, Integer sharelevel) throws PortalServiceException {
		try {
			
			List<CredentialWrapper> list = new ArrayList<CredentialWrapper>();
			PtSlotVO[] slots = getSlots(null,portletId,className,sharelevel);
			PtBaseDAO dao = new PtBaseDAO();
			
			if (slots != null && slots.length > 0) {
				for (int i = 0; i < slots.length; i++) {
					PtSlotVO slot = slots[i];
					String credentialWhere = " fk_slot = '" + slot.getPk_slot() + "'";
					
					PtCredentialVO[] credentials = (PtCredentialVO[]) dao.retrieveByClause(PtCredentialVO.class, credentialWhere).toArray(new PtCredentialVO[0]);
					if (credentials != null && credentials.length > 0) {

						CredentialWrapper credentialWrapper = new CredentialWrapper();
						credentialWrapper.setSlot(slot);
						// 将credential中加密的口令解密
						credentials[0].setPassword(iEncodeService
								.decode(credentials[0].getPassword()));
						credentials[0]
								.setCredentialReference(CredentialReferenceSerializer
										.fromXML(credentials[0].getReference()));
						credentialWrapper.setCredentail(credentials[0]);
						list.add(credentialWrapper);
					}
				}
			}
			return (CredentialWrapper[]) list.toArray(new CredentialWrapper[list.size()]);
		} catch (BusinessException e) {
			Logger.error(e, e);
			throw new PortalServiceException(e);
		}
	}

	@Override
	public PtSlotVO getSlotByPK(String pk_slot) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			return (PtSlotVO) dao.retrieveByPK(PtSlotVO.class, pk_slot);
		} catch (DAOException e) {
			LfwLogger.error("根据PK获取配置槽异常:" + e.getMessage(),e);
			throw new PortalServiceException(e.getMessage());
		}
	}
}
