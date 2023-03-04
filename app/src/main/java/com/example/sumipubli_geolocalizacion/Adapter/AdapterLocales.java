package com.example.sumipubli_geolocalizacion.Adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sumipubli_geolocalizacion.ListaNegocio;
import com.example.sumipubli_geolocalizacion.Model.GetSetLocales;
import com.example.sumipubli_geolocalizacion.R;
import com.example.sumipubli_geolocalizacion.TrazarUbicacion;

import java.util.List;

public class AdapterLocales extends RecyclerView.Adapter<AdapterLocales.ViewHolder>{

    private LayoutInflater layoutInflater;
    private static List<GetSetLocales> data;
    private ListaNegocio context;
    double latitude, longitude;
    String nombreCliente, tipoLocal, nombreEmpresa, direccion, ruc, correo;

    public AdapterLocales(List<GetSetLocales> data, ListaNegocio context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.lista_negocio, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GetSetLocales locales = data.get(position);
        holder.txt_Local.setText(locales.getNombre_local());
        holder.txt_Cliente.setText("Cliente: "+locales.getNombres());

        holder.imgLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombreCliente = locales.getNombres()+" "+locales.getApellidos();
                nombreEmpresa = locales.getNombre_local();
                tipoLocal = locales.getTipo_local();
                ruc = locales.getRUC();
                correo = locales.getCrreo();
                direccion = locales.getDireccion();
                latitude = locales.getLatitud();
                longitude = locales.getLongitud();
                Bundle code = new Bundle();
                code.putString("latitud", String.valueOf(latitude));
                code.putString("longitud", String.valueOf(longitude));
                Intent i = new Intent(context, TrazarUbicacion.class);
                i.putExtras(code);
                context.startActivity(i);


            }
        });
        holder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombreCliente = locales.getNombres()+" "+locales.getApellidos();
                nombreEmpresa = locales.getNombre_local();
                tipoLocal = locales.getTipo_local();
                ruc = locales.getRUC();
                correo = locales.getCrreo();
                direccion = locales.getDireccion();
                latitude = locales.getLatitud();
                longitude = locales.getLongitud();
                View alertCustomDialog = LayoutInflater.from(context).inflate(R.layout.detalle_negocio, null);
                androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(context);
                TextView salir, nombreClienteE, tipoLocalE, nombreEmpresaE, RUCE, CorreoE, direccionE;
                alertDialog.setView(alertCustomDialog);

                salir = (TextView) alertCustomDialog.findViewById(R.id.salir);
                nombreClienteE = (TextView) alertCustomDialog.findViewById(R.id.nombreClienteDet);
                nombreEmpresaE = (TextView) alertCustomDialog.findViewById(R.id.nombreEmpresaDet);
                tipoLocalE = (TextView) alertCustomDialog.findViewById(R.id.tipoLocalDet);
                RUCE = (TextView) alertCustomDialog.findViewById(R.id.RUCDet);
                CorreoE = (TextView) alertCustomDialog.findViewById(R.id.correoDet);
                direccionE = (TextView) alertCustomDialog.findViewById(R.id.direccionDet);

                nombreClienteE.setText(""+nombreCliente);
                nombreEmpresaE.setText(""+nombreEmpresa);
                tipoLocalE.setText(""+tipoLocal);
                RUCE.setText(""+ruc);
                CorreoE.setText(""+correo);
                direccionE.setText(""+direccion);

                final AlertDialog alertDialog1 = alertDialog.create();
                alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                salir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog1.dismiss();
                    }
                });
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
        TextView txt_Local, txt_Cliente;
        public ViewHolder(View itemView) {
            super(itemView);
            txt_Local = itemView.findViewById(R.id.nombreNegocio);
            imgView = itemView.findViewById(R.id.viewNegocio);
            imgLocation = itemView.findViewById(R.id.locationNegocio);
            txt_Cliente = itemView.findViewById(R.id.nombreCliente);
        }
    }


}
