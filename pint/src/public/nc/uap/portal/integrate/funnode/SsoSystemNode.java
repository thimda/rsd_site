package nc.uap.portal.integrate.funnode;

/**
 * 系统的通用节点VO
 * @author gd 2009-5-20
 * @since NC5.6
 * @version NC5.6
 */
public class SsoSystemNode {
	// 节点主键
	private String nodeId;
	// 节点名称
	private String nodeName;
	// 节点编码
	private String nodeCode;
	// 父节点主键 
	private String pNodeId;
	// 节点路径
	private String nodeUrl;
	// 节点类型 0:链接类型 1:执行脚本类型
	private Integer nodeType;
	
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getNodeCode() {
		return nodeCode;
	}
	public void setNodeCode(String nodeCode) {
		this.nodeCode = nodeCode;
	}
	public String getPNodeId() {
		return pNodeId;
	}
	public void setPNodeId(String nodeId) {
		pNodeId = nodeId;
	}
	public String getNodeUrl() {
		return nodeUrl;
	}
	public void setNodeUrl(String nodeUrl) {
		this.nodeUrl = nodeUrl;
	}
	public Integer getNodeType() {
		return nodeType;
	}
	public void setNodeType(Integer nodeType) {
		this.nodeType = nodeType;
	}
}
