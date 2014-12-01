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
package com.kohlschutter.boilerpipe.sax;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

import org.xml.sax.InputSource;

/**
 * An {@link InputSourceable} for {@link HTMLFetcher}.
 */
public class HTMLDocument implements InputSourceable {
  private final Charset charset;
  private final byte[] data;

  public HTMLDocument(final byte[] data, final Charset charset) {
    this.data = data;
    this.charset = charset;
  }

  public HTMLDocument(final String data) {
    Charset cs = Charset.forName("utf-8");
    this.data = data.getBytes(cs);
    this.charset = cs;
  }

  public Charset getCharset() {
    return charset;
  }

  public byte[] getData() {
    return data;
  }

  public InputSource toInputSource() {
    final InputSource is = new InputSource(new ByteArrayInputStream(data));
    is.setEncoding(charset.name());
    return is;
  }
}
