package nc.uap.portal.container.om;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Portlet∏Ωº”–≈œ¢
 * 
 * @author licza
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PortletAdjunct", propOrder = { "supportedProcessingEvent",
		"supportedPublishingEvent", "supportedPublicRenderParameter" })
public class PortletAdjunct implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5306853705668330200L;
	@XmlElement(name = "supported-processing-event")
	protected List<EventDefinitionReference> supportedProcessingEvent;
	@XmlElement(name = "supported-publishing-event")
	protected List<EventDefinitionReference> supportedPublishingEvent;
	@XmlElement(name = "supported-public-render-parameter")
	protected List<String> supportedPublicRenderParameter;

	public List<EventDefinitionReference> getSupportedProcessingEvent() {
		return supportedProcessingEvent;
	}

	public void setSupportedProcessingEvent(
			List<EventDefinitionReference> supportedProcessingEvent) {
		this.supportedProcessingEvent = supportedProcessingEvent;
	}

	public List<EventDefinitionReference> getSupportedPublishingEvent() {
		return supportedPublishingEvent;
	}

	public void setSupportedPublishingEvent(
			List<EventDefinitionReference> supportedPublishingEvent) {
		this.supportedPublishingEvent = supportedPublishingEvent;
	}

	public List<String> getSupportedPublicRenderParameter() {
		return supportedPublicRenderParameter;
	}

	public void setSupportedPublicRenderParameter(
			List<String> supportedPublicRenderParameter) {
		this.supportedPublicRenderParameter = supportedPublicRenderParameter;
	}

}
