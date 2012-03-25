
package nc.uap.portal.container.portlet;

import java.io.IOException;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventPortlet;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceServingPortlet;

import nc.uap.portal.container.om.Filter;
import nc.uap.portal.container.om.FilterMapping;
import nc.uap.portal.container.service.itf.FilterManager;

 

/**
 * Manage the initialization and doFilter {@link FilterChainImpl} for the filter which are
 * declareted in the deployment descriptor.
 * @since 05/29/2007
 * @version 2.0
 */
public class FilterManagerImpl implements FilterManager{
    private FilterChainImpl filterchain;
//    private PortletApplicationDefinition portletApp;
//    private String portletName;
//    private String lifeCycle;

    public FilterManagerImpl(PortletWindow portletWindow, String lifeCycle) {
// final PortletDefinition pd = portletWindow.getPortletDefinition();
//        this.portletApp = pd.getApplication();
//        this.portletName =  pd.getPortletName();
//        this.lifeCycle = lifeCycle;
        filterchain = new FilterChainImpl(lifeCycle);
        initFilterChain();
    }

    private void initFilterChain(){
//        List<? extends FilterMapping> filterMappingList = portletApp.getFilterMappings();
//        if (filterMappingList!= null){
//            for (FilterMapping filterMapping : filterMappingList) {
//                if (isFilter(filterMapping, portletName)){
//                    //the filter is specified for the portlet, check the filter for the lifecycle
//                    List<? extends Filter> filterList = portletApp.getFilters();
//                    for (Filter filter : filterList) {
//                        //search for the filter in the filter
//                        if (filter.getFilterName().equals(filterMapping.getFilterName())){
//                            //check the lifecycle
//                            if (isLifeCycle(filter, lifeCycle)){
//                                //the filter match to the portlet and has the specified lifecycle -> add to chain
//                                filterchain.addFilter(filter);
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }

    /**
     * @see nc.uap.portal.container.service.itf.FilterManager#processFilter(javax.portlet.EventRequest, javax.portlet.EventResponse, javax.portlet.EventPortlet, javax.portlet.PortletContext)
     */
    public void processFilter(EventRequest req, EventResponse res, EventPortlet eventPortlet,PortletContext portletContext)throws PortletException, IOException{
        filterchain.processFilter(req, res, eventPortlet, portletContext);
    }

    /**
     * @see nc.uap.portal.container.service.itf.FilterManager#processFilter(javax.portlet.ResourceRequest, javax.portlet.ResourceResponse, javax.portlet.ResourceServingPortlet, javax.portlet.PortletContext)
     */
    public void processFilter(ResourceRequest req, ResourceResponse res, ResourceServingPortlet resourceServingPortlet,PortletContext portletContext)throws PortletException, IOException{
        filterchain.processFilter(req, res, resourceServingPortlet, portletContext);
    }

    /**
     * @see nc.uap.portal.container.service.itf.FilterManager#processFilter(javax.portlet.RenderRequest, javax.portlet.RenderResponse, javax.portlet.Portlet, javax.portlet.PortletContext)
     */
    public void processFilter(RenderRequest req, RenderResponse res, Portlet portlet,PortletContext portletContext) throws PortletException, IOException{
        filterchain.processFilter(req, res, portlet, portletContext);
    }

    /**
     * @see nc.uap.portal.container.service.itf.FilterManager#processFilter(javax.portlet.ActionRequest, javax.portlet.ActionResponse, javax.portlet.Portlet, javax.portlet.PortletContext)
     */
    public void processFilter(ActionRequest req, ActionResponse res, Portlet portlet,PortletContext portletContext) throws PortletException, IOException{
        filterchain.processFilter(req, res, portlet, portletContext);
    }

    @SuppressWarnings("unused")
	private boolean isLifeCycle(Filter filter, String lifeCycle){
        List <String> lifeCyclesList = filter.getLifecycles();
        for (String string : lifeCyclesList) {
            if (string.equals(lifeCycle))
                return true;
        }
        return false;
    }

    @SuppressWarnings("unused")
	private boolean isFilter(FilterMapping filterMapping,String portletName){
        List <String> portletNamesList = filterMapping.getPortletNames();
        for (String portletNameFromFilterList : portletNamesList) {
            if (portletNameFromFilterList.endsWith("*")){
                if (portletNameFromFilterList.length()==1){
                    //if name contains only *
                    return true;
                }
                portletNameFromFilterList = portletNameFromFilterList.substring(0, portletNameFromFilterList.length()-1);
                if (portletName.length()>= portletNameFromFilterList.length()){
                    if (portletName.substring(0, portletNameFromFilterList.length()).equals(portletNameFromFilterList)){
                        return true;
                    }
                }
            }
            else if (portletNameFromFilterList.equals(portletName))
                return true;
        }
        return false;
    }


}
