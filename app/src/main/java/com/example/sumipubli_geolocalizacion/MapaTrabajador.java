package com.example.sumipubli_geolocalizacion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.FrameLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapaTrabajador extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    String user;
    GoogleMap mMap;
    FrameLayout map;
    //private ActivityMapsBinding binding;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE=101;
    Boolean actualPosicion = true;
    JSONObject jsonObject;
    Double latOrigen, lonOrigen, latDestino, lonDestino;

    private ArrayList<Marker> tmpRealTimeMarkets = new ArrayList<>();
    private ArrayList<Marker> realTimeMarkets = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_trabajador);
        map = findViewById(R.id.mapTraba);
        user =  Preferences.obtenerPreferenceString(getApplicationContext(), Preferences.PREFERENCE_USUARIO_LOGIN);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocation();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        //mMap.getUiSettings().setMyLocationButtonEnabled(false);
        Antut(googleMap);
    }

    public void Antut(GoogleMap googleMap){
        mMap = googleMap;

        String url2 = "https://devtesis.com/tesis_sumipubli/list_map.php";
        RequestQueue queue2 = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    System.out.println(response);
                    JSONArray jsonArray1 = jsonObject.getJSONArray("ubicacion");
                    for(Marker marker:realTimeMarkets){
                        marker.remove();
                    }

                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                        int id = jsonObject1.getInt("id_trabajador");
                        Double latitud = jsonObject1.getDouble("latitud");
                        Double longitud = jsonObject1.getDouble("longitud");
                        String direccion = jsonObject1.getString("direccion");

                        LatLng ubicacion = new LatLng(latitud,longitud);
                        MarkerOptions markerOptions = new MarkerOptions();
                        mMap.addMarker(markerOptions.position(ubicacion).title(direccion));
                        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                            @Override
                            public void onMapClick(@NonNull LatLng latLng) {

                            }
                        });

                    }


                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue2.add(stringRequest2);
        queue2.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                queue2.getCache().clear();
            }
        });


        latOrigen = currentLocation.getLatitude();
        lonOrigen = currentLocation.getLongitude();

        //databaseReference.child("delay").setValue(tiempo);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latOrigen, lonOrigen))
                .zoom(18)
                .bearing(30)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 500, null);
        //LatLng ubicacion = new LatLng(latOrigen, lonOrigen);
        //MarkerOptions markerOptions = new MarkerOptions();
        //mMap.addMarker(markerOptions.position(ubicacion).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        //this.mMap.moveCamera(CameraUpdateFactory.newLatLng(ubicacion));
    }

    private void getCurrentLocation(){
        if(ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location!=null){
                    currentLocation = location;
                    //Toast.makeText(getApplicationContext(), (int) currentLocation.getLatitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapTraba);
                    mapFragment.getMapAsync(MapaTrabajador.this);

                }
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (REQUEST_CODE){
            case REQUEST_CODE:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

                }
        }
    }

    public void countDownTimer(){
        new CountDownTimer(5000,1000){
            public void onTick(long millisUntilFinished){
                Log.i("Second: ", ""+millisUntilFinished/1000);
                onMapReady(mMap);
            }
            public void onFinish(){

            }

        }.start();
    }
    public void AlertMarket(String title, final LatLng latLng){
        AlertDialog.Builder builder2= new AlertDialog.Builder(MapaTrabajador.this);

        builder2.setMessage("Desea ir este punto?");
        builder2.setTitle(title);
        builder2.setCancelable(false);

        builder2.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Toast.makeText(getApplicationContext(), ""+latLng, Toast.LENGTH_SHORT).show();

            }
        });

        builder2.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        AlertMarket(marker.getTitle(),marker.getPosition());
        return false;
    }

}