package com.example.sumipubli_geolocalizacion;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sumipubli_geolocalizacion.Adapter.AdapterLocales;
import com.example.sumipubli_geolocalizacion.Adapter.AdapterLocalesAdmin;
import com.example.sumipubli_geolocalizacion.Model.GetSetLocales;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListarNegocio2 extends Fragment {

    List<GetSetLocales> playerlist1;
    RecyclerView recyclerView;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    String URL="https://devtesis.com/tesis_sumipubli/listar_negocio.php";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_listar_negocio2, container, false);

        recyclerView = view.findViewById(R.id.recyclerNegocioUser2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        playerlist1= new ArrayList<>();
        request = Volley.newRequestQueue(getContext());

        cargarservice1();




        return view;
    }

    private void cargarservice1() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

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
                            AdapterLocalesAdmin adaptador = new AdapterLocalesAdmin(playerlist1, getContext());
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