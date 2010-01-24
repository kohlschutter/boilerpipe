/**
 * boilerpipe
 *
 * Copyright (c) 2009 Christian Kohlschütter
 *
 * The author licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.l3s.boilerpipe.extractors;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import de.l3s.boilerpipe.BoilerpipeExtractor;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.document.TextDocument;
import de.l3s.boilerpipe.sax.BoilerpipeSAXInput;

/**
 * The base class of Extractors. Also provides some helper methods to quickly
 * retrieve the text that remained after processing.
 * 
 * @author Christian Kohlschütter
 */
public abstract class ExtractorBase implements BoilerpipeExtractor {
    
    /**
     * Extracts text from the HTML code given as a String.
     * 
     * @param html  The HTML code as a String.
     * @return  The extracted text.
     * @throws BoilerpipeProcessingException
     */
    public String getText(final String html)
            throws BoilerpipeProcessingException {
        try {
            return getText(new BoilerpipeSAXInput(new InputSource(
                    new StringReader(html))).getTextDocument());
        } catch (SAXException e) {
            throw new BoilerpipeProcessingException(e);
        }
    }

    /**
     * Extracts text from the HTML code available from the given {@link InputSource}.
     * 
     * @param is The InputSource containing the HTML
     * @return  The extracted text.
     * @throws BoilerpipeProcessingException
     */
    public String getText(final InputSource is)
            throws BoilerpipeProcessingException {
        try {
            return getText(new BoilerpipeSAXInput(is).getTextDocument());
        } catch (SAXException e) {
            throw new BoilerpipeProcessingException(e);
        }
    }

    /**
     * Extracts text from the HTML code available from the given {@link URL}.
     * NOTE: This method is mainly to be used for show case purposes. If you are
     * going to crawl the Web, consider using {@link #getText(InputSource)}
     * instead.
     * 
     * @param url  The URL pointing to the HTML code.
     * @return  The extracted text.
     * @throws BoilerpipeProcessingException
     */
    public String getText(final URL url) throws BoilerpipeProcessingException {
        try {
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
            
            InputSource is = new InputSource(in);
            if(cs != null) {
                is.setEncoding(cs.name());
            }
            
            final String text = getText(is);
            in.close();
            return text;
        } catch (IOException e) {
            throw new BoilerpipeProcessingException(e);
        }
    }

    /**
     * Extracts text from the HTML code available from the given {@link Reader}.
     * 
     * @param r The Reader containing the HTML
     * @return  The extracted text.
     * @throws BoilerpipeProcessingException
     */
    public String getText(final Reader r) throws BoilerpipeProcessingException {
        return getText(new InputSource(r));
    }

    /**
     * Extracts text from the given {@link TextDocument} object.
     * 
     * @param doc The {@link TextDocument}.
     * @return  The extracted text.
     * @throws BoilerpipeProcessingException
     */
    public String getText(TextDocument doc)
            throws BoilerpipeProcessingException {
        process(doc);
        return doc.getContent();
    }    
}
