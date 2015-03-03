package com.kohlschutter.boilerpipe.jsoup;

import com.kohlschutter.boilerpipe.sax.ExtendedContentHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Parser that emulates SAX and to enable parsing content via SAX-style events.
 * Right now this is NOT a full SAX parser.  We just implement the basic
 * functionality that boilerpipe requires.
 *
 */
public class JsoupSAXParser {

    // the namespace isn't actually used by boilerpipe but required by the SAX
    // API.
    private static final String NAMESPACE = "http://www.w3.org/1999/xhtml";

    private ExtendedContentHandler contentHandler;

    public JsoupSAXParser(ExtendedContentHandler contentHandler) {
        this.contentHandler = contentHandler;
    }

    public void parse( InputStream inputStream,
                       String charsetName ,
                       String baseUri ) throws IOException, SAXException {
        parse( Jsoup.parse( inputStream, charsetName, baseUri ) );
    }

    public void parse( URL url,
                       int timeoutMillis ) throws IOException, SAXException  {
        parse( Jsoup.parse( url, timeoutMillis ) );
    }

    public void parse( String html ) throws SAXException {
        parse( Jsoup.parse( html ) );
    }

    /**
     * Parse the given HTML to return a TextDocument.
     *
     * @return
     */
    public void parse( Document document ) throws SAXException {

        contentHandler.startDocument();

        for (Element element : document.children()) {
            handle( element );
        }

        contentHandler.endDocument();

    }

    public void handle( Element element ) throws SAXException {

        String localName = element.tagName().toUpperCase();

        Attributes attributes = new AttributesImpl( element.attributes() );

        contentHandler.startElement( NAMESPACE, localName, localName, attributes );

        contentHandler.startElement( element );

        for ( Node current : element.childNodes() ) {

            if ( current instanceof Element ) {

                Element currentElement = (Element)current;

                handle( currentElement );

            }

            if ( current instanceof TextNode) {

                TextNode currentTextNode = (TextNode)current;
                char[] chars = currentTextNode.text().toCharArray();

                contentHandler.textNode( currentTextNode );
                contentHandler.characters( chars, 0, chars.length );

            }

        }

        contentHandler.endElement( NAMESPACE, localName, localName );

    }

}
