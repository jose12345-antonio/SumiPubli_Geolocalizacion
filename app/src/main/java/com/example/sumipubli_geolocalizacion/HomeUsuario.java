package com.example.sumipubli_geolocalizacion;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sumipubli_geolocalizacion.Model.GetSetLocales;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeUsuario extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    MenuItem UpdateDatosAdmin, UpdatePasswordAdmin, CerrarSesionAdmin, listaUsuario, viewDate;
    int idAdmin;
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
    String datosnombres, datosfecha, datoscorreo, datoscelular;
    int datosrol;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    private ArrayList<Marker> tmpRealTimeMarkets = new ArrayList<>();
    private ArrayList<Marker> realTimeMarkets = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_usuario);
        map = findViewById(R.id.map);
        user =  Preferences.obtenerPreferenceString(getApplicationContext(), Preferences.PREFERENCE_USUARIO_LOGIN);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocation();
        mostrarUsuario2("https://devtesis.com/tesis_sumipubli/obtenerInfo.php?correo="+user);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_user, menu);
        UpdateDatosAdmin = menu.findItem(R.id.updateDatosAdmin);
        UpdateDatosAdmin.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent i = new Intent(getApplicationContext(), UpdateDatos.class);
                startActivity(i);
                return false;
            }
        });
        UpdatePasswordAdmin = menu.findItem(R.id.updatePasswordAdmin);
        UpdatePasswordAdmin.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent i = new Intent(getApplicationContext(), UpdatePassword.class);
                startActivity(i);
                return false;
            }
        });
        listaUsuario = menu.findItem(R.id.listaUsuario);
        listaUsuario.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent i = new Intent(getApplicationContext(), ListaNegocio.class);
                startActivity(i);
                return false;
            }
        });
        viewDate = menu.findItem(R.id.viewDate);
        viewDate.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                View alertCustomDialog = LayoutInflater.from(HomeUsuario.this).inflate(R.layout.dialog_view, null);
                androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(HomeUsuario.this);
                TextView salir, nombres, fecha, celular, email, rol;
                Button actualizar;

                alertDialog.setView(alertCustomDialog);
                actualizar = (Button) alertCustomDialog.findViewById(R.id.btnActualizarTraba);
                 nombres= (TextView) alertCustomDialog.findViewById(R.id.nombreTraba);
                fecha = (TextView) alertCustomDialog.findViewById(R.id.fecha_traba);
                celular = (TextView) alertCustomDialog.findViewById(R.id.celularTraba);
                email = (TextView) alertCustomDialog.findViewById(R.id.emailTraba);
                rol = (TextView) alertCustomDialog.findViewById(R.id.rol_asignado);

                nombres.setText(""+datosnombres);
                fecha.setText(""+datosfecha);
                celular.setText(""+datoscelular);
                email.setText(""+datoscorreo);
                if(datosrol == 1){
                    rol.setText("ADMINISTRADOR");
                }else{
                    rol.setText("TRABAJADOR");
                }

                final androidx.appcompat.app.AlertDialog alertDialog1 = alertDialog.create();
                alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                actualizar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), UpdateDatos.class));
                    }
                });
                alertDialog1.show();

                return false;
            }
        });

        CerrarSesionAdmin = menu.findItem(R.id.cerrarSesionAdmin);
        CerrarSesionAdmin.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Preferences.guardarPreferenceBoolean(getApplicationContext(), false, Preferences.PREFERENCE_ESTADO_BUTTON_SESION);
                startActivity(new Intent(getApplicationContext(), Login.class));

                return false;
            }
        });
        return true;
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

        String url2 = "https://devtesis.com/tesis_sumipubli/listar_negocio.php";
        RequestQueue queue2 = Volley.newRequestQueue(getApplicationContext());
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
                        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(@NonNull LatLng latLng) {
                                    AlertDialog.Builder builder= new AlertDialog.Builder(HomeUsuario.this);
                                    builder.setMessage("¿Desea ir al punto seleccionado?");
                                    builder.setTitle(R.string.app_name);
                                    builder.setCancelable(false);

                                    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Bundle code = new Bundle();
                                            code.putString("latitud", String.valueOf(latLng.latitude));
                                            code.putString("longitud", String.valueOf(latLng.longitude));
                                            Intent i = new Intent(HomeUsuario.this, TrazarUbicacion.class);
                                            i.putExtras(code);
                                            startActivity(i);

                                        }
                                    });

                                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });


                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();

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
        UpdateUbicacion("https://devtesis.com/tesis_sumipubli/insertarUbicacion.php");
        //LatLng ubicacion = new LatLng(latOrigen, lonOrigen);
        //MarkerOptions markerOptions = new MarkerOptions();
        //mMap.addMarker(markerOptions.position(ubicacion).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        //this.mMap.moveCamera(CameraUpdateFactory.newLatLng(ubicacion));
    }

    private void getCurrentLocation(){
        if(ActivityCompat.checkSelfPermission(
                this,Manifest.permission.ACCESS_FINE_LOCATION
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
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(HomeUsuario.this);

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
        AlertDialog.Builder builder2= new AlertDialog.Builder(HomeUsuario.this);

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

    private void mostrarUsuario2(String URL6) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL6, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("info");

                        for(int i = 0; i<jsonArray.length(); i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            datosnombres = jsonObject1.getString("nombres")+" "+jsonObject1.getString("apellidos");
                            datoscorreo = jsonObject1.getString("email");
                            datoscelular = jsonObject1.getString("celular");
                            datosfecha = jsonObject1.getString("fecha_nacimiento");
                             datosrol = jsonObject1.getInt("rol_id");
                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();

                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });
    }

    public void UpdateUbicacion(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(), "Los datos se actualizaron correctamente", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), "No se pudo Actualizar", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //Toast.makeText(getApplicationContext(), radioButton.getText(), Toast.LENGTH_SHORT).show();
                Map<String, String> params = new HashMap<String, String>();
                params.put("direccion", "DIRECCIÓN DEL TRABAJADOR");
                params.put("latitud", String.valueOf(latOrigen));
                params.put("longitud", String.valueOf(lonOrigen));

                params.put("correo", user);
                //params.put("sexo", sexo);
                return params;
            }
        };
        queue.add(stringRequest);
        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                queue.getCache().clear();
            }
        });
    }
}