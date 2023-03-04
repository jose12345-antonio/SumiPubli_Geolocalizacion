package com.example.sumipubli_geolocalizacion.ui.slideshow;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sumipubli_geolocalizacion.Model.GetSetRol;
import com.example.sumipubli_geolocalizacion.R;
import com.example.sumipubli_geolocalizacion.databinding.FragmentSlideshowBinding;
import com.example.sumipubli_geolocalizacion.ui.home.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SlideshowFragment extends Fragment {

    EditText edtFecha, edtCelular, edtCedula, edtNombres, edtApellidos, edtCorreo;
    Button btnUser;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    AutoCompleteTextView rolesSp;
    int id_rol;
    int listar = 0;
    int listarCedula = 0;
    private String cedulaValidate;
    String date;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        VariableEnlace(root);
        Fecha();
        llenarspinnerRol();
        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correoRegister();
            }
        });

        return root;
    }

    private void VariableEnlace(View root) {
        rolesSp = root.findViewById(R.id.rolesUser);

        edtNombres = root.findViewById(R.id.edtNombresUser);
        edtApellidos = root.findViewById(R.id.edtApellidosUser);
        edtCedula = root.findViewById(R.id.edtCedulaUser);
        edtFecha = root.findViewById(R.id.edtFechaUser);
        edtCorreo = root.findViewById(R.id.edtCorreoUser);
        edtCelular = root.findViewById(R.id.edtCelularUser);


        btnUser = root.findViewById(R.id.btnUsers);

    }


    private void Fecha() {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        edtFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        onDateSetListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 1000);
                datePickerDialog.show();
            }
        });
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                i1 = i1 + 1;
                date = i + "-" + i1 + "-" + i2;
                edtFecha.setText(date);
            }
        };
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

    public void RegistrarUsuario(String URL){
        cedulaRegister();
        String cedula = edtCedula.getText().toString();
        int cantidad = 0;
        cantidad = cedula.length();
        if(edtFecha.getText().toString().equals("")){
            edtFecha.setError("El campo no puede estar vacío");
        }else if(edtCelular.getText().toString().equals("")) {
            edtCelular.setError("El campo no puede estar vacío");
        }else if (edtCedula.getText().toString().equals("")){
            edtCedula.setError("El campo no puede estar vacío");
        } else if(cantidad <= 9){
            edtCedula.setError("La cédula es de 10 dígitos");
        } else if (isEcuadorianDocumentValid() == false) {
            edtCedula.setError("Cédula ingresada es incorrecta");
        }else if(listarCedula>=1){
            edtCedula.setError("La cédula se encuentra registrada");
        }else if(edtNombres.getText().toString().equals("")) {
            Toast.makeText(getContext(), "cedula correcta", Toast.LENGTH_SHORT).show();
            edtNombres.setError("El campo no puede estar vacío");
        }else if(edtApellidos.getText().toString().equals("")) {
            edtApellidos.setError("El campo no puede estar vacío");
        } else if(ValidationEmail()==false){
            edtCorreo.setError("Ingrese un correo válido");
        }else if(listar>=1){
            edtCorreo.setError("El correo ya se encuentra registrado");
            edtCorreo.setText("");
        }else{

            RequestQueue queue = Volley.newRequestQueue(getContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getContext(), "El usuario fue registrado exitosamente", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(getApplicationContext(), RegistroUsuario.class));
                    //sendMail();
                    edtCedula.setText("");
                    edtNombres.setText("");
                    edtApellidos.setText("");
                    edtCorreo.setText("");
                    edtFecha.setText("");
                    edtCelular.setText("");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "No se pudo registrar", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    //Toast.makeText(getApplicationContext(), radioButton.getText(), Toast.LENGTH_SHORT).show();
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("cedula", edtCedula.getText().toString());
                    params.put("nombres", edtNombres.getText().toString());
                    params.put("apellidos", edtApellidos.getText().toString());
                    params.put("correo", edtCorreo.getText().toString());
                    params.put("celular", edtCelular.getText().toString());
                    params.put("fecha", edtFecha.getText().toString());
                    params.put("rol", String.valueOf(id_rol));
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

    public void correoRegister()
    {
        String URLCORREO = "https://devtesis.com/tesis_sumipubli/validar_correo.php?correo="+edtCorreo.getText().toString();
        StringRequest request2 = new StringRequest(Request.Method.POST, URLCORREO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("correo");
                    listar = jsonArray.length();
                    RegistrarUsuario("https://devtesis.com/tesis_sumipubli/insertar_usuario.php");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
        requestQueue2.add(request2);
        requestQueue2.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue2.getCache().clear();
            }
        });

    }
    public void cedulaRegister() {
        String url_Cedula = "https://devtesis.com/tesis_sumipubli/validar_cedula.php?cedula="+edtCedula.getText().toString();
        StringRequest request3 = new StringRequest(Request.Method.GET, url_Cedula, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("cedula");
                    listarCedula = jsonArray.length();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue3 = Volley.newRequestQueue(getContext());
        requestQueue3.add(request3);
        requestQueue3.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue3.getCache().clear();
            }
        });
    }
    private boolean isEcuadorianDocumentValid() {

        cedulaValidate = edtCedula.getText().toString();
        int suma = 0;
        int a[] = new int[cedulaValidate.length() / 2];
        int b[] = new int[(cedulaValidate.length() / 2)];
        int c = 0;
        int d = 1;
        for (int i = 0; i < cedulaValidate.length() / 2; i++) {
            a[i] = Integer.parseInt(String.valueOf(cedulaValidate.charAt(c)));
            c = c + 2;
            if (i < (cedulaValidate.length() / 2) - 1) {
                b[i] = Integer.parseInt(String.valueOf(cedulaValidate.charAt(d)));
                d = d + 2;
            }
        }

        for (int i = 0; i < a.length; i++) {
            a[i] = a[i] * 2;
            if (a[i] > 9) {
                a[i] = a[i] - 9;
            }
            suma = suma + a[i] + b[i];
        }
        int aux = suma / 10;
        int dec = (aux + 1) * 10;
        if ((dec - suma) == Integer.parseInt(String.valueOf(cedulaValidate.charAt(cedulaValidate.length() - 1))))
            return true;
        else if (suma % 10 == 0 && cedulaValidate.charAt(cedulaValidate.length() - 1) == '0') {
            return true;
        } else {
            return false;
        }
    }

    public void llenarspinnerRol() {
        String URL = "https://devtesis.com/tesis_sumipubli/obtener_rol.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    final ArrayList<GetSetRol> listRol = new ArrayList<GetSetRol>();

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("roles");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        GetSetRol p = new GetSetRol();
                        p.setId_rol(jsonObject1.getInt("id_rol"));
                        p.setNombre_rol(jsonObject1.getString("nombre_rol"));
                        listRol.add(p);

                    }
                    ArrayAdapter<GetSetRol> tipoRol = new ArrayAdapter<GetSetRol>(getContext(), android.R.layout.simple_dropdown_item_1line, listRol);
                    rolesSp.setAdapter(tipoRol);
                    rolesSp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            id_rol = listRol.get(i).getId_rol();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });
    }

}