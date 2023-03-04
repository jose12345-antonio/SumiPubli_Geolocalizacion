package com.example.sumipubli_geolocalizacion;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UpdateDatos extends AppCompatActivity {

    private EditText edtFecha, edtNombre, edtApellido, edtCelular, edtCedula, edtCorreo;
    private Button btnActualizar, btnCancelarUpdate;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    String date, user;
    int rol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_datos);

        Variable();
        user =  Preferences.obtenerPreferenceString(getApplicationContext(), Preferences.PREFERENCE_USUARIO_LOGIN);
        obtenerRol("http://devtesis.com/tesis_sumipubli/ObtenerRol.php");
        mostrarUsuario();

        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        edtFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateDatos.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        onDateSetListener,year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()+1000);
                datePickerDialog.show();
            }
        });
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                i1 = i1+1;
                date = i+"-"+i1+"-"+i2;
                edtFecha.setText(date);

            }
        };
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateUsuario("http://devtesis.com/tesis_sumipubli/update_usuario.php");
            }
        });
        btnCancelarUpdate = findViewById(R.id.btnCancelarUpdate);
        btnCancelarUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    private void obtenerRol(String URL) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("validar");
                    for(int i = 0; i<jsonArray.length(); i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        rol = jsonObject1.getInt("rol");
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
                parametros.put("usuario", user);
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


    private void Variable(){
        edtFecha = findViewById(R.id.edtFechaUpdate);
        edtNombre = findViewById(R.id.edtNombreUpdate);
        edtApellido = findViewById(R.id.edtApellidoUpdate);
        edtCelular = findViewById(R.id.edtCelularUpdate);
        edtCedula = findViewById(R.id.edtCedulaUpdate);
        edtCorreo = findViewById(R.id.edtCorreoUpdate);
        btnActualizar = findViewById(R.id.btnRegistrarUpdate);
    }

    private void mostrarUsuario() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://devtesis.com/tesis_sumipubli/DatosUpdate.php?usuario="+user, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String succes = jsonObject.getString("succes");

                    JSONArray jsonArray = jsonObject.getJSONArray("usuario");

                    if (succes.equals("1")){
                        String Fecha = null;
                        for(int i = 0; i<jsonArray.length(); i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            edtNombre.setText(jsonObject1.getString("nombre"));
                            edtApellido.setText(jsonObject1.getString("apellido"));
                            edtCelular.setText(jsonObject1.getString("celular"));
                            Fecha = jsonObject1.getString("fecha_nacimiento");
                            edtCorreo.setText(jsonObject1.getString("correo"));
                            edtCedula.setText(jsonObject1.getString("cedula"));
                        }
                        edtFecha.setText(Fecha);
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

    public void UpdateUsuario(String URL){
        String cedula = edtCedula.getText().toString();
        int cantidad = 0;
        cantidad = cedula.length();
        if(edtFecha.getText().toString().equals("")){
            edtFecha.setError("El campo está vacío");
        }else if(edtNombre.getText().toString().equals("")) {
            edtNombre.setError("El campo no puede estar vacío");
        }else if(edtApellido.getText().toString().equals("")) {
            edtApellido.setError("El campo no puede estar vacío");
        }else if (edtCedula.getText().toString().equals("")){
            edtCedula.setError("El campo está vacío");
        } else if(cantidad <= 9){
            edtCedula.setError("La cédula es de 10 dígitos");
        } else if(ValidationEmail()==false){
            Toast.makeText(getApplicationContext(), "cedula correcta", Toast.LENGTH_SHORT).show();
            edtCorreo.setError("Ingrese un correo válido");
        }else {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getApplicationContext(), "Los datos se actualizaron correctamente", Toast.LENGTH_SHORT).show();
                    if(rol==1){
                        startActivity(new Intent(getApplicationContext(), HomeAdministrador.class));
                    }else if(rol==2){
                        startActivity(new Intent(getApplicationContext(), HomeUsuario.class));
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "No se pudo Actualizar", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    //Toast.makeText(getApplicationContext(), radioButton.getText(), Toast.LENGTH_SHORT).show();
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("cedula", edtCedula.getText().toString());
                    params.put("nombre", edtNombre.getText().toString());
                    params.put("apellido", edtApellido.getText().toString());
                    params.put("correo", edtCorreo.getText().toString());
                    params.put("fecha", edtFecha.getText().toString());
                    params.put("usuario", user);
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

    //Método de validación de correo electrónico
    private boolean ValidationEmail(){
        String emailInput = edtCorreo.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            return false;
        } else {
            return true;
        }
    }

}