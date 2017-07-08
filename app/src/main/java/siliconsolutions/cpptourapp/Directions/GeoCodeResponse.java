
package siliconsolutions.cpptourapp.Directions;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeoCodeResponse {

    @SerializedName("geocoded_waypoints")
    @Expose
    public List<GeocodedWaypoint> geocodedWaypoints = null;
    @SerializedName("routes")
    @Expose
    public List<Route> routes = null;
    @SerializedName("status")
    @Expose
    public String status;

    public GeoCodeResponse withGeocodedWaypoints(List<GeocodedWaypoint> geocodedWaypoints) {
        this.geocodedWaypoints = geocodedWaypoints;
        return this;
    }

    public GeoCodeResponse withRoutes(List<Route> routes) {
        this.routes = routes;
        return this;
    }

    public GeoCodeResponse withStatus(String status) {
        this.status = status;
        return this;
    }

}
