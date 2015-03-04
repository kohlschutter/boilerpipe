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
package com.kohlschutter.boilerpipe.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import static org.jsoup.Jsoup.connect;

/**
 * Small little jsoup app to fetch pages on reddit to obtain links that we can
 * use for tests.  We also format the output so that it can be pasted into Java
 * code to define new tests.
 */
public class FetchRedditCorpusExamples {

    public static void main(String[] args) throws Exception {

        int startingIdentifier = 10;

        Document doc = connect( "http://www.reddit.com/r/politics" ).get();

        //System.out.printf( "%s\n", doc.outerHtml() );

        int id = startingIdentifier;

        for (Element link : doc.select( ".entry p a[class~=title]" )) {

            System.out.printf( "test( \"%s\", \"test-%04d\" );\n", link.attr( "href" ), id );
            ++id;
        }

        System.out.printf( "done\n" );

    }



}
