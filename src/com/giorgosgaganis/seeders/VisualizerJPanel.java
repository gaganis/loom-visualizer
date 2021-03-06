package com.giorgosgaganis.seeders;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

public class VisualizerJPanel extends JPanel {
    private final int time1;
    private final Seeder[] seeders1;
    private final int time2;
    private final Seeder[] seeders2;

    VisualizerJPanel(int time1, Seeder[] seeders1, int time2, Seeder[] seeders2) {
        this.time1 = time1;
        this.seeders1 = seeders1;
        this.time2 = time2;
        this.seeders2 = seeders2;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setBackground(Color.WHITE);
        int height = getHeight();
        int width = getWidth();

        int partitionSize = Integer.max(time1, time2) / width;
        int laneHeight = height / (seeders1.length * 2);

        for (int i = 0; i < seeders1.length; i++) {
            int[] ground1 = seeders1[i].getGround();
            int[] ground2 = seeders2[i].getGround();

            drawGrounds(g2d, partitionSize,
                    i * 2, laneHeight,
                    ground1,
                    (int gradient) -> new Color(255, 255 - gradient, 255 - gradient));
            drawGrounds(g2d, partitionSize,
                    i * 2 + 1, laneHeight,
                    ground2,
                    (int gradient) -> new Color(255 - gradient, 255 - gradient, 255));

            drawLaneBorders(g2d, width, laneHeight, i);
        }

        drawCaptions(g, g2d, width);
    }

    private void drawCaptions(Graphics g, Graphics2D g2d, int width) {
        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        g2d.setColor(Color.RED);
        g2d.drawString("Thread run ", width - 120, 20);

        g2d.setColor(Color.BLUE);
        g2d.drawString("Fiber run ", width - 120, 50);
    }

    private void drawLaneBorders(Graphics2D g2d, int width, int laneHeight, int i) {
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawLine(0, (i * 2 + 1) * laneHeight, width, (i * 2 + 1) * laneHeight);
        g2d.drawLine(0, (i * 2) * laneHeight, width, (i * 2) * laneHeight);
    }

    private void drawGrounds(Graphics2D g2d, int partitionSize, int laneNumber, int laneHeight, int[] ground1, ColorCreator colorCreator) {
        Map<Integer, Integer> integerIntegerMap = calculatePartitionCounts(partitionSize, ground1);

        for (Integer key : integerIntegerMap.keySet()) {

            int count = integerIntegerMap.get(key);
            int gradient = ((count * 160) / partitionSize) + (255 - 160);
            g2d.setColor(colorCreator.createColor(gradient));

            g2d.drawLine(key, laneNumber * laneHeight, key, (laneNumber + 1) * laneHeight);
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

@FunctionalInterface
interface ColorCreator {
    Color createColor(int gradient);
}