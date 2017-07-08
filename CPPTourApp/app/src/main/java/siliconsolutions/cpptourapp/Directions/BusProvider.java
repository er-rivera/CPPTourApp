package siliconsolutions.cpptourapp.Directions;

import com.squareup.otto.Bus;

/**
 * Created by user on 5/17/17.
 */

public final class BusProvider {
    private static final Bus BUS = new Bus();

    public static Bus getInstance(){
        return BUS;
    }

    private BusProvider(){

    }
}
