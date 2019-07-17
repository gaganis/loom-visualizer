package com.giorgosgaganis.seeders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import java.util.Map;

import org.junit.Test;

public class VisualizerJPanelTest {

    @Test
    public void testCalculation1() {
        Map<Integer, Integer> partitionCounts = VisualizerJPanel.calculatePartitionCounts(4,
                new int[]{1, 3, 9, 10, 11});

        assertThat(partitionCounts).containsExactly(
                entry(0, 2),
                entry(2, 3));
    }

    @Test
    public void testCalculation2() {
        Map<Integer, Integer> partitionCounts = VisualizerJPanel.calculatePartitionCounts(4,
                new int[]{2, 4, 5, 12, 13});

        assertThat(partitionCounts).containsOnly(
                entry(0, 1),
                entry(3, 2),
                entry(1, 2));
    }

    @Test
    public void testCalculation3() {
        Map<Integer, Integer> partitionCounts = VisualizerJPanel.calculatePartitionCounts(4,
                new int[]{14, 15, 16, 17, 18});

        assertThat(partitionCounts).containsOnly(
                entry(3, 2),
                entry(4, 3));
    }

    @Test
    public void testCalculation4() {
        Map<Integer, Integer> partitionCounts = VisualizerJPanel.calculatePartitionCounts(4,
                new int[]{0, 6, 7, 8, 19});

        assertThat(partitionCounts).containsOnly(
                entry(0, 1),
                entry(1, 2),
                entry(2, 1),
                entry(4, 1));
    }
}