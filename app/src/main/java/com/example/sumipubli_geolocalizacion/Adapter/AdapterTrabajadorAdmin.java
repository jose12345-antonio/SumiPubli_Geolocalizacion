package com.example.sumipubli_geolocalizacion.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sumipubli_geolocalizacion.Model.GetSetLocales;
import com.example.sumipubli_geolocalizacion.Model.GetSetTrabajador;
import com.example.sumipubli_geolocalizacion.R;
import com.example.sumipubli_geolocalizacion.TrazarUbicacion;
import com.example.sumipubli_geolocalizacion.TrazarUbicacion2;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterTrabajadorAdmin extends RecyclerView.Adapter<AdapterTrabajadorAdmin.ViewHolder>{

    private LayoutInflater layoutInflater;
    private static List<GetSetTrabajador> data;
    private Context context;
    double latitude, longitude;
    double latitud, longitud;
    String nombre, apellido, fecha_nacimiento, cedula, celular, email, estado, created_at, direccion;
    FirebaseDatabase firebaseDatabase;
    Query databaseReference;
    int idfire, idbd;

    public AdapterTrabajadorAdmin(List<GetSetTrabajador> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.lista_trabajador, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GetSetTrabajador trabajador = data.get(position);
        holder.txt_Trabajador.setText(trabajador.getNombres()+" "+trabajador.getApellidos());
        holder.txt_Cedula.setText("Cédula: "+trabajador.getCedula());

        nombre = trabajador.getNombres()+" "+trabajador.getApellidos();
        fecha_nacimiento = trabajador.getFecha_nacimiento();
        cedula = trabajador.getCedula();
        celular = trabajador.getCelular();
        email = trabajador.getEmail();
        estado = trabajador.getEstado();
        created_at = trabajador.getCreated_at();


        holder.imgLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idbd = trabajador.getId_usuario();

                mostrarUbi("http://devtesis.com/tesis_sumipubli/list_ubicacion.php?id="+idbd);

                Bundle code = new Bundle();
                code.putString("latitud", String.valueOf(latitud));
                code.putString("longitud", String.valueOf(longitud));
                code.putString("direccion", direccion);
                Intent i = new Intent(context, TrazarUbicacion2.class);
                i.putExtras(code);
                context.startActivity(i);

            }
        });
        holder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idbd = trabajador.getId_usuario();
                nombre = trabajador.getNombres()+" "+trabajador.getApellidos();
                fecha_nacimiento = trabajador.getFecha_nacimiento();
                cedula = trabajador.getCedula();
                celular = trabajador.getCelular();
                email = trabajador.getEmail();
                estado = trabajador.getEstado();
                created_at = trabajador.getCreated_at();
                View alertCustomDialog = LayoutInflater.from(context).inflate(R.layout.detalle_trabajador, null);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                TextView salir, nombreTra, cedulaTra, celularTra, correoTra;
                alertDialog.setView(alertCustomDialog);

                nombreTra = (TextView) alertCustomDialog.findViewById(R.id.nombreTra);
                cedulaTra = (TextView) alertCustomDialog.findViewById(R.id.cedulaTra);
                celularTra = (TextView) alertCustomDialog.findViewById(R.id.celularTra);
                correoTra = (TextView) alertCustomDialog.findViewById(R.id.correoTra);


               nombreTra.setText(""+nombre);
               cedulaTra.setText("N° Identidad: "+cedula);
               celularTra.setText(""+celular);
               correoTra.setText(""+email);
               // CorreoE.setText(""+correo);
               // direccionE.setText(""+direccion);

                final AlertDialog alertDialog1 = alertDialog.create();
                alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                alertDialog1.show();
            }

        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView imgView, imgLocation;
        TextView txt_Trabajador, txt_Cedula;
        public ViewHolder(View itemView) {
            super(itemView);
            txt_Trabajador = itemView.findViewById(R.id.nombreTrabajador);
            imgView = itemView.findViewById(R.id.viewNegocio2);
            imgLocation = itemView.findViewById(R.id.locationNegocio2);
            txt_Cedula = itemView.findViewById(R.id.cedula);
        }
    }

    private void mostrarUbi(String URL6) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL6, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("ubicacion");

                    for(int i = 0; i<jsonArray.length(); i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        latitud = Double.parseDouble(jsonObject1.getString("latitud"));
                        longitud = Double.parseDouble(jsonObject1.getString("longitud"));
                        direccion = jsonObject1.getString("direccion");
                    }
                    Toast.makeText(context, "LATITUD"+latitud+"LONGITUD"+longitud, Toast.LENGTH_SHORT).show();
                    System.out.println("LATITUD"+latitud+"LONGITUD"+longitud);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();

                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });
    }


}
