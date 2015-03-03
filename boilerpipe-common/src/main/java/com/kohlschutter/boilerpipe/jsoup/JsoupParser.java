package com.kohlschutter.boilerpipe.jsoup;

import com.kohlschutter.boilerpipe.document.TextDocument;
import com.kohlschutter.boilerpipe.sax.BoilerpipeHTMLContentHandler;
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
 * Parser that emulates SAX and just uses a stock BoilerpipeHTMLContentHandler
 */
public class JsoupParser {

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

        JsoupSAXParser jsoupSAXParser = new JsoupSAXParser( contentHandler );

        jsoupSAXParser.parse( document );

        return contentHandler.toTextDocument();

    }

}
