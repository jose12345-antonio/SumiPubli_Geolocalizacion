package com.example.sumipubli_geolocalizacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class UpdatePasswordCode extends AppCompatActivity {

    EditText edtPassword, edtConfirmPassword;
    Button btnUpdatePassword;
    String correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password_code);
        correo = getIntent().getStringExtra("correo");
        edtPassword = findViewById(R.id.newPasswordCode);
        edtConfirmPassword = findViewById(R.id.confirmPasswordCode);
        btnUpdatePassword = findViewById(R.id.btnRecuperarCode);
        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String URL = "https://devtesis.com/tesis_sumipubli/update_password.php";
                ActualizarDatosPasword(URL);
            }
        });
    }

    private boolean validarPassword() {
        String passwordP = edtPassword.getText().toString();
        String password2 = edtConfirmPassword.getText().toString();
        if (passwordP.equals(password2)) {
            return true;
        } else {
            return false;
        }
    }


    public void ActualizarDatosPasword(String URL){
        if (edtPassword.getText().toString().equals("") || validarPassword() == false) {
            edtPassword.setError("Las contraseñas no coinciden");
        } else if (edtPassword.getText().toString().length() < 8) {
            edtPassword.setError("La contraseña requiere mínimo de 8 caracteres");
        } else if (edtConfirmPassword.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "el campo confirmar contraseña está vacía está vacío", Toast.LENGTH_SHORT).show();
        } else{
            final RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getApplicationContext(), "La contraseña se actualizó con éxito", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), ConfirmUpdatePassword.class));
                    finish();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("password", edtPassword.getText().toString());
                    params.put("usuario", correo);

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

}