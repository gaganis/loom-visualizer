package com.giorgosgaganis.ploughers;

import javax.swing.JFrame;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import com.giorgosgaganis.ploughers.runners.FiberSeederRunner;
import com.giorgosgaganis.ploughers.runners.SeederRunner;
import com.giorgosgaganis.ploughers.runners.ThreadSeederRunner;


public class Main {


    public static void main(String[] args) throws InterruptedException {


        final int COUNT = 40;
        final int GROUND_LENGTH = 1_512_000;
        SeederRunner runner1 = new ThreadSeederRunner();
        Seeder[] ploughers1 = new Seeder[COUNT];
        int time1 = runExperiment(COUNT, GROUND_LENGTH, runner1, ploughers1);

        SeederRunner runner2 = new FiberSeederRunner();
        Seeder[] ploughers2 = new Seeder[COUNT];
        int time2 = runExperiment(COUNT, GROUND_LENGTH, runner2, ploughers2);
        visualizeData(time1, ploughers1, time2, ploughers2);

    }

    private static int runExperiment(int COUNT, int GROUND_LENGTH, SeederRunner runner, Seeder[] seeders) throws InterruptedException {
        AtomicInteger timer = new AtomicInteger(1);
        CountDownLatch completionLatch = new CountDownLatch(COUNT);

        for (int i = 0; i < COUNT; i++) {
            seeders[i] = new Seeder(timer, completionLatch, GROUND_LENGTH);
        }


        runner.doPloughs(seeders);


        completionLatch.await();


        return timer.get();
    }

    private static void visualizeData(int time1, Seeder[] ploughers1, int time2, Seeder[] ploughers2) {
        JFrame jFrame = new JFrame();
        jFrame.setSize(800, 600);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
        jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jFrame.add(new VisualizerJPanel(time1, ploughers1, time2, ploughers2));
    }


}

