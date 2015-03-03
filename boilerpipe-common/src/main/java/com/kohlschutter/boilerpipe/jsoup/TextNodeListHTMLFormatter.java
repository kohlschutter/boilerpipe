package com.kohlschutter.boilerpipe.jsoup;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.*;

/**
 *
 */
public class TextNodeListHTMLFormatter {

    public static String format( List<TextNode> textNodes ) {

        // a list of roots which are actually element holders which contain all
        // the child nodes in the index.

        Elements roots = new Elements();

        // keep an index from the parent node of the text node to a 'holder'
        // clone element which is a copy of the parent but just contains its
        // markup and not the actual element.

        Map<Element,Element> holderIndex = new HashMap<>();

        StringBuilder buff = new StringBuilder();

        for (TextNode textNode : textNodes) {

            // text nodes have a container element that holds them.  For example
            // if we have <b>hello</b> then the text node would be just 'hello'
            // and the container would be <b>

            Element container = (Element)textNode.parent();
            Element containerParent = container.parent();

            Element holder = holderIndex.get( container );

            Node childToAppend = textNode;

            if ( holder == null && holderIndex.containsKey( containerParent ) ) {
                holder = holderIndex.get( containerParent );

                Element copy = createShallowAndEmptyCopy( container );
                copy.appendChild( textNode );

                // we need an index of ALL elements because we are NOT sure if a
                // text node later in the chain could become a holder
                holderIndex.put( container, copy );

                childToAppend = copy;
            }

            if ( holder == null ) {

                holder = createShallowAndEmptyCopy( container );

                roots.add( holder );
                holderIndex.put( container, holder );

                childToAppend = textNode.clone();

            }


            holder.appendChild( childToAppend );

        }

        for (Element rootHolder : roots) {
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
