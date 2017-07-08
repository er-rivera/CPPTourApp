package siliconsolutions.cpptourapp.Tour;

import android.graphics.Color;
import android.util.Log;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import retrofit2.Response;
import siliconsolutions.cpptourapp.Adapters.Utilities;
import siliconsolutions.cpptourapp.Directions.GeoCodeResponse;
import siliconsolutions.cpptourapp.Directions.Leg;
import siliconsolutions.cpptourapp.Directions.StartLocation;
import siliconsolutions.cpptourapp.Directions.Step;


public class TourNavigation {
    private static TourNavigation instance;
    private List<Response<GeoCodeResponse>> responseList;
    private double totalTime;
    private List<LatLng> latLngsList;
    private com.google.android.gms.maps.model.Polyline line;
    private GoogleMap mMap;
    private String polyLine;
    private List<String> instructionList;
    private ArrayList<Marker> markersList;

    public static TourNavigation getInstance(List<Response<GeoCodeResponse>> responseList, GoogleMap mMap){
        if(instance == null){
            instance =  new TourNavigation(responseList,mMap);

        }
        return instance;
    }

    private TourNavigation(List<Response<GeoCodeResponse>> responseList, GoogleMap mMap){
        this.responseList = responseList;
        this.mMap = mMap;
    }

    private void generateInstructionList() {
        instructionList = new ArrayList<>();
        for(int i = 0; i < responseList.size();i++){
            List<Step> stepList = responseList.get(i).body().routes.get(0).getLegs().get(0).getSteps();
            if(i < markersList.size()){
                instructionList.add(markersList.get(i).getTitle());
            }
            for(int j = 0; j < stepList.size();j++){
                String html = stepList.get(j).getHtmlInstructions();
                String result = Utilities.htmlToText(html);
                instructionList.add(result);
            }
        }
    }

    public void setMarkersList(ArrayList<Marker> markersList) {
        this.markersList = markersList;
    }

    public List<String> getInstructionList() {
        generateInstructionList();
        return instructionList;
    }

    private void generateTotalTime(){
        for(Response<GeoCodeResponse> response : responseList){
            String timeString = response.body().routes.get(0).getLegs().get(0).getDuration().getText();
            if(timeString.toLowerCase().contains("hour")){
                String str = timeString.replaceAll("\\D+","");
                int hour = (int)str.charAt(0) * 60;
                int minutes = Integer.parseInt(str.substring(1));
                totalTime += hour + minutes;
            }
            else{
                String str = timeString.replaceAll("\\D+","");
                totalTime += Double.parseDouble(str);
            }
        }
    }

    public void generatePolyLines(){
        for(int i = 0; i < responseList.size();i++){
            polyLine = responseList.get(i).body().routes.get(0).getOverviewPolyline().getPoints();
            latLngsList = decodePoly(polyLine);
            for(int j = 0; j < latLngsList.size() - 1;j++){
                LatLng s = latLngsList.get(j);
                LatLng d = latLngsList.get(j + 1);
                line = mMap.addPolyline(new PolylineOptions().add(new LatLng(s.latitude,s.longitude),new LatLng(d.latitude,d.longitude))
                        .width(5).color(Color.BLUE).geodesic(true));
            }
        }
    }


    public void displayRouteMarkers(int i){
        List<Step> legs = responseList.get(i).body().routes.get(0).getLegs().get(0).getSteps();
        for(int index = 0; index < legs.size();index++){
            StartLocation s = legs.get(index).getStartLocation();
            String title = Utilities.htmlToText(legs.get(index).getHtmlInstructions());

            mMap.addMarker(new MarkerOptions().visible(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).
                    position(new LatLng(s.getLat(),s.getLng())).title(title));
        }
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

    private String getTotalDuration(){
        return null;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }

    public String getTotalTime(){
        generateTotalTime();
        if(totalTime > 60){
            int hours = (int)totalTime / 60;
            int minutes = (int)totalTime % 60;
            return hours + " hours and " + minutes + " minutes";
        }
        else{
            return (totalTime + "mins");
        }
    }

}
