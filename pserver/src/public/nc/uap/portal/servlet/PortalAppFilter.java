package nc.uap.portal.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import nc.bs.framework.common.NCLocator;
import nc.uap.cpb.templaterela.itf.ITemplateRelationQryService;
import nc.uap.lfw.app.filter.AppFilter;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.constants.AppConsts;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.deploy.vo.PtSessionBean;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.service.itf.IPtPageQryService;
import nc.uap.portal.user.impl.CpUser;

/**
 * App个性化过滤器
 * @author licza
 *
 */
public class PortalAppFilter extends AppFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String url = makeAppURL(req);
		String nodeid = req.getParameter("nodecode");
		if(nodeid != null){
			
			PtSessionBean sb = (PtSessionBean) LfwRuntimeEnvironment.getLfwSessionBean();
			if(sb != null){
				CpUser user = (CpUser) sb.getUser();
				ITemplateRelationQryService trq = NCLocator.getInstance().lookup(ITemplateRelationQryService.class);
				try {
					String pk_tmp = trq.getTemplatePkByUser(user.getUser(), nodeid);
					if(pk_tmp != null && pk_tmp.length() > 0){
						url = url + "&pk_templateDB=" + pk_tmp;
					}
				} catch (LfwBusinessException e) {
					LfwLogger.error(e.getMessage(), e);
				}
			}
		}
		
		RequestDispatcher disp = LfwRuntimeEnvironment.getServletContext().getRequestDispatcher(url);
		disp.forward(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
