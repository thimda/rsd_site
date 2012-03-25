package nc.uap.portal.vo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import nc.uap.lfw.core.log.LfwLogger;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;

/**
 * Portal数据库文件VO
 * 
 * @author licza
 * 
 */
public class PtDbfileVO extends SuperVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4966146255227942683L;
	/**
	 * 主键 与LFWFileVO的主键相同
	 */
	private String pk_dbfile;
	/**
	 * 文件内容
	 */
	private byte[] filecontent;
	
	/**------------------------------------**/
	
	private UFDateTime ts;
	private Integer dr;

	/**
	 * 写入内容
	 * 
	 * @param output
	 */
	public void doGetOutPutStream(OutputStream output) {
		if(output == null)
			return;
		try {
			IOUtils.write(filecontent, output);
		} catch (IOException e) {
			LfwLogger.error(e.getMessage(), e);
		}
	}

	/**
	 * 读入内容
	 * 
	 * @param input
	 */
	public void doSetInputStream(InputStream input) {
		if(input == null)
			return;
		try {
			filecontent = IOUtils.toByteArray(input);
		} catch (IOException e) {
			LfwLogger.error(e.getMessage(), e);
		}
	}

	public String getPk_dbfile() {
		return pk_dbfile;
	}

	public void setPk_dbfile(String pk_dbfile) {
		this.pk_dbfile = pk_dbfile;
	}

	public byte[] getFilecontent() {
		return filecontent;
	}

	public void setFilecontent(byte[] filecontent) {
		this.filecontent = filecontent;
	}

	public PtDbfileVO(String pk_dbfile) {
		super();
		this.pk_dbfile = pk_dbfile;
	}

	public PtDbfileVO() {
	}

	@Override
	public String getPKFieldName() {
		return "pk_dbfile";
	}

	@Override
	public String getTableName() {
		return "pt_dbfile";
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

}
