package de.l3s.boilerpipe.sax;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

import org.xml.sax.InputSource;

/**
 * An {@link InputSourceable} for {@link HTMLFetcher}.
 * 
 * @author Christian Kohlschütter
 */
public class HTMLDocument implements InputSourceable {
	private final Charset encoding;
	private final byte[] data;

	public HTMLDocument(final byte[] data, final Charset encoding) {
		this.data = data;
		this.encoding = encoding;
	}
	
	public Charset getEncoding() {
		return encoding;
	}
	
	public byte[] getData() {
		return data;
	}
	
	public InputSource toInputSource() {
		final InputSource is = new InputSource(new ByteArrayInputStream(data));
		is.setEncoding(encoding.name());
		return is;
	}
}
