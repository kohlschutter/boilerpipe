package com.kohlschutter.boilerpipe.jsoup;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.*;

/**
 *
 */
public class TextNodeListHTMLFormatter {

    public static String format( List<TextNode> textNodes ) {

        Elements roots = ElementsConverter.convert( textNodes );

        StringBuilder buff = new StringBuilder();

        for (Element root : roots) {
            buff.append( root.outerHtml() );
        }

        return buff.toString();

    }

}
