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
package com.kohlschutter.boilerpipe.filters.english;

import com.kohlschutter.boilerpipe.BoilerpipeFilter;
import com.kohlschutter.boilerpipe.BoilerpipeProcessingException;
import com.kohlschutter.boilerpipe.document.TextBlock;
import com.kohlschutter.boilerpipe.document.TextDocument;

/**
 * Keeps only those content blocks which contain at least k full-text words (measured by
 * {@link HeuristicFilterBase#getNumFullTextWords(TextBlock)}). k is 30 by default.
 */
public final class MinFulltextWordsFilter extends HeuristicFilterBase implements BoilerpipeFilter {
  public static final MinFulltextWordsFilter DEFAULT_INSTANCE = new MinFulltextWordsFilter(30);
  private final int minWords;

  public static MinFulltextWordsFilter getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  public MinFulltextWordsFilter(final int minWords) {
    this.minWords = minWords;
  }

  public boolean process(final TextDocument doc) throws BoilerpipeProcessingException {

    boolean changes = false;

    for (TextBlock tb : doc.getTextBlocks()) {
      if (!tb.isContent()) {
        continue;
      }
      if (getNumFullTextWords(tb) < minWords) {
        tb.setIsContent(false);
        changes = true;
      }

    }

    return changes;

  }
}
