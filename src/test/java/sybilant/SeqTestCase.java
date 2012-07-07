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

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Test;

public abstract class SeqTestCase extends ObjectTestCase {
  @Test
  public void testSeqCreate() {
    final Seq<Number> seq = createSeq();
    assertSame(seq, createSeq());
    assertSame(seq, createSeq(new Number[0]));
    assertSame(seq, createSeq((Number[]) null));
  }

  @Test
  public void testSeqCreateGivenNull() {
    try {
      createSeq(1, null);
      fail("should throw NullPointerException");
    } catch (final NullPointerException e) {
    }
  }

  @Test
  public void testSeqFirst() {
    assertEquals(1, createSeq(1).first());
    try {
      createSeq().first();
      fail("should throw NoSuchElementException");
    } catch (final NoSuchElementException e) {
    }
  }

  @Test
  public void testSeqIsEmpty() {
    assertTrue(createSeq().isEmpty());
    assertTrue(!createSeq(1).isEmpty());
    assertTrue(createSeq(1).pop().isEmpty());
  }

  @Test
  public void testSeqIterator() {
    final Iterator<Number> itr = createSeq(1, 2).iterator();
    assertTrue(itr.hasNext());
    try {
      itr.remove();
      fail("should throw UnsupportedOperationException");
    } catch (final UnsupportedOperationException e) {
    }
    assertEquals(1, itr.next());
    assertTrue(itr.hasNext());
    assertEquals(2, itr.next());
    assertTrue(!itr.hasNext());
    try {
      itr.next();
      fail("should throw NoSuchElementException");
    } catch (final NoSuchElementException e) {
    }
  }

  @Test
  public void testSeqPop() {
    Seq<Number> seq = createSeq(1, 2);
    assertTrue(!seq.isEmpty());
    assertEquals(1, seq.first());
    seq = seq.pop().seq();
    assertTrue(!seq.isEmpty());
    assertEquals(2, seq.first());
    seq = seq.pop().seq();
    assertTrue(seq.isEmpty());
    assertSame(seq, seq.pop());
    try {
      seq.first();
      fail("should throw NoSuchElementException");
    } catch (final NoSuchElementException e) {
    }
  }

  @Test
  public void testSeqSeq() {
    final Seq<Number> seq = createSeq(1);
    assertSame(seq, seq.seq());
  }

  @Test
  public void testSeqToString() {
    assertEquals("()", createSeq().toString());
    assertEquals("(1)", createSeq(1).toString());
    assertEquals("(1 2)", createSeq(1, 2).toString());
  }

  @Override
  protected Object[] createEqualObjects() {
    return new Object[] { ArrayVector.create(1, 2), PersistentDeque.create(1, 2) };
  }

  @Override
  protected Object[] createNotEqualObjects() {
    return new Object[] { createSeq(), createSeq(1), createSeq(3, 2),
        createSeq(1, 3), createSeq(2, 1), createSeq(1, 2, 3) };
  }

  @Override
  protected Object createObject() {
    return createSeq(1, 2);
  }

  protected abstract Seq<Number> createSeq(Number... values);
}
