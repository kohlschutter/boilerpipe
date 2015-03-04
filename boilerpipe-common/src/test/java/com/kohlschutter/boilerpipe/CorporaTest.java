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
import com.kohlschutter.boilerpipe.corpora.CorporaCache;
import com.kohlschutter.boilerpipe.document.TextDocument;
import com.kohlschutter.boilerpipe.extractors.ArticleExtractor;
import com.kohlschutter.boilerpipe.jsoup.JsoupParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

/**
 * Tests using real world documents to look at real world accuracy.
 */
public class CorporaTest {

    CorporaCache corporaCache = new CorporaCache( getClass() );
    CorporaAsserter corporaAsserter = new CorporaAsserter( getClass() );

    @Test
    public void testAll() throws Exception {

        test( "http://www.sanders.senate.gov/newsroom/press-releases/senators-to-us-trade-rep-end-trade-deal-secrecy", "test-0010" );

        test( "http://www.bbc.com/news/world-europe-31669061", "testBbc1" );
        test( "http://www.cnn.com/2015/02/26/us/arizona-llamas-escape/index.html", "testCnn1" );
        test( "http://www.cnn.com/videos/us/2015/02/26/pkg-woman-loses-over-800-pounds.ktrk", "testCnn2" );
        test( "http://www.cnn.com/2015/02/27/world/mexico-knights-templar-leader-detained/index.html", "testCnn3" );
        test( "http://techcrunch.com/2015/02/27/nsa-bullk-telephony-metadata-program-reupped-until-parts-of-the-patriot-act-potentially-sunset/", "testTechcrunch1" );

        test( "http://thinkprogress.org/climate/2015/03/03/3628631/secret-science-bills-are-back/", "test-0011" );
        test( "http://www.washingtonpost.com/blogs/plum-line/wp/2015/03/03/why-netanyahus-speech-didnt-do-his-american-allies-any-favors/?hpid=z2", "test-0012" );
        test( "http://thehill.com/policy/technology/234011-21-house-republicans-call-for-vote-of-disapproval-on-internet-rules", "test-0013" );
        test( "http://www.theguardian.com/society/2015/mar/03/un-drugs-body-warns-us-states-and-uruguay-over-cannabis-legalisation", "test-0014" );
        test( "http://www.washingtonpost.com/posteverything/wp/2015/03/02/right-to-work-for-less-gov-scott-walker-wants-to-lower-worker-pay-in-wisconsin/?tid=rssfeed", "test-0015" );
        test( "http://www.washingtonpost.com/blogs/wonkblog/wp/2015/03/03/unplanned-pregnancies-cost-taxpayers-21-billion-each-year/", "test-0016" );
        test( "http://news.sciencemag.org/people-events/2015/02/lone-physicist-congress-joins-science-panel?utm_campaign=app-ipad", "test-0017" );
        test( "http://www.abc2news.com/news/political/goucher-poll-52-percent-of-marylanders-support-marijuana-legalization", "test-0018" );
        test( "http://www.politico.com/story/2015/03/nancy-pelosi-benjamin-netanyahu-speech-react-115701.html", "test-0019" );
        test( "http://motherboard.vice.com/read/fcc-confirms-net-neutrality-order-wont-be-released-today", "test-0020" );
        test( "http://www.nytimes.com/2015/03/03/us/politics/in-four-word-phrase-challenger-spied-health-care-laws-vulnerability.html?_r=0", "test-0021" );
        test( "http://thehill.com/blogs/congress-blog/politics/234341-feinstein-is-correct-netanyahu-does-not-speak-for-jewish", "test-0022" );
        test( "http://www.thedailybeast.com/articles/2015/03/03/hillary-email-scandal-not-so-fast.html", "test-0023" );
        test( "http://www.ibtimes.com/chris-christie-administration-paid-600m-financial-fees-2014-1833872", "test-0024" );
        test( "http://www.washingtonpost.com/opinions/boehners-pointless-leadership/2015/03/02/a9b81e40-c11a-11e4-ad5c-3b8ce89f1b89_story.html", "test-0025" );
        test( "http://www.bloomberg.com/politics/articles/2015-03-03/netanyahu-warns-congress-an-iran-deal-guarantees-nuclear-arms", "test-0026" );
        //test( "http://www.startribune.com/politics/294758021.html", "test-0027" );
        test( "http://www.huffingtonpost.com/david-miles/interests-not-infatuation_b_6786910.html?utm_hp_ref=politics", "test-0028" );
        test( "http://www.huffingtonpost.com/2015/03/03/homeland-security-funding_n_6791502.html?ncid=txtlnkusaolp00000592", "test-0029" );
        test( "http://therealnews.com/t2/index.php?option=com_content&task=view&id=31&Itemid=74&jumival=13326", "test-0030" );
        test( "http://www.slate.com/articles/news_and_politics/foreigners/2015/03/benjamin_netanyahu_did_president_obama_a_big_favor_the_israeli_prime_minister.html", "test-0031" );
        test( "http://www.vox.com/2015/3/3/8141157/netanyahu-congress-speech-whitehouse", "test-0032" );
        test( "http://www.theguardian.com/commentisfree/2015/mar/03/every-time-winters-extreme-cold-claims-a-life-we-have-failed-the-homeless", "test-0033" );
        test( "http://www.scientificamerican.com/podcast/episode/climate-skeptic-senator-burned-after-snowball-stunt/", "test-0034" );


    }

    private void test( String link, String key ) throws Exception {

        System.out.printf( "Testing: %s\n", link );

        testContentAsText( link, key );
        testContentAsHTML( link, key );
    }

    private void testContentAsText(String link, String key) throws Exception {

        String html = read( link );

        ArticleExtractor articleExtractor = ArticleExtractor.getInstance();

        TextDocument textDocumentFromJsoup = new JsoupParser().parse( html );

        corporaAsserter.assertCorpora( key + "-text", articleExtractor.getText( textDocumentFromJsoup ) );

    }

    private void testContentAsHTML(String link, String key) throws Exception {

        String html = read( link );

        ArticleExtractor articleExtractor = ArticleExtractor.getInstance();

        TextDocument textDocumentFromJsoup = new JsoupParser().parse( html );
        corporaAsserter.assertCorpora( key + "-html", articleExtractor.getHTML( textDocumentFromJsoup ) );

    }


    private String key( String link ) {

        return link.replaceAll( "[:/?=&%]", "_" );

    }


    private String read( String link ) throws IOException {

        String key = key( link );

        String data = null;

        if ( corporaCache.contains( key ) ) {
            data = corporaCache.read( key );
        }

        if ( data != null ) {
            return data;
        }

        data = fetch( link );

        corporaCache.write( key, data );

        return data;

    }


    // fetch the given link by going over the network.
    private String fetch( String link ) throws IOException {

        // TODO: should we strip any charset in the <meta> since after this we
        // always save as UTF8 ?

        System.out.printf( "Fetching from network: %s\n", link );

        URL url = new URL( link );

        Document doc = Jsoup.parse( url, 30000 );

        String content = doc.outerHtml();

        return content;

    }

}
