package com.giorgosgaganis.seeders.runners;

import java.util.concurrent.CountDownLatch;

import com.giorgosgaganis.seeders.Seeder;

public class FiberSeederRunner implements SeederRunner {
    public void doSeeding(Seeder[] seeders) {
        FiberScope scope = FiberScope.open();
        CountDownLatch gate = new CountDownLatch(seeders.length);
        for (int i = 0; i < seeders.length; i++) {
            int finalI = i;
            Fiber fiber = scope.schedule(() -> {

                gate.countDown();
                try {
                    gate.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
                seeders[finalI].seed();
            });
        }
    }
}
