package com.example.sumipubli_geolocalizacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sumipubli_geolocalizacion.Received.ReceivedResetPassword;

import java.util.ArrayList;

public class ResetPassword extends AppCompatActivity {

    Button btnRecuperarPassword;
    EditText edtCorreo;
    int codigo;
    String receivedCodigo, correo;
    TextView loginR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        getSupportActionBar().hide();
        Variable();
    }

    private void Variable(){
        edtCorreo = findViewById(R.id.edtCorreoReset);
        loginR = findViewById(R.id.loginR);
        loginR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
        btnRecuperarPassword = findViewById(R.id.btnRecuperarPassword);
        btnRecuperarPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //startActivity(new Intent(getApplicationContext(), VerifyCode.class));
                //sendMail();
                ArrayList numeros = new ArrayList();
                for(int i=1; i<=6; i++){
                    codigo = (int) (Math.random() * 9+1);
                    if(numeros.contains(codigo)){
                        i--;
                    }else{
                        numeros.add(codigo);
                    }
                }
                //correo="josecarrasco1998@outlook.com";
                if(edtCorreo.getText().toString().isEmpty()){
                    edtCorreo.setError("El campo está vacío");
                }/*else if(!edtCorreo.getText().toString().equals(correo)){

                }*/else{
                    receivedCodigo = String.valueOf(numeros.get(0)+""+numeros.get(1)+""+numeros.get(2)+""+numeros.get(3)+""+numeros.get(4)+""+numeros.get(5));
                    sendMail(receivedCodigo);
                    Bundle code = new Bundle();
                    code.putString("code", receivedCodigo);
                    code.putString("correo", edtCorreo.getText().toString());
                    Intent i = new Intent(ResetPassword.this, VerifyCode.class);
                    i.putExtras(code);
                    startActivity(i);
                }
            }
        });
    }

    private void sendMail(String codigo2) {
        String subject = "SUMI-PUBLI S.A.";
        String frase = "CONTRASEÑA OLVIDADA \n.";
        String mensaje = frase+"\n Su código es: "+codigo2;
        ReceivedResetPassword javaMailAPI = new ReceivedResetPassword(ResetPassword.this, edtCorreo.getText().toString(), subject, mensaje);
        javaMailAPI.execute();
    }

}