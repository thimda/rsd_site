package nc.portal.service.impl;


import java.util.List;

import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.portal.service.itf.ILfwTokenQryService;
import nc.uap.lfw.login.vo.LfwTokenVO;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.persist.dao.PtBaseDAO;
import nc.vo.pub.BusinessException;

/**
 * 2010-12-9 下午08:08:53  limingf
 */

public class LfwTokenQryServiceImpl implements ILfwTokenQryService { 
 
	@Override
	public LfwTokenVO[] getOnlineUserByGroupPk(String pk_group)
			throws PortalServiceException {
		PtBaseDAO baseDAO = new PtBaseDAO();
		String sql = "select * from uw_lfwtoken where pkcrop =  ? and ext1 = '/portal'";
		SQLParameter parameter = new SQLParameter();
		parameter.addParam(pk_group);
		try {
			List<LfwTokenVO> list = (List<LfwTokenVO>) baseDAO.executeQuery(sql, parameter, new BeanListProcessor(LfwTokenVO.class));
			return (LfwTokenVO[]) list.toArray(new LfwTokenVO[list.size()]);
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new PortalServiceException(e);
		}		
	}

	/**
	 * 根据登录用户查询所管理集团下的所有在线用户，不能分页
	 */
	@Override
	public LfwTokenVO[] getOnlineUserByGroupPk(String[] pk_groups)
			throws PortalServiceException {
		PtBaseDAO baseDAO = new PtBaseDAO();
		String sql = "";
		StringBuffer groupBuffer = new StringBuffer();
		for(int i=0;i<pk_groups.length;i++){
			groupBuffer.append("'").append(pk_groups[i]).append("'");
			if(i!=pk_groups.length-1)groupBuffer.append(",");
		}			
		//查出所授权的集团的在线用户
		sql = "select p.* from uw_lfwtoken p where p.pkcrop in (?) and ext1 = '/portal'";		
		SQLParameter parameter = new SQLParameter();
		parameter.addParam(groupBuffer.toString());
		try {
			List<LfwTokenVO> list = (List<LfwTokenVO>) baseDAO.executeQuery(sql, parameter, new BeanListProcessor(LfwTokenVO.class));
			return (LfwTokenVO[]) list.toArray(new LfwTokenVO[list.size()]);
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new PortalServiceException(e);
		}		
	}


	@Override
	public int getOnlineUserCount(boolean isDistinct, String pk_group)
			throws BusinessException {
		try { 
			String sql = null;
			Integer inte = null;
			if(isDistinct){
				sql = "select count(distinct uw_lfwtoken.userpk) from uw_lfwtoken, cp_user where uw_lfwtoken.userpk = cp_user.pk_user and cp_user.pk_group = ?";
				PtBaseDAO dao = new PtBaseDAO();
				SQLParameter param = new SQLParameter();
				param.addParam(pk_group);
				inte = (Integer)dao.executeQuery(sql, param, new ColumnProcessor(1));
			}
			else {
				sql = "select count(*) from uw_lfwtoken";
				PtBaseDAO dao = new PtBaseDAO();
			SQLParameter param = new SQLParameter();
			param.addParam(pk_group);
			inte = (Integer)dao.executeQuery(sql, new ColumnProcessor(1));
			}
			return inte.intValue();
			
		} catch(DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new PortalServiceException(e);
		}
	}
	
	

}
