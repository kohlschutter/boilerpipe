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

import java.util.List;
import java.util.ListIterator;

import com.kohlschutter.boilerpipe.BoilerpipeFilter;
import com.kohlschutter.boilerpipe.BoilerpipeProcessingException;
import com.kohlschutter.boilerpipe.document.TextBlock;
import com.kohlschutter.boilerpipe.document.TextDocument;
import com.kohlschutter.boilerpipe.labels.DefaultLabels;

/**
 * Marks trailing headlines ({@link TextBlock}s that have the label {@link DefaultLabels#HEADING})
 * as boilerplate. Trailing means they are marked content and are below any other content block.
 */
public final class TrailingHeadlineToBoilerplateFilter implements BoilerpipeFilter {
  public static final TrailingHeadlineToBoilerplateFilter INSTANCE =
      new TrailingHeadlineToBoilerplateFilter();

  /**
   * Returns the singleton instance for ExpandTitleToContentFilter.
   */
  public static TrailingHeadlineToBoilerplateFilter getInstance() {
    return INSTANCE;
  }

  public boolean process(TextDocument doc) throws BoilerpipeProcessingException {
    boolean changes = false;

    List<TextBlock> list = doc.getTextBlocks();

    for (ListIterator<TextBlock> it = list.listIterator(list.size()); it.hasPrevious();) {
      TextBlock tb = it.previous();
      if (tb.isContent()) {
        if (tb.hasLabel(DefaultLabels.HEADING)) {
          tb.setIsContent(false);
          changes = true;
        } else {
          break;
        }
      }
    }

    return changes;
  }

}
