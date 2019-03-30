package com.giorgosgaganis.ploughers;

import javax.swing.JFrame;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import com.giorgosgaganis.ploughers.runners.FiberPloughRunner;
import com.giorgosgaganis.ploughers.runners.PloughRunner;
import com.giorgosgaganis.ploughers.runners.ThreadPloughRunner;


public class Main {


    public static void main(String[] args) throws InterruptedException {


        final int COUNT = 40;
        final int GROUND_LENGTH = 1_512_000;
        PloughRunner runner1 = new ThreadPloughRunner();
        Plougher[] ploughers1 = new Plougher[COUNT];
        int time1 = runExperiment(COUNT, GROUND_LENGTH, runner1, ploughers1);

        PloughRunner runner2 = new FiberPloughRunner();
        Plougher[] ploughers2 = new Plougher[COUNT];
        int time2 = runExperiment(COUNT, GROUND_LENGTH, runner2, ploughers2);
        visualizeData(time1, ploughers1, time2, ploughers2);

    }

    private static int runExperiment(int COUNT, int GROUND_LENGTH, PloughRunner runner, Plougher[] ploughers) throws InterruptedException {
        AtomicInteger timer = new AtomicInteger(1);
        CountDownLatch completionLatch = new CountDownLatch(COUNT);

        for (int i = 0; i < COUNT; i++) {
            ploughers[i] = new Plougher(timer, completionLatch, GROUND_LENGTH);
        }


        runner.doPloughs(ploughers);


        completionLatch.await();


        return timer.get();
    }

    private static void visualizeData(int time1, Plougher[] ploughers1, int time2, Plougher[] ploughers2) {
        JFrame jFrame = new JFrame();
        jFrame.setSize(800, 600);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
        jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jFrame.add(new VisualizerJPanel(time1, ploughers1, time2, ploughers2));
    }


}

