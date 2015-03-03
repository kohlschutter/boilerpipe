package com.kohlschutter.boilerpipe.jsoup;

import com.kohlschutter.boilerpipe.document.TextDocument;
import com.kohlschutter.boilerpipe.sax.BoilerpipeHTMLContentHandler;
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
 * Parser that emulates SAX and just uses a stock BoilerpipeHTMLContentHandler
 */
public class JsoupParser {

    // the namespace isn't actually used by boilerpipe but required by the SAX
    // API.
    private static final String NAMESPACE = "http://www.w3.org/1999/xhtml";

    public TextDocument parse( InputStream inputStream, String charsetName , String baseUri ) throws IOException, SAXException {
        return parse( Jsoup.parse( inputStream, charsetName, baseUri ) );
    }

    public TextDocument parse( URL url, int timeoutMillis ) throws IOException, SAXException  {
        return parse( Jsoup.parse( url, timeoutMillis ) );
    }

    public TextDocument parse( String html ) throws SAXException {
        return parse( Jsoup.parse( html ) );
    }

    /**
     * Parse the given HTML to return a TextDocument.
     *
     * @return
     */
    public TextDocument parse( Document document ) throws SAXException {

        BoilerpipeHTMLContentHandler contentHandler = new BoilerpipeHTMLContentHandler();

        contentHandler.startDocument();

        for (Element element : document.children()) {
            handle( element, contentHandler );
        }

        contentHandler.endDocument();

        return contentHandler.toTextDocument();

    }

    public void handle( Element element, BoilerpipeHTMLContentHandler contentHandler) throws SAXException {

        String localName = element.tagName().toUpperCase();

        Attributes attributes = new AttributesImpl( element.attributes() );

        contentHandler.startElement( NAMESPACE, localName, localName, attributes );

        contentHandler.startElement( element );

        for ( Node current : element.childNodes() ) {

            if ( current instanceof Element ) {

                Element currentElement = (Element)current;

                handle( currentElement, contentHandler );

            }

            if ( current instanceof TextNode) {

                TextNode currentTextNode = (TextNode)current;

                char[] chars = currentTextNode.text().toCharArray();

                contentHandler.characters( chars, 0, chars.length );

            }

        }

        contentHandler.endElement( NAMESPACE, localName, localName );

    }

}
