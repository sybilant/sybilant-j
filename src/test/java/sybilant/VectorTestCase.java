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

import org.junit.Test;

public abstract class VectorTestCase extends SeqTestCase {
  @Override
  public void testSeqToString() {
    assertEquals("[]", createVector().toString());
    assertEquals("[1]", createVector(1).toString());
    assertEquals("[1 2]", createVector(1, 2).toString());
  }

  @Test
  public void testVectorAt() {
    final Vector<Number> vector = createVector(1, 2, 3);
    assertEquals(1, vector.at(0));
    assertEquals(2, vector.at(1));
    assertEquals(3, vector.at(2));
  }

  @Test
  public void testVectorAtGivenNegativeIndex() {
    try {
      createVector(1).at(-1);
      fail("should throw ArrayIndexOutOfBoundsException");
    } catch (final ArrayIndexOutOfBoundsException e) {
    }
  }

  @Test
  public void testVectorAtGivenOutOfBoundsIndex() {
    try {
      createVector(1).at(1);
      fail("should throw ArrayIndexOutOfBoundsException");
    } catch (final ArrayIndexOutOfBoundsException e) {
    }
  }

  @Test
  public void testVectorCount() {
    assertEquals(0, createVector().count());
    assertEquals(1, createVector(1).count());
  }

  @Test
  public void testVectorIsEmpty() {
    assertTrue(createVector(1, 2, 3).slice(0, 0).isEmpty());
    assertTrue(createVector(1, 2, 3).slice(1, 1).isEmpty());
    assertTrue(createVector(1, 2, 3).slice(2, 2).isEmpty());
  }

  @Test
  public void testVectorSet() {
    final Vector<Number> vector = createVector(1, 2);
    assertEquals(createVector(3, 2), vector.set(0, 3));
    assertEquals(createVector(1, 3), vector.set(1, 3));
  }

  @Test
  public void testVectorSetGivenNegativeIndex() {
    try {
      createVector(1).set(-1, 3);
      fail("should throw ArrayIndexOutOfBoundsException");
    } catch (final ArrayIndexOutOfBoundsException e) {
    }
  }

  @Test
  public void testVectorSetGivenNull() {
    try {
      createVector(1).set(0, null);
      fail("should throw NullPointerException");
    } catch (final NullPointerException e) {
    }
  }

  @Test
  public void testVectorSetGivenOutOfBoundsIndex() {
    try {
      createVector(1).set(1, 3);
      fail("should throw ArrayIndexOutOfBoundsException");
    } catch (final ArrayIndexOutOfBoundsException e) {
    }
  }

  @Test
  public void testVectorSlice() {
    final Vector<Number> vector = createVector(1, 2, 3);
    assertSame(vector, vector.slice(0, 3));
    assertEquals(createVector(1, 2), vector.slice(0, 2));
    assertEquals(createVector(2, 3), vector.slice(1, 3));
    assertEquals(createVector(1), vector.slice(0, 1));
    assertEquals(createVector(2), vector.slice(1, 2));
    assertEquals(createVector(3), vector.slice(2, 3));
    assertSame(createVector(), vector.slice(0, 0));
    assertSame(createVector(), vector.slice(1, 1));
    assertSame(createVector(), vector.slice(2, 2));
  }

  @Test
  public void testVectorSliceGivenEndLessThanStart() {
    try {
      createVector(1, 2, 3).slice(2, 1);
      fail("should throw ArrayIndexOutOfBoundsException");
    } catch (final ArrayIndexOutOfBoundsException e) {
    }
  }

  @Test
  public void testVectorSliceGivenNegativeStart() {
    try {
      createVector(1, 2, 3).slice(-1, 3);
      fail("should throw ArrayIndexOutOfBoundsException");
    } catch (final ArrayIndexOutOfBoundsException e) {
    }
  }

  @Test
  public void testVectorSliceGivenOutOfBoundsEnd() {
    try {
      createVector(1, 2, 3).slice(0, 4);
      fail("should throw ArrayIndexOutOfBoundsException");
    } catch (final ArrayIndexOutOfBoundsException e) {
    }
  }

  @Test
  public void testVectorSliceGivenOutOfBoundsStart() {
    try {
      createVector(1, 2, 3).slice(3, 3);
      fail("should throw ArrayIndexOutOfBoundsException");
    } catch (final ArrayIndexOutOfBoundsException e) {
    }
  }

  @Override
  protected Seq<Number> createSeq(final Number... values) {
    return createVector(values);
  }

  protected abstract Vector<Number> createVector(Number... values);
}
