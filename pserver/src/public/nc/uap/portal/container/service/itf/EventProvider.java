package nc.uap.portal.container.service.itf;

import java.io.Serializable;

import javax.portlet.Event;
import javax.xml.namespace.QName;

/**
 *事件生产接口
 * 
 * @author licza
 * 
 */
public interface EventProvider {
	/**
	 * 创建一个事件
	 * 
	 * @param name 事件名称
	 * @param value 参数
	 * @return
	 * @throws IllegalArgumentException 参数异常
	 */
	Event createEvent(QName name, Serializable value) throws IllegalArgumentException;
}
