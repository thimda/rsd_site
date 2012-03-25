package nc.uap.portal.container.portlet;

import java.io.IOException;

import javax.portlet.ActionResponse;

import nc.uap.portal.container.driver.ResourceURLProvider;
import nc.uap.portal.util.ArgumentUtility;

public class ActionResponseImpl extends StateAwareResponseImpl implements
		ActionResponse {
	public ActionResponseImpl(StateAwareResponseContext context) {
		super(context);
	}

	private boolean stateChanged;
	protected boolean redirected;

	protected void checkSetRedirected() {
		if (stateChanged) {
			throw new IllegalStateException(
					"sendRedirect no longer allowed after navigational state changes");
		}
		if (redirected) {
			throw new IllegalStateException("sendRedirect already called");
		}
		redirected = true;
	}

	protected void checkSetStateChanged() {
		if (redirected) {
			throw new IllegalStateException(
					"State change no longer allowed after a sendRedirect");
		}
		stateChanged = true;
	}

	protected String getRedirectLocation(String location) {
		ArgumentUtility.validateNotEmpty("location", location);
		ResourceURLProvider provider = new ResourceURLProvider();

		if (location.indexOf("://") != -1) {
			provider.setAbsoluteURL(location);
		} else {
			provider.setFullPath(location);
		}
		location = getServletResponse().encodeRedirectURL(provider.toString());
		if (location.indexOf("/") == -1) {
			throw new IllegalArgumentException(
					"There is a relative path given, an IllegalArgumentException must be thrown.");
		}
		return location;
	}

	public void sendRedirect(String location) throws IOException {
		location = getRedirectLocation(location);
		
	}

	public void sendRedirect(String location, String renderUrlParamName)
			throws IOException {
		ArgumentUtility.validateNotEmpty("renderUrlParamName",
				renderUrlParamName);
		location = getRedirectLocation(location);
		if (!redirected) {
			stateChanged = false;
		}
		checkSetRedirected();
		// ((PortletActionResponseContext)getResponseContext()).setRedirect(location,
		// renderUrlParamName);
	}

	public boolean isRedirect() {
		return redirected;
	}

}
