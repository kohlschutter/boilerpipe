package de.l3s.boilerpipe.demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import org.xml.sax.InputSource;

import de.l3s.boilerpipe.document.TextDocument;
import de.l3s.boilerpipe.extractors.DefaultExtractor;
import de.l3s.boilerpipe.sax.BoilerpipeSAXInput;
import de.l3s.boilerpipe.sax.HTMLHighlighter;

/**
 * A simple highlighting example.
 */
public class Highlight {
	public static void main(String[] args) throws Exception {
		URL url = new URL(
				"http://research.microsoft.com/en-us/um/people/ryenw/hcir2010/challenge.html"
		        );

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

		byte[] data = bos.toByteArray();

		InputSource is = new InputSource(new ByteArrayInputStream(data));
		is.setEncoding(cs.name());

		TextDocument doc = new BoilerpipeSAXInput(is).getTextDocument();
//		ArticleExtractor.getInstance().process(doc);
        DefaultExtractor.getInstance().process(doc);

		is = new InputSource(new ByteArrayInputStream(data));
		is.setEncoding(cs.name());
		HTMLHighlighter hh = new HTMLHighlighter(doc, is);
		PrintWriter out = new PrintWriter("/tmp/highlighted.html", "UTF-8");
		out.println("<base href=\"" + url + "\" >");
		out.println("<meta http-equiv=\"Content-Type\" content=\"text-html; charset=utf-8\" />");
		out.println(hh.getHTML());
		out.close();
		
		// now open /tmp/highlighted.html in your web browser
	}
}
