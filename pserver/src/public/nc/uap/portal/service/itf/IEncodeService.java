package nc.uap.portal.service.itf;

import nc.vo.pub.BusinessException;

public interface IEncodeService {
	public String encode(String str) throws BusinessException;
	public String decode(String str) throws BusinessException;
}
