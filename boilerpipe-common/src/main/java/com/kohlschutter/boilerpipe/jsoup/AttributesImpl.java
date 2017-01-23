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
