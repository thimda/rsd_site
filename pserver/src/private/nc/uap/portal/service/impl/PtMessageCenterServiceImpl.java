package nc.uap.portal.service.impl;


import nc.bs.dao.DAOException;
import nc.jdbc.framework.SQLParameter;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.integrate.message.MCConstant;
import nc.uap.portal.integrate.message.vo.PtMessageVO;
import nc.uap.portal.persist.dao.PtBaseDAO;
import nc.uap.portal.service.itf.IPtMessageCenterService;

import org.apache.commons.lang.StringUtils;

public class PtMessageCenterServiceImpl implements IPtMessageCenterService {

	@Override
	public String add(PtMessageVO vo) throws PortalServiceException {
		return add(new PtMessageVO[] { vo })[0];
	}

	public String[] add(PtMessageVO[] vo) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			return dao.insertVOs(vo);
		} catch (DAOException e) {
			LfwLogger.error("信息保存失败", e);
			throw new PortalServiceException(e);
		}
	}

	@Override
	public void delete(String[] pk, boolean onwer) throws PortalServiceException {
		LfwLogger.info("删除PK为:"+pk+"的消息");
		if(onwer)
			modMessageState(pk, MCConstant.STATE_DELETE);
		else
			modSentMessageState(pk, MCConstant.STATE_DELETE);
		LfwLogger.warn("成功将PK为:"+pk+"的消息删除");
	}

	@Override
	public void read(String pk) throws PortalServiceException {
		LfwLogger.info("修改PK为:"+pk+"的消息状态为已读");
		modMessageState(new String[]{pk}, MCConstant.STATE_READ);
		LfwLogger.warn("成功将PK为:"+pk+"的消息状态为已读");
	}

	@Override
	public void trash(String[] pk ,boolean onwer) throws PortalServiceException {
		LfwLogger.info("修改PK为:"+pk+"的消息状态为已删除");
		if(onwer)
			modMessageState(pk, MCConstant.STATE_TRASH);
		else
			modSentMessageState(pk, MCConstant.STATE_TRASH);
		LfwLogger.warn("成功将PK为:"+pk+"的消息状态为已删除");
	}

	/**
	 * 修改信息状态
	 * 
	 * @param pk
	 * @param state
	 * @throws PortalServiceException
	 */
	public void modMessageState(String[] pk, String state)
			throws PortalServiceException {
		StringBuffer sb = new StringBuffer("UPDATE pt_message SET state = ? ");
		if(state != null && state.equals("1"))
			sb.append(" , readtime = getdate() ");
		sb.append("where pk_message in ('"+StringUtils.join(pk, "','")+"') ");
		SQLParameter param = new SQLParameter();
		param.addParam(state);
		PtBaseDAO dao = new PtBaseDAO();
		try {
			dao.executeUpdate(sb.toString(), param);
		} catch (DAOException e) { 
			LfwLogger.error("信息修改失败", e);
			throw new PortalServiceException(e);
		}
	}
	/**
	 * 修改用户发件箱信息状态
	 * @param pk
	 * @param state
	 */
	public void modSentMessageState(String[] pk, String state) throws PortalServiceException {
		StringBuffer sb = new StringBuffer("UPDATE pt_message SET ownerstate = ? ");
		sb.append("where pk_message in ('"+StringUtils.join(pk, "','")+"') ");
		SQLParameter param = new SQLParameter();
		param.addParam(state);
		PtBaseDAO dao = new PtBaseDAO();
		try {
			dao.executeUpdate(sb.toString(), param);
		} catch (DAOException e) { 
			LfwLogger.error("信息修改失败", e);
			throw new PortalServiceException(e);
		}
	}
}
