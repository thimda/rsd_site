package nc.uap.portal.util;

import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import nc.uap.lfw.core.log.LfwLogger;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 * <b>JAXB解析器工厂</b>
 * <p>
 * 避免重复创建marshal
 * </p>
 * 
 * @author licza
 * 
 */
public class JaxbMarshalFactory {
	
	/**
	 * 工厂
	 */
	private static JaxbMarshalFactory factory;
	private static ThreadLocal<JaxbMarshalBox> marshalLocal = new ThreadLocal<JaxbMarshalBox>(){
		@Override
		protected JaxbMarshalBox initialValue() {
			return new JaxbMarshalBox();
		}
		
	};
	
	private JaxbMarshalFactory() {
	
	}

	public static JaxbMarshalFactory newIns() {
		if (factory == null) {
			synchronized (JaxbMarshalFactory.class) {
				if (factory == null) {
					factory = new JaxbMarshalFactory();
				}
			}
		}
		return factory;
	}

	/**
	 * lookup a Marshaller object that can be used to convert a java content
	 * tree into XML data.
	 * 
	 * @param clazz
	 * @return a Marshaller object
	 */
	public Marshaller lookupMarshaller(Class<?> clazz) {
		String key = clazz.getName();
		if (!marshalLocal.get().getMarshal().containsKey(key)) {
			try {
				JAXBContext jc = JAXBContext.newInstance(clazz);
				Marshaller value = jc.createMarshaller();
				value.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				marshalLocal.get().getMarshal().put(key, value);
			} catch (JAXBException e) {
				LfwLogger.error("JAXBContext 初始化失败 .class name: " + key, e);
				throw new IllegalArgumentException("传入的类: " + key + "不是一个可序列化的类:" + e.getMessage());
			}
		}
		return marshalLocal.get().getMarshal().get(key);
	}

	/**
	 * lookup an Unmarshaller object that can be used to convert XML data into a
	 * java content tree.
	 * 
	 * @param clazz
	 * @return Unmarshaller Object
	 */
	public Unmarshaller lookupUnMarshaller(Class<?> clazz) {
		String key = clazz.getName();
		if (!marshalLocal.get().getUnmarshal().containsKey(key)) {
			try {
				JAXBContext jc = JAXBContext.newInstance(clazz);
				Unmarshaller value = jc.createUnmarshaller();
				marshalLocal.get().getUnmarshal().put(key, value);
			} catch (JAXBException e) {
				LfwLogger.error("JAXBContext 初始化失败 class name: " + key, e);
				throw new IllegalArgumentException("传入的类: " + key + "不是一个可序列化的类:" + e.getMessage());
			}
		}
		return marshalLocal.get().getUnmarshal().get(key);
	}
	
	/**
	 * 反序列化XML
	 * @param jaxbElement
	 * @return
	 */
	public String decodeXML(Serializable jaxbElement){
		StringWriter writer = null;
		if(jaxbElement == null)
			return StringUtils.EMPTY;
		Class<?> clazz = jaxbElement.getClass();
		 try {
			writer = new StringWriter();
			this.lookupMarshaller(clazz).marshal(jaxbElement, writer);
			return writer.toString();
		} catch (JAXBException e) {
			LfwLogger.error("XML反序列化出现异常 : Class : " + clazz.getName() + ";Error message: " + e.getMessage()  ,e);
			return StringUtils.EMPTY;
		}finally{
			IOUtils.closeQuietly(writer);
		}
	}
	
	/**
	 * 序列化一个XML
	 * @param <T>
	 * @param clazz
	 * @param xml
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T encodeXML(Class<T> clazz , String xml){
		if(xml == null)
			return null;
		Reader reader = new StringReader(xml);
		try {
			return (T) this.lookupUnMarshaller(clazz).unmarshal(reader);
		} catch (Exception e) {
			LfwLogger.error("XML序列化出现异常 : Class : " + clazz.getName() + ";Error message: " + e.getMessage() +"XML:" + xml ,e);
			return null;
		}finally{
			IOUtils.closeQuietly(reader);
		}
	}
}
class JaxbMarshalBox{
	private Map<String, Marshaller> marshal =  Collections.synchronizedMap(new HashMap<String, Marshaller>());
	private Map<String, Unmarshaller> unmarshal =  Collections.synchronizedMap(new HashMap<String, Unmarshaller>());
	public Map<String, Marshaller> getMarshal() {
		return marshal;
	}
	public Map<String, Unmarshaller> getUnmarshal() {
		return unmarshal;
	}
}