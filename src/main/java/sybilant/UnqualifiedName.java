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

public class UnqualifiedName {
  public static UnqualifiedName create(final String name) {
    return new UnqualifiedName(name.intern());
  }

  public final String name;

  UnqualifiedName(final String name) {
    assert name != null;
    assert name == name.intern();
    this.name = name;
  }

  @Override
  public boolean equals(final Object obj) {
    if (!(obj instanceof UnqualifiedName)) {
      return false;
    }
    final UnqualifiedName that = (UnqualifiedName) obj;
    return this.name == that.name;
  }

  @Override
  public int hashCode() {
    return 1472020675 + this.name.hashCode();
  }

  @Override
  public String toString() {
    return this.name;
  }
}
