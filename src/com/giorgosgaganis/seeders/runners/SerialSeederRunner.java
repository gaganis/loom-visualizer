package com.giorgosgaganis.seeders.runners;

import com.giorgosgaganis.seeders.Seeder;

public class SerialSeederRunner implements SeederRunner {
    public void doSeeding(Seeder[] seeders) {
        for (Seeder seeder : seeders) {
            seeder.seed();
        }
    }
}
