package nc.uap.portal.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.uap.lfw.core.log.LfwLogger;

/**
 * 常量处理工具 将类中常量转换为Key-Value对集合
 * 供前台调用
 * @author licza
 * 
 */
public class ConstantUtil {
	/**
	 * 将类中常量转换为Key-Value对集合
	 * @param <T> 
	 * @param t
	 * @return Key-Value对集合
	 */
	public static <T> Set<Map<String,String>> shatter(Class<T> t) {
		Set<Map<String,String>> fieldValSet=new HashSet<Map<String,String>>();
		try {
		Field[]	fieldArray=	t.getFields();
		 for(Field field:fieldArray){
			 String fieldName= field.getName();
			 String fieldVal=	 (String)field.get(fieldName);
			 Map<String,String> fieldMap=new HashMap<String,String>();
			 fieldMap.put("fieldName", fieldName);
			 fieldMap.put("fieldVal",fieldVal );
			 fieldValSet.add(fieldMap);
		 }
		} catch ( Exception e) {
			LfwLogger.info(e.getMessage());
		} 
		return fieldValSet;
	}
}
