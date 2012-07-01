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

public class test {
  public static void main(final String[] args) throws InterruptedException {
    final int RUNS = 8;
    final int STRIDE = 1;
    final int LIMIT = 10_000;
    final int WARMUP = 10_000;
    final long results[][] = new long[RUNS][LIMIT / STRIDE];
    for (int i = 0; i < RUNS + WARMUP; i++) {
      System.err.println(i);
      if (i >= WARMUP) {
        System.gc();
        Thread.sleep(1000);
        System.gc();
        Thread.sleep(1000);
        System.gc();
        Thread.sleep(1000);
        System.gc();
        Thread.sleep(1000);
        System.gc();
        Thread.sleep(1000);
      }
      PersistentDeque<Integer> deque = PersistentDeque.create();
      for (int j = 0; j < LIMIT; j++) {
        final long start = System.nanoTime();
        deque = deque.inject(j);
        final long duration = System.nanoTime() - start;
        if (i >= WARMUP && j % STRIDE == 0) {
          results[RUNS - (WARMUP + RUNS - i)][j / STRIDE] = duration;
        }
      }
    }
    for (int j = 0; j < LIMIT / STRIDE; j++) {
      System.out.print(j * STRIDE);
      for (int i = 0; i < RUNS; i++) {
        System.out.print(",");
        System.out.print(results[i][j]);
      }
      System.out.println();
    }
  }
}
