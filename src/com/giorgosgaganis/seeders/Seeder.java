package com.giorgosgaganis.seeders;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class Seeder {
    private final AtomicInteger seedTimer;
    private final CountDownLatch completionLatch;

    int[] getGround() {
        return ground;
    }

    private final int[] ground;

    Seeder(AtomicInteger seedTimer, CountDownLatch completionLatch, int groundLength) {
        this.seedTimer = seedTimer;
        this.completionLatch = completionLatch;
        this.ground = new int[groundLength];
    }

    public void seed() {
        for (int i = 0; i < ground.length; i++) {
            ground[i] = seedTimer.incrementAndGet();
        }
        completionLatch.countDown();
    }
}
