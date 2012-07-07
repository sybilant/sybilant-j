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

public class TestArrayVector extends VectorTestCase {
  @Test
  public void testArrayDelete() {
    assertEquals(createArray(2, 3), createArray(1, 2, 3).delete(0));
    assertEquals(createArray(1, 3), createArray(1, 2, 3).delete(1));
    assertEquals(createArray(1, 2), createArray(1, 2, 3).delete(2));
  }

  @Test
  public void testArrayInsert() {
    assertEquals(createArray(3, 1, 2), createArray(1, 2).insert(0, 3));
    assertEquals(createArray(1, 3, 2), createArray(1, 2).insert(1, 3));
    assertEquals(createArray(1, 2, 3), createArray(1, 2).insert(2, 3));
  }

  @Test
  public void testArrayInsertGivenNull() {
    try {
      createArray().insert(0, null);
      fail("should throw NullPointerException");
    } catch (final NullPointerException e) {
    }
  }

  @Test
  public void testArrayIsEmpty() {
    assertTrue(createArray(1).delete(0).isEmpty());
  }

  protected ArrayVector<Number> createArray(final Number... values) {
    return ArrayVector.create(values);
  }

  @Override
  protected Vector<Number> createVector(final Number... values) {
    return createArray(values);
  }
}
