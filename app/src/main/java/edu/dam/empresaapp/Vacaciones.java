package edu.dam.empresaapp;

public class Vacaciones {

    private String idTrabajador;
    private String anioVacaciones;
    private String numeroPeriodos;
    private String fechaInicioPeriodo1;
    private String fechaFinPeriodo1;
    private String fechaInicioPeriodo2;
    private String fechaFinPeriodo2;
    private String estadoVacaciones;

    public Vacaciones(){
    }


    public Vacaciones(String idTrabajador, String anioVacaciones, String numeroPeriodos,
                      String fechaInicioPeriodo1, String fechaFinPeriodo1, String fechaInicioPeriodo2,
                      String fechaFinPeriodo2, String estadoVacaciones) {
        this.idTrabajador = idTrabajador;
        this.anioVacaciones = anioVacaciones;
        this.numeroPeriodos = numeroPeriodos;
        this.fechaInicioPeriodo1 = fechaInicioPeriodo1;
        this.fechaFinPeriodo1 = fechaFinPeriodo1;
        this.fechaInicioPeriodo2 = fechaInicioPeriodo2;
        this.fechaFinPeriodo2 = fechaFinPeriodo2;
        this.estadoVacaciones = estadoVacaciones;
    }

    public String getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(String idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public String getAnioVacaciones() {
        return anioVacaciones;
    }

    public void setAnioVacaciones(String anioVacaciones) {
        this.anioVacaciones = anioVacaciones;
    }

    public String getNumeroPeriodos() {
        return numeroPeriodos;
    }

    public void setNumeroPeriodos(String numeroPeriodos) {
        this.numeroPeriodos = numeroPeriodos;
    }

    public String getFechaInicioPeriodo1() {
        return fechaInicioPeriodo1;
    }

    public void setFechaInicioPeriodo1(String fechaInicioPeriodo1) {
        this.fechaInicioPeriodo1 = fechaInicioPeriodo1;
    }

    public String getFechaFinPeriodo1() {
        return fechaFinPeriodo1;
    }

    public void setFechaFinPeriodo1(String fechaFinPeriodo1) {
        this.fechaFinPeriodo1 = fechaFinPeriodo1;
    }

    public String getFechaInicioPeriodo2() {
        return fechaInicioPeriodo2;
    }

    public void setFechaInicioPeriodo2(String fechaInicioPeriodo2) {
        this.fechaInicioPeriodo2 = fechaInicioPeriodo2;
    }

    public String getFechaFinPeriodo2() {
        return fechaFinPeriodo2;
    }

    public void setFechaFinPeriodo2(String fechaFinPeriodo2) {
        this.fechaFinPeriodo2 = fechaFinPeriodo2;
    }

    public String getEstadoVacaciones() {
        return estadoVacaciones;
    }

    public void setEstadoVacaciones(String estadoVacaciones) {
        this.estadoVacaciones = estadoVacaciones;
    }
}
