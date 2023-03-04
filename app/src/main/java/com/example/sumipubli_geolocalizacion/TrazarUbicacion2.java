package com.example.sumipubli_geolocalizacion;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class TrazarUbicacion2 extends AppCompatActivity implements OnMapReadyCallback{

    GoogleMap mMap;
    FrameLayout map;
    //private ActivityMapsBinding binding;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE=101;
    JSONObject jsonObject;
    Double latOrigen, lonOrigen;
    Double latDestino, lonDestino;
    String direccionInicio, direccionDestino, distancia, duracionllegada, direccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trazar_ubicacion2);
        map = findViewById(R.id.mapTrazar);

        latDestino = Double.parseDouble(getIntent().getStringExtra("latitud"));
        lonDestino = Double.parseDouble(getIntent().getStringExtra("longitud"));
        direccion = getIntent().getStringExtra("direccion");



        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocation();


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
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapTrazar);
                    mapFragment.getMapAsync(TrazarUbicacion2.this);

                }
            }
        });

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

        latOrigen = currentLocation.getLatitude();
        lonOrigen = currentLocation.getLongitude();
        //this.mMap.moveCamera(CameraUpdateFactory.newLatLng(guayaquil));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latOrigen, lonOrigen))
                .zoom(16)
                .bearing(30)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        Double latitud = Double.parseDouble(String.valueOf(latDestino));
        Double longitud = Double.parseDouble(String.valueOf(lonDestino));

        LatLng ubicacion = new LatLng(latitud, longitud);
        MarkerOptions markerOptions = new MarkerOptions();
        mMap.addMarker(markerOptions.position(ubicacion).title(direccion));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                View alertCustomDialog = LayoutInflater.from(TrazarUbicacion2.this).inflate(R.layout.dialog_info, null);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(TrazarUbicacion2.this);
                TextView ubicacion, ubicacionDestino, distanciallegada, duracion;
                alertDialog.setView(alertCustomDialog);

                ubicacion = (TextView) alertCustomDialog.findViewById(R.id.ubicaciondialog);
                ubicacionDestino = (TextView) alertCustomDialog.findViewById(R.id.ubicacionDestino);
                distanciallegada = (TextView) alertCustomDialog.findViewById(R.id.distancia);
                duracion = (TextView) alertCustomDialog.findViewById(R.id.tiempoLlegada);

                ubicacion.setText("Dirección origen: "+direccionInicio+"\nLatitud: "+latOrigen+"\nLongitud: "+lonOrigen);
                ubicacionDestino.setText("Dirección destino: "+direccionDestino+"\nLatitud: "+latDestino+"\nLongitud: "+latDestino);
                distanciallegada.setText("La distancia de la ruta es: "+distancia);
                duracion.setText("El tiempo de llegada es: "+duracionllegada);
                final AlertDialog alertDialog1 = alertDialog.create();
                alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button btn = (Button) alertCustomDialog.findViewById(R.id.btnOkay);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog1.dismiss();
                    }
                });
                Button btnRegistr = (Button) alertCustomDialog.findViewById(R.id.btnRegistr);
                btnRegistr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog1.cancel();
                        Bundle code = new Bundle();
                        code.putString("latitud", String.valueOf(latDestino));
                        code.putString("longitud", String.valueOf(lonDestino));
                        code.putString("direccion", direccionDestino);
                        Intent i = new Intent(TrazarUbicacion2.this, RegistrarNegocio.class);
                        i.putExtras(code);
                        startActivity(i);
                        finish();
                    }
                });
                alertDialog1.show();

                return false;
            }
        });

        //String url = "https://maps.googleapis.com/maps/api/directions/json?origin="+latOrigen+","+lonOrigen+",-79.55882732698257&destination=-1.6241026991739054,-79.56200306250952&key=AIzaSyC1Bdt_EFbnG7rmAWxFsepFMhFI-zA6MK0";
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="+latOrigen+","+lonOrigen+"&destination="+latitud+","+longitud+"&key=AIzaSyC1Bdt_EFbnG7rmAWxFsepFMhFI-zA6MK0";
        System.out.println("https://maps.googleapis.com/maps/api/directions/json?origin="+latOrigen+","+lonOrigen+"&destination="+latitud+","+longitud+"&key=AIzaSyC1Bdt_EFbnG7rmAWxFsepFMhFI-zA6MK0");
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jsonObject = new JSONObject(response);
                    //Log.i("jsonRUta: ",""+response);
                    trazarRuta(jsonObject);

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                queue.getCache().clear();
            }
        });

    }

    private void trazarRuta(JSONObject jsonObject) {

        JSONArray jRoutes;
        JSONArray jLegs;
        JSONArray jSteps;
        JSONArray jDistance;

        try {
            jRoutes = jsonObject.getJSONArray("routes");
            for(int i=0; i<jRoutes.length(); i++){
                jLegs = ((JSONObject)(jRoutes.get(i))).getJSONArray("legs");

                direccionInicio = "" + (((JSONObject) jLegs.get(i)).get("start_address"));
                direccionDestino = "" + (((JSONObject) jLegs.get(i)).get("end_address"));

                distancia = "" + ((JSONObject) ((JSONObject) jLegs.get(i)).get("distance")).get("text");
                duracionllegada = "" + ((JSONObject) ((JSONObject) jLegs.get(i)).get("duration")).get("text");

                    for (int j = 0; j < jLegs.length(); j++) {
                        jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                        for (int k = 0; k < jSteps.length(); k++) {
                            String polyline = "" + ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                            Log.i("end", "" + polyline);
                            List<LatLng> list = PolyUtil.decode(polyline);
                            mMap.addPolyline(new PolylineOptions().addAll(list).color(Color.BLUE).width(5));
                        }
                    }


            }
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

}