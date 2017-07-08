package siliconsolutions.cpptourapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Phuoc on 5/26/2017.
 */

public class Restaurants extends Location{

    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longtitude")
    private String longtitude;
    @SerializedName("name")
    private String restaurantName;
    @SerializedName("number")
    private String restaurantNumber;
    private boolean isFavorite;
    @SerializedName("description")
    private String description;
    @SerializedName("offices")
    private List<Office> offices;
    @SerializedName("image")
    private String imageUrl;

    public Restaurants(String latitude, String longtitude, String restaurantName, String restaurantNumber, List<Office> officeList) {
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.restaurantName = restaurantName;
        this.restaurantNumber = restaurantNumber;
        offices = officeList;
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

    public String getRestaurantName()
    {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName)
    {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantNumber() {
        return restaurantNumber;
    }

    public void setRestaurantNumber(String restaurantNumber)
    {
        this.restaurantNumber = restaurantNumber;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public List<Office> getOffices() {
        return offices;
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
