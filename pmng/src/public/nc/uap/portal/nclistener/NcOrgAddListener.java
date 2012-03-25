package nc.uap.portal.nclistener;

import nc.bs.businessevent.IBusinessEvent;
import nc.bs.businessevent.IBusinessListener;
import nc.bs.businessevent.bd.BDCommonEvent;
import nc.bs.businessevent.bd.BDCommonEvent.BDCommonUserObj;
import nc.vo.pub.BusinessException;

public class NcOrgAddListener implements IBusinessListener{
 
	@Override
	public void doAction(IBusinessEvent event) throws BusinessException {
		BDCommonEvent bevent = (BDCommonEvent) event;
		BDCommonUserObj ncorgvo = (BDCommonUserObj) bevent.getUserObject();
	}
}
