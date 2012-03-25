package nc.uap.portal.comm.file;

import org.apache.commons.lang.StringUtils;

import nc.bs.framework.common.RuntimeEnv;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.file.FileManager;
import nc.uap.lfw.file.filesystem.DocumentCenterFileSystem;
import nc.uap.lfw.file.filesystem.ILfwFileSystem;
import nc.uap.lfw.file.vo.LfwFileVO;
import nc.uap.portal.constant.WebKeys;

/**
 * Portal文件管理
 * 
 * <pre>
 * 实现了Portal的文件系统
 * </pre>
 * 
 * @author licza
 * 
 */
public class PortalFileManager extends FileManager {

	@Override
	public String getFileStore() {
		String portalFileStore = "/resources/portal/attachment/";
		return RuntimeEnv.getInstance().getNCHome() + portalFileStore;
	}

	@Override
	public ILfwFileSystem getFileSystem(LfwFileVO vo) {
		if(StringUtils.equals(vo.getCreator(),WebKeys.EMPTY_GROUP)){
			return new DocumentCenterFileSystem(getFileStore());
		}else{
			String ds = LfwRuntimeEnvironment.getDatasource();
			return new PortalDBFileSystemImpl(ds);
		}
	}

}
