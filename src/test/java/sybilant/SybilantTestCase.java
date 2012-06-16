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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

public abstract class SybilantTestCase extends Assert {
  @Before
  public void setUp() throws Exception {
    Sybilant.set(new Sybilant());
  }

  @After
  public void tearDown() throws Exception {
  }
}
