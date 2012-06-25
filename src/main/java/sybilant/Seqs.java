/**
 * Copyright © 2012 Paul Stadig. All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * This Source Code Form is "Incompatible With Secondary Licenses", as
 * defined by the Mozilla Public License, v. 2.0.
 */
package sybilant;


public class Seqs {
  public static boolean equals(final Seq<?> seq1, final Seq<?> seq2) {
    Seq<?> s1 = seq1;
    Seq<?> s2 = seq2;
    while (!s1.isEmpty() && !s2.isEmpty()) {
      if (!s1.first().equals(s2.first())) {
        return false;
      }
      s1 = s1.pop().seq();
      s2 = s2.pop().seq();
    }
    return s1.isEmpty() && s2.isEmpty();
  }

  public static int hashCode(final Seq<?> seq) {
    int result = -997873203;
    for (final Object object : seq) {
      result = 31 * result + object.hashCode();
    }
    return result;
  }

  public static String toString(final Seq<?> seq) {
    final StringBuilder sb = new StringBuilder("(");
    if (!seq.isEmpty()) {
      sb.append(seq.first());
      Seq<?> s = seq.pop().seq();
      while (!s.isEmpty()) {
        sb.append(" ");
        sb.append(s.first());
        s = s.pop().seq();
      }
    }
    sb.append(")");
    return sb.toString();
  }
}
