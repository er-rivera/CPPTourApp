package siliconsolutions.cpptourapp.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Phuoc on 5/28/2017.
 */

public class BusRouteC extends Location {
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longtitude")
    private String longtitude;
    @SerializedName("name")
    private String busRouteName;
    @SerializedName("number")
    private String busRouteNumber;
    private boolean isFavorite;

    public BusRouteC(String latitude, String longtitude, String busRouteName, String busRouteNumber) {
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.busRouteName = busRouteName;
        this.busRouteNumber = busRouteNumber;
        isFavorite = false;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitudee(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getBusRouteName() {
        return busRouteName;
    }

    public void setBusRouteName(String busRouteName) {
        this.busRouteName = busRouteName;
    }

    public String getBusRouteNumber() {
        return busRouteNumber;
    }

    public void setBusRouteNumber(String busRouteNumber) {
        this.busRouteNumber = busRouteNumber;
    }

    @Override
    public boolean isFavorite() {
        return false;
    }

    @Override
    public void setFavorite(boolean favorite) {

    }
}
