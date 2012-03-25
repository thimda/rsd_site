package nc.uap.portal.deploy;

import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.cluster.itf.ClusterMessage;
import nc.bs.framework.cluster.itf.ClusterMessageHeader;

/**
 * Portal部署消息
 * @author licza
 *
 */
public class PortalDeployMessage implements ClusterMessage{
	private String messageId;
	private Map<String,String> head = new HashMap<String, String>();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4218482424882410121L;

	@Override
	public boolean addHeader(ClusterMessageHeader header) {
		return false;
	}

	@Override
	public boolean addHeader(String name, String value) {
		if(name == null || value == null)
			throw new NullPointerException("设置的消息头信息为空");
		this.head.put(name, value);
		return false;
	}

	@Override
	public ClusterMessageHeader getHeader(String headerName) {
		return null;
	}

	@Override
	public String getHeaderValue(String name) {
		return this.head.get(name);
	}

	@Override
	public ClusterMessageHeader[] getHeaders() {
		return null;
	}

	@Override
	public String getMessageId() {
		return this.messageId;
	}

	@Override
	public void setMessateId(String messageId) {
		this.messageId = messageId;
	}

}
