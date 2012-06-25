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

import org.junit.Test;

public abstract class DequeTestCase extends StackTestCase {
  @Test
  public void testDequeEject() {
    assertEquals(createDeque(1), createDeque(1, 2).eject());
    final Deque<Number> deque = createDeque();
    assertSame(deque, deque.eject());
  }

  @Test
  public void testDequeInject() {
    assertEquals(createDeque(1, 2), createDeque(1).inject(2));
  }

  @Test
  public void testDequeInjectGivenNull() {
    try {
      createDeque(1).inject(null);
      fail("should throw NullPointerException");
    } catch (final NullPointerException e) {
    }
  }

  @Test
  public void testDequeIsEmpty() {
    assertTrue(createDeque(1).eject().isEmpty());
  }

  @Test
  public void testDequeLast() {
    assertEquals(2, createDeque(1, 2).last());
    try {
      createDeque().last();
      fail("should throw NoSuchElementException");
    } catch (final NoSuchElementException e) {
    }
  }

  protected abstract Deque<Number> createDeque(Number... values);

  @Override
  protected Stack<Number> createStack(final Number... values) {
    return createDeque(values);
  }
}
