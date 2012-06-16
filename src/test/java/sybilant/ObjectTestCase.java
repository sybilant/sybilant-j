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

public abstract class ObjectTestCase extends SybilantTestCase {
  @Test
  public void testObjectEquals() {
    final Object object = createObject();
    final Object object2 = createObject();
    assertTrue(object.equals(object2));
    assertTrue(object2.equals(object));
  }

  @Test
  public void testObjectEqualsGivenEqualObjects() {
    final Object object = createObject();
    for (final Object o : createEqualObjects()) {
      assertTrue(o.toString(), object.equals(o));
      assertTrue(o.toString(), o.equals(object));
    }
  }

  @Test
  public void testObjectEqualsGivenInstanceOfDifferentClass() {
    try {
      assertTrue(!createObject().equals(new Object() {
      }));
    } catch (final ClassCastException e) {
      fail("should not throw ClassCastException");
    }
  }

  @Test
  public void testObjectEqualsGivenNotEqualObjects() {
    final Object object = createObject();
    for (final Object o : createNotEqualObjects()) {
      assertTrue(o.toString(), !object.equals(o));
      assertTrue(o.toString(), !o.equals(object));
    }
  }

  @Test
  public void testObjectEqualsGivenNull() {
    try {
      assertTrue(!createObject().equals(null));
    } catch (final NullPointerException e) {
      fail("should not throw NullPointerException");
    }
  }

  @Test
  public void testObjectEqualsGivenSame() {
    final Object object = createObject();
    assertTrue(object.equals(object));
  }

  @Test
  public void testObjectHashCode() {
    final Object object = createObject();
    assertTrue(object.hashCode() == object.hashCode());
  }

  @Test
  public void testObjectHashCodeGivenEqualObjects() {
    final int hashCode = createObject().hashCode();
    for (final Object o : createEqualObjects()) {
      assertTrue(o.toString(), hashCode == o.hashCode());
      assertTrue(o.toString(), o.hashCode() == hashCode);
    }
  }

  @Test
  public void testObjectHashCodeGivenNotEqualObjects() {
    final int hashCode = createObject().hashCode();
    for (final Object o : createNotEqualObjects()) {
      assertTrue(o.toString(), hashCode != o.hashCode());
      assertTrue(o.toString(), o.hashCode() != hashCode);
    }
  }

  @Test
  public void testObjectHashCodeGivenSame() {
    final Object object = createObject();
    assertTrue(object.hashCode() == object.hashCode());
  }

  protected Object[] createEqualObjects() {
    return new Object[0];
  }

  protected Object[] createNotEqualObjects() {
    return new Object[0];
  }

  protected abstract Object createObject();
}
