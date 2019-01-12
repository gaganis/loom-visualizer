package com.giorgosgaganis;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {


    public static void main(String[] args) throws InterruptedException {


        final int COUNT = 40;
        final int GROUND_LENGTH = 128000;
        PloughRunner runner = new ThreadPloughRunner();
        runExperiment(COUNT, GROUND_LENGTH, runner);

        PloughRunner runner2 = new FiberPloughRunner();
        runExperiment(COUNT, GROUND_LENGTH, runner2);
    }

    private static void runExperiment(int COUNT, int GROUND_LENGTH, PloughRunner runner) throws InterruptedException {
        AtomicInteger timer = new AtomicInteger(1);
        CountDownLatch completionLatch = new CountDownLatch(COUNT);

        Plougher[] ploughers = new Plougher[COUNT];
        for (int i = 0; i < COUNT; i++) {
            ploughers[i] = new Plougher(timer, completionLatch, GROUND_LENGTH);
        }


        runner.doPloughs(ploughers);


        completionLatch.await();


        JFrame jFrame = new JFrame(runner.getClass().getCanonicalName());
        jFrame.setSize(800, 600);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
        jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jFrame.add(new VisualizerJPanel(timer.get(), ploughers));
    }


}

interface PloughRunner {
    void doPloughs(Plougher[] ploughers);
}

class SerialPloughRunner implements PloughRunner {
    public void doPloughs(Plougher[] ploughers) {
        for (int i = 0; i < ploughers.length; i++) {
            ploughers[i].plough();
        }
    }
}

class ThreadPloughRunner implements PloughRunner {
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

class FiberPloughRunner implements PloughRunner {
    public void doPloughs(Plougher[] ploughers) {
        CountDownLatch gate = new CountDownLatch(ploughers.length);
        for (int i = 0; i < ploughers.length; i++) {
            int finalI = i;
            Fiber fiber = Fiber.schedule(() -> {

                gate.countDown();
                try {
                    gate.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
                ploughers[finalI].plough();
            });
        }
    }
}

class Plougher {
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

class VisualizerJPanel extends JPanel {
    private static final int LANE_HEIGHT = 20;
    private final int time;
    private final Plougher[] ploughers;

    VisualizerJPanel(int time, Plougher[] ploughers) {
        this.time = time;
        this.ploughers = ploughers;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setBackground(Color.BLACK);
        int height = getHeight();
        int width = getWidth();

        int partitionSize = (time) / (width);

        for (int i = 0; i < ploughers.length; i++) {
            int[] ground = ploughers[i].getGround();

            Map<Integer, Integer> integerIntegerMap = calculatePartitionCounts(partitionSize, ground);

            for (Integer key : integerIntegerMap.keySet()) {
                int currentSize = partitionSize;

                int count = integerIntegerMap.get(key);
                int red = (count * 255) / currentSize;
                g2d.setColor(new Color(255, 255 - red, 255 - red));

                g2d.drawLine(key, i * LANE_HEIGHT, key, (i + 1) * LANE_HEIGHT);
            }
        }


    }

    static Map<Integer, Integer> calculatePartitionCounts(int partitionSize, int[] array) {
        HashMap<Integer, Integer> map = new HashMap<>();
        int partitionNumber = array[0] / partitionSize;
        int count = 1;
        int i = 1;
        while (i < array.length) {
            if (array[i] < (partitionNumber + 1) * partitionSize) {
                count++;
            } else {
                map.put(partitionNumber, count);
                partitionNumber = array[i] / partitionSize;
                count = 1;
            }
            i++;
        }
        map.put(partitionNumber, count);
        return map;
    }
}

