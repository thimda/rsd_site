package nc.uap.portal.user.itf;

import java.util.List;

import nc.uap.portal.om.Page;

/**
 * Portal权限服务
 * 
 * @author licza
 * 
 */
public interface IPortalPowerService {
	List<Page> filterPagesByUserResource(Page[] pages);

	boolean hasPower(String originalid);
}
