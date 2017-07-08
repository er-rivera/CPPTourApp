package siliconsolutions.cpptourapp.Model;

import com.google.gson.annotations.SerializedName;

public class Office {
    @SerializedName("name")
    private String name;
    @SerializedName("room")
    private String room;
    @SerializedName("phone")
    private String phoneNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
