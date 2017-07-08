package siliconsolutions.cpptourapp.Directions;


/**
 * Created by user on 5/3/17.
 */
public class GoogleGeoCodeResponse {
    private String status;

    private GeocodedWaypoint[] waypoints;

    private Route[] routes;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public GeocodedWaypoint[] getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(GeocodedWaypoint[] waypoints) {
        this.waypoints = waypoints;
    }

    public Route[] getRoutes() {
        return routes;
    }

    public void setRoutes(Route[] routes) {
        this.routes = routes;
    }
}