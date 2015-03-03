package com.kohlschutter.boilerpipe.sax;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * Provides an easy way for implementers to build ExtendedContentHandlers without
 * having to implement all methods since this class provides noop/default
 * implementations.
 */
public class DefaultExtendedContentHandler implements ExtendedContentHandler {

    @Override
    public void startElement(Element element) {

    }

    @Override
    public void endElement(Element element) {

    }

    @Override
    public void textNode(TextNode textNode) {

    }

    @Override
    public void setDocumentLocator(Locator locator) {

    }

    @Override
    public void startDocument() throws SAXException {

    }

    @Override
    public void endDocument() throws SAXException {

    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {

    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {

    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {

    }

    @Override
    public void skippedEntity(String name) throws SAXException {

    }
}
