package com.example.sumipubli_geolocalizacion.ui.gallery;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.sumipubli_geolocalizacion.Model.GetSetTipoLocal;
import com.example.sumipubli_geolocalizacion.R;
import com.example.sumipubli_geolocalizacion.databinding.FragmentGalleryBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class GalleryFragment extends Fragment {

    EditText edtFecha, edtCelular, edtRUC, edtNombres, edtApellidos, edtCorreo, edtNombreLocal, edtDireccion;
    Double latitud=-2.1563730002774357, longitud=-79.91511423972497;
    Button btnRegisterUser;
    AutoCompleteTextView tipolocal;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    int id_tipoLocal;
    String date;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        Variable(root);
        Fecha();
        llenarspinnerTipoLocal();

        return root;
    }

    private void Variable(View root){
        edtFecha = root.findViewById(R.id.edtFecha);
        edtCelular = root.findViewById(R.id.edtCelularRegister);
        edtRUC = root.findViewById(R.id.edtRUC);
        edtNombres = root.findViewById(R.id.edtNombres);
        edtApellidos = root.findViewById(R.id.edtApellidos);
        edtCorreo = root.findViewById(R.id.edtCorreo);

        tipolocal = root.findViewById(R.id.tipoLocal);
        edtNombreLocal = root.findViewById(R.id.edtNombreLocal);
        edtDireccion = root.findViewById(R.id.edtDireccion);



        btnRegisterUser = root.findViewById(R.id.btnUsersRegister);
        btnRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrarNegocio("https://devtesis.com/tesis_sumipubli/insertar_negocio.php");
            }
        });
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
    public void llenarspinnerTipoLocal() {
        String URL = "https://devtesis.com/tesis_sumipubli/obtener_tipoLocal.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    final ArrayList<GetSetTipoLocal> listTipo = new ArrayList<GetSetTipoLocal>();
                    GetSetTipoLocal listTip = new GetSetTipoLocal();

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("tipo_local");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        GetSetTipoLocal p = new GetSetTipoLocal();
                        p.setId_tipo(jsonObject1.getInt("id_tipo"));
                        p.setDescripcion(jsonObject1.getString("descripcion"));
                        listTipo.add(p);

                    }
                    ArrayAdapter<GetSetTipoLocal> tipoSp = new ArrayAdapter<GetSetTipoLocal>(getContext(), android.R.layout.simple_dropdown_item_1line, listTipo);
                    tipolocal.setAdapter(tipoSp);
                    tipolocal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            id_tipoLocal = listTipo.get(i).getId_tipo();
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
    /*
    public void correoRegister() {
        String URLCORREO = "http://appcartorama.es/validarCorreo.php";
        StringRequest request2 = new StringRequest(Request.Method.POST, URLCORREO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("validar");
                    listar = jsonArray.length();
                    RegistrarUsuario("http://appcartorama.es/insertar_usuario.php");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("correo",edtCorreo.getText().toString().trim());
                return params;
            }

        };
        RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
        requestQueue2.add(request2);
        requestQueue2.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue2.getCache().clear();
            }
        });

    }
    */

    public void RegistrarNegocio(String URL){
        //cedulaRegister();
        //String cedula = edtCedula.getText().toString();
        //int cantidad = 0;
        //cantidad = cedula.length();
        if(edtFecha.getText().toString().equals("")){
            edtFecha.setError("El campo no puede estar vacío");
        }else if(edtCelular.getText().toString().equals("")) {
            edtCelular.setError("El campo no puede estar vacío");
        }else if (edtRUC.getText().toString().equals("")){
            edtRUC.setError("El campo no puede estar vacío");
        }else if(edtNombres.getText().toString().equals("")) {
            edtNombres.setError("El campo no puede estar vacío");
        }else if(edtApellidos.getText().toString().equals("")) {
            edtApellidos.setError("El campo no puede estar vacío");
        }/* else if(cantidad <= 9){
            edtCedula.setError("La cédula es de 10 dígitos");
        } else if (isEcuadorianDocumentValid() == false) {
            edtCedula.setError("Cédula ingresada es incorrecta");
        }else if(listarCedula>=1){
            edtCedula.setError("La cédula se encuentra registrada");
        } else if(ValidationEmail()==false){
            Toast.makeText(getApplicationContext(), "cedula correcta", Toast.LENGTH_SHORT).show();
            edtCorreo.setError("Ingrese un correo válido");
        }else if(listar>=1){
            edtCorreo.setError("El correo ya se encuentra registrado");
            edtCorreo.setText("");
        }*/else if(edtCorreo.getText().toString().equals("")){
            edtCorreo.setError("El campo no puede estar vacío");
        }else if(edtNombreLocal.getText().toString().equals("")){
            edtNombreLocal.setError("El campo no puede estar vacío");
        }else if(edtDireccion.getText().toString().equals("")){
            edtDireccion.setError("El campo no puede estar vacío");
        }else {
            RequestQueue queue = Volley.newRequestQueue(getContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getContext(), "El usuario fue registrado exitosamente", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(getContext(), RegistroUsuario.class));
                    //sendMail();
                    edtFecha.setText("");
                    edtRUC.setText("");
                    edtCelular.setText("");
                    edtNombres.setText("");
                    edtApellidos.setText("");
                    edtCorreo.setText("");
                    edtNombreLocal.setText("");
                    edtDireccion.setText("");
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
                    params.put("fecha", edtFecha.getText().toString());
                    params.put("celular", edtCelular.getText().toString());
                    params.put("RUC", edtRUC.getText().toString());
                    params.put("nombres", edtNombres.getText().toString());
                    params.put("apellidos", edtApellidos.getText().toString());
                    params.put("correo", edtCorreo.getText().toString());
                    params.put("tipo_local", String.valueOf(id_tipoLocal));
                    params.put("nombre_local", edtNombreLocal.getText().toString());
                    params.put("direccion", edtDireccion.getText().toString());
                    params.put("latitud", String.valueOf(latitud));
                    params.put("longitud", String.valueOf(longitud));
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