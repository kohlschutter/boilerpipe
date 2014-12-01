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
package com.kohlschutter.boilerpipe.extractors;

import com.kohlschutter.boilerpipe.BoilerpipeProcessingException;
import com.kohlschutter.boilerpipe.document.TextDocument;
import com.kohlschutter.boilerpipe.filters.heuristics.SimpleBlockFusionProcessor;
import com.kohlschutter.boilerpipe.filters.simple.MarkEverythingContentFilter;
import com.kohlschutter.boilerpipe.filters.simple.MinWordsFilter;

/**
 * A full-text extractor which extracts the largest text component of a page. For news articles, it
 * may perform better than the {@link DefaultExtractor}, but usually worse than
 * {@link ArticleExtractor}.
 */
public final class KeepEverythingWithMinKWordsExtractor extends ExtractorBase {

  private final MinWordsFilter filter;

  public KeepEverythingWithMinKWordsExtractor(final int kMin) {
    this.filter = new MinWordsFilter(kMin);
  }

  public boolean process(TextDocument doc) throws BoilerpipeProcessingException {
    return SimpleBlockFusionProcessor.INSTANCE.process(doc)
        | MarkEverythingContentFilter.INSTANCE.process(doc) | filter.process(doc);
  }

}
