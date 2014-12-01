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
package com.kohlschutter.boilerpipe.labels;

import com.kohlschutter.boilerpipe.conditions.TextBlockCondition;
import com.kohlschutter.boilerpipe.document.TextBlock;

/**
 * Adds labels to a {@link TextBlock} if the given criteria are met.
 */
public final class ConditionalLabelAction extends LabelAction {

  private final TextBlockCondition condition;

  public ConditionalLabelAction(TextBlockCondition condition, String... labels) {
    super(labels);
    this.condition = condition;
  }

  public void addTo(final TextBlock tb) {
    if (condition.meetsCondition(tb)) {
      addLabelsTo(tb);
    }
  }
}
