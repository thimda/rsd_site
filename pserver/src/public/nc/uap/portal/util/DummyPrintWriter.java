package nc.uap.portal.util;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;

public final class DummyPrintWriter extends PrintWriter
{
    private static final DummyPrintWriter instance = new DummyPrintWriter(DummyServletOutputStream.getInstance());
    
    public static PrintWriter getInstance()
    {
        return instance;
    }
    
	private DummyPrintWriter(OutputStream out)
	{
		super(out);
	}
	
	@Override
	public PrintWriter append(char c) {
		return this;
	}

	@Override
	public PrintWriter append(CharSequence csq, int start, int end) {
		return this;
	}

	@Override
	public PrintWriter append(CharSequence csq) {
		return this;
	}

	@Override
	public boolean checkError() {
		return false;
	}

	@Override
	public void close() {
	}

	@Override
	public void flush() {
	}

	@Override
	public PrintWriter format(Locale l, String format, Object... args) {
		return this;
	}

	@Override
	public PrintWriter format(String format, Object... args) {
		return this;
	}

	@Override
	public void print(boolean b) {
	}

	@Override
	public void print(char c) {
	}

	@Override
	public void print(char[] s) {
	}

	@Override
	public void print(double d) {
	}

	@Override
	public void print(float f) {
	}

	@Override
	public void print(int i) {
	}

	@Override
	public void print(long l) {
	}

	@Override
	public void print(Object obj) {
	}

	@Override
	public void print(String s) {
	}

	@Override
	public PrintWriter printf(Locale l, String format, Object... args) {
		return this;
	}

	@Override
	public PrintWriter printf(String format, Object... args) {
        return this;
	}

	@Override
	public void println() {
	}

	@Override
	public void println(boolean x) {
	}

	@Override
	public void println(char x) {
	}

	@Override
	public void println(char[] x) {
	}

	@Override
	public void println(double x) {
	}

	@Override
	public void println(float x) {
	}

	@Override
	public void println(int x) {
	}

	@Override
	public void println(long x) {
	}

	@Override
	public void println(Object x) {
	}

	@Override
	public void println(String x) {
	}

	@Override
	protected void setError() {
	}

	@Override
	public void write(char[] buf, int off, int len) {
	}

	@Override
	public void write(char[] buf) {
	}

	@Override
	public void write(int c) {
	}

	@Override
	public void write(String s, int off, int len) {
	}

	@Override
	public void write(String s) {
	}
}
