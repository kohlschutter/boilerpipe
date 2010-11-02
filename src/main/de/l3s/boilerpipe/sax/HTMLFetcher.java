package de.l3s.boilerpipe.sax;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

/**
 * A very simple HTTP/HTML fetcher, really just for demo purposes.
 * 
 * @author Christian Kohlschütter
 */
public class HTMLFetcher {
	private HTMLFetcher() {
	}
	
	/**
	 * Fetches the document at the given URL, using {@link URLConnection}.
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static HTMLDocument fetch(final URL url) throws IOException {
		final URLConnection conn = url.openConnection();
		final String encoding = conn.getContentEncoding();

		Charset cs = Charset.forName("Cp1252");
		if (encoding != null) {
			try {
				cs = Charset.forName(encoding);
			} catch (UnsupportedCharsetException e) {
				// keep default
			}
		}

		final InputStream in = conn.getInputStream();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buf = new byte[4096];
		int r;
		while ((r = in.read(buf)) != -1) {
			bos.write(buf, 0, r);
		}
		in.close();

		final byte[] data = bos.toByteArray();
		
		return new HTMLDocument(data, cs);
	}
}
