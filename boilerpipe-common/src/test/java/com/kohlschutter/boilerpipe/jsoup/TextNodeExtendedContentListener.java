package com.kohlschutter.boilerpipe.jsoup;

import com.google.common.collect.Lists;
import com.kohlschutter.boilerpipe.sax.DefaultExtendedContentHandler;
import org.jsoup.nodes.TextNode;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

/**
 *
 */
public class TextNodeExtendedContentListener extends DefaultExtendedContentHandler {

    private List<TextNode> textNodes = Lists.newArrayList();

    @Override
    public void textNode(TextNode textNode) {
        textNodes.add( textNode );
    }

    public List<TextNode> getTextNodes() {
        return textNodes;
    }

}
