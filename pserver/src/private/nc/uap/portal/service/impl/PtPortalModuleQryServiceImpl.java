package nc.uap.portal.service.impl;

import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.container.om.ModuleApplication;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.persist.dao.PtBaseDAO;
import nc.uap.portal.service.itf.IPtPortalModuleQryService;
import nc.uap.portal.util.JaxbMarshalFactory;
import nc.uap.portal.vo.PtModuleVO;
import nc.uap.portal.vo.PtModuleAppVO;
/**
 * Portal模块查询服务
 * 
 * @author licza
 * 
 */
public class PtPortalModuleQryServiceImpl implements IPtPortalModuleQryService {
	/**
	 * 查询
	 * 
	 * @param moduleId
	 * @return PtModuleVO||NULL
	 * @throws PortalServiceException
	 */
	@Override
	public PtModuleVO find(String moduleId) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		SQLParameter parameter = new SQLParameter();
		String sql = "select * from pt_module where ISNULL(pk_group, '~') ='~' and moduleid=?";
		parameter.addParam(moduleId);
		try {
			List<PtModuleVO> list = (List<PtModuleVO>) dao.executeQuery(sql,
					parameter, new BeanListProcessor(PtModuleVO.class));
			if (list != null && !list.isEmpty()) {
				return list.get(0);
			}
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e.getCause());
			throw new PortalServiceException(e);
		}
		return null;
	}
	/**
	 * 获得所有ModuleVO
	 * 
	 * @return
	 * @throws PortalServiceException
	 */
	@Override
	public PtModuleVO[] findALl() throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		String sql = "select * from pt_module where ISNULL(pk_group, '~') = '~'";
		try {
			List<PtModuleVO> list = (List<PtModuleVO>) dao.executeQuery(sql,
					new BeanListProcessor(PtModuleVO.class));
			return list.toArray(new PtModuleVO[0]);
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e.getCause());
			throw new PortalServiceException(e);
		}
	}
	@Override
	public ModuleApplication getModuleAppByModuleName(String module) {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			List<PtModuleAppVO> list = (List<PtModuleAppVO>)dao.retrieveByClause(PtModuleAppVO.class, " module = '" + module + "'");
			if(list != null && !list.isEmpty()){
				PtModuleAppVO pa = list.get(0);
				String def = pa.getAppsdef();
				ModuleApplication ma = JaxbMarshalFactory.newIns().encodeXML(ModuleApplication.class, def);
				ma.setModule(pa.getModule());
				ma.setDefaultNameSpace(pa.getDefaultns());
				return ma;
			}
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(),e);
		}
		return null;
	}
	
	@Override
	public ModuleApplication[] getModuleAppByNs(String ns) {
		PtBaseDAO dao = new PtBaseDAO();
		List<ModuleApplication> mas = new ArrayList<ModuleApplication>();
		try {
			List<PtModuleAppVO> list = (List<PtModuleAppVO>) dao.retrieveByClause(PtModuleAppVO.class, " defaultns = '" + ns + "' ");
			if(list != null && !list.isEmpty()){
				for(PtModuleAppVO o : list){
					String def = o.getAppsdef();
					ModuleApplication ma = JaxbMarshalFactory.newIns().encodeXML(ModuleApplication.class, def);
					ma.setModule(o.getModule());
					ma.setDefaultNameSpace(o.getDefaultns());
					mas.add(ma);
				}
			}
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(),e);
		}
		return mas.toArray(new ModuleApplication[0]);
	}

}
