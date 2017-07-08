package siliconsolutions.cpptourapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Phuoc on 5/27/2017.
 */

public class BusB {
    @SerializedName("ID")
    private String iD;
    @SerializedName("APCPercentage")
    private String aPCPercentage;
    @SerializedName("RouteId")
    private String routeID;
    @SerializedName("PatternId")
    private String patternID;
    @SerializedName("Name")
    private String busName;
    @SerializedName("HasAPC")
    private String hasAPC;
    @SerializedName("IconPrefix")
    private String iconPrefix;
    @SerializedName("DoorStatus")
    private String doorStatus;
    @SerializedName("Latitude")
    private String latitude;
    @SerializedName("Longitude")
    private String longitude;
    @SerializedName("Coordinate")
    private List<Office> coor;
    @SerializedName("Speed")
    private String speed;
    @SerializedName("Heading")
    private String heading;
    @SerializedName("Updated")
    private String updated;
    @SerializedName("UpdatedAgo")
    private String upAgo;
    private boolean isFavorite;

    public BusB(String aPCPercentage, String routeID, String patternID, String busName, String iD, String imageUrl) {
        this.aPCPercentage = aPCPercentage;
        this.routeID = routeID;
        this.patternID = patternID;
        this.busName = busName;
        this.iD = iD;
        isFavorite = false;
    }


    public String getUpAgo() {
        return upAgo;
    }

    public void setUpAgo(String upAgo) {
        this.upAgo = upAgo;
    }

    public String getiD() {
        return iD;
    }

    public void setiD(String iD) {
        this.iD = iD;
    }

    public String getaPCPercentage() {
        return aPCPercentage;
    }

    public void setaPCPercentage(String aPCPercentage) {
        this.aPCPercentage = aPCPercentage;
    }

    public String getRouteID() {
        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

    public void setPatternID(String patternID) {
        this.patternID = patternID;
    }

    public String getPatternID() {
        return patternID;
    }

    public void setBusNumber(String busName) {
        this.busName = busName;
    }

    public String getBusName() {
        return busName;
    }

    public void setHasAPC(String hasAPC) {
        this.hasAPC = hasAPC;
    }

    public String getHasAPC() {
        return hasAPC;
    }

    public void setIconPrefix(String iconPrefix) {
        this.iconPrefix = iconPrefix;
    }

    public String getIconPrefix() {
        return iconPrefix;
    }

    public void setDoorStatus(String doorStatus) {
        this.doorStatus = doorStatus;
    }

    public String getDoorStatus() {
        return doorStatus;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setCoor(List<Office> coor) {
        this.coor = coor;
    }

    public List<Office> getCoor() {
        return coor;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getSpeed() {
        return speed;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getHeading() {
        return heading;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getUpdated() {
        return updated;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isFavorite() {
        return isFavorite;
    }
}
