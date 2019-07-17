package com.giorgosgaganis.seeders.runners;

import com.giorgosgaganis.seeders.Seeder;

public interface SeederRunner {
    void doSeeding(Seeder[] seeders);
}
