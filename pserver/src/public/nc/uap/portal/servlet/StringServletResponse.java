package nc.uap.portal.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
/**
 * 此Response封装可方便获取输出流内容
 * @author dengjt
 * 2006-2-24
 */
public class StringServletResponse extends HttpServletResponseWrapper {
	//内置输出流
	private ByteArrayOutputStream baoStream = new ByteArrayOutputStream();

	private ServletOutputStream output = new StringServletOutputStream(baoStream);

	private int status = SC_OK;
    //内置输出writer
	private StringWriter sw = new StringWriter();

	private PrintWriter pw = new PrintWriter(sw);

	private boolean isUseStream;

	private boolean isUseWriter;
	
	public StringServletResponse(HttpServletResponse res) {
		super(res);
	}
	
	@Override
	public void flushBuffer() throws IOException {
		
	}

	@Override
	public boolean isCommitted() {
		return false;
	}

	public String getContentType() {
		return super.getContentType();
	}

	public void setContentType(String contentType) {
		super.setContentType(contentType);
	}

	public void setLocale(Locale locale) {
		super.setLocale(locale);
	}

	public ServletOutputStream getOutputStream() {
		isUseStream = true;
		return output;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getString() throws UnsupportedEncodingException {
		if (isUseStream) {
			return baoStream.toString();
		} 
		else if (isUseWriter) {
			return sw.toString();
		} 
		else {
			return "";
		}
	}

	public PrintWriter getWriter() {
		isUseWriter = true;
		return pw;
	}

	public void reset() {
		super.reset();
		baoStream.reset();
		status = SC_OK;
		sw = new StringWriter();
		pw = new PrintWriter(sw);
		isUseStream = false;
		isUseWriter = false;
	}

	@Override
	public void resetBuffer() {
		baoStream.reset();
		status = SC_OK;
		sw = new StringWriter();
		pw = new PrintWriter(sw);
		isUseStream = false;
		isUseWriter = false;
	}
	
}