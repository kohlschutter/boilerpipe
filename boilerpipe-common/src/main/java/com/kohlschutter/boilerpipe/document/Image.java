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
package com.kohlschutter.boilerpipe.document;

/**
 * Represents an Image resource that is contained in the document.
 * 
 * Any of the attributes may be null, except for "src".
 */
public class Image implements Comparable<Image> {
  private final String src;
  private final String width;
  private final String height;
  private final String alt;
  private final int area;

  public Image(final String src, final String width, final String height, final String alt) {
    this.src = src;
    if (src == null) {
      throw new NullPointerException("src attribute must not be null");
    }
    this.width = nullTrim(width);
    this.height = nullTrim(height);
    this.alt = nullTrim(alt);

    if (width != null && height != null) {
      int a;
      try {
        a = Integer.parseInt(width) * Integer.parseInt(height);
      } catch (NumberFormatException e) {
        a = -1;
      }
      this.area = a;
    } else {
      this.area = -1;
    }
  }

  public String getSrc() {
    return src;
  }

  public String getWidth() {
    return width;
  }

  public String getHeight() {
    return height;
  }

  public String getAlt() {
    return alt;
  }

  private static String nullTrim(String s) {
    if (s == null) {
      return null;
    }
    s = s.trim();
    if (s.length() == 0) {
      return null;
    }
    return s;
  }

  /**
   * Returns the image's area (specified by width * height), or -1 if width/height weren't both
   * specified or could not be parsed.
   * 
   * @return
   */
  public int getArea() {
    return area;
  }

  public String toString() {
    return src + "\twidth=" + width + "\theight=" + height + "\talt=" + alt + "\tarea=" + area;
  }

  @Override
  public int compareTo(Image o) {
    if (o == this) {
      return 0;
    }
    if (area > o.area) {
      return -1;
    } else if (area == o.area) {
      return src.compareTo(o.src);
    } else {
      return 1;
    }
  }
}
