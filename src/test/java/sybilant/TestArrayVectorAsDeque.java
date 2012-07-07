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

public class TestArrayVectorAsDeque extends DequeTestCase {
  @Override
  public void testSeqToString() {
    assertEquals("[]", createArray().toString());
    assertEquals("[1]", createArray(1).toString());
    assertEquals("[1 2]", createArray(1, 2).toString());
  }

  protected ArrayVector<Number> createArray(final Number... values) {
    return ArrayVector.create(values);
  }

  @Override
  protected Deque<Number> createDeque(final Number... values) {
    return createArray(values);
  }
}
