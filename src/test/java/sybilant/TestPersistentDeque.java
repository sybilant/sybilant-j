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

public class TestPersistentDeque extends DequeTestCase {
  static final int LIMIT = 1000;

  @Test
  public void testAt() {
    for (int i = 1; i <= LIMIT; i++) {
      PersistentDeque<Number> deque = createDeque();
      for (int j = 0; j < i; j++) {
        assertEquals(j, deque.count());
        deque = deque.inject(j);
      }
      for (int j = 0; j < i; j++) {
        assertEquals(Integer.toString(i), j, deque.at(j));
      }
    }
  }

  @Test
  public void testInjectThenEject() {
    for (int i = 1; i <= LIMIT; i++) {
      Deque<Number> deque = createDeque();
      for (int j = 0; j < i; j++) {
        deque = deque.inject(j);
      }
      for (int j = i - 1; j >= 0; j--) {
        assertEquals(j, deque.last());
        deque = deque.eject();
      }
    }
  }

  @Test
  public void testInjectThenPop() {
    for (int i = 1; i <= LIMIT; i++) {
      Deque<Number> deque = createDeque();
      for (int j = 0; j < i; j++) {
        deque = deque.inject(j);
      }
      for (int j = 0; j < i; j++) {
        assertEquals(j, deque.first());
        deque = deque.pop();
      }
    }
  }

  @Test
  public void testInjectThenPopEject() {
    for (int i = 1; i <= LIMIT; i++) {
      Deque<Number> deque = createDeque();
      for (int j = 0; j < i; j++) {
        deque = deque.inject(j);
      }
      for (int j = 0; j < i / 2; j++) {
        assertEquals(j, deque.first());
        deque = deque.pop();
      }
      for (int j = i - 1; j >= i / 2; j--) {
        assertEquals(j, deque.last());
        deque = deque.eject();
      }
    }
  }

  @Test
  public void testPushInjectThenEject() {
    for (int i = 1; i <= LIMIT; i++) {
      Deque<Number> deque = createDeque();
      for (int j = i / 2 - 1; j >= 0; j--) {
        deque = deque.push(j);
      }
      for (int j = i / 2; j < i; j++) {
        deque = deque.inject(j);
      }
      for (int j = i - 1; j >= 0; j--) {
        assertEquals(j, deque.last());
        deque = deque.eject();
      }
    }
  }

  @Test
  public void testPushInjectThenPop() {
    for (int i = 1; i <= LIMIT; i++) {
      Deque<Number> deque = createDeque();
      for (int j = i / 2 - 1; j >= 0; j--) {
        deque = deque.push(j);
      }
      for (int j = i / 2; j < i; j++) {
        deque = deque.inject(j);
      }
      for (int j = 0; j < i; j++) {
        assertEquals(j, deque.first());
        deque = deque.pop();
      }
    }
  }

  @Test
  public void testPushThenEject() {
    for (int i = 1; i <= LIMIT; i++) {
      Deque<Number> deque = createDeque();
      for (int j = 0; j < i; j++) {
        deque = deque.push(j);
      }
      for (int j = 0; j < i; j++) {
        assertEquals(j, deque.last());
        deque = deque.eject();
      }
    }
  }

  @Test
  public void testPushThenPop() {
    for (int i = 1; i <= LIMIT; i++) {
      PersistentDeque<Number> deque = createDeque();
      for (int j = 0; j < i; j++) {
        assertEquals(j, deque.count());
        deque = deque.push(j);
      }
      for (int j = i - 1; j >= 0; j--) {
        assertEquals(j, deque.first());
        deque = deque.pop();
        assertEquals(j, deque.count());
      }
    }
  }

  @Test
  public void testPushThenPopEject() {
    for (int i = 1; i <= LIMIT; i++) {
      Deque<Number> deque = createDeque();
      for (int j = 0; j < i; j++) {
        deque = deque.push(j);
      }
      for (int j = i - 1; j >= i / 2; j--) {
        assertEquals(j, deque.first());
        deque = deque.pop();
      }
      for (int j = 0; j < i / 2; j++) {
        assertEquals(j, deque.last());
        deque = deque.eject();
      }
    }
  }

  @Override
  protected PersistentDeque<Number> createDeque(final Number... values) {
    return PersistentDeque.create(values);
  }
}
