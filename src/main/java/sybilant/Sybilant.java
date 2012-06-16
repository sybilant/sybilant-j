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

public class Sybilant {
  static final ThreadLocal<Sybilant> current = new InheritableThreadLocal<Sybilant>() {
    @Override
    protected Sybilant initialValue() {
      return new Sybilant();
    }
  };

  public static Sybilant get() {
    return current.get();
  }

  public static void set(final Sybilant current) {
    Sybilant.current.set(current);
  }
}
