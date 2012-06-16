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

public class QualifiedName {
  public static QualifiedName create(final String namespace, final String name) {
    return new QualifiedName(namespace.intern(), name.intern());
  }

  public final String namespace;
  public final String name;

  QualifiedName(final String namespace, final String name) {
    assert namespace != null;
    assert namespace == namespace.intern();
    assert name != null;
    assert name == name.intern();
    this.namespace = namespace;
    this.name = name;
  }

  @Override
  public boolean equals(final Object obj) {
    if (!(obj instanceof QualifiedName)) {
      return false;
    }
    final QualifiedName that = (QualifiedName) obj;
    return this.namespace == that.namespace && this.name == that.name;
  }

  @Override
  public int hashCode() {
    int result = -1183918184 + this.namespace.hashCode();
    result = 31 * result + this.name.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return this.namespace + "/" + this.name;
  }
}
