package nc.uap.portal.msg.provide.impl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.math.RandomUtils;

import nc.uap.lfw.core.cmd.base.FromWhereSQL;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.msg.model.Msg;
import nc.uap.portal.msg.model.MsgBox;
import nc.uap.portal.msg.model.MsgCategory;
import nc.uap.portal.msg.provide.IMsgCmd;
import nc.uap.portal.msg.provide.IMsgProvide;
import nc.uap.portal.util.JaxbMarshalFactory;
import nc.vo.pub.lang.UFDateTime;

/**
 * 模拟消息提供者
 * 
 * @author licza
 * 
 */
public class MockMsgProvideImpl implements IMsgProvide{
	
	private static HashMap<String, Msg> msgs = new HashMap<String, Msg>();
	static {
		for (int i = 0; i < 10; i++) {
			Msg msg = fillData(Msg.class);
			msgs.put(msg.getId(), msg);
		}

	}

	static <T> T fillData(Class<T> clazz) {
		Object o = null;
		try {
			o = clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		Method[] mds = clazz.getMethods();
		for (Method md : mds) {
			String mdm = md.getName();
			if (mdm.startsWith("set")) {
				if (md.getParameterTypes() != null
						&& md.getParameterTypes().length > 0) {
					Object[] param = new Object[md.getParameterTypes().length];
					for (int i = 0; i < md.getParameterTypes().length; i++) {
						Class<?> cls = md.getParameterTypes()[i];
						if (cls.getName().toLowerCase().equals(
								"java.lang.string")) {
							param[i] = "" + new Random().nextFloat();
						}
					}
					try {
						md.invoke(o, param);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return (T) o;
	}

	@Override
	public Msg get(String id) {
		return msgs.get(id);
	}

	@Override
	public List<MsgCategory> getCategory() {
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("nc/uap/portal/testcase/oa.xml");
		String xml = null;
		try {
			xml = IOUtils.toString(in,"utf-8");
		} catch (IOException e) {
			LfwLogger.error(e.getMessage(), e);
		}
		MsgCategory mc = JaxbMarshalFactory.newIns().encodeXML(MsgCategory.class, xml);
		List<MsgCategory> mcs = new ArrayList<MsgCategory>();
		mcs.add(mc);
		return mc.getChild();
	}

	@Override
	public IMsgCmd getCmd(String id) {
		return null;
	}

	@Override
	public Integer getNewMessageCount(String category) {
		return RandomUtils.nextInt(120);
	}

	@Override
	public Msg[] query(String category, UFDateTime start, UFDateTime end,
			MsgBox box, PaginationInfo pi, FromWhereSQL whereSql) {
		return msgs.values().toArray(new Msg[0]);
	}

	@Override
	public void afterCategoryInit(MsgCategory currentCategory) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeCategoryInit(MsgCategory currentCategory) {
		// TODO Auto-generated method stub
		
	}

	 
 
}
