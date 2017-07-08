package siliconsolutions.cpptourapp.GPS;

import android.location.Location;
import android.os.Bundle;

/**
 * Created by Phuoc on 4/27/2017.
 */

public interface GPSTrackerListener {
    void onGPSTrackerLocationChanged(Location location);
    void onGPSTrackerStatusChanged(String provider, int status, Bundle extra);
}
