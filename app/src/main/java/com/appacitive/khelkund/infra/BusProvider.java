package com.appacitive.khelkund.infra;

import com.squareup.otto.Bus;

/**
 * Created by sathley on 3/28/2015.
 */
public final class BusProvider {
    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {
        // No instances.
    }
}
