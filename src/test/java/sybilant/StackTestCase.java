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

public abstract class StackTestCase extends SeqTestCase {
  @Test
  public void testStackPush() {
    assertEquals(createStack(2, 1), createStack(1).push(2));
  }

  @Test
  public void testStackPushGivenNull() {
    try {
      createStack(1).push(null);
      fail("should throw NullPointerException");
    } catch (final NullPointerException e) {
    }
  }

  @Override
  protected Seq<Number> createSeq(final Number... values) {
    return createStack(values);
  }

  protected abstract Stack<Number> createStack(Number... values);
}
