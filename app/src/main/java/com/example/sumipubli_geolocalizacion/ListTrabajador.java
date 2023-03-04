package com.example.sumipubli_geolocalizacion;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sumipubli_geolocalizacion.Adapter.AdapterLocalesAdmin;
import com.example.sumipubli_geolocalizacion.Adapter.AdapterTrabajadorAdmin;
import com.example.sumipubli_geolocalizacion.Model.GetSetLocales;
import com.example.sumipubli_geolocalizacion.Model.GetSetTrabajador;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListTrabajador extends Fragment {


    List<GetSetTrabajador> playerlist1;
    RecyclerView recyclerView;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    String URL="https://devtesis.com/tesis_sumipubli/listar_trabajador.php";
    Button btnLocation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_trabajador, container, false);
        btnLocation = view.findViewById(R.id.btnLocation);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MapaTrabajador.class));
            }
        });
        recyclerView = view.findViewById(R.id.recyclerNegocioUser3);
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
                            JSONArray jsonArray1 = jsonObject.getJSONArray("trabajador");
                            playerlist1.clear();
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                                int id = jsonObject1.getInt("id_usuario");
                                String nombres = jsonObject1.getString("nombres");
                                String apellidos = jsonObject1.getString("apellidos");
                                String cedula = jsonObject1.getString("cedula");
                                String fecha_nacimiento = jsonObject1.getString("fecha_nacimiento");
                                String celular = jsonObject1.getString("celular");
                                String email = jsonObject1.getString("email");
                                String estado = jsonObject1.getString("estado");
                                String created_at = jsonObject1.getString("created_at");

                                playerlist1.add(new GetSetTrabajador(id, nombres, apellidos, cedula, fecha_nacimiento, celular, email, estado, created_at));
                            }
                            AdapterTrabajadorAdmin adaptador = new AdapterTrabajadorAdmin(playerlist1, getContext());
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