package siliconsolutions.cpptourapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TourMarkers implements Parcelable{
    @SerializedName("description")
    private String description;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longtitude")
    private String longitude;
    @SerializedName("name")
    private String name;
    @SerializedName("number")
    private String number;
    /*@SerializedName("events")
    private List<Event> events;*/
    @SerializedName("image")
    private String imageUrl;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
/*
    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }*/

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static final Parcelable.Creator<TourMarkers> CREATOR
            = new Parcelable.Creator<TourMarkers>() {
        public TourMarkers createFromParcel(Parcel in) {
            return new TourMarkers(in);
        }

        public TourMarkers[] newArray(int size) {
            return new TourMarkers[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(description);
        parcel.writeString(latitude);
        parcel.writeString(longitude);
        parcel.writeString(name);
        parcel.writeString(number);
        //parcel.writeA
        parcel.writeString(imageUrl);
    }

    private TourMarkers(Parcel in){
        description = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        name = in.readString();
        number = in.readString();
        imageUrl = in.readString();
    }
}
