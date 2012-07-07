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

public class ArrayVector<T> implements Vector<T>, Deque<T> {
  class Iterator implements java.util.Iterator<T> {
    int index = 0;

    @Override
    public boolean hasNext() {
      return this.index < count();
    }

    @Override
    public T next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      return at(this.index++);
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  static final ArrayVector<?> Empty = new ArrayVector<>(new Object[0]);

  @SafeVarargs
  @SuppressWarnings("unchecked")
  public static <T> ArrayVector<T> create(final T... values) {
    if (values == null || values.length == 0) {
      return (ArrayVector<T>) Empty;
    }
    for (final T t : values) {
      if (t == null) {
        throw new NullPointerException();
      }
    }
    return new ArrayVector<>(values);
  }

  final Object[] values;

  ArrayVector(final Object[] values) {
    this.values = values;
    assert invariant();
  }

  @Override
  @SuppressWarnings("unchecked")
  public T at(final int index) {
    return (T) this.values[index];
  }

  @Override
  public int count() {
    return this.values.length;
  }

  public ArrayVector<T> delete(final int index) {
    if (index == 0 && count() == 1) {
      return empty();
    }
    final Object[] newValues = new Object[count() - 1];
    System.arraycopy(this.values, 0, newValues, 0, index);
    System.arraycopy(this.values, index + 1, newValues, index, count() - index
        - 1);
    return new ArrayVector<>(newValues);
  }

  @Override
  public ArrayVector<T> eject() {
    if (isEmpty()) {
      return this;
    }
    if (count() == 1) {
      return empty();
    }
    return slice(0, count() - 1);
  }

  @SuppressWarnings("unchecked")
  public ArrayVector<T> empty() {
    return (ArrayVector<T>) ArrayVector.Empty;
  }

  @Override
  public boolean equals(final Object obj) {
    if (!(obj instanceof Seq<?>)) {
      return false;
    }
    if (!(obj instanceof ArrayVector<?>)) {
      return Seqs.equals(this, (Seq<?>) obj);
    }
    final ArrayVector<?> that = (ArrayVector<?>) obj;
    if (count() != that.count()) {
      return false;
    }
    for (int i = 0; i < count(); i++) {
      if (!at(i).equals(that.at(i))) {
        return false;
      }
    }
    return true;
  }

  @Override
  public T first() {
    if (isEmpty()) {
      throw new NoSuchElementException();
    }
    return at(0);
  }

  @Override
  public int hashCode() {
    int result = -997873203;
    for (int i = 0; i < count(); i++) {
      result = 31 * result + at(i).hashCode();
    }
    return result;
  }

  @Override
  public ArrayVector<T> inject(final T last) {
    return insert(count(), last);
  }

  public ArrayVector<T> insert(final int index, final T value) {
    if (value == null) {
      throw new NullPointerException();
    }
    final Object[] newValues = new Object[count() + 1];
    System.arraycopy(this.values, 0, newValues, 0, index);
    newValues[index] = value;
    System.arraycopy(this.values, index, newValues, index + 1, count() - index);
    return new ArrayVector<>(newValues);
  }

  @Override
  public boolean isEmpty() {
    return this == Empty;
  }

  @Override
  public java.util.Iterator<T> iterator() {
    return new Iterator();
  }

  @Override
  public T last() {
    if (isEmpty()) {
      throw new NoSuchElementException();
    }
    return at(count() - 1);
  }

  @Override
  public ArrayVector<T> pop() {
    if (isEmpty()) {
      return this;
    }
    if (count() == 1) {
      return empty();
    }
    return slice(1, count());
  }

  @Override
  public ArrayVector<T> push(final T first) {
    return insert(0, first);
  }

  @Override
  public ArrayVector<T> seq() {
    return this;
  }

  @Override
  public ArrayVector<T> set(final int index, final T value) {
    if (value == null) {
      throw new NullPointerException();
    }
    final Object[] newValues = this.values.clone();
    newValues[index] = value;
    return new ArrayVector<>(newValues);
  }

  @Override
  public ArrayVector<T> slice(final int start, final int end) {
    if (start >= count()) {
      throw new ArrayIndexOutOfBoundsException("start: " + start);
    }
    if (end < start) {
      throw new ArrayIndexOutOfBoundsException("end: " + end);
    }
    if (start == 0 && end == count()) {
      return this;
    }
    if (start == end) {
      return empty();
    }
    final Object[] newValues = new Object[end - start];
    System.arraycopy(this.values, start, newValues, 0, end - start);
    return new ArrayVector<>(newValues);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("[");
    if (!isEmpty()) {
      sb.append(at(0));
      for (int i = 1; i < count(); i++) {
        sb.append(" ");
        sb.append(at(i));
      }
    }
    sb.append("]");
    return sb.toString();
  }

  private boolean invariant() {
    assert this.values != null;
    assert this.values.length > 0 || Empty == null
        || this.values == Empty.values;
    for (final Object object : this.values) {
      assert object != null;
    }
    return true;
  }
}
