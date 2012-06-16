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

public class TestUnqualifiedName extends ObjectTestCase {
  @Test
  public void testToString() {
    assertEquals("name", createObject().toString());
  }

  @Override
  protected Object[] createEqualObjects() {
    return new Object[] { UnqualifiedName.create(new String("name")) };
  }

  @Override
  protected Object[] createNotEqualObjects() {
    return new Object[] { UnqualifiedName.create("notEqual") };
  }

  @Override
  protected Object createObject() {
    return UnqualifiedName.create("name");
  }
}
