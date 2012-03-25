package nc.uap.portal.service.itf;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.vo.PtDeviceVO;
public interface IPtDeviceQryService {
	PtDeviceVO getDeviceVoByPk(String devicePk) throws PortalServiceException;
	PtDeviceVO[] getAllDevice() throws PortalServiceException;
}
