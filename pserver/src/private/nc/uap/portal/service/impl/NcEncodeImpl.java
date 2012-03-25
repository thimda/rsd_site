package nc.uap.portal.service.impl;

import nc.uap.portal.service.itf.IEncodeService;
import nc.vo.framework.rsa.Encode;
import nc.vo.pub.BusinessException;


public class NcEncodeImpl implements IEncodeService {

	public String encode(String str) throws BusinessException {
		Encode ncencode = new Encode();
		return ncencode.encode(str);
	}

	public String decode(String str) throws BusinessException {
		Encode ncencode = new Encode();
		return ncencode.decode(str);
	}
}
