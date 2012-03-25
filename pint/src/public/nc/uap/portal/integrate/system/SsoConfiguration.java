package nc.uap.portal.integrate.system;

import java.util.ArrayList;
import java.util.List;

import nc.uap.portal.integrate.system.IpReference;
import nc.uap.portal.integrate.system.Reference;
import nc.uap.portal.integrate.system.SSOProviderVO;
import nc.uap.portal.integrate.system.SsoConfigItem;

/**
 * sso配置对外工具
 * 
 * @author gd 2009-01-03
 * @version NC5.5
 * @since NC5.5
 */
public class SsoConfiguration { 
	/**
	 * 筛选IP映射配置
	 * @param systemCode
	 * @param ssoProviders
	 * @return
	 */
	public static SsoConfigItem[] selectIpMappingConfigItems(String systemCode ,SSOProviderVO[] ssoProviders  ){
		if(systemCode != null)
		{
				for(SSOProviderVO provider:ssoProviders)
				{
					if(provider.getSystemCode().equals(systemCode))
					{
						ArrayList<SsoConfigItem> items = new ArrayList<SsoConfigItem>();
						List<IpReference> ipRef = provider.getMappingReference();
						if(ipRef != null && ipRef.size() > 0)
						{
							for(int i = 0; i < ipRef.size(); i++)
							{
								IpReference ref = ipRef.get(i);
								if(ref.getSourceIp() != null && !"".equals(ref.getSourceIp()))
								{
									SsoConfigItem item = new SsoConfigItem();
									item.setKey(ref.getSourceIp());
									item.setValue(ref.getTargetIp());
									items.add(item);
								}
							}
							return items.toArray(new SsoConfigItem[0]);
						}
				}
			}
		}
		return new SsoConfigItem[0];
	}
	/**
	 * 筛选配置项目
	 * @param systemCode
	 * @param ssoProviders
	 * @return
	 */
	public static SsoConfigItem[] selectReferenceConfigItems(String systemCode ,SSOProviderVO[] ssoProviders ) {
		if(systemCode != null)
		{
				for(SSOProviderVO provider:ssoProviders)
				{
					if(provider.getSystemCode().equals(systemCode))
					{
						ArrayList<SsoConfigItem> items = new ArrayList<SsoConfigItem>();
						List<Reference> refs = provider.getProviderReference();
						if(refs!= null && refs.size() > 0)
						{
							for(int i = 0; i < refs.size(); i++)
							{
								Reference ref = refs.get(i);
								SsoConfigItem item = new SsoConfigItem();
								item.setKey(ref.getName());
								item.setValue(ref.getValue());
								items.add(item);
							}
							return items.toArray(new SsoConfigItem[0]);
						}
					}
			}
		}
		return new SsoConfigItem[0];
	}
}
