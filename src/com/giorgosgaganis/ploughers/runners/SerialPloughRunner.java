package com.giorgosgaganis.ploughers.runners;

import com.giorgosgaganis.ploughers.Plougher;

public class SerialPloughRunner implements PloughRunner {
    public void doPloughs(Plougher[] ploughers) {
        for (int i = 0; i < ploughers.length; i++) {
            ploughers[i].plough();
        }
    }
}
