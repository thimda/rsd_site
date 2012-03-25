package nc.uap.portal.container.driver;

 import nc.uap.portal.constant.ContainerConstant;
import nc.uap.portal.container.service.ContainerServices;


public class ResourceURLProvider{

    private String stringUrl = "";
    private String base = "";

    public ResourceURLProvider() {
        this.base = ContainerServices.getInstance().getPortalContext().getProperty(ContainerConstant.PORTAL_BASE_URL);
    }

    public void setAbsoluteURL(String path) {
        stringUrl = path;
    }

    public void setFullPath(String path) {
        stringUrl = base + path;
    }

    public String toString() {
    	return 	stringUrl;
    }
}
