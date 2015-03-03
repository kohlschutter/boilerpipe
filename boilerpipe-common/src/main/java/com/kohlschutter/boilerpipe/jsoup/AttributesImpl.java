package com.kohlschutter.boilerpipe.jsoup;

import org.xml.sax.Attributes;

/**
 *
 */
public class AttributesImpl implements Attributes {

    private final org.jsoup.nodes.Attributes backing;

    public AttributesImpl(org.jsoup.nodes.Attributes backing) {
        this.backing = backing;
    }

    @Override
    public int getLength() {
        return backing.size();
    }

    @Override
    public String getURI(int index) {
        throw new RuntimeException( "not implemented" );
    }

    @Override
    public String getLocalName(int index) {
        throw new RuntimeException( "not implemented" );
    }

    @Override
    public String getQName(int index) {
        throw new RuntimeException( "not implemented" );
    }

    @Override
    public String getType(int index) {
        throw new RuntimeException( "not implemented" );
    }

    @Override
    public String getValue(int index) {
        throw new RuntimeException( "not implemented" );
    }

    @Override
    public int getIndex(String uri, String localName) {
        throw new RuntimeException( "not implemented" );
    }

    @Override
    public int getIndex(String qName) {
        throw new RuntimeException( "not implemented" );
    }

    @Override
    public String getType(String uri, String localName) {
        throw new RuntimeException( "not implemented" );
    }

    @Override
    public String getType(String qName) {
        throw new RuntimeException( "not implemented" );
    }

    @Override
    public String getValue(String uri, String localName) {
        throw new RuntimeException( "not implemented" );
    }

    @Override
    public String getValue(String qName) {
        return backing.get( qName );
    }

}
