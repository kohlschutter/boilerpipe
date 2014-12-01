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
package com.kohlschutter.boilerpipe.demo;

import java.io.PrintWriter;
import java.net.URL;

import com.kohlschutter.boilerpipe.BoilerpipeExtractor;
import com.kohlschutter.boilerpipe.extractors.CommonExtractors;
import com.kohlschutter.boilerpipe.sax.HTMLHighlighter;

/**
 * Demonstrates how to use Boilerpipe to get the main content, highlighted as HTML.
 * 
 * @see Oneliner if you only need the plain text.
 */
public class HTMLHighlightDemo {
  public static void main(String[] args) throws Exception {
    URL url =
        new URL(
            "http://blog.openshift.com/day-18-boilerpipe-article-extraction-for-java-developers/");

    // choose from a set of useful BoilerpipeExtractors...
    final BoilerpipeExtractor extractor = CommonExtractors.ARTICLE_EXTRACTOR;
    // final BoilerpipeExtractor extractor = CommonExtractors.DEFAULT_EXTRACTOR;
    // final BoilerpipeExtractor extractor = CommonExtractors.CANOLA_EXTRACTOR;
    // final BoilerpipeExtractor extractor = CommonExtractors.LARGEST_CONTENT_EXTRACTOR;

    // choose the operation mode (i.e., highlighting or extraction)
    final HTMLHighlighter hh = HTMLHighlighter.newHighlightingInstance();
    // final HTMLHighlighter hh = HTMLHighlighter.newExtractingInstance();

    PrintWriter out = new PrintWriter("/tmp/highlighted.html", "UTF-8");
    out.println("<base href=\"" + url + "\" >");
    out.println("<meta http-equiv=\"Content-Type\" content=\"text-html; charset=utf-8\" />");
    out.println(hh.process(url, extractor));
    out.close();

    System.out.println("Now open file:///tmp/highlighted.html in your web browser");
  }
}
