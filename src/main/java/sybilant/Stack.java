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

public interface Stack<T> extends Seq<T> {
  @Override
  Stack<T> pop();

  Stack<T> push(T first);

  @Override
  Stack<T> seq();
}
