package siliconsolutions.cpptourapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Phuoc on 5/8/2017.
 */

public class Landmarks extends Location{

    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longtitude")
    private String longtitude;
    @SerializedName("name")
    private String landmarksName;
    @SerializedName("number")
    private String landmarksNumber;
    @SerializedName("description")
    private String description;
    @SerializedName("image")
    private String imageUrl;
    @SerializedName("events")
    private List<Event> eventList;
    private boolean isFavorite;

    public Landmarks(String latitude, String longtitude, String landmarksName, String landmarksNumber,List<Event> eventList) {
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.landmarksName = landmarksName;
        this.landmarksNumber = landmarksNumber;
        this.eventList = eventList;
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

    public String getLandmarkName()
    {
        return landmarksName;
    }

    public void setLandmarkName(String landmarksName)
    {
        this.landmarksName = landmarksName;
    }

    public String getLandmarkNumber() {
        return landmarksNumber;
    }

    public void setLandmarkNumber(String landmarksNumber)
    {
        this.landmarksNumber = landmarksNumber;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
