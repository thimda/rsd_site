package nc.uap.portal.util;

import java.io.IOException;

import javax.servlet.ServletOutputStream;

public final class DummyServletOutputStream extends ServletOutputStream{

    private static final ServletOutputStream instance = new DummyServletOutputStream();
    
    public static ServletOutputStream getInstance()
    {
        return instance;
    }
    
    private DummyServletOutputStream()
    {
    }
    
	@Override
	public void write(int b) throws IOException {
	}

	@Override
	public void print(boolean arg0) throws IOException {
	}

	@Override
	public void print(char arg0) throws IOException {
	}

	@Override
	public void print(double arg0) throws IOException {
	}

	@Override
	public void print(float arg0) throws IOException {
	}

	@Override
	public void print(int arg0) throws IOException {
	}

	@Override
	public void print(long arg0) throws IOException {
	}

	@Override
	public void print(String arg0) throws IOException {
	}

	@Override
	public void println() throws IOException {
	}

	@Override
	public void println(boolean arg0) throws IOException {
	}

	@Override
	public void println(char arg0) throws IOException {
	}

	@Override
	public void println(double arg0) throws IOException {
	}

	@Override
	public void println(float arg0) throws IOException {
	}

	@Override
	public void println(int arg0) throws IOException {
	}

	@Override
	public void println(long arg0) throws IOException {
	}

	@Override
	public void println(String arg0) throws IOException {
	}

	@Override
	public void close() throws IOException {
	}

	@Override
	public void flush() throws IOException {
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
	}

	@Override
	public void write(byte[] b) throws IOException {
	}

	
}
