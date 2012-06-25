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

public interface Vector<T> extends Countable, Seq<T> {
  T at(int index);

  @Override
  Vector<T> pop();

  @Override
  Vector<T> seq();

  Vector<T> set(int index, T value);

  Vector<T> slice(int start, int end);
}