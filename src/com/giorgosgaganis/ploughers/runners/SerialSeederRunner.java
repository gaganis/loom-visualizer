package com.giorgosgaganis.ploughers.runners;

import com.giorgosgaganis.ploughers.Seeder;

public class SerialSeederRunner implements SeederRunner {
    public void doPloughs(Seeder[] seeders) {
        for (Seeder seeder : seeders) {
            seeder.seed();
        }
    }
}
