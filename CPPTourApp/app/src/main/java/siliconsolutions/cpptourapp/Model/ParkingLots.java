package siliconsolutions.cpptourapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Phuoc on 5/8/2017.
 */

public class ParkingLots extends Location{

    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longtitude")
    private String longtitude;
    @SerializedName("name")
    private String parkingLotsName;
    @SerializedName("number")
    private String parkingLotsNumber;
    private boolean isFavorite;
    @SerializedName("description")
    private String description;
    @SerializedName("meters")
    private List<Meter> meters;
    @SerializedName("image")
    private String imageUrl;

    public ParkingLots(String latitude, String longtitude, String parkingLotsName, String parkingLotsNumber, List<Meter> meterList) {
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.parkingLotsName = parkingLotsName;
        this.parkingLotsNumber = parkingLotsNumber;
        meters = meterList;
        isFavorite = false;
    }

    public String getLatitude()
    {
        return latitude;
    }

    public void setLatitudee(String latitude)
    {
        this.latitude = latitude;
    }

    public String getLongtitude()
    {
        return longtitude;
    }

    public void setLongtitude(String longtitude)
    {
        this.longtitude = longtitude;
    }

    public String getParkingLotsName()
    {
        return parkingLotsName;
    }

    public void setParkingLotsName(String parkingLotsName)
    {
        this.parkingLotsName = parkingLotsName;
    }

    public String getParkingLotsNumber() {
        return parkingLotsNumber;
    }

    public void setParkingLotsNumber(String parkingLotsNumber)
    {
        this.parkingLotsNumber = parkingLotsNumber;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public List<Meter> getMeters() {
        return meters;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
