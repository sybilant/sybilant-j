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

import java.util.NoSuchElementException;

public interface Seqable<T> extends Iterable<T> {
  public static class Iterator<T> implements java.util.Iterator<T> {
    Seqable<T> seqable;

    Iterator(final Seq<T> seqable) {
      this.seqable = seqable;
    }

    @Override
    public boolean hasNext() {
      return !this.seqable.isEmpty();
    }

    @Override
    public T next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      final Seq<T> seq = this.seqable.seq();
      this.seqable = seq.pop();
      return seq.first();
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  boolean isEmpty();

  Seq<T> seq();
}
