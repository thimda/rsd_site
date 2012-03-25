package nc.uap.portal.sso.util;

import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import javax.xml.bind.JAXBException;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.integrate.itf.IPtSsoConfigQryService;
import nc.uap.portal.integrate.system.SSOProviderVO;
import nc.uap.portal.integrate.system.SSOReference;
import nc.uap.portal.util.JaxbMarshalFactory;
import nc.uap.portal.vo.PtSsopropVO;
import nc.vo.pub.lang.UFBoolean;

import org.apache.commons.io.IOUtils;

/**
 * SSO配置文件工具类
 * @author licza
 *
 */
public class SSOUtil {
	/**
	 * 将SSO配置VO转换为sso配置
	 * @param vo
	 * @return
	 */
	public static SSOProviderVO prop2provider(PtSsopropVO vo){
		SSOProviderVO p = new SSOProviderVO();
		p.setAuthClass(vo.getAuthclass());
		p.setEnableMapping(vo.getEnablemapping() == null ? false : vo.getEnablemapping().booleanValue());
		p.setGateUrl(vo.getGateurl());
		if(vo.getRef() != null){
			p.setMappingReference(vo.getRef().getIpReference());
			p.setProviderReference(vo.getRef().getReference());
		}
		p.setNodesClass(vo.getNodesclass());
		p.setSystemCode(vo.getSystemcode());
		p.setSystemName(vo.getSystemname());
		return p;
	}
	
	/**
	 * 将sso配置转换为SSO配置VO
	 * @param prop
	 * @return
	 */
	public static PtSsopropVO provider2prop(SSOProviderVO p){
		PtSsopropVO vo = new PtSsopropVO();
		vo.setAuthclass(p.getAuthClass());
		vo.setEnablemapping(UFBoolean.valueOf(p.isEnableMapping()));
		vo.setGateurl(p.getGateUrl());
		vo.setNodesclass(p.getNodesClass());
		vo.setSystemcode(p.getSystemCode());
		vo.setSystemname(p.getSystemName());
		SSOReference sr = new SSOReference();
		sr.setIpReference(p.getMappingReference());
		sr.setReference(p.getProviderReference());
		String reference = null;
		Writer writer = new StringWriter();
			try {
				JaxbMarshalFactory.newIns().lookupMarshaller(SSOReference.class).marshal(sr, writer);
			} catch (JAXBException e) {
				LfwLogger.error("jaxb序列化sso配置文件错误",e);
			}
		if(writer != null){
			reference = writer.toString();
			IOUtils.closeQuietly(writer);
		}
		vo.setReference(reference);
		vo.setRef(sr);
		return vo;
	}
	
	public static SSOProviderVO getProviderVOBySystemCode(String systemCode){
		SSOProviderVO provider = null;
		IPtSsoConfigQryService ssoQry = NCLocator.getInstance().lookup(IPtSsoConfigQryService.class);
		try {
			List<SSOProviderVO> list = ssoQry.getAllConfig();
			for (SSOProviderVO vo : list) {
				if (vo.getSystemCode().equals(systemCode)) {
					provider = vo;
					break;
				}
			}
		} catch (PortalServiceException e) {
			LfwLogger.error(e);
		}
		if(provider == null){
			throw new LfwRuntimeException("SSOProviderVO对象为空!");
		}
		return provider;
	}
	
}
