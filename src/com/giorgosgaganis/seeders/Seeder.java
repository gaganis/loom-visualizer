package com.giorgosgaganis.seeders;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class Seeder {
    private final AtomicInteger timer;
    private final CountDownLatch completionLatch;

    int[] getGround() {
        return ground;
    }

    private final int[] ground;

    Seeder(AtomicInteger timer, CountDownLatch completionLatch, int groundLength) {
        this.timer = timer;
        this.completionLatch = completionLatch;
        this.ground = new int[groundLength];
    }

    public void seed() {
        for (int i = 0; i < ground.length; i++) {
            ground[i] = timer.incrementAndGet();
        }
        completionLatch.countDown();
    }
}