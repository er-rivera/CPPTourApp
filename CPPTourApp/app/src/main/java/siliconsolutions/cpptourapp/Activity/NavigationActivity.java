package siliconsolutions.cpptourapp.Activity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import siliconsolutions.cpptourapp.Adapters.NavigationListAdapter;
import siliconsolutions.cpptourapp.Directions.APIService;
import siliconsolutions.cpptourapp.Directions.BusProvider;
import siliconsolutions.cpptourapp.Directions.DirectionEvent;
import siliconsolutions.cpptourapp.Directions.GeofenceTransitionsIntentService;
import siliconsolutions.cpptourapp.Directions.Step;
import siliconsolutions.cpptourapp.GPS.GPSTracker;
import siliconsolutions.cpptourapp.GPS.GPSTrackerListener;
import siliconsolutions.cpptourapp.Model.Navigation;
import siliconsolutions.cpptourapp.R;

public class NavigationActivity extends AppCompatActivity implements OnMapReadyCallback,View.OnClickListener,
        GPSTrackerListener, GoogleMap.OnMarkerClickListener, GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<Status> {

    private static final int REQ_PERMISSION = 1;
    private static GoogleMap mMap;
    private GPSTracker gpsTracker;
    private Marker destinationMarker;
    private TextView headerTextTv;
    private TextView timeTv;
    private View bottomSheet;
    private View dividerView;
    private ImageButton beginNavButton;
    private LinearLayout startContainer;
    private ListView stepListView;
    private BottomSheetBehavior mBottomSheetBehavior;
    private APIService apiSerivce;
    private static List<Marker> markers;
    private Button recenterButton;
    private Bundle bundle;
    private Bus mBus;
    private CameraUpdate cameraUpdate;
    private List<Step> instructionList;
    private Geofence mGeofence;
    private final MyHandler mHandler = new MyHandler(this);
    private GoogleApiClient googleApiClient;
    public static final int MESSAGE_NOT_CONNECTED = 1;
    private Location lastLocation;
    private LocationRequest locationRequest;
    // This number in extremely low, and should be used only for debug
    private final int UPDATE_INTERVAL =  1000;
    private final int FASTEST_INTERVAL = 900;
    //private final int UPDATE_INTERVAL =  3 * 60 * 1000; // 3 minutes
    //private final int FASTEST_INTERVAL = 30 * 1000;  // 30 secs
    private static Marker geoFenceMarker;
    private static final long GEO_DURATION = 60 * 60 * 100;
    private static final String GEOFENCE_REQ_ID = "My Geofence";
    private static final float GEOFENCE_RADIUS = 2f * 3.28084f;
    private boolean isRunning = false;
    private PendingIntent geoFencePendingIntent;
    private Circle geoFenceLimits;
    private final int GEOFENCE_REQ_CODE = 0;// in meters
    private final Runnable sRunnable = new Runnable() {
        @Override
        public void run() {
            try{
                isRunning = true;
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                LatLngBounds bounds;
                builder.include(new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude()));
                builder.include(geoFenceMarker.getPosition());
                bounds = builder.build();
                // define value for padding
                int padding =20;
                //This cameraupdate will zoom the map to a level where both location visible on map and also set the padding on four side.
                cameraUpdate =  CameraUpdateFactory.newLatLngBounds(bounds,padding);
                changeCameraAngle(geoFenceMarker);
                mHandler.postDelayed(sRunnable,6000);
            }catch (Exception e){
                e.printStackTrace();
                isRunning = false;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getIntent().getParcelableExtra("bundle");
        String header = bundle.getString("header");
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Navigation to " + header);
        initViews();
        SupportMapFragment smp = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.nav_map);
        smp.getMapAsync(this);
        createGoogleApi();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initViews() {
        bottomSheet = findViewById(R.id.nav_display_bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        timeTv = (TextView) findViewById(R.id.nav_display_time);
        headerTextTv = (TextView) findViewById(R.id.nav_display_distance);
        dividerView = findViewById(R.id.nav_dividerView);
        startContainer = (LinearLayout) findViewById(R.id.nav_start_container);
        stepListView = (ListView) findViewById(R.id.nav_step_list);
        beginNavButton = (ImageButton) findViewById(R.id.nav_beginButton);
        recenterButton = (Button) findViewById(R.id.nav_recenter);
    }

    private void generateDestinationMarker() {
        LatLng toPosition = bundle.getParcelable("position");
        String title = bundle.getString("title");
        String snippet = bundle.getString("snippet");
        destinationMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).visible(true).title(title).snippet(snippet).position(toPosition));
        destinationMarker.setTag("destination");

    }


    private void createGoogleApi() {
        if ( googleApiClient == null ) {
            googleApiClient = new GoogleApiClient.Builder( this )
                    .addConnectionCallbacks( this )
                    .addOnConnectionFailedListener( this )
                    .addApi( LocationServices.API )
                    .build();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        gpsTracker = BaseMap.gpsTracker;
        initGPS();
        generateDestinationMarker();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destinationMarker.getPosition(), 16));
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude()));
        builder.include(destinationMarker.getPosition());
        final LatLngBounds bounds = builder.build();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                /*mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        if(marker.getTag() != null && (marker.getTag().toString()).equals("destination")){
                            Log.i("SELECTED",destinationMarker.getTitle());
                            //makeInvisible
                            //changeLayout
                        }
                        else{
                            marker.showInfoWindow();
                        }
                        return true;

                    }
                });*/
                Log.i("MAP","CLICKED");
                if(isRunning == true){
                    mHandler.removeCallbacks(sRunnable);
                }
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(isRunning == true){
                    mHandler.removeCallbacks(sRunnable);
                }
                return false;
            }
        });
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,30));
            }
        });
        String origin = gpsTracker.getLatitude() + "," + gpsTracker.getLongitude();
        String dest = destinationMarker.getPosition().latitude+ "," + destinationMarker.getPosition().longitude;
        String mode = "walking";
        apiSerivce.onLoadDirections(origin,dest,mode);
    }

    private void initGPS() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        if (gpsTracker.canGetLocation()) {
            double lat = gpsTracker.getLatitude();
            double lng = gpsTracker.getLongitude();
            LatLng userLatLng = new LatLng(lat, lng);
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 16));
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onGPSTrackerLocationChanged(Location location) {

    }

    @Override
    public void onGPSTrackerStatusChanged(String provider, int status, Bundle extra) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
        //mGeofenceList = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBus().register(this);
        apiSerivce = new APIService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getBus().unregister(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    private Bus getBus() {
        if (mBus == null) {
            mBus = BusProvider.getInstance();
        }
        return mBus;
    }


    @Subscribe
    public void onDirectionEvent(DirectionEvent event){
        final Navigation navigation = Navigation.getInstance();
        navigation.setResponse(event.response);
        navigation.setMap(mMap);
        navigation.setUp();
        markers = navigation.getMarkers();
        timeTv.setText(navigation.getDuration());
        headerTextTv.setText(navigation.getDistance());
        startContainer.setVisibility(View.VISIBLE);
        dividerView.setVisibility(View.VISIBLE);
        instructionList = navigation.getSteps();
        setDirectionsList(navigation.getSteps(),markers);
        setButtonClicks(navigation);
    }

    public void setDirectionsList(List<Step> directionsList, final List<Marker> markers) {
        NavigationListAdapter navigationListAdapter = new NavigationListAdapter(this,R.id.nav_step_list,directionsList, markers);
        stepListView.setAdapter(navigationListAdapter);
        stepListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goToMarker(markers.get(i));
            }
        });
    }

    public void setButtonClicks(final Navigation n){
        beginNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(markers.size() == 0){
                    Toast.makeText(getApplicationContext(),"No Markers on map to navigate between",Toast.LENGTH_SHORT);
                }
                else{
                    startContainer.setVisibility(View.GONE);
                    dividerView.setVisibility(View.GONE);
                    recenterButton.setVisibility(View.VISIBLE);
                    n.start();
                    headerTextTv.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                    updateInformationDisplay();
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    LatLngBounds bounds;
                    builder.include(new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude()));
                    builder.include(markers.get(0).getPosition());
                    bounds = builder.build();
                    // define value for padding
                    int padding =20;
                    //This cameraupdate will zoom the map to a level where both location visible on map and also set the padding on four side.
                    cameraUpdate =  CameraUpdateFactory.newLatLngBounds(bounds,padding);
                    mMap.moveCamera(cameraUpdate);
                    changeCameraAngle(markers.get(0));
                    geoFenceMarker = markers.get(0);
                    Log.i("MARKER 1 LOCATION", markers.get(0).getPosition().latitude + "," + markers.get(0).getPosition().longitude);
                    Log.i("MARKER 1 NAME", markers.get(0).getTitle());
                    Log.i("GEOFENCE LOCATION", geoFenceMarker.getPosition().latitude + "," + geoFenceMarker.getPosition().longitude);
                    Log.i("GEOFENCE NAME", geoFenceMarker.getTitle());
                    //markerForGeofence(markers.get(0).getPosition());
                    mHandler.postDelayed(sRunnable,100);
                    startGeofence();
                    LocalBroadcastManager lbc = LocalBroadcastManager.getInstance(getApplicationContext());
                    GoogleReceiver receiver = new GoogleReceiver(NavigationActivity.this);
                    lbc.registerReceiver(receiver, new IntentFilter("googlegeofence"));
                }
            }
        });

        recenterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // changeCameraAngle(markers.get(0));
                /*for(Marker m : markers){
                    geoFenceMarker = m;
                    Geofence geofence = createGeofence( geoFenceMarker.getPosition(), GEOFENCE_RADIUS );
                    mGeofenceList.add(geofence);
                    GeofencingRequest geofenceRequest = createGeofenceRequest( geofence );
                    addGeofence( geofenceRequest );
                }*/
                mHandler.postDelayed(sRunnable,100);
            }
        });
    }

    public void changeCameraAngle(Marker marker) {
        Location startLocation = new Location("startLocation");
        startLocation.setLatitude(gpsTracker.getLatitude());
        startLocation.setLongitude(gpsTracker.getLongitude());
        Location endLocation = new Location("endLocation");
        endLocation.setLatitude(marker.getPosition().latitude);
        endLocation.setLongitude(marker.getPosition().longitude);
        float targetBearing = startLocation.bearingTo(endLocation);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude()))
                .zoom(mMap.getCameraPosition().zoom)
                .bearing(targetBearing).tilt(90)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                cameraPosition));
    }

    private void goToMarker(Marker m){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(m.getPosition(),18));
        m.showInfoWindow();
    }

    private void updateInformationDisplay(){
        headerTextTv.setText(instructionList.get(0).getHtmlInstructions());

        //subTextUPDATE.setText(instructionList.get(0).getDistance());
        timeTv.setText(instructionList.get(0).getDistance().getText());
        instructionList.remove(0);
        NavigationListAdapter adapter = (NavigationListAdapter) stepListView.getAdapter();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(sRunnable);
        super.onDestroy();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("STATUS", "onConnected()");
        getLastKnownLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("STATUS", "onConnectionSuspended()");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("STATUS", "onConnectionFailed()");
    }
private int i= 0;
    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        checkPermission();
        mMap.setMyLocationEnabled(true);
        if (gpsTracker.canGetLocation()) {
            double lat = gpsTracker.getLatitude();
            double lng = gpsTracker.getLongitude();
            LatLng userLatLng = new LatLng(lat, lng);
            i++;
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 16));
        }
        writeActualLocation(location);
    }

    /*TODO:ADD TO NEW FILE*/
    private void getLastKnownLocation() {
        Log.d("STATUS", "getLastKnownLocation()");
        if ( checkPermission() ) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if ( lastLocation != null ) {
                Log.i("STATUS", "LasKnown location. " +
                        "Long: " + lastLocation.getLongitude() +
                        " | Lat: " + lastLocation.getLatitude());
                writeLastLocation();
                startLocationUpdates();
            } else {
                Log.w("STATUS", "No location retrieved yet");
                startLocationUpdates();
            }
        }
        else askPermission();
    }

    // Write location coordinates on UI
    private void writeActualLocation(Location location) {
        Log.i("LOCATION", "Lat: " + location.getLatitude() );
        Log.i("LOCATION", "Long: " + location.getLongitude() );
        //gpsTracker =
    }

    private void writeLastLocation() {
        writeActualLocation(lastLocation);
    }

    private void startLocationUpdates(){
        Log.i("STATUS", "startLocationUpdates()");
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        if ( checkPermission() )
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    private boolean checkPermission() {
        Log.d("STATUS", "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }

    private void askPermission() {
        Log.d("STATUS", "askPermission()");
        ActivityCompat.requestPermissions(
                this,
                new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                REQ_PERMISSION
        );
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("STATUS", "onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch ( requestCode ) {
            case REQ_PERMISSION: {
                if ( grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    // Permission granted
                    getLastKnownLocation();

                } else {
                    // Permission denied
                    permissionsDenied();
                }
                break;
            }
        }
    }

    private static void markerForGeofence(LatLng latLng) {
        Log.i("STATUS", "markerForGeofence("+latLng+")");
        String title = latLng.latitude + ", " + latLng.longitude;
        // Define marker options
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .title(title);
        if ( mMap!=null ) {
            // Remove last geoFenceMarker
            if (geoFenceMarker != null)
                geoFenceMarker.remove();

            geoFenceMarker = markers.get(0);
        }
    }

    private Geofence createGeofence(LatLng latLng, float radius ) {
        Log.d("STATUS", "createGeofence");
        mGeofence =  new Geofence.Builder()
                .setRequestId(GEOFENCE_REQ_ID)
                .setCircularRegion( latLng.latitude, latLng.longitude, radius)
                .setExpirationDuration( GEO_DURATION )
                .setTransitionTypes( Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_EXIT )
                .build();
        return mGeofence;
    }

    // Create a Geofence Request
    private GeofencingRequest createGeofenceRequest(Geofence geofence ) {
        Log.d("STATUS", "createGeofenceRequest");
        return new GeofencingRequest.Builder()
                .setInitialTrigger( GeofencingRequest.INITIAL_TRIGGER_ENTER )
                .addGeofence( geofence )
                .build();
    }

    private PendingIntent createGeofencePendingIntent() {
        Log.d("STATUS", "createGeofencePendingIntent");
        if ( geoFencePendingIntent != null )
            return geoFencePendingIntent;

        Intent intent = new Intent( this, GeofenceTransitionsIntentService.class);
        return PendingIntent.getService(
                this, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT );
    }

    private void addGeofence(GeofencingRequest request) {
        Log.d("STATUS", "addGeofence");
        if (checkPermission())
            LocationServices.GeofencingApi.addGeofences(
                    googleApiClient,
                    request,
                    createGeofencePendingIntent()
            ).setResultCallback(this);
    }

    private void removeGeofence(){
        Log.d("STATUS", "removeGeofence");
        if(checkPermission()){
            geoFenceLimits.setVisible(false);
            List<String> geofencesToRemove = new ArrayList<>();
            geofencesToRemove.add(mGeofence.getRequestId());
            LocationServices.GeofencingApi.removeGeofences(googleApiClient, geofencesToRemove);

        }
    }

    private void permissionsDenied() {
        Log.w("STATUS", "permissionsDenied()");
    }

    @Override
    public void onResult(@NonNull Status status) {
        Log.i("STATUS", "onResult: " + status);
        if ( status.isSuccess() ) {

            drawGeofence();
        } else {
            // inform about fail
        }
    }

    // Draw Geofence circle on GoogleMap
    private void drawGeofence() {
        Log.d("STATUS", "drawGeofence()");

        //if ( geoFenceLimits != null )
            //geoFenceLimits.remove();

        CircleOptions circleOptions = new CircleOptions()
                .center( geoFenceMarker.getPosition())
                .strokeColor(Color.argb(50, 70,70,70))
                .fillColor( Color.argb(100, 150,150,150) )
                .radius( GEOFENCE_RADIUS );
        geoFenceLimits = mMap.addCircle( circleOptions );


    }

    private void drawGeofence(Marker m) {
        Log.d("STATUS", "drawGeofence()");

        //if ( geoFenceLimits != null )
        //geoFenceLimits.remove();

        CircleOptions circleOptions = new CircleOptions()
                .center( m.getPosition())
                .strokeColor(Color.argb(50, 70,70,70))
                .fillColor( Color.argb(100, 150,150,150) )
                .radius( GEOFENCE_RADIUS );
        geoFenceLimits = mMap.addCircle( circleOptions );
    }

    private void startGeofence() {
        Log.i("STATUS", "startGeofence()");
        if( geoFenceMarker != null ) {
            Geofence geofence = createGeofence( geoFenceMarker.getPosition(), GEOFENCE_RADIUS );
            GeofencingRequest geofenceRequest = createGeofenceRequest( geofence );
            addGeofence( geofenceRequest );
        } else {
            Log.e("STATUS", "Geofence marker is null");
        }
    }

    public static Intent makeNotificationIntent(Context geofenceService, String msg)
    {
        Log.d("THIS",msg);
        /*if((markers.get(0).getTitle()).equals(s))
        markers.get(0).setVisible(false);
        markers.remove(0);
        updateNavigation();*/
        /*drawGeofence();
        markerForGeofence(m.getPosition());
        startGeofence();*/
        return new Intent(geofenceService,NavigationActivity.class);
    }


    private int check = 0;
    private static void updateNavigation() {
        //markerForGeofence(markers.get(0).getPosition());

        //startGeofence();
    }

    private static class MyHandler extends android.os.Handler {
        private final WeakReference<NavigationActivity> mActivity;

        public MyHandler(NavigationActivity activity) {
            mActivity = new WeakReference<NavigationActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            NavigationActivity activity = mActivity.get();
            if (activity != null) {
                Log.e("ERROR:","HANDLER ACTIVITY FAILURE");
            }

            switch (msg.what) {
                case MESSAGE_NOT_CONNECTED:
                    //activity.changeCameraAngle();
                    Log.i("IN HANDLER","CHANGING CAMERA");
                    break;
            }
        }
    }

    class GoogleReceiver extends BroadcastReceiver{

        NavigationActivity mActivity;
        int index = 0;

        public GoogleReceiver(NavigationActivity activity){
            mActivity = (NavigationActivity) activity;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("STRING","HERE");
            int retrieveTest = intent.getIntExtra("transition",-1);
            Log.i("ENTER",String.valueOf(retrieveTest == Geofence.GEOFENCE_TRANSITION_ENTER));
            if(index < instructionList.size()){
                mHandler.removeCallbacks(sRunnable);
                geoFenceMarker.setVisible(false);
                markers.get(index).setVisible(false);
                updateInformationDisplay();
                removeGeofence();
                index++;
                markerForGeofence(markers.get(index).getPosition());
                changeCameraAngle(markers.get(index));
                mHandler.postDelayed(sRunnable,1000);
                startGeofence();
            }
            if(index >= instructionList.size()){
                mHandler.removeCallbacks(sRunnable);
                Toast.makeText(getApplicationContext(), "YOU HAVE ARRIVED", Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(destinationMarker.getPosition()));
                finish();
            }
        }
    }
}