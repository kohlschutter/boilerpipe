package com.kohlschutter.boilerpipe.jsoup;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.util.*;

/**
 *
 */
public class TextNodeListHTMLFormatter {

    public static String format( List<TextNode> textNodes ) {

        // a list of roots which are actually element holders which contain all
        // the child nodes in the index.
        List<Element> rootHolders = new ArrayList<>();

        // keep an index from the parent node of the text node to a 'holder'
        // clone element which is a copy of the parent but just contains its
        // markup and not the actual element.
        Map<Element,Element> rootToHolderIndex = new HashMap<>();

        StringBuilder buff = new StringBuilder();

        for (TextNode textNode : textNodes) {

            Element parent = (Element)textNode.parent();

            Element holder = rootToHolderIndex.get( parent );

            if ( holder == null ) {

                holder = createShallowAndEmptyCopy( parent );

                rootHolders.add( holder );
                rootToHolderIndex.put( parent, holder );

            }

            holder.appendChild( textNode.clone() );

        }

        for (Element rootHolder : rootHolders) {
            buff.append( rootHolder.outerHtml() );
        }

        return buff.toString();

    }

    /**
     * Create a copy of the element by preserving the tag name, and the attributes,
     * and nothing else.  It's just an empty vessel into which we can store text
     * nodes.
     *
     * @param element
     * @return
     */
    protected static Element createShallowAndEmptyCopy( Element element ) {

        Element copy = new Element( element.tag(), element.baseUri(), element.attributes() );

        return copy;

    }

}
