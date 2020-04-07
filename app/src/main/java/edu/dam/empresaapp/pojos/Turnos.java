package edu.dam.empresaapp.pojos;

public class Turnos {

    private String idTrabajador;
    private String anioTurno;
    private String mesTurno;
    private String diaTurno;

    public Turnos() {
    }

    public Turnos(String idTrabajador, String anioTurno, String mesTurno, String diaTurno) {
        this.idTrabajador = idTrabajador;
        this.anioTurno = anioTurno;
        this.mesTurno = mesTurno;
        this.diaTurno = diaTurno;
    }

    public String getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(String idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public String getAnioTurno() {
        return anioTurno;
    }

    public void setAnioTurno(String anioTurno) {
        this.anioTurno = anioTurno;
    }

    public String getMesTurno() {
        return mesTurno;
    }

    public void setMesTurno(String mesTurno) {
        this.mesTurno = mesTurno;
    }

    public String getDiaTurno() {
        return diaTurno;
    }

    public void setDiaTurno(String diaTurno) {
        this.diaTurno = diaTurno;
    }
}
