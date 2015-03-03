package com.kohlschutter.boilerpipe.jsoup;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.util.List;

/**
 *
 */
public class TextNodeListHTMLFormatter {

    public static String format( List<TextNode> textNodes ) {

        StringBuilder buff = new StringBuilder();

        for (TextNode textNode : textNodes) {

            //buff.append( element.html() );

            buff.append( textNode.text() );
            buff.append( "\n" );

        }

        return buff.toString();

    }

}
