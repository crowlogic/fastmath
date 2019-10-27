/*
 * Decompiled with CFR 0.144.
 */
package util;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

public final class TerseThreadFactory implements
                                      ForkJoinPool.ForkJoinWorkerThreadFactory
{
  @Override
  public ForkJoinWorkerThread newThread(ForkJoinPool pool)
  {
    ForkJoinWorkerThread worker = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
    worker.setName("thread-" + worker.getPoolIndex());
    return worker;
  }
}
