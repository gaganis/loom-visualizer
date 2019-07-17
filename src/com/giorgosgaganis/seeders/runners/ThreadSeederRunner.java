package com.giorgosgaganis.seeders.runners;

import java.util.concurrent.CountDownLatch;

import com.giorgosgaganis.seeders.Seeder;


public class ThreadSeederRunner implements SeederRunner {
    public void doSeeding(Seeder[] seeders) {
        CountDownLatch gate = new CountDownLatch(seeders.length);
        for (int i = 0; i < seeders.length; i++) {
            int finalI = i;
            new Thread() {
                @Override
                public void run() {

                    gate.countDown();
                    try {
                        gate.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                    seeders[finalI].seed();
                    super.run();
                }
            }.start();
        }
    }
}
