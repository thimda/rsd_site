package nc.uap.portal.integrate.credential;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.patch.XmlUtilPatch;
import nc.uap.lfw.util.Validator;
import nc.uap.portal.exception.PortalServiceException;
import nc.vo.jcom.xml.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 凭证Reference XML存储序列化工具
 * 序列化后的XML不是存储在文件中，而是存储在数据库中，故序列化后转成字符串存储
 * XML如下格式：
 * 	<cpreferences>
 *		<cpreference>
 *			<name>accountcode</name>
 *			<value>2005-12-18</value>
 *		</cpreference>
 *		<cpreference>
 *			<name>corppk</name>
 *			<value>1001</value>
 *		</cpreference>
 *		……
 *	</cpreferences>
 * @author yzy
 *  Created on 2006-02-26
 */
public class CredentialReferenceSerializer {

	/**
	 * 将序列化后的XML还原为Reference对象
	 * @param xml 序列化为字符串的XML
	 * @return 凭证的Reference对象
	 * @throws PortalServiceException
	 */
	public static ICredentialReference fromXML(String xml)
			throws PortalServiceException {
		ICredentialReference cri = new CredentialReferenceImpl();
		if (Validator.isNull(xml)) {
			return cri;
		}
		try {
			//实例化Document
			Document document = XmlUtilPatch.getDocumentBuilder().parse(new InputSource(new StringReader(xml)));
			NodeList nodeList = document.getElementsByTagName("cpreference");
			for (int i=0;i<nodeList.getLength();i++) {
				Element rfe = (Element)nodeList.item(i);
				String name = XMLUtil.getChildNodeValueOf(rfe, "name");
				String value = XMLUtil.getChildNodeValueOf(rfe, "value");
				cri.setValue(name, value);
			}
			return cri;
		} catch (IOException e) {
			Logger.error(e.getMessage(), e);
			throw new PortalServiceException(e);
		} catch (SAXException e) {
			Logger.error(e.getMessage(), e);
			throw new PortalServiceException(e);
		}
	}

	/**
	 * 将凭证的Reference对象序列化为XML存储
	 * @param cri 凭证Reference对象
	 * @return 序列化为XML的字符串
	 * @throws PortalServiceException
	 */
	public static String toXML(ICredentialReference cri)
			throws PortalServiceException {
		StringBuffer xmlContext = new StringBuffer("");
		xmlContext.append("<cpreferences>");
		Iterator itr = cri.getMap().entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry entry = (Map.Entry) itr.next();
			xmlContext.append("<cpreference>");
			String nameText = (String) entry.getKey();
			String valueText = cri.getValue(nameText);
			xmlContext.append("<name>");
			xmlContext.append(nameText);
			xmlContext.append("</name>");
			xmlContext.append("<value>");
			if (valueText != null && valueText.trim().length() > 0) {
				xmlContext.append(valueText);
			}
			xmlContext.append("</value>");
			xmlContext.append("</cpreference>");
		}
		xmlContext.append("</cpreferences>");
		return xmlContext.toString();
	}
}
