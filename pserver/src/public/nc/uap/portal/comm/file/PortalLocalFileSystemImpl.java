package nc.uap.portal.comm.file;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import nc.bs.framework.common.RuntimeEnv;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.file.filesystem.LocalFileSystem;
import nc.uap.lfw.file.vo.LfwFileVO;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.vo.PtProtalConfigVO;

/**
 * Portal本地文件系统实现
 * @author licza
 *
 */
public class PortalLocalFileSystemImpl extends LocalFileSystem{

	public PortalLocalFileSystemImpl(String filePath) {
		super(filePath);
	}

	@Override
	protected String createFilePath(LfwFileVO fileVersionVO) {
		/**
		 * 如果没有文件名.则使用默认设置
		 */
		if(fileVersionVO.getFilename() == null)
			return super.createFilePath(fileVersionVO);

		/**
		 * 如果使用了Portal配置中的单据类型,则使用Portal配置中的设置
		 */
		if(fileVersionVO.getPk_billtype() != null && ArrayUtils.contains(FileBillTypeConstant.BUNCH, fileVersionVO.getPk_billtype()))
		{
			try {
				PtProtalConfigVO cfg  = PortalServiceUtil.getConfigQryService().getPortalConfig(fileVersionVO.getPk_billtype());
				if(cfg != null && !StringUtils.isEmpty(cfg.getConfig_value())){
					String filePath_tmp = cfg.getConfig_value();
					filePath = (filePath_tmp.replace("[NC_HOME]", RuntimeEnv.getInstance().getNCHome()));
				}
			} catch (PortalServiceException e) {
				LfwLogger.error(e.getMessage(),e);
			}
		}

		String id = fileVersionVO.getPk_lfwfile();
		if (id == null || id.length() == 0) {
			return null;
		}
		//补齐
		int idLength = id.length() + 1;
		// K为每段的长度
		final int k = 3;
		int rootPathLength = idLength % k ;
		StringBuilder sb = new StringBuilder();
		sb.append(filePath);
		// 返回文件分隔符
		String seprator = System.getProperty("file.separator");
		if (rootPathLength != 0) {
			sb.append(seprator);
			sb.append(id.substring(0, rootPathLength));
		}
		for (int i = 0; i < idLength / k; i++) {
			sb.append(seprator);
			sb.append(StringUtils.substring(id, i * k, i * k + k));
		}
		sb.append(seprator);
		sb.append(fileVersionVO.getFilename());
		return sb.toString();
	}

 
}
