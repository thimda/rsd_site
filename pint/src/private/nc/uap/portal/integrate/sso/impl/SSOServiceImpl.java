package nc.uap.portal.integrate.sso.impl;
 
 
import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.inte.PintServiceFactory;
import nc.uap.portal.integrate.credential.CredentialReferenceSerializer;
import nc.uap.portal.integrate.credential.PtCredentialVO;
import nc.uap.portal.integrate.sso.itf.ISSOService;
import nc.uap.portal.persist.dao.PtBaseDAO;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.service.itf.IEncodeService;
import nc.uap.portal.vo.PtSlotVO;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;

/**
 * SSO 凭证管理实现类
 */
public class SSOServiceImpl implements ISSOService {
	private IEncodeService iEncodeService;
	public IEncodeService getIEncodeService() {
		return this.iEncodeService;
	}
 
	public void setIEncodeService(IEncodeService iEncodeService) {
		this.iEncodeService = iEncodeService;
	}

	public void createCredentials(PtCredentialVO credentialVO, PtSlotVO slotVO) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			slotVO.setActive(Integer.valueOf(1));
            /**
             * 首先判断是否存在这样的凭证槽，如果不存在，则新插入；
             * 如果存在，则获取主键信息。
             */
			PtSlotVO[] oldSlots = PintServiceFactory.getSsoQryService().getSlots(slotVO.getUserid(), slotVO.getPortletid(), 
					                 slotVO.getClassname(), slotVO.getSharelevel());
			String slotId = null;
			if(oldSlots == null || oldSlots.length == 0)
				slotId = dao.insertVO(slotVO);
			else
			    slotId = oldSlots[0].getPk_slot();
			
			//设置凭证槽信息
			credentialVO.setFk_slot(slotId);
			//对用户口令加密
			credentialVO.setPassword(iEncodeService.encode(credentialVO.getPassword()));
			// 设置凭证其它（reference）信息
			String xmlStr = CredentialReferenceSerializer
					                    .toXML(credentialVO.getCredentialReference());
			credentialVO.setReference(xmlStr);
			// 确保一个凭证槽仅对应一个凭证记录
			String delSql = "delete from pt_credential where fk_slot = '" + slotId + "'";
			dao.executeUpdate(delSql);
			dao.insertVO(credentialVO);
		} catch (BusinessException e) {
			Logger.error("创建credential失败", e);
			throw new PortalServiceException(NCLangRes4VoTransl.getNCLangRes().getStrByID("portal", "SSOServiceImpl-000000", null, new String[]{e.getMessage()})/*创建credential失败{0}*/);
		}
	}

	public void removeApplicationSharedCredentials(String userId, String portletId, String className, Integer shareLevel) throws BusinessException {
		PtSlotVO[] slots = PintServiceFactory.getSsoQryService().getSlots(userId, portletId, className, shareLevel);
		if(slots == null || slots.length == 0)
			return ;
		String slotId = slots[0].getPk_slot();
		PtBaseDAO dao = new PtBaseDAO();
		try {
			// 确保一个凭证槽仅对应一个凭证记录
			String delSql = "delete from pt_credential where fk_slot = '" + slotId + "'";
			dao.executeUpdate(delSql);
			dao.deleteByPK(PtSlotVO.class, slotId);
		} catch(BusinessException e) {
			Logger.error("删除credential失败", e);
			throw new PortalServiceException(NCLangRes4VoTransl.getNCLangRes().getStrByID("portal", "SSOServiceImpl-000001", null, new String[]{e.getMessage()})/*删除credential失败{0}*/);
		}
	}
	
	public void addSlot(PtSlotVO vo) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			dao.insertVO(vo);
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(),e);
			throw new PortalServiceException("Slot保存失败");
		}
	}

	@Override
	public void removeSlot(String pk_slot) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			// 确保一个凭证槽仅对应一个凭证记录
			String delSql = "delete from pt_credential where fk_slot = '" + pk_slot + "'";
			dao.executeUpdate(delSql);
			dao.deleteByPK(PtSlotVO.class, pk_slot);
		} catch(BusinessException e) {
			Logger.error("删除凭证槽失败", e);
			throw new PortalServiceException("删除凭证槽失败");
		}
	}
}
