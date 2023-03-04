package com.example.sumipubli_geolocalizacion.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sumipubli_geolocalizacion.HomeAdministrador;
import com.example.sumipubli_geolocalizacion.HomeUsuario;
import com.example.sumipubli_geolocalizacion.Preferences;
import com.example.sumipubli_geolocalizacion.R;
import com.example.sumipubli_geolocalizacion.TrazarUbicacion;
import com.example.sumipubli_geolocalizacion.databinding.FragmentHomeBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    String user;
    GoogleMap mMap;
    HomeAdministrador home;
    FrameLayout map;
    //private ActivityMapsBinding binding;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE=101;
    Boolean actualPosicion = true;
    JSONObject jsonObject;
    Double latOrigen, lonOrigen, latDestino, lonDestino;
    String datosnombres, datosfecha, datoscorreo, datoscelular;
    int datosrol;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private ArrayList<Marker> tmpRealTimeMarkets = new ArrayList<>();
    private ArrayList<Marker> realTimeMarkets = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        map = root.findViewById(R.id.mapadmin);
        user =  Preferences.obtenerPreferenceString(getContext(), Preferences.PREFERENCE_USUARIO_LOGIN);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());


        return root;
    }

    public void onMapReady(@com.example.sumipubli_geolocalizacion.NonNull GoogleMap googleMap) {
        this.mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        //mMap.getUiSettings().setMyLocationButtonEnabled(false);
        Antut(googleMap);
    }

    public void Antut(GoogleMap googleMap){
        mMap = googleMap;

        String url2 = "https://devtesis.com/tesis_sumipubli/listar_negocio.php";
        RequestQueue queue2 = Volley.newRequestQueue(getContext());
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    System.out.println(response);
                    JSONArray jsonArray1 = jsonObject.getJSONArray("negocio");
                    for(Marker marker:realTimeMarkets){
                        marker.remove();
                    }

                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                        int id = jsonObject1.getInt("id_local");
                        Double latitud = jsonObject1.getDouble("latitud");
                        Double longitud = jsonObject1.getDouble("longitud");
                        String direccion = jsonObject1.getString("direccion");

                        LatLng ubicacion = new LatLng(latitud,longitud);
                        MarkerOptions markerOptions = new MarkerOptions();
                        mMap.addMarker(markerOptions.position(ubicacion).title(direccion));

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

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @com.example.sumipubli_geolocalizacion.NonNull String[] permissions, @com.example.sumipubli_geolocalizacion.NonNull int[] grantResults) {
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
        AlertDialog.Builder builder2= new AlertDialog.Builder(getContext());

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

    public boolean onMarkerClick(@com.example.sumipubli_geolocalizacion.NonNull Marker marker) {
        AlertMarket(marker.getTitle(),marker.getPosition());
        return false;
    }

}