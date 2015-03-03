package com.kohlschutter.boilerpipe.jsoup;

import com.kohlschutter.boilerpipe.sax.DefaultExtendedContentHandler;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

/**
 * Simple pretty printer for HTML which uses the parsed DOM to print elements
 * and text to enable easily understandable HTML.
 */
public class HTMLPrettyPrinter {

    public String format( Element root ) {

        return null;

    }

    class PrintingContentHandler extends DefaultExtendedContentHandler {

        StringBuilder buff = new StringBuilder();

        @Override
        public void startElement(Element element) {

            buff.append( "<" );
            buff.append( element.tagName() );
            buff.append( ">" );

        }

        @Override
        public void endElement(Element element) {

            buff.append( "</" );
            buff.append( element.tagName() );
            buff.append( "/>" );

        }

        @Override
        public void textNode(TextNode textNode) {
        }

    }

}
