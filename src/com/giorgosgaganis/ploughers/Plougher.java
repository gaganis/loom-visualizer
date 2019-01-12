package com.giorgosgaganis.ploughers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class Plougher {
    private final AtomicInteger timer;
    private final CountDownLatch completionLatch;

    public int[] getGround() {
        return ground;
    }

    private final int[] ground;

    Plougher(AtomicInteger timer, CountDownLatch completionLatch, int groundLength) {
        this.timer = timer;
        this.completionLatch = completionLatch;
        this.ground = new int[groundLength];
    }

    public void plough() {
        for (int i = 0; i < ground.length; i++) {
            ground[i] = timer.incrementAndGet();
        }
        completionLatch.countDown();
    }
}
