package com.example.sumipubli_geolocalizacion.Model;

public class GetSetLocales {

    int idLocal;
    String tipo_local, nombre_local, direccion, nombres, apellidos, RUC, fecha_nacimiento, crreo;
    Double latitud, longitud;

    public GetSetLocales(int idLocal, String tipo_local, String nombre_local, String direccion, String nombres, String apellidos, String RUC, String fecha_nacimiento, String crreo, Double latitud, Double longitud) {
        this.idLocal = idLocal;
        this.tipo_local = tipo_local;
        this.nombre_local = nombre_local;
        this.direccion = direccion;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.RUC = RUC;
        this.fecha_nacimiento = fecha_nacimiento;
        this.crreo = crreo;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public GetSetLocales() {
    }

    public int getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(int idLocal) {
        this.idLocal = idLocal;
    }

    public String getTipo_local() {
        return tipo_local;
    }

    public void setTipo_local(String tipo_local) {
        this.tipo_local = tipo_local;
    }

    public String getNombre_local() {
        return nombre_local;
    }

    public void setNombre_local(String nombre_local) {
        this.nombre_local = nombre_local;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getRUC() {
        return RUC;
    }

    public void setRUC(String RUC) {
        this.RUC = RUC;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getCrreo() {
        return crreo;
    }

    public void setCrreo(String crreo) {
        this.crreo = crreo;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
}
