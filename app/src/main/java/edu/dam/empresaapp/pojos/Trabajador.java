package edu.dam.empresaapp.pojos;

import android.os.Parcel;
import android.os.Parcelable;

public class Trabajador implements Parcelable {

    private String email;
    private String nif;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String telefono;
    private String id;
    private Boolean esResponsable;

    public Trabajador() {
    }

    /*// este constructor solo se usará para el registro inicial del usuario
    public Trabajador(String email, String nif, String nombre, String apellido1, String apellido2, String telefono, Boolean esResponsable) {
        this.email = email;
        this.nif = nif;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.telefono = telefono;
        this.esResponsable = esResponsable;
    }*/

    public Trabajador(String id, String email, String nif, String nombre, String apellido1, String apellido2, String telefono, Boolean esResponsable) {
        this.id = id;
        this.email = email;
        this.nif = nif;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.telefono = telefono;
        this.esResponsable = esResponsable;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getEsResponsable() {
        return esResponsable;
    }

    public void setEsResponsable(Boolean esResponsable) {
        this.esResponsable = esResponsable;
    }

    protected Trabajador(Parcel in) {
        email = in.readString();
        nif = in.readString();
        nombre = in.readString();
        apellido1 = in.readString();
        apellido2 = in.readString();
        telefono = in.readString();
        id = in.readString();
        byte tmpEsResponsable = in.readByte();
        esResponsable = tmpEsResponsable == 0 ? null : tmpEsResponsable == 1;
    }

    public static final Creator<Trabajador> CREATOR = new Creator<Trabajador>() {
        @Override
        public Trabajador createFromParcel(Parcel in) {
            return new Trabajador(in);
        }

        @Override
        public Trabajador[] newArray(int size) {
            return new Trabajador[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(nif);
        dest.writeString(nombre);
        dest.writeString(apellido1);
        dest.writeString(apellido2);
        dest.writeString(telefono);
        dest.writeString(id);
        dest.writeByte((byte) (esResponsable == null ? 0 : esResponsable ? 1 : 2));
    }


    @Override
    public String toString() {
        return "Trabajador{" +
                "email='" + email + '\'' +
                ", nif='" + nif + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido1='" + apellido1 + '\'' +
                ", apellido2='" + apellido2 + '\'' +
                ", telefono='" + telefono + '\'' +
                ", id='" + id + '\'' +
                ", esResponsable=" + esResponsable +
                '}';
    }
}


