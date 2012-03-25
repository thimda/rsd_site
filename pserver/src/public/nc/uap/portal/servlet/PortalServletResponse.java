package nc.uap.portal.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;

public class PortalServletResponse
    extends javax.servlet.http.HttpServletResponseWrapper {

    private StringWriter buffer = null;
    private PrintWriter writer = null;

    public PortalServletResponse(HttpServletResponse response) {
        super(response);
        buffer = new StringWriter();
        writer = new PrintWriter(buffer);
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public StringWriter getInternalBuffer() {
        return buffer;
    }

    public PrintWriter getInternalResponseWriter()
        throws IOException {
        return super.getWriter();
    }

    @Override
    public void flushBuffer() throws IOException
    {
    }

    @Override
    public int getBufferSize()
    {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isCommitted()
    {
        return false;
    }

    @Override
    public void reset()
    {
        resetBuffer();
    }

    @Override
    public void resetBuffer()
    {
        buffer.getBuffer().setLength(0);
    }

    @Override
    public void setBufferSize(int size)
    {
    }
}
