package nc.uap.portal.util.freemarker.functions;

import java.util.List;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.log.LfwLogger;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;
/**
 * äÖÈ¾Êý×ÖÑùÊ½
 * @author licza
 *
 */
public class NumberDye implements TemplateMethodModel{

	@Override
	public Object exec(List arg0) throws TemplateModelException {
		String[][] cssMap = new String[][]{{"blue", "green" , "orange"},{"blue3", "green3" , "orange3"}};
		Integer dcount = (Integer)LfwRuntimeEnvironment.getWebContext().getRequest().getAttribute("_$NMC_D0M");
		if(dcount == null)
			dcount = 0;
		String cssName = "blue";
		int count = Integer.parseInt(arg0.get(0).toString());
		int idx1 = count < 100 ? 0 : 1;
		
		int idx2 = dcount % 3;
		
		try {
			cssName = cssMap[idx1][idx2];
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
		}
		
		LfwRuntimeEnvironment.getWebContext().getRequest().setAttribute("_$NMC_D0M", dcount + 1);
		
		return cssName;
	}

}
