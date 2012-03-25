package nc.portal.sso;

import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;

import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.CtxMenuMouseEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.ctx.WidgetContext;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.integrate.system.SSOProviderVO;
import nc.uap.portal.integrate.system.SSOReference;
import nc.uap.portal.util.JaxbMarshalFactory;
import nc.uap.portal.vo.PtSsopropVO;
import nc.vo.pub.lang.UFBoolean;

/**
 * 页面
 * 
 * @author licza
 * 
 */
public class SsoDataWarpUtil {
	
	
	
	/**
	 * 从事件中得到当前的数据集
	 * 
	 * @param event
	 * @param ctx
	 * @return
	 */
	public static Dataset getCurrentDs(MouseEvent<MenuItem> event, WidgetContext ctx) {
		CtxMenuMouseEvent e = (CtxMenuMouseEvent) event;
		String triggerId = e.getTriggerId();
		String dsName = "referenceDs";
		if (!"refgrid".equals(triggerId)) {
			dsName = "ipMappingDs";
		}
		Dataset ds = ctx.getWidget().getViewModels().getDataset(dsName);
		String currKey = ds.getCurrentKey();
		if (currKey == null || currKey.equals("")) {
			ds.getRowSet(Dataset.MASTER_KEY, true);
			ds.setCurrentKey(Dataset.MASTER_KEY);
		}
		return ds;
	}

	/**
	 * 根据系统编码创建一个ProviderVO
	 * 
	 * @param systemCode
	 * @return
	 */
	public static SSOProviderVO createSSOProviderVO(String systemCode) {
		SSOProviderVO vo = new SSOProviderVO();
		vo.setSystemCode(systemCode);
		return vo;
	}

	/**
	 * 从数据行中读取数据
	 * 
	 * @param row
	 * @param provider
	 * @param ds
	 * @return
	 */
	public static SSOProviderVO row2SSOProviderVO(Row row, SSOProviderVO provider, Dataset ds) {
		provider.setAuthClass(row.getString(ds.nameToIndex("authClass")));
		UFBoolean enableMapping = row.getUFBoolean(ds.nameToIndex("enableMapping"));
		provider.setEnableMapping(enableMapping == null ?false:enableMapping.booleanValue());
		provider.setGateUrl(row.getString(ds.nameToIndex("gateUrl")));
		provider.setNodesClass(row.getString(ds.nameToIndex("nodesClass")));
		provider.setSystemName(row.getString(ds.nameToIndex("systemName")));
		return provider;
	}
}
