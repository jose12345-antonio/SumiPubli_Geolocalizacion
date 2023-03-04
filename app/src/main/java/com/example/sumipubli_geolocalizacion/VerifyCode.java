package com.example.sumipubli_geolocalizacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class VerifyCode extends AppCompatActivity {

    EditText edtCode;
    TextView reenviarCodigo;
    Button btnVerificar;
    String code, correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);
        getSupportActionBar().hide();
        code = getIntent().getStringExtra("code");
        correo = getIntent().getStringExtra("correo");
        Variable();
    }

    public void Variable(){
        edtCode = findViewById(R.id.edtCode);
        reenviarCodigo = findViewById(R.id.reenvioCode);
        btnVerificar = findViewById(R.id.btnCode);
        btnVerificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtCode.getText().toString().equals(code)){
                    edtCode.setError("Código incorrecto. Verifique su correo");
                }else{
                    Toast.makeText(getApplicationContext(), "Código correcto. Actualice su contraseña", Toast.LENGTH_SHORT).show();
                    Bundle email = new Bundle();
                    email.putString("correo", correo);
                    Intent i = new Intent(VerifyCode.this, UpdatePasswordCode.class);
                    i.putExtras(email);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

}