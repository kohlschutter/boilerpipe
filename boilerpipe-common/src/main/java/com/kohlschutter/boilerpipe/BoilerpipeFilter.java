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

import com.kohlschutter.boilerpipe.document.TextDocument;

/**
 * A generic {@link BoilerpipeFilter}. Takes a {@link TextDocument} and processes it somehow.
 */
public interface BoilerpipeFilter {

  /**
   * Processes the given document <code>doc</code>.
   * 
   * @param doc The {@link TextDocument} that is to be processed.
   * @return <code>true</code> if changes have been made to the {@link TextDocument}.
   * @throws BoilerpipeProcessingException
   */
  boolean process(final TextDocument doc) throws BoilerpipeProcessingException;

}
