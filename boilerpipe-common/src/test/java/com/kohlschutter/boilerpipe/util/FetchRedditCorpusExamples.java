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
