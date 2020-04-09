package edu.dam.empresaapp.pojos;

import android.os.Parcel;
import android.os.Parcelable;

public class Empresa implements Parcelable {

    private String direccion;
    private String email;
    private String municipio;
    private String nif;
    private String nombre;
    private String pais;
    private String provincia;
    private String telefono;

    public Empresa() {
    }

    public Empresa(String direccion, String email, String municipio, String nif, String nombre, String pais, String provincia, String telefono) {
        this.direccion = direccion;
        this.email = email;
        this.municipio = municipio;
        this.nif = nif;
        this.nombre = nombre;
        this.pais = pais;
        this.provincia = provincia;
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
