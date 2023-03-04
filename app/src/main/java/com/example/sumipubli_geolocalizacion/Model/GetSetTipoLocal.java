package com.example.sumipubli_geolocalizacion.Model;

public class GetSetTipoLocal {

    int id_tipo;
    String descripcion;

    public GetSetTipoLocal(int id_tipo, String descripcion) {
        this.id_tipo = id_tipo;
        this.descripcion = descripcion;
    }

    public GetSetTipoLocal() {
    }

    public int getId_tipo() {
        return id_tipo;
    }

    public void setId_tipo(int id_tipo) {
        this.id_tipo = id_tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
