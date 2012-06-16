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

public class TestQualifiedName extends ObjectTestCase {
  @Test
  public void testToString() {
    assertEquals("namespace/name", createObject().toString());
  }

  @Override
  protected Object[] createEqualObjects() {
    return new Object[] {
        QualifiedName.create(new String("namespace"), "name"),
        QualifiedName.create("namespace", new String("name")) };
  }

  @Override
  protected Object[] createNotEqualObjects() {
    return new Object[] { QualifiedName.create("notEqual", "name"),
        QualifiedName.create("namespace", "notEqual") };
  }

  @Override
  protected Object createObject() {
    return QualifiedName.create("namespace", "name");
  }
}
