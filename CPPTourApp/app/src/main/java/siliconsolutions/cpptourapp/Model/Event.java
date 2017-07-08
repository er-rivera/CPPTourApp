package siliconsolutions.cpptourapp.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 5/24/17.
 */

public class Event {
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
