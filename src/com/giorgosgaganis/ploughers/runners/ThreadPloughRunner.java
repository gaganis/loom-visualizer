package com.giorgosgaganis.ploughers.runners;

import java.util.concurrent.CountDownLatch;

import com.giorgosgaganis.ploughers.Plougher;


public class ThreadPloughRunner implements PloughRunner {
    public void doPloughs(Plougher[] ploughers) {
        CountDownLatch gate = new CountDownLatch(ploughers.length);
        for (int i = 0; i < ploughers.length; i++) {
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
                    ploughers[finalI].plough();
                    super.run();
                }
            }.start();
        }
    }
}
