package com.ibm.app.services.ws;

import static com.ibm.app.services.ws.HttpHeaders.ACCEPT_ENCODING;
import static com.ibm.app.services.ws.HttpHeaders.CONTENT_ENCODING;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.DeflaterInputStream;
import java.util.zip.GZIPInputStream;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

public class EnableServerCompressionInterceptor implements
		ClientHttpRequestInterceptor {
	static final String ENCODING_GZIP = "gzip";
	static final String ENCODING_DEFLATE = "deflate";

	static final String ACCEPT_ENCODING_VALUE = ENCODING_GZIP + ","
			+ ENCODING_DEFLATE;

	private boolean requestCompression;

	public EnableServerCompressionInterceptor(boolean requestCompression) {
		this.requestCompression = requestCompression;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body,
			ClientHttpRequestExecution execution) throws IOException {
		HttpRequestWrapper wrapper = new HttpRequestWrapper(request);
		if (requestCompression) {
			wrapper.getHeaders().add(ACCEPT_ENCODING, ACCEPT_ENCODING_VALUE);
		}
		ClientHttpResponse response = execution.execute(wrapper, body);
		List<String> contentEncodings = response.getHeaders().get(
				CONTENT_ENCODING);
		if (!CollectionUtils.isEmpty(contentEncodings)) {
			if (contentEncodings.contains(ENCODING_GZIP)) {
				return new GZipClientHttpResponse(response);
			}
			if (contentEncodings.contains(ENCODING_DEFLATE)) {
				return new DeflateClientHttpResponse(response);
			}
		}
		return response;
	}

	/**
	 * {@link ClientHttpResponse} class which automatically decompresses a
	 * compressed server response.
	 */
	static class GZipClientHttpResponse implements ClientHttpResponse {
		private final ClientHttpResponse response;

		public GZipClientHttpResponse(ClientHttpResponse response) {
			this.response = response;
		}

		@Override
		public InputStream getBody() throws IOException {
			return new GZIPInputStream(response.getBody());
		}

		@Override
		public org.springframework.http.HttpHeaders getHeaders() {
			return response.getHeaders();
		}

		@Override
		public HttpStatus getStatusCode() throws IOException {
			return response.getStatusCode();
		}

		@Override
		public int getRawStatusCode() throws IOException {
			return response.getRawStatusCode();
		}

		@Override
		public String getStatusText() throws IOException {
			return response.getStatusText();
		}

		@Override
		public void close() {
			response.close();
		}
	}

	/**
	 * {@link ClientHttpResponse} class which automatically decompresses a
	 * compressed server response.
	 */
	static class DeflateClientHttpResponse implements ClientHttpResponse {
		private final ClientHttpResponse response;

		public DeflateClientHttpResponse(ClientHttpResponse response) {
			this.response = response;
		}

		@Override
		public InputStream getBody() throws IOException {
			return new DeflaterInputStream(response.getBody());
		}

		@Override
		public org.springframework.http.HttpHeaders getHeaders() {
			return response.getHeaders();
		}

		@Override
		public HttpStatus getStatusCode() throws IOException {
			return response.getStatusCode();
		}

		@Override
		public int getRawStatusCode() throws IOException {
			return response.getRawStatusCode();
		}

		@Override
		public String getStatusText() throws IOException {
			return response.getStatusText();
		}

		@Override
		public void close() {
			response.close();
		}
	}
}
