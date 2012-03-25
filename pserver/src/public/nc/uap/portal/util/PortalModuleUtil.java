package nc.uap.portal.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.deploy.vo.PortalDeployDefinition;
import nc.uap.portal.deploy.vo.PortalModule;
import nc.uap.portal.exception.PortalServiceException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.digester.Digester;

/**
 * <b>Portal 模块工具类.</b>
 * <p>
 * 增加了模块依赖部署排序.
 * </p>
 * 
 * @author licza.
 */
public final class PortalModuleUtil {
	/** Portal模块定义解析器 **/
	private static Digester digester;
	static {
		getPortalModuleDigester();
	}

	/**
	 * 获得Portal模块
	 * 
	 * @param f
	 * @return
	 * @throws PortalServiceException
	 */
	public static PortalModule parsePortalModule(File f) throws PortalServiceException {
		try {
			return (PortalModule) digester.parse(f);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new PortalServiceException("解析PortalModule出现异常," + f.getName());
		}
	}

	/**
	 * 产生模块解析器
	 */
	private static void getPortalModuleDigester() {
		digester = new Digester();
		digester.setValidating(false);
		digester.addObjectCreate("portal", PortalModule.class.getName());
		digester.addSetProperties("portal");

		digester.addCallMethod("portal/module", "setModule", 0);
		digester.addCallMethod("portal/depends", "setDepends", 0);
		digester.addCallMethod("portal/depends", "setDescription", 0);
	}

	/**
	 * 将Portal模块定义按照依赖关系排序
	 * 
	 * @param dfis Portal模块定义
	 * @return 排序后的Portal模块定义
	 * @throws PortalServiceException 出现模块循环依赖
	 */
	public static PortalDeployDefinition[] sortDefinition(List<PortalDeployDefinition> dfis) throws PortalServiceException {
		List<PortalDeployDefinition> definitions = new LinkedList<PortalDeployDefinition>(dfis);
		/**
		 * 完成排序的模块
		 */
		List<PortalDeployDefinition> sortedDefinitions = new ArrayList<PortalDeployDefinition>();
		/**
		 * 拷贝一份依赖模块 
		 * 还原时使用 
		 */
		Map<String, String[]> localDependsModuleMirror = new HashMap<String, String[]>();
		if (CollectionUtils.isNotEmpty(definitions)) {
			for (PortalDeployDefinition pd : definitions) {
				localDependsModuleMirror.put(pd.getModule(), pd.getDependsModule());
			}
			int defSize = definitions.size();
			/**
			 * 顶点
			 */
			List<PortalDeployDefinition> peaks = hasPeak(definitions);
			while (!CollectionUtils.isEmpty(peaks)) {
				for (PortalDeployDefinition pd : peaks) {
					/**
					 * 从列表中删除
					 */
					definitions.remove(pd);
					/**
					 * 还原真实的依赖关系
					 */
					pd.setDependsModule(localDependsModuleMirror.get(pd.getModule()));
					/**
					 * 加入排序 
					 */
					sortedDefinitions.add(pd);
					/**
					 * 删除依赖关系
					 */
					removeDefinition(definitions, pd);
				}
				peaks = hasPeak(definitions);
			}
			/**
			 * 模块数与顶点数不一致
			 */
			if (defSize != sortedDefinitions.size()) {
				String msg = "部署时发现模块存在循环依赖!模块为:" + definitions.toString() + "(仅供参考).请检查!";
				LfwLogger.error(msg);
				throw new PortalServiceException(msg);
			}
		}
		return sortedDefinitions.toArray(new PortalDeployDefinition[0]);
	}

	/**
	 * 删除现有模块中对顶点模块的依赖关系
	 * 
	 * @param remainDefList 现有模块
	 * @param pd 顶点模块
	 */
	private static void removeDefinition(List<PortalDeployDefinition> remainDefList, PortalDeployDefinition pd) {
		for (int i = 0; i < remainDefList.size(); i++) {
			String[] modules = remainDefList.get(i).getDependsModule();
			List<String> moduleList = new LinkedList<String>();
			if (modules != null && modules.length > 0) {
				String anObject = pd.getModule();
				for (String module : modules) {
					if (!module.equals(anObject))
						moduleList.add(module);
				}
			}
			remainDefList.get(i).setDependsModule(moduleList.toArray(new String[0]));
		}
	}

	/**
	 * 获得当前模块中的顶点模块
	 * 
	 * @param pds 当前模块
	 * @return 顶点模块
	 */
	private static List<PortalDeployDefinition> hasPeak(List<PortalDeployDefinition> pds) {
		List<PortalDeployDefinition> orders = new LinkedList<PortalDeployDefinition>();
		for (PortalDeployDefinition pd : pds) {
			/**
			 * 只进不出的元素
			 * 即为顶点
			 */
			if (pd.getDependsModule() == null || pd.getDependsModule().length == 0) {
				orders.add(pd);
			}
		}
		return orders;
	}
}
