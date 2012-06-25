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

public interface Deque<T> extends Stack<T> {
  Deque<T> eject();

  Deque<T> inject(T last);

  T last();

  @Override
  Deque<T> pop();

  @Override
  Deque<T> push(final T first);;

  @Override
  Deque<T> seq();
}
