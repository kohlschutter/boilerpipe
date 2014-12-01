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
package com.kohlschutter.boilerpipe.filters.heuristics;

import com.kohlschutter.boilerpipe.BoilerpipeFilter;
import com.kohlschutter.boilerpipe.BoilerpipeProcessingException;
import com.kohlschutter.boilerpipe.document.TextBlock;
import com.kohlschutter.boilerpipe.document.TextDocument;
import com.kohlschutter.boilerpipe.labels.DefaultLabels;

/**
 * Marks nested list-item blocks after the end of the main content.
 */
public final class ListAtEndFilter implements BoilerpipeFilter {
  public static final ListAtEndFilter INSTANCE = new ListAtEndFilter();

  private ListAtEndFilter() {
  }

  public boolean process(final TextDocument doc) throws BoilerpipeProcessingException {

    boolean changes = false;

    int tagLevel = Integer.MAX_VALUE;
    for (TextBlock tb : doc.getTextBlocks()) {
      if (tb.isContent() && tb.hasLabel(DefaultLabels.VERY_LIKELY_CONTENT)) {
        tagLevel = tb.getTagLevel();
      } else {
        if (tb.getTagLevel() > tagLevel && tb.hasLabel(DefaultLabels.MIGHT_BE_CONTENT)
            && tb.hasLabel(DefaultLabels.LI) && tb.getLinkDensity() == 0) {
          tb.setIsContent(true);
          changes = true;
        } else {
          tagLevel = Integer.MAX_VALUE;
        }
      }
    }

    return changes;

  }
}
