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

import com.kohlschutter.boilerpipe.document.TextBlock;

/**
 * Base class for some heuristics that are used by boilerpipe filters.
 */
abstract class HeuristicFilterBase {

  protected static int getNumFullTextWords(final TextBlock tb) {
    return getNumFullTextWords(tb, 9);
  }

  protected static int getNumFullTextWords(final TextBlock tb, float minTextDensity) {
    if (tb.getTextDensity() >= minTextDensity) {
      return tb.getNumWords();
    } else {
      return 0;
    }
  }
}
