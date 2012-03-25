package nc.uap.portal.testcase;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.test.AbstractTestCase;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.integrate.message.vo.PtMessageVO;
import nc.uap.portal.service.itf.IPtMessageCenterService;
import nc.vo.pub.lang.UFDateTime;

/**
 * 消息中心测试用例
 * 
 * @author licza
 * 
 */
public class MessageCenterTest extends AbstractTestCase {
	public void testAddMessage() throws PortalServiceException {
		IPtMessageCenterService imcs = NCLocator.getInstance().lookup(IPtMessageCenterService.class);
		for(int i=0;i<10;i++){
			PtMessageVO vo = mockMessageVO();
			imcs.add(vo);
		}
		
	}

	public PtMessageVO mockMessageVO() {
		PtMessageVO vo = new PtMessageVO();
		vo.setPk_user("00000000");
		vo.setTitle("test"+System.currentTimeMillis());
		vo.setMsgtype(0);
		vo.doSetContent("mmm");
		vo.setPk_sender("0000001");
		vo.setState("0");
		vo.setPriority(1);
		vo.setSendtime(new UFDateTime());
		vo.setSystemcode("workflow");
		return vo;
	}
}
