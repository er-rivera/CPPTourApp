package siliconsolutions.cpptourapp.Model;

import android.graphics.Color;
import android.util.Log;
import android.widget.ImageButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import siliconsolutions.cpptourapp.Adapters.Utilities;
import siliconsolutions.cpptourapp.Directions.Bounds;
import siliconsolutions.cpptourapp.Directions.DirectionsService;
import siliconsolutions.cpptourapp.Directions.GeoCodeResponse;
import siliconsolutions.cpptourapp.Directions.Leg;
import siliconsolutions.cpptourapp.Directions.Route;
import siliconsolutions.cpptourapp.Directions.Step;
import siliconsolutions.cpptourapp.GPS.GPSTracker;

public class Navigation {
    private static Navigation instance;
    private LatLng destination;
    private GPSTracker gpsTracker;
    private DirectionsService service;
    private com.google.android.gms.maps.model.Polyline line;
    private GoogleMap mMap;
    private Marker marker;
    private String distance;
    private String duration;
    private Response<GeoCodeResponse> response;
    private List<Route> routes;
    private List<Response<GeoCodeResponse> > tourResponses;
    private List<Leg> legs;
    private List<Step> steps;
    private List<LatLng> latLngsList;
    private List<Marker> markers;
    private Bounds bounds;
    String polyLine;

    public static Navigation getInstance(){
        if(instance == null){
            instance =  new Navigation();
        }
        return instance;
    }

    private Navigation(){

        distance = "";
        duration = "";
        markers = new ArrayList<>();
        tourResponses = new ArrayList<>();
    }

    public void generatePolyLine(){
        latLngsList = decodePoly(polyLine);
        for(int i = 0; i < latLngsList.size() - 1;i++){
            LatLng s = latLngsList.get(i);
            LatLng d = latLngsList.get(i + 1);
            line = mMap.addPolyline(new PolylineOptions().add(new LatLng(s.latitude,s.longitude),new LatLng(d.latitude,d.longitude))
                    .width(5).color(Color.BLUE).geodesic(true));
        }
    }

    private void generateMarkers(){
        for(int i = 0; i < steps.size();i++){
            String s = Utilities.htmlToText(steps.get(i).getHtmlInstructions());
            Marker m = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).visible(true).
                    title(s)
                    .position(new LatLng(steps.get(i).getStartLocation().getLat(),steps.get(i).getStartLocation().getLng())));
            markers.add(m);

            Log.i("MARKER " + i + "LOCATION", markers.get(i).getPosition().latitude + "," + markers.get(i).getPosition().longitude);
            Log.i("MARKER " + i + " NAME", markers.get(i).getTitle());
            //mMap.addMarker(new MarkerOptions().position(latLng).title(s));
            //m.setAlpha(0.0f);
            //m.setVisible(true);
            //m.setInfoWindowAnchor(.5f,1.0f);
        }
    }


    public void setUp(){
        routes = response.body().routes;
        bounds = response.body().routes.get(0).getBounds();
        legs = routes.get(0).getLegs();
        steps = legs.get(0).getSteps();
        polyLine = routes.get(0).getOverviewPolyline().getPoints();
        generatePolyLine();
        generateMarkers();
        distance = legs.get(0).getDistance().getText();
        duration = legs.get(0).getDuration().getText();
    }

    public void start(){

    }

    private ArrayList<LatLng> decodePoly(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),(((double) lng / 1E5)));
            poly.add(p);
        }

        for(int i=0;i < poly.size(); i++){
            Log.i("Location", "Point sent: Latitude: " + poly.get(i).latitude + " Longitude: " + poly.get(i).longitude);
        }
        return poly;
    }

    public void setGPSTracker(GPSTracker gps){
        gpsTracker = gps;
    }

    public void setDestination(LatLng dest){
        destination = dest;
    }

    public void setMap(GoogleMap map){
        mMap = map;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public List<Marker> getMarkers() {
        return markers;
    }

    public String getDistance() {
        return distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setResponse(Response<GeoCodeResponse> response) {
        this.response = response;
    }

    public void setMarkersList(ArrayList<Marker> markersList) {
        this.markers = markersList;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public List<Response<GeoCodeResponse>> getTourResponses() {
        return tourResponses;
    }

    public void setPolyLine(String polyLine) {
        this.polyLine = polyLine;
    }
}
