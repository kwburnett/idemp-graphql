package org.bandahealth.idempiere.graphql.filter;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * This class is meant to allow multiple reads from the servlet request body, which is done by the Authentication
 * filter, followed by the GraphQL servlet reader
 * Borrowed from: https://blog.csdn.net/zmj199536/article/details/102940047
 */
public class BandaServletRequestWrapper extends HttpServletRequestWrapper {

	private final byte[] body;

	/**
	 * Constructs a request object wrapping the given request.
	 *
	 * @param request the {@link HttpServletRequest} to be wrapped.
	 * @throws IllegalArgumentException if the request is null
	 */
	public BandaServletRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		body = readBytes(request.getReader(), "utf-8");
	}

	public BandaServletRequestWrapper(ServletRequest request) throws IOException {
		this((HttpServletRequest) request);
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		final ByteArrayInputStream bais = new ByteArrayInputStream(body);
		return new ServletInputStream() {

			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener listener) {

			}

			@Override
			public int read() throws IOException {
				return bais.read();
			}
		};
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	private byte[] readBytes(BufferedReader br, String encoding) throws IOException {
		String str = null;
		StringBuilder retStr = new StringBuilder();
		while ((str = br.readLine()) != null) {
			retStr.append(str);
		}
		if (StringUtils.isNotBlank(retStr.toString())) {
			return retStr.toString().getBytes(Charset.forName(encoding));
		}
		return null;
	}
}
