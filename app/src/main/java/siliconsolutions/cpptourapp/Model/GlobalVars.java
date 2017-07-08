package siliconsolutions.cpptourapp.Model;

/**
 * Created by Phuoc on 4/27/2017.
 */

public class GlobalVars {

    public static MyLocation location = new MyLocation(10.7629886, 106.6821975);

    public static MyLocation getUserLocation() {
            return location;
    }
}
