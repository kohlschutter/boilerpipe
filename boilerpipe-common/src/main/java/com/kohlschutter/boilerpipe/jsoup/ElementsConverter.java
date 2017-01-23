/**
 * boilerpipe
 *
 * Copyright (c) 2009, 2014 Christian Kohlschütter
 *
 * The author licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kohlschutter.boilerpipe.jsoup;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Convert a list of text nodes to their original HTML hierarchy and preserve
 * the original root elements with the proper children.
 */
public class ElementsConverter {

    public static Elements convert(List<TextNode> textNodes) {

        // a list of roots which are actually element holders which contain all
        // the child nodes in the index.

        Elements roots = new Elements();

        // keep an index from the parent node of the text node to a 'holder'
        // clone element which is a copy of the parent but just contains its
        // markup and not the actual element.

        Map<Element, Element> holderIndex = new HashMap<>();

        for (TextNode textNode : textNodes) {

            // text nodes have a container element that holds them.  For example
            // if we have <b>hello</b> then the text node would be just 'hello'
            // and the container would be <b>

            Element container = (Element) textNode.parent();
            Element containerParent = container.parent();

            Element holder = holderIndex.get( container );

            Node childToAppend = textNode;

            if (holder == null && holderIndex.containsKey( containerParent )) {
                holder = holderIndex.get( containerParent );

                Element copy = createShallowAndEmptyCopy( container );
                copy.appendChild( textNode );

                // we need an index of ALL elements because we are NOT sure if a
                // text node later in the chain could become a holder
                holderIndex.put( container, copy );

                childToAppend = copy;
            }

            if (holder == null) {

                holder = createShallowAndEmptyCopy( container );

                roots.add( holder );
                holderIndex.put( container, holder );

                childToAppend = textNode.clone();

            }


            holder.appendChild( childToAppend );

        }

        return roots;

    }

    /**
     * Create a copy of the element by preserving the tag name, and the attributes,
     * and nothing else.  It's just an empty vessel into which we can store text
     * nodes.
     *
     */
    protected static Element createShallowAndEmptyCopy( Element element ) {

        return new Element( element.tag(), element.baseUri(), element.attributes() );

    }

}
