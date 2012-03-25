package nc.uap.portal.container.portlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.portlet.PortletURL;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;

/**
 * 资源响应实现
 * @author licza
 *
 */
public class ResourceResponseImpl extends MimeResponseImpl implements
		ResourceResponse {
	public ResourceResponseImpl(PortletResponseContext context,String requestCacheLevel) {
		super(context);
		this.requestCacheLevel = requestCacheLevel == null ? ResourceURL.PAGE
				: requestCacheLevel;
	}

	private static final String DEFAULT_CONTAINER_CHARSET = "UTF-8";

//	private boolean canSetLocaleEncoding = true;
	private String charset;
	private String requestCacheLevel;

 

	@Override
	public PortletURL createActionURL() {
		if (ResourceURL.PAGE.equals(requestCacheLevel)) {
			return new PortletURLImpl(null);
		}
		throw new IllegalStateException(
				"Not allowed to create an ActionURL with current request cacheability level "
						+ requestCacheLevel);
	}

	@Override
	public PortletURL createRenderURL() {
		if (ResourceURL.PAGE.equals(requestCacheLevel)) {
			return new PortletURLImpl(null);
		}
		throw new IllegalStateException(
				"Not allowed to create a RenderURL with current request cacheability level "
						+ requestCacheLevel);
	}

	@Override
	public ResourceURL createResourceURL() {
		return new PortletURLImpl(null);
	}

	@Override
	public PrintWriter getWriter() throws IllegalStateException, IOException {
		if (charset == null) {
			// enforce the default Container encoding == UTF-8 if encoding
			// hasn't been set yet.
			setCharacterEncoding(DEFAULT_CONTAINER_CHARSET);
		}
		return super.getWriter();
	}

	@Override
	public String getCharacterEncoding() {
		return charset != null ? charset : DEFAULT_CONTAINER_CHARSET;
	}

	public void setCharacterEncoding(String encoding) {
		// ensure client hasn't passed us the full name/value pair; i.e
		// charset=utf-8
		int index = encoding.indexOf('=');
		if (index != -1 && index < encoding.length() - 1) {
			encoding = encoding.substring(index + 1).trim();
		}

		if (encoding != null && encoding.length() > 0) {
			this.charset = encoding;
			response.setCharacterEncoding(charset);
//			canSetLocaleEncoding = false;
		}
	}

	@Override
	public void setContentType(String contentType) {
		if (contentType != null) {
			int index = contentType.indexOf(';');
			if (index != -1 && index < contentType.length() - 1) {
				String encoding = contentType.substring(index + 1).trim();
				if (encoding.length() > 0) {
					setCharacterEncoding(encoding);
				}
			}
			super.setContentType(contentType);
		}
	}

	public void setContentLength(int len) {
		response.setContentLength(len);
	}

	public void setLocale(Locale locale) {
		if (locale != null) {
			response.setLocale(locale);
//			if (canSetLocaleEncoding) {
//				String encoding = (String)getPortletWindow().getPortletDefinition()
//						.getApplication().getLocaleEncodingMappings().get(
//								locale);
//				if (encoding != null) {
//					setCharacterEncoding(encoding);
//					// allow repeated setLocale usage for characterEncoding
//					canSetLocaleEncoding = true;
//				}
//			}
		}
	}
}
