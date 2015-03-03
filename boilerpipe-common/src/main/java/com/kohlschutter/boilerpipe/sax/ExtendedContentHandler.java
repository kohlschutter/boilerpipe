package com.kohlschutter.boilerpipe.sax;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.xml.sax.ContentHandler;

/**
 * Extended content handler with methods for JSoup callbacks which provide
 * element and text node information.  Used so that we can have the JSoupParser
 * work with ExtendedContentHandlers which can also be used for testing the
 * JSoupParser's behavior.
 */
public interface ExtendedContentHandler extends ContentHandler {

    /**
     * Called at the start of each element.
     *
     * @param element
     */
    public void startElement( Element element );

    /**
     * Called at the end of each element.
     *
     * @param element
     */
    public void endElement( Element element );

    /**
     * Called when we have found a text node so we can keep a reference to it
     * for use with TextBlocks.
     *
     * @param textNode
     */
    public void textNode(TextNode textNode);

}