package nc.uap.portal.service.impl;

import java.util.List;

import nc.bs.dao.DAOException;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.persist.dao.PtBaseDAO;
import nc.uap.portal.plugins.model.PtExtension;
import nc.uap.portal.plugins.model.PtExtensionPoint;
import nc.uap.portal.service.itf.IPtPluginQryService;

/**
 * 插件查询服务实现
 * 
 * @author licza
 * @since 2010年9月10日12:37:25
 */
public class PtPluginQryServiceImpl implements IPtPluginQryService {
	@Override
	public PtExtension[] getAllExtension(String module) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			return (PtExtension[]) dao.queryByCondition(PtExtension.class, " module='" + module + "'");
		} catch (DAOException e) {
			throw new PortalServiceException(e);
		}
	}

	@Override
	public PtExtension[] getAllExtension() throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			return (PtExtension[]) dao.queryByCondition(PtExtension.class, " 1=1  ");
		} catch (DAOException e) {
			throw new PortalServiceException(e);
		}
	}

	@Override
	public PtExtensionPoint[] getAllExtensionPoint() throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			return (PtExtensionPoint[]) dao.queryByCondition(PtExtensionPoint.class, "");
		} catch (DAOException e) {
			throw new PortalServiceException(e);
		}
	}

	@Override
	public PtExtension[] getExtensionByPoint(String point) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			SQLParameter parameter = new SQLParameter();
			parameter.addParam(point);
			List<PtExtension> list = (List<PtExtension>)dao.executeQuery("SELECT * FROM dbo.pt_extension where point=?", parameter, new BeanListProcessor(PtExtension.class));
			return list != null ? list.toArray(new PtExtension[0]) : new PtExtension[0];
		} catch (DAOException e) {
			throw new PortalServiceException(e);
		}
	}

	@Override
	public PtExtension getExtension(String pk_extension) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			return (PtExtension) dao.retrieveByPK(PtExtension.class, pk_extension);
		} catch (DAOException e) {
			throw new PortalServiceException(e);
		}
	}

}
