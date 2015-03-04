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
package com.kohlschutter.boilerpipe;

import com.kohlschutter.boilerpipe.corpora.CorporaAsserter;
import com.kohlschutter.boilerpipe.corpora.Formatter;
import com.kohlschutter.boilerpipe.document.TextDocument;
import com.kohlschutter.boilerpipe.sax.BoilerpipeSAXInput;
import com.kohlschutter.boilerpipe.sax.HTMLDocument;
import com.kohlschutter.boilerpipe.sax.HTMLFetcher;
import org.junit.Test;

import java.net.URL;

import static com.kohlschutter.boilerpipe.corpora.Formatter.*;

public class TextDocumentParserTest {

    CorporaAsserter corporaAsserter = new CorporaAsserter( getClass() );

    @Test
    public void testBasicDocument1() throws Exception {

        TextDocument doc = parse( "/test1.html" );

        corporaAsserter.assertCorpora( "testBasicDocument1", doc.getTextBlocks().toString() );

    }

    @Test
    public void testBasicDocument2() throws Exception {

        TextDocument doc = parse( "/test2.html" );

        corporaAsserter.assertCorpora( "testBasicDocument2", doc.getTextBlocks().toString() );

    }

    @Test
    public void testBasicDocument3() throws Exception {

        TextDocument doc = parse( "/test3.html" );

        // I can use jsoup for this by taking the code from flushBlock in
        // boilerpipe.sax.BoilerpipeHTMLContentHandler and doing a jsoup query
        // for div,p ... in jsoup and then taking the text from the elements to
        // build a  TextBlock

        corporaAsserter.assertCorpora( "testBasicDocument3", table( doc.getTextBlocks() ) );

    }

    private TextDocument parse( String path ) throws Exception {

        URL url = getClass().getResource( path );

        final HTMLDocument htmlDoc = HTMLFetcher.fetch( url );

        return new BoilerpipeSAXInput(htmlDoc.toInputSource())
            .getTextDocument();

    }

}