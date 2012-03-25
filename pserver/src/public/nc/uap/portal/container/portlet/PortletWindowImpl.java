package nc.uap.portal.container.portlet;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;

import org.apache.commons.lang.StringUtils;

//import nc.portal.org.vo.PtResourceVO;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
//import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.container.om.PortletDefinition;
import nc.uap.portal.deploy.vo.PtSessionBean;
import nc.uap.portal.exception.PortalServerRuntimeException;
//import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.service.PortalServiceUtil;
//import nc.uap.portal.util.PortalPageDataWrap;

 
/**
 * Implementation of <code>PortletWindow</code> interface.
 */
public class PortletWindowImpl implements PortletWindow {

    private PortletWindowID windowId;
    private PortletDefinition portlet;
    private WindowState windowState;
    public void setWindowState(WindowState windowState) {
		this.windowState = windowState;
	}
	public void setPortletMode(PortletMode portletMode) {
		this.portletMode = portletMode;
	}

	private PortletMode portletMode;

    // Constructor -------------------------------------------------------------

    /**
     * Constructs an instance.
     * @param config  the portlet window configuration.
     * @param portalURL  the portal URL.
     */
    public PortletWindowImpl(PortletWindowID id, WindowState ws, PortletMode pm) {
    	windowState = ws;
    	portletMode = pm;
        this.windowId = id;
        init();
        if(this.portlet == null)
        	throw new PortalServerRuntimeException("unable to load portlet:" + id.getStringId());
    }
    public PortletWindowImpl(PortletWindowID id){
    	this.windowId = id;
    	init();
    }
    public void init(){
        String module = this.windowId.getModule();
        String pageModule = this.windowId.getPageModule();
        String pageName = this.windowId.getPageName();
        String portletName = this.windowId.getPortletName();
    	PtSessionBean ses = (PtSessionBean) LfwRuntimeEnvironment.getLfwSessionBean();
    	if(ses != null){
        	String pk_user = ses.getPk_user();
        	String pkGroup = ses.getPk_unit();
//			PtResourceVO[] resouces = null;
//			try {
//				resouces = PortalServiceUtil.getResourceQryService().getResoureces(pk_user);
//			} catch (PortalServiceException e) {
//				LfwLogger.error(e.getMessage(),e);
//			}
//			String originalid = PortalPageDataWrap.modModuleName(module, portletName);
			/**
			 * 权限校验
			 */
//			boolean hasPower = PortalPageDataWrap.hasPower(originalid, resouces);
			if(true){
	        	this.portlet = PortalServiceUtil.getPortletQryService().findPortlet(pk_user, pkGroup, portletName, module, pageModule, pageName);
			}
    	}else{
        	throw new PortalServerRuntimeException("session time out!");
    	}
    }

    public PortletDefinition getPortlet() {
		return portlet;
	}


	public void setPortlet(PortletDefinition portlet) {
		this.portlet = portlet;
	}


	public String getPortletName() {
        return windowId.getPortletName();
    }

    public WindowState getWindowState() {
        return windowState;
    }

    public PortletMode getPortletMode() {
        return portletMode;
    }

    public PortletWindowID getId() {
        return windowId;
    }

    public PortletDefinition getPortletDefinition() {
        return portlet;
    }
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof PortletWindow){
			PortletWindow oThis = (PortletWindow) obj;
			return StringUtils.equals(oThis.getId().getStringId(), this.getId().getStringId());
		}
		return false;
	}
    
}
