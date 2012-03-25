package nc.uap.portal.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import nc.bs.dao.DAOException;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.uap.lfw.core.crud.CRUDHelper;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.integrate.message.MCConstant;
import nc.uap.portal.integrate.message.vo.PtMessageVO;
import nc.uap.portal.persist.dao.PtBaseDAO;
import nc.uap.portal.service.itf.IPtMessageCenterQryService;
import nc.vo.pub.lang.UFDate;

public class PtMessageCenterQryServiceImpl implements IPtMessageCenterQryService {
	/**
	 * 消息中心SQL头
	 */
	private static final String SQLHEAD = "select  pk_message,pk_user,state,priority,title,pk_sender,systemcode,sendername,sendtime,readtime,msgtype,ts,dr,targeturl,ownerstate,username,ext0,ext1,ext2,ext3,ext4,ext5,ext6,ext7,ext8,ext9 from pt_message ";
	@SuppressWarnings("unchecked")
	@Override
	public PtMessageVO[] getMessage(String pk_user) throws PortalServiceException {
		String sql = "select * from pt_message where pk_user=?";
		SQLParameter parameter = new SQLParameter();
		parameter.addParam(pk_user);
		PtBaseDAO dao = new PtBaseDAO();
		try {
			List<PtMessageVO> l = (List<PtMessageVO>) dao.executeQuery(sql, parameter, new BeanListProcessor(PtMessageVO.class));
			return CollectionUtils.isNotEmpty(l) ? l.toArray(new PtMessageVO[0]) : new PtMessageVO[0];
		} catch (DAOException e) {
			LfwLogger.error("信息查询失败!", e);
			throw new PortalServiceException(e);
		}
	}

	@Override
	public Integer  getNewMessageCounts(String pk_user, String systemCodes[]) throws PortalServiceException {
		String  sql = ("select count(pk_message) as msgcount from pt_message where pk_user=? and systemcode in ('" + StringUtils.join(systemCodes, "','") + "') and state = 0 ");
		SQLParameter parameter = new SQLParameter();
		parameter.addParam(pk_user);
		PtBaseDAO dao = new PtBaseDAO();
		try {
			Object o = dao.executeQuery(sql, parameter,new  ColumnProcessor("msgcount"));
			if(o != null && !o.toString().equals("") && StringUtils.isNumeric(o.toString()))
				return Integer.parseInt(o.toString());
		} catch (DAOException e) {
			LfwLogger.error("新信息数量查询失败!", e);
			throw new PortalServiceException(e);
		}
		return 0;
		  
	}

	@Override
	public PtMessageVO[] getMessage(String pk_user, String[] systemCodes) throws PortalServiceException {
		return getMessage(pk_user, systemCodes, 0);
	}

	@Override
	public PtMessageVO[] getMessage(String pk_user, String[] systemCodes, Integer type) throws PortalServiceException {
		 return getMessage(pk_user, systemCodes, type, new String[]{"0","1"});
	}
	@Override
	public PtMessageVO[] getTrashMessage(String pk_user, PaginationInfo pinfo) throws PortalServiceException{
		return getTrashMessage(pk_user, null, pinfo);
	}
	
	
	public Integer getTrashMessageCount(String pk_user) throws PortalServiceException{
		String sql = "select count(pk_message) as msgcount from pt_message where  (pk_user = ? and  state = " +MCConstant.STATE_TRASH+") or (pk_sender = ? and ownerstate ="+MCConstant.STATE_TRASH+" )";
		SQLParameter parameter = new SQLParameter();
		parameter.addParam(pk_user);
		parameter.addParam(pk_user);
		PtBaseDAO dao = new PtBaseDAO();
		try {
			Object o = dao.executeQuery(sql, parameter,new  ColumnProcessor("msgcount"));
			if(o != null && !o.toString().equals("") && StringUtils.isNumeric(o.toString()))
				return Integer.parseInt(o.toString());
		} catch (DAOException e) {
			LfwLogger.error("新信息数量查询失败!", e);
			throw new PortalServiceException(e);
		}
		return 0;
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public PtMessageVO[] getTrashMessage(String pk_user,Integer type, PaginationInfo pinfo) throws PortalServiceException {
		String sql = SQLHEAD +" where  (pk_user = ? and  state = " +MCConstant.STATE_TRASH+") or (pk_sender = ? and ownerstate ="+MCConstant.STATE_TRASH+" )";
		sql = sql + timeSection(type);
		SQLParameter parameter = new SQLParameter();
		parameter.addParam(pk_user);
		parameter.addParam(pk_user);
		PtBaseDAO dao = new PtBaseDAO();
		try {
			List<PtMessageVO> l = (List<PtMessageVO>) dao.executeQuery(sql, parameter, new BeanListProcessor(PtMessageVO.class));
			return CollectionUtils.isNotEmpty(l) ? l.toArray(new PtMessageVO[0]) : new PtMessageVO[0];
		} catch (DAOException e) {
			LfwLogger.error("信息查询失败!", e);
			throw new PortalServiceException(e);
		}
	}
	@Override
	public  PtMessageVO[] getSentMessage(String pk_user, PaginationInfo pinfo) throws PortalServiceException{
		return getSentMessage(pk_user, null, pinfo);
	}
	
	
	public  Integer getSentMessageCount(String pk_user) throws PortalServiceException{
		String sql = "select count(pk_message) as msgcount from pt_message  where (pk_sender = ? and ownerstate ="+MCConstant.STATE_NEW+" )";
		SQLParameter parameter = new SQLParameter();
		parameter.addParam(pk_user);
		PtBaseDAO dao = new PtBaseDAO();
		try {
			Object o = dao.executeQuery(sql, parameter,new  ColumnProcessor("msgcount"));
			if(o != null && !o.toString().equals("") && StringUtils.isNumeric(o.toString()))
				return Integer.parseInt(o.toString());
		} catch (DAOException e) {
			LfwLogger.error("新信息数量查询失败!", e);
			throw new PortalServiceException(e);
		}
		return 0;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public  PtMessageVO[] getSentMessage(String pk_user,Integer type, PaginationInfo pinfo) throws PortalServiceException {
		String sql = SQLHEAD +" where    (pk_sender = ? and ownerstate ="+MCConstant.STATE_NEW+" )";
		sql = sql + timeSection(type);
		SQLParameter parameter = new SQLParameter();
		parameter.addParam(pk_user);
		PtBaseDAO dao = new PtBaseDAO();
		try {
			List<PtMessageVO> l = (List<PtMessageVO>) dao.executeQuery(sql, parameter, new BeanListProcessor(PtMessageVO.class));
			return CollectionUtils.isNotEmpty(l) ? l.toArray(new PtMessageVO[0]) : new PtMessageVO[0];
		} catch (DAOException e) {
			LfwLogger.error("信息查询失败!", e);
			throw new PortalServiceException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public PtMessageVO[] getMessage(String pk_user, String[] systemCodes, Integer type, String states[]) throws PortalServiceException {
		StringBuffer sql = new StringBuffer(SQLHEAD + " where pk_user=? and systemcode in ('" + StringUtils.join(systemCodes, "','") + "')");
		sql.append(timeSection(type));
		if (states != null) {
			sql.append(" and state in ('");
			sql.append(StringUtils.join(states, "','"));
			sql.append("'  ) ");
		}
		
		SQLParameter parameter = new SQLParameter();
		parameter.addParam(pk_user);
		PtBaseDAO dao = new PtBaseDAO();
		try {
			List<PtMessageVO> l = (List<PtMessageVO>) dao.executeQuery(sql.toString(), parameter, new BeanListProcessor(PtMessageVO.class));
			return CollectionUtils.isNotEmpty(l) ? l.toArray(new PtMessageVO[0]) : new PtMessageVO[0];
		} catch (DAOException e) {
			LfwLogger.error("信息查询失败!", e);
			throw new PortalServiceException(e);
		}
	}

	@Override
	public PtMessageVO getMessageByPK(String pk_message) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			return (PtMessageVO)dao.retrieveByPK(PtMessageVO.class, pk_message);
		} catch (DAOException e) {
			LfwLogger.error("信息查询失败!", e);
			throw new PortalServiceException(e);
		}
	}
	
	/**
	 * 加入时间段选择
	 * @param type
	 * @return
	 */
	private String timeSection(Integer type){
		String sql = "";
		if (type != null) {
			switch (type) {
			case 1:
				sql=(" and sendtime > '"+ (new UFDate()).getDateBefore(7) +"' ");
				break;
			case 2:
				sql=(" and sendtime > '"+ (new UFDate()).getDateBefore(30) +"' ");
				break;
			case 3:
				sql=(" and sendtime > '"+ (new UFDate()).getDateBefore(90) +"' ");
				break;
			default:
				break;
			}
		}
		return sql;
	}

	@Override
	public PtMessageVO[] getMessage(String pk_user, String[] systemCodes,Integer timespan, String[] states, PaginationInfo pinfo)
			throws PortalServiceException {
		StringBuffer sql = new StringBuffer(SQLHEAD + " where pk_user='"+pk_user+"' and systemcode in ('" + StringUtils.join(systemCodes, "','") + "')");
		sql.append(timeSection(timespan));
		if (states != null) {
			sql.append(" and state in ('");
			sql.append(StringUtils.join(states, "','"));
			sql.append("') ");
		}
		try {
			return CRUDHelper.getCRUDService().queryVOs(sql.toString(), PtMessageVO.class, pinfo,"  ORDER BY state ASC ,priority DESC , sendtime DESC ", null);
		} catch (LfwBusinessException e) {
			LfwLogger.error("信息查询失败!", e);
			throw new PortalServiceException(e);
		}
	}
}
