package com.example.sumipubli_geolocalizacion;

import androidx.appcompat.app.AppCompatActivity;

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

public class UpdatePassword extends AppCompatActivity {

    private Button btnUpdatePassword;
    private EditText edtPassword, edtPasswordRepetir;
    String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        user =  Preferences.obtenerPreferenceString(getApplicationContext(), Preferences.PREFERENCE_USUARIO_LOGIN);
        Variable();

        //mostrarUsuario("https://proyectosdominios.com/tesina-ultrasonido/DatosUpdate.php");
        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActualizarDatosPasword("http://devtesis.com/tesis_sumipubli/update_password.php");
            }
        });

    }
    private void Variable(){
        edtPassword = findViewById(R.id.edtPassword);
        edtPasswordRepetir = findViewById(R.id.edtPasswordRepetir);
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);

    }

    private boolean validarPassword() {
        String passwordP = edtPassword.getText().toString();
        String password2 = edtPasswordRepetir.getText().toString();
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
        } else if (edtPasswordRepetir.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "el campo confirmar contraseña está vacía está vacío", Toast.LENGTH_SHORT).show();
        } else{
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //if (rol==1){
                    Toast.makeText(getApplicationContext(), "La contraseña se actualizó con éxito", Toast.LENGTH_SHORT).show();
                    // startActivity(new Intent(getApplicationContext(), HomeAdmin.class));
                    finish();

                    // }else if(rol==2){
                    // Toast.makeText(getApplicationContext(), "La contraseña se actualizó con éxito", Toast.LENGTH_SHORT).show();
                    //   startActivity(new Intent(getApplicationContext(), HomeUser.class));
                    finish();
                    // }

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
                    params.put("usuario", user);

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