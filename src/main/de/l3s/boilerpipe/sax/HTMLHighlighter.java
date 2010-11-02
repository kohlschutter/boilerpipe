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
package de.l3s.boilerpipe.sax;

import java.io.IOException;
import java.io.StringReader;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.xerces.parsers.AbstractSAXParser;
import org.cyberneko.html.HTMLConfiguration;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.document.TextBlock;
import de.l3s.boilerpipe.document.TextDocument;

/**
 * Highlights text blocks in an HTML document that have been marked as "content"
 * in the corresponding {@link TextDocument}.
 * 
 * @author Christian Kohlschütter
 */
public final class HTMLHighlighter {
    private final Implementation implementation = new Implementation();

    /**
     * Prepares the {@link HTMLHighlighter} for the given {@link TextDocument}
     * and the original HTML text (as a String).
     * 
     * @param doc
     *            The processed {@link TextDocument}.
     * @param origHTML
     *            The original HTML document.
     * @throws BoilerpipeProcessingException
     */
    public HTMLHighlighter(final TextDocument doc, final String origHTML)
            throws BoilerpipeProcessingException {
        this(doc, new InputSource(new StringReader(origHTML)));
    }

    /**
     * Prepares the {@link HTMLHighlighter} for the given {@link TextDocument}
     * and the original HTML text (as an {@link InputSource}). Please remember
     * to re-initialize the {@link InputSource} if you have used it already for
     * creating the {@link TextDocument}.
     * 
     * @param doc
     *            The processed {@link TextDocument}.
     * @param is
     *            The original HTML document.
     * @throws BoilerpipeProcessingException
     */
    public HTMLHighlighter(final TextDocument doc, final InputSource is)
            throws BoilerpipeProcessingException {
        
        implementation.process(doc, is);
    }

    /**
     * Returns the highlighted HTML code.
     * 
     * @return The highlighted HTML as a String.
     */
    public String getHTML() {
        return implementation.html.toString();
    }
    
    private abstract static class TagAction {
        void beforeStart(final Implementation instance, final String localName) {
        }

        void afterStart(final Implementation instance, final String localName) {
        }

        void beforeEnd(final Implementation instance, final String localName) {
        }

        void afterEnd(final Implementation instance, final String localName) {
        }
    }

    private static final TagAction TA_IGNORABLE_ELEMENT = new TagAction() {
        void beforeStart(final Implementation instance, final String localName) {
            instance.inIgnorableElement++;
        }

        void afterEnd(final Implementation instance, final String localName) {
            instance.inIgnorableElement--;
        }
    };

    private static final TagAction TA_HEAD = new TagAction() {

        void beforeEnd(final Implementation instance, String localName) {
            instance.html.append("\n<style type=\"text/css\">\n"
                    + ".x-boilerpipe-mark1 {" + " text-decoration:none; "
                    + "background-color: #ffff42 !important; "
                    + "color: black !important; "
                    + "display:inline !important; "
                    + "visibility:visible !important; }\n" + //
                    "</style>\n");
        }
    };

    private static Map<String, TagAction> TAG_ACTIONS = new HashMap<String, TagAction>();
    static {
        TAG_ACTIONS.put("STYLE", TA_IGNORABLE_ELEMENT);
        TAG_ACTIONS.put("SCRIPT", TA_IGNORABLE_ELEMENT);
        TAG_ACTIONS.put("OPTION", TA_IGNORABLE_ELEMENT);
//        TAG_ACTIONS.put("NOSCRIPT", TA_IGNORABLE_ELEMENT);
        TAG_ACTIONS.put("OBJECT", TA_IGNORABLE_ELEMENT);
        TAG_ACTIONS.put("EMBED", TA_IGNORABLE_ELEMENT);
        TAG_ACTIONS.put("APPLET", TA_IGNORABLE_ELEMENT);
        TAG_ACTIONS.put("LINK", TA_IGNORABLE_ELEMENT);

        TAG_ACTIONS.put("HEAD", TA_HEAD);
    }
    

    private final class Implementation extends AbstractSAXParser implements
            ContentHandler {
        StringBuilder html = new StringBuilder();

        private int inIgnorableElement = 0;
        private int characterElementIdx = 0;
        private final BitSet contentBitSet = new BitSet();

        Implementation() {
            super(new HTMLConfiguration());
            setContentHandler(this);
        }

        void process(final TextDocument doc, final InputSource is) throws BoilerpipeProcessingException {
            for (TextBlock block : doc.getTextBlocks()) {
                if (block.isContent()) {
                    contentBitSet.or(block.getContainedTextElements());
                }
            }

            try {
                parse(is);
            } catch (SAXException e) {
                throw new BoilerpipeProcessingException(e);
            } catch (IOException e) {
                throw new BoilerpipeProcessingException(e);
            }
        }
        
        public void endDocument() throws SAXException {
        }

        public void endPrefixMapping(String prefix) throws SAXException {
        }

        public void ignorableWhitespace(char[] ch, int start, int length)
                throws SAXException {
        }

        public void processingInstruction(String target, String data)
                throws SAXException {
        }

        public void setDocumentLocator(Locator locator) {
        }

        public void skippedEntity(String name) throws SAXException {
        }

        public void startDocument() throws SAXException {
        }

        public void startElement(String uri, String localName, String qName,
                Attributes atts) throws SAXException {
            TagAction ta = TAG_ACTIONS.get(localName);
            if (ta != null) {
                ta.beforeStart(this, localName);
            }

            if (inIgnorableElement == 0) {
                html.append('<');
                html.append(qName);
                final int numAtts = atts.getLength();
                for (int i = 0; i < numAtts; i++) {
                    final String attr = atts.getQName(i);
                    final String value = atts.getValue(i);
                    html.append(' ');
                    html.append(attr);
                    html.append("=\"");
                    html.append(xmlEncode(value));
                    html.append("\"");
                }
                html.append('>');
            }
            if (ta != null) {
                ta.afterStart(this, localName);
            }
        }

        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            TagAction ta = TAG_ACTIONS.get(localName);
            if (ta != null) {
                ta.beforeEnd(this, localName);
            }
            if (inIgnorableElement == 0) {
                html.append("</");
                html.append(qName);
                html.append('>');
            }

            if (ta != null) {
                ta.afterEnd(this, localName);
            }
        }

        public void characters(char[] ch, int start, int length)
                throws SAXException {
            characterElementIdx++;
            if (inIgnorableElement == 0) {

                boolean highlight = contentBitSet.get(characterElementIdx);

                if (highlight) {
                    html.append("<span class=\"x-boilerpipe-mark1\">");
                }
                html.append(xmlEncode(String.valueOf(ch, start, length)));
                if (highlight) {
                    html.append("</span>");
                }
            }
        }

        public void startPrefixMapping(String prefix, String uri)
                throws SAXException {
        }

       
    }

    private static String xmlEncode(final String in) {
        if (in == null) {
            return "";
        }
        char c;
        StringBuilder out = new StringBuilder(in.length());

        for (int i = 0; i < in.length(); i++) {
            c = in.charAt(i);
            switch (c) {
            case '<':
                out.append("&lt;");
                break;
            case '>':
                out.append("&gt;");
                break;
            case '&':
                out.append("&amp;");
                break;
            case '"':
                out.append("&quot;");
                break;
            default:
                out.append(c);
            }
        }

        return out.toString();
    }

}
