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
package com.kohlschutter.boilerpipe.filters.simple;

import com.kohlschutter.boilerpipe.BoilerpipeFilter;
import com.kohlschutter.boilerpipe.BoilerpipeProcessingException;
import com.kohlschutter.boilerpipe.document.TextBlock;
import com.kohlschutter.boilerpipe.document.TextDocument;

/**
 * Marks all blocks as content.
 */
public final class MarkEverythingContentFilter implements BoilerpipeFilter {
  public static final MarkEverythingContentFilter INSTANCE = new MarkEverythingContentFilter();

  private MarkEverythingContentFilter() {
  }

  public boolean process(final TextDocument doc) throws BoilerpipeProcessingException {

    boolean changes = false;

    for (TextBlock tb : doc.getTextBlocks()) {
      if (!tb.isContent()) {
        tb.setIsContent(true);
        changes = true;
      }
    }

    return changes;

  }
}
