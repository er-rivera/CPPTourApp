package siliconsolutions.cpptourapp.Directions;

import com.squareup.otto.Subscribe;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by user on 5/3/17.
 */

public interface DirectionsService {

    @GET("maps/api/directions/json")
    public Call<GeoCodeResponse> getJson(@Query("origin") String origin, @Query("destination") String destination,@Query("mode") String mode);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/").addConverterFactory(GsonConverterFactory.create())
            .build();
}
