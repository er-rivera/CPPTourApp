package siliconsolutions.cpptourapp.Directions;

import android.util.Log;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIService {
    private DirectionsService mService;
    private Bus mBus;

    public APIService(){
        mService = buildApi();
        mBus = BusProvider.getInstance();
    }


    public void onLoadDirections(String origin ,String dest ,String mode){
        mService.getJson(origin, dest, mode).enqueue(new Callback<GeoCodeResponse>() {
            @Override
            public void onResponse(Call<GeoCodeResponse> call, Response<GeoCodeResponse> response) {
                mBus.post(produceDirectionEvent(response));
            }

            @Override
            public void onFailure(Call<GeoCodeResponse> call, Throwable t) {
                Log.i("FAILURE","did not receive call");
            }
        });
    }

    private DirectionsService buildApi(){
        return DirectionsService.retrofit.create(DirectionsService.class);
    }

    @Produce
    public DirectionEvent produceDirectionEvent(Response<GeoCodeResponse> response){
        return new DirectionEvent(response);
    }
}