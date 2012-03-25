package nc.uap.portal.comm.file;

/**
 * 文件单据类型常量类
 * @author licza
 *
 */
public class FileBillTypeConstant {
	/**发文**/
	public static final String 	COMPDOC = "portal.compdoc.savepath";
	/**新闻**/
	public static final String 	NEWS = "portal.news.savepath";
	/**相册**/
	public static final String 	ALBUM = "portal.album.savepath";
	/**用户图片**/
	public static final String 	USERPHOTO = "portal.userphoto.savepath";
	/** 视频 **/
	public static final String 	VIDEO = "portal.video.savepath";
	
	/** 支持的单据类型 **/
	public static final String[] BUNCH = {COMPDOC,NEWS,ALBUM,USERPHOTO,VIDEO};
}
