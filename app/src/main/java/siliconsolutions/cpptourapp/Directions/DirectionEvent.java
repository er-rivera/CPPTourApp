package siliconsolutions.cpptourapp.Directions;

import android.util.Log;

import retrofit2.Response;



public class DirectionEvent {
    public Response<GeoCodeResponse> response;

    public DirectionEvent(Response<GeoCodeResponse> response){
        this.response = response;
        Log.i("is","com");
    }
}
