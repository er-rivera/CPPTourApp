package siliconsolutions.cpptourapp.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Response;
import siliconsolutions.cpptourapp.Directions.APIService;
import siliconsolutions.cpptourapp.Directions.BusProvider;
import siliconsolutions.cpptourapp.Directions.DirectionEvent;
import siliconsolutions.cpptourapp.Directions.GeoCodeResponse;
import siliconsolutions.cpptourapp.GPS.GPSTracker;
import siliconsolutions.cpptourapp.Model.Navigation;
import siliconsolutions.cpptourapp.Model.TourMarkers;
import siliconsolutions.cpptourapp.R;
import siliconsolutions.cpptourapp.Tour.TourNavigation;

public class TourActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ArrayList<TourMarkers> tourPointsArrayList;
    private static GoogleMap mMap;
    private ArrayList<Marker> markers;
    private Bus mBus;
    private View bottomSheet;
    private View dividerView;
    private ImageButton beginNavButton;
    private LinearLayout startContainer;
    private ListView stepListView;
    private BottomSheetBehavior mBottomSheetBehavior;
    private TextView headerTextTv;
    private TextView timeTv;
    private Button recenterButton;
    private LinearLayout listLayout;
    private LinearLayout detailLayout;
    private APIService apiService;
    private List<String> instructionList;
    private GPSTracker gpsTracker;
    private int callbackCount = 0;
    private List<Response<GeoCodeResponse>> tourResponses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String tourName = getIntent().getStringExtra("name");
        getSupportActionBar().setTitle("Tour of " + tourName);
        initViews();
        tourResponses = new ArrayList<>();
        SupportMapFragment smp = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.tour_map);
        smp.getMapAsync(this);
        tourPointsArrayList = getIntent().getParcelableArrayListExtra("markers");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        markers = new ArrayList<>();
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.i("MAP", "CLICK");
            }
        });
        generateTourMarkers();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markers.get(0).getPosition(), 16));
        gpsTracker = BaseMap.gpsTracker;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.tour_info_window,null);
                TextView title = (TextView) v.findViewById(R.id.info_title);
                TextView snippet = (TextView) v.findViewById(R.id.info_snippet);
                title.setText(marker.getTitle());
                snippet.setText(marker.getSnippet());
                return v;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                listLayout.setVisibility(View.GONE);
                detailLayout.setVisibility(View.VISIBLE);
            }
        });

        mMap.setOnInfoWindowCloseListener(new GoogleMap.OnInfoWindowCloseListener() {
            @Override
            public void onInfoWindowClose(Marker marker) {
                //detailLayout.scrollTo(0,0);
                detailLayout.setVisibility(View.GONE);
                listLayout.setVisibility(View.VISIBLE);
                if(mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED){
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
        callAPI();
    }

    private void callAPI(){
        String mode = "walking";
        String user = gpsTracker.getLatitude() + "," + gpsTracker.getLongitude();
        String firstLocation = markers.get(0).getPosition().latitude + "," + markers.get(0).getPosition().longitude;
        apiService.onLoadDirections(user,firstLocation,mode);
        for(int i = 0; i < markers.size() - 1; i++ ){
            String origin = markers.get(i).getPosition().latitude + "," + markers.get(i).getPosition().longitude;
            String dest = markers.get(i + 1).getPosition().latitude + "," + markers.get(i + 1).getPosition().longitude;
            Log.i("WALK TO",markers.get(i).getTitle());
            Log.i("WALK FROM",markers.get(i +1).getTitle());
            apiService.onLoadDirections(origin,dest,mode);
            android.os.SystemClock.sleep(1000);
        }
        setButtonClicks();
    }


    @Subscribe
    public void onDirectionEvent(DirectionEvent event){
        final Navigation navigation = Navigation.getInstance();
        tourResponses.add(event.response);
        navigation.setResponse(event.response);
        navigation.setMap(mMap);
        navigation.setPolyLine(event.response.body().routes.get(0).getOverviewPolyline().getPoints());
        //navigation.generatePolyLine();
        callbackCount++;
        Log.i("RESPONSE STATUS", event.response.body().status);
        //if(callbackCount >= markers.size()){
            setUpTour();

        //navigation.setUp();
        //markers = navigation.getMarkers();
        startContainer.setVisibility(View.VISIBLE);
        dividerView.setVisibility(View.VISIBLE);
        //instructionList = navigation.getSteps();
        //setDirectionsList(navigation.getSteps(),markers);
        //setButtonClicks(navigation);
    }

    private void setUpTour() {
        TourNavigation tourNav = TourNavigation.getInstance(tourResponses,mMap);
        timeTv.setText(tourNav.getTotalTime());
        tourNav.setMarkersList(markers);
        instructionList = tourNav.getInstructionList();
        tourNav.generatePolyLines();
        updateDetailListView();

    }


    private boolean locationPermissonCheck(){
        Log.d("STATUS", "checkPermission()");

        // Ask for permission if it wasn't granted yet
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED);
    }

    private void initViews() {
        bottomSheet = findViewById(R.id.tour_display_bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        //mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        timeTv = (TextView) findViewById(R.id.nav_display_time);
        headerTextTv = (TextView) findViewById(R.id.nav_display_distance);
        dividerView = findViewById(R.id.nav_dividerView);
        startContainer = (LinearLayout) findViewById(R.id.nav_start_container);
        stepListView = (ListView) findViewById(R.id.nav_step_list);
        beginNavButton = (ImageButton) findViewById(R.id.nav_beginButton);
        recenterButton = (Button) findViewById(R.id.nav_recenter);
        listLayout = (LinearLayout) findViewById(R.id.route_tour_view);
        detailLayout = (LinearLayout) findViewById(R.id.marker_detail_tour);
    }



    private void generateTourMarkers() {
        for (TourMarkers marker: tourPointsArrayList){
            double latitude = Double.parseDouble(marker.getLatitude());
            double longitude = Double.parseDouble(marker.getLongitude());
            markers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).visible(true)
                    .title(marker.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))));
        }
    }

    private Bus getBus() {
        if (mBus == null) {
            mBus = BusProvider.getInstance();
        }
        return mBus;
    }

    private void updateDetailListView(){
        ArrayAdapter<String> adapter =  new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1,
                instructionList);
        stepListView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBus().register(this);
        apiService = new APIService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getBus().unregister(this);
    }

    @Override
    public void onBackPressed() {
        if(mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED){
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        else{
            super.onBackPressed();
        }

    }

    private void setButtonClicks(){
        beginNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}
