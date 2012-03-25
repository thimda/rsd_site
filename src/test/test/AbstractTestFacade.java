package test;

import java.util.Properties;


import nc.bs.framework.common.NCLocator;
import nc.bs.framework.common.RuntimeEnv;
import nc.bs.framework.common.UserExit;
 
/**
 * ≤‚ ‘ª˘¿‡
 * 
 * @author licza
 * 
 */
public class AbstractTestFacade   {
	
	  protected Properties props = new Properties();

    public void setUp() throws Exception {
        RuntimeEnv.getInstance().setProperty("CLIENT_COMMUNICATOR", "nc.bs.framework.comn.cli.JavaURLCommunicator");
        props.setProperty("CLIENT_COMMUNICATOR", "nc.bs.framework.comn.cli.JavaURLCommunicator");

        RuntimeEnv.getInstance().setRunningInServer(false);

        String baseURL = "http://" + getHost() + ":" + getPort();

        RuntimeEnv.getInstance().setProperty("SERVICEDISPATCH_URL", baseURL + "/ServiceDispatcherServlet");
        props.setProperty("SERVICEDISPATCH_URL", baseURL + "/ServiceDispatcherServlet");
        RuntimeEnv.getInstance().setProperty("CLIENT_COMMUNICATOR", "nc.bs.framework.comn.cli.JavaURLCommunicator");
        props.setProperty("CLIENT_COMMUNICATOR", "nc.bs.framework.comn.cli.JavaURLCommunicator");
        UserExit.getInstance().setBizCenterCode(getBizCenterCode());
    }
    protected String getBizCenterCode() {
        return System.getProperty("nc.systemid", "0001");
    }
    
    protected String getHost() {
        return System.getProperty("nc.host", "localhost");
    }

    protected String getPort() {
        return System.getProperty("nc.port", "80");
    }

    public NCLocator getLocator() {
        return NCLocator.getInstance(props);
    }
}
