package edu.dam.empresaapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Fichajes implements Parcelable {

    private String idTrabajador;
    private String fecha;
    private String horaEntrada;
    private String horaSalida;
    private String textoEntrada;
    private String textoSalida;


    public Fichajes() {
    }

    public Fichajes(String idTrabajador, String fecha, String horaEntrada, String horaSalida, String textoEntrada, String textoSalida) {
        this.idTrabajador = idTrabajador;
        this.fecha = fecha;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.textoEntrada = textoEntrada;
        this.textoSalida = textoSalida;
    }

    protected Fichajes(Parcel in) {
        idTrabajador = in.readString();
        fecha = in.readString();
        horaEntrada = in.readString();
        horaSalida = in.readString();
        textoEntrada = in.readString();
        textoSalida = in.readString();
    }

    public static final Creator<Fichajes> CREATOR = new Creator<Fichajes>() {
        @Override
        public Fichajes createFromParcel(Parcel in) {
            return new Fichajes(in);
        }

        @Override
        public Fichajes[] newArray(int size) {
            return new Fichajes[size];
        }
    };

    public String getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(String idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(String horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public String getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }

    public String getTextoEntrada() {
        return textoEntrada;
    }

    public void setTextoEntrada(String textoEntrada) {
        this.textoEntrada = textoEntrada;
    }

    public String getTextoSalida() {
        return textoSalida;
    }

    public void setTextoSalida(String textoSalida) {
        this.textoSalida = textoSalida;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idTrabajador);
        dest.writeString(fecha);
        dest.writeString(horaEntrada);
        dest.writeString(horaSalida);
        dest.writeString(textoEntrada);
        dest.writeString(textoSalida);
    }
}
