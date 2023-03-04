package com.example.sumipubli_geolocalizacion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sumipubli_geolocalizacion.Adapter.AdapterLocales;
import com.example.sumipubli_geolocalizacion.Model.GetSetLocales;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaNegocio extends AppCompatActivity {

    List<GetSetLocales> playerlist1;
    RecyclerView recyclerView;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    String URL="https://devtesis.com/tesis_sumipubli/listar_negocio.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_negocio);

        recyclerView = findViewById(R.id.recyclerNegocioUser);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        playerlist1= new ArrayList<>();
        request = Volley.newRequestQueue(getApplicationContext());

        cargarservice1();
    }

    private void cargarservice1() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            System.out.println(response);
                            JSONArray jsonArray1 = jsonObject.getJSONArray("negocio");
                            playerlist1.clear();
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                                int id = jsonObject1.getInt("id_local");
                                String tipo_local = jsonObject1.getString("nombre_tipoLocal");
                                String nombre_local = jsonObject1.getString("nombre_local");
                                String direccion = jsonObject1.getString("direccion");
                                Double latitud = jsonObject1.getDouble("latitud");
                                Double longitud = jsonObject1.getDouble("longitud");
                                String nombres = jsonObject1.getString("nombres");
                                String apellidos = jsonObject1.getString("apellidos");
                                String fecha_nacimiento = jsonObject1.getString("fecha_nacimiento");
                                String RUC = jsonObject1.getString("RUC");
                                String correo = jsonObject1.getString("correo");

                                playerlist1.add(new GetSetLocales(id, tipo_local, nombre_local, direccion, nombres, apellidos, RUC, fecha_nacimiento, correo, latitud, longitud));
                            }
                            AdapterLocales adaptador = new AdapterLocales(playerlist1, ListaNegocio.this);
                            recyclerView.setAdapter(adaptador);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> parametros = new HashMap<String, String>();

                return parametros;
            }
        };

        requestQueue.add(stringRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });

    }

}