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

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.xerces.parsers.AbstractSAXParser;
import org.cyberneko.html.HTMLConfiguration;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import de.l3s.boilerpipe.document.TextBlock;
import de.l3s.boilerpipe.util.UnicodeTokenizer;

/**
 * A simple SAX Parser, used by {@link BoilerpipeSAXInput}.
 * The parser uses <a href="http://nekohtml.sourceforge.net/">CyberNeko</a> to parse HTML content.
 * 
 * @author Christian Kohlschütter
 */
final class DefaultHTMLParser extends AbstractSAXParser implements ContentHandler {
    private static final String ANCHOR_TEXT_START = "$\ue00a<";
    private static final String ANCHOR_TEXT_END = ">\ue00a$";

    private StringBuilder tokenBuffer = new StringBuilder();
    private StringBuilder textBuffer = new StringBuilder();

    private int inBody = 0;
    private int inAnchor = 0;
    private int inIgnorableElement = 0;
    private boolean sbLastWasWhitespace = false;
    private int textElementIdx = 0;

    private final List<TextBlock> textBlocks = new ArrayList<TextBlock>();

    private String lastStartTag = null;
    @SuppressWarnings("unused")
    private String lastEndTag = null;
    @SuppressWarnings("unused")
    private Event lastEvent = null;

    DefaultHTMLParser() {
        super(new HTMLConfiguration());
        setContentHandler(this);
    }

    public void endDocument() throws SAXException {
        flushBlock();
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

    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
    }

    private boolean flush = false;

    public void startElement(String uri, String localName, String qName,
            Attributes atts) throws SAXException {
        TagAction ta = TAG_ACTIONS.get(localName);
        if (ta != null) {
            flush = ta.start(this, localName) | flush;
        } else {
            flush = true;
        }

        lastEvent = Event.START_TAG;
        lastStartTag = localName;
    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        TagAction ta = TAG_ACTIONS.get(localName);
        if (ta != null) {
            flush = ta.end(this, localName) | flush;
        } else {
            flush = true;
        }

        lastEvent = Event.END_TAG;
        lastEndTag = localName;
    }

    private int offsetBlocks = 0;
    private BitSet currentContainedTextElements = new BitSet();

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        textElementIdx++;

        if (flush) {
            flushBlock();
            flush = false;
        }

        if (inIgnorableElement != 0) {
            return;
        }
        char c;
        boolean startWhitespace = false;
        boolean endWhitespace = false;
        switch (length) {
        case 0:
            return;
        case 1:
            c = ch[start];
            startWhitespace = Character.isWhitespace(c);
            endWhitespace = startWhitespace;
            if (sbLastWasWhitespace) {
                if (startWhitespace) {
                    lastEvent = Event.WHITESPACE;
                    return;
                }
            } else if (startWhitespace) {
                textBuffer.append(' ');
                tokenBuffer.append(' ');
            } else {
                textBuffer.append(c);
                tokenBuffer.append(c);
            }
            break;
        default:
            final int end = start + length;
            for (int i = start; i < end; i++) {
                if (Character.isWhitespace(ch[i])) {
                    ch[i] = ' ';
                }
            }
            while (start < end) {
                c = ch[start];
                if (c == ' ') {
                    startWhitespace = true;
                    start++;
                    length--;
                } else {
                    break;
                }
            }
            while (length > 0) {
                c = ch[start + length - 1];
                if (c == ' ') {
                    endWhitespace = true;
                    length--;
                } else {
                    break;
                }
            }
            if (length == 0) {
                if (startWhitespace || endWhitespace) {
                    if (!sbLastWasWhitespace) {
                        textBuffer.append(' ');
                        tokenBuffer.append(' ');
                    }
                    sbLastWasWhitespace = true;
                } else {
                    sbLastWasWhitespace = false;
                }
                lastEvent = Event.WHITESPACE;
                return;
            }
            if (startWhitespace) {
                if (!sbLastWasWhitespace) {
                    textBuffer.append(' ');
                    tokenBuffer.append(' ');
                }
            }
            textBuffer.append(ch, start, length);
            tokenBuffer.append(ch, start, length);
            if (endWhitespace) {
                textBuffer.append(' ');
                tokenBuffer.append(' ');
            }
        }

        sbLastWasWhitespace = endWhitespace;
        lastEvent = Event.CHARACTERS;

        currentContainedTextElements.set(textElementIdx);
    }

    public List<TextBlock> getTextBlocks() {
        return textBlocks;
    }

    boolean inAnchorText = false;
    private void flushBlock() {
        if (inBody == 0) {
            if ("TITLE".equals(lastStartTag) && inBody == 0) {
                setTitle(tokenBuffer.toString().trim());
            }
            tokenBuffer.setLength(0);
            return;
        }

        final int length = tokenBuffer.length();
        switch (length) {
        case 0:
            return;
        case 1:
            if (sbLastWasWhitespace) {
                tokenBuffer.setLength(0);
                return;
            }
        }
        final String[] tokens = UnicodeTokenizer.tokenize(tokenBuffer);
        
        int numWords = 0;
        int numLinkedWords = 0;
        int numWrappedLines = 0;
        int currentLineLength = -1; // don't count the first space
        final int maxLineLength = 80;
        int numTokens = 0;
        int numWordsCurrentLine = 0;

        for (String token : tokens) {
            if (ANCHOR_TEXT_START.equals(token)) {
                inAnchorText = true;
            } else if (ANCHOR_TEXT_END.equals(token)) {
                inAnchorText = false;
            } else if (isWord(token)) {
                numTokens++;
                numWords++;
                numWordsCurrentLine++;
                if (inAnchorText) {
                    numLinkedWords++;
                }
                final int tokenLength = token.length();
                currentLineLength += tokenLength + 1;
                if (currentLineLength > maxLineLength) {
                    numWrappedLines++;
                    currentLineLength = tokenLength;
                    numWordsCurrentLine = 1;
                }
            } else {
                numTokens++;
            }
        }
        if (numTokens == 0) {
            return;
        }

        int numWordsInWrappedLines;
        if (numWrappedLines == 0) {
            numWordsInWrappedLines = numWords;
            numWrappedLines = 1;
        } else {
            numWordsInWrappedLines = numWords - numWordsCurrentLine;
        }

        TextBlock tb = new TextBlock(textBuffer.toString().trim(),
                currentContainedTextElements, numWords, numLinkedWords,
                numWordsInWrappedLines, numWrappedLines, offsetBlocks);
        currentContainedTextElements = new BitSet();

        offsetBlocks++;

        textBuffer.setLength(0);
        tokenBuffer.setLength(0);

        // System.out.println(tb.getText());
        // System.out.println(numWords + "\t" + numLinkedWords + "\t"
        // + tb.textDensity + "\t" +
        // tb.linkDensity+"\t"+numWordsCurrentLine+"\t"+numWrappedLines);

        textBlocks.add(tb);
    }

    private static final Pattern PAT_VALID_WORD_CHARACTER = Pattern
            .compile("[\\p{L}\\p{Nd}\\p{Nl}\\p{No}]");

    private static boolean isWord(final String token) {
        return PAT_VALID_WORD_CHARACTER.matcher(token).find();
    }

    private interface TagAction {
        public boolean start(final DefaultHTMLParser instance,
                final String localName);

        public boolean end(final DefaultHTMLParser instance,
                final String localName);

    }

    private static final TagAction TA_IGNORABLE_ELEMENT = new TagAction() {

        public boolean start(final DefaultHTMLParser instance,
                final String localName) {
            instance.inIgnorableElement++;
            return true;
        }

        public boolean end(final DefaultHTMLParser instance,
                final String localName) {
            instance.inIgnorableElement--;
            return true;
        }
    };
    private static final TagAction TA_ANCHOR_TEXT = new TagAction() {

        public boolean start(DefaultHTMLParser instance, final String localName) {
            if (instance.inAnchor++ == 0) {
                if (instance.inIgnorableElement == 0) {
                    if (!instance.sbLastWasWhitespace) {
                        instance.tokenBuffer.append(' ');
                        instance.textBuffer.append(' ');
                    }
                    instance.tokenBuffer.append(ANCHOR_TEXT_START);
                    instance.tokenBuffer.append(' ');
                    instance.sbLastWasWhitespace = true;
                }
            }
            return false;
        }

        public boolean end(DefaultHTMLParser instance, final String localName) {
            if (--instance.inAnchor == 0) {
                if (instance.inIgnorableElement == 0) {
                    if (!instance.sbLastWasWhitespace) {
                        instance.tokenBuffer.append(' ');
                        instance.textBuffer.append(' ');
                    }
                    instance.tokenBuffer.append(ANCHOR_TEXT_END);
                    instance.tokenBuffer.append(' ');
                    instance.sbLastWasWhitespace = true;
                }
            }
            return false;
        }

    };
    private static final TagAction TA_BODY = new TagAction() {
        public boolean start(final DefaultHTMLParser instance,
                final String localName) {
            instance.inBody++;
            return false;
        }

        public boolean end(final DefaultHTMLParser instance,
                final String localName) {
            instance.inBody--;
            return false;
        }
    };

    private static final TagAction TA_INLINE = new TagAction() {

        public boolean start(DefaultHTMLParser instance, final String localName) {
            if (!instance.sbLastWasWhitespace) {
                instance.tokenBuffer.append(' ');
                instance.textBuffer.append(' ');
            }
            return false;
        }

        public boolean end(DefaultHTMLParser instance, final String localName) {
            if (!instance.sbLastWasWhitespace) {
                instance.tokenBuffer.append(' ');
                instance.textBuffer.append(' ');
            }
            return false;
        }
    };

    private static Map<String, TagAction> TAG_ACTIONS = new HashMap<String, TagAction>();
    static {
        TAG_ACTIONS.put("STYLE", TA_IGNORABLE_ELEMENT);
        TAG_ACTIONS.put("SCRIPT", TA_IGNORABLE_ELEMENT);
        TAG_ACTIONS.put("OPTION", TA_IGNORABLE_ELEMENT);
        TAG_ACTIONS.put("OBJECT", TA_IGNORABLE_ELEMENT);
        TAG_ACTIONS.put("EMBED", TA_IGNORABLE_ELEMENT);
        TAG_ACTIONS.put("APPLET", TA_IGNORABLE_ELEMENT);
        TAG_ACTIONS.put("A", TA_ANCHOR_TEXT);
        TAG_ACTIONS.put("BODY", TA_BODY);

        TAG_ACTIONS.put("STRIKE", TA_INLINE);
        TAG_ACTIONS.put("U", TA_INLINE);
        TAG_ACTIONS.put("B", TA_INLINE);
        TAG_ACTIONS.put("I", TA_INLINE);
        TAG_ACTIONS.put("EM", TA_INLINE);
        TAG_ACTIONS.put("STRONG", TA_INLINE);
        TAG_ACTIONS.put("SPAN", TA_INLINE);

        TAG_ACTIONS.put("ABBR", TA_INLINE);
        TAG_ACTIONS.put("ACRONYM", TA_INLINE);
    }

    private static enum Event {
        START_TAG, END_TAG, CHARACTERS, WHITESPACE
    }

    private String title =null;

    public String getTitle() {
        return title;
    }
    private void setTitle(String s) {
        if(s == null || s.length() == 0) {
            return;
        }
        title = s;
    }
}