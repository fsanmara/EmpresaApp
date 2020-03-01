package edu.dam.empresaapp;

public class Vacaciones {

    private String id_trabajador;
    private String anio_vacaciones;
    private int numero_periodos;
    private String fecha_inicio_periodo1;
    private String fecha_fin_periodo1;
    private String fecha_inicio_periodo2;
    private String fecha_fin_periodo2;
    private String estado_vacaciones;

    public Vacaciones(){
    }


    public Vacaciones(String id_trabajador, String anio_vacaciones,
                      int numero_periodos, String fecha_inicio_periodo1,
                      String fecha_fin_periodo1, String fecha_inicio_periodo2,
                      String fecha_fin_periodo2, String estado_vacaciones) {
        this.id_trabajador = id_trabajador;
        this.anio_vacaciones = anio_vacaciones;
        this.numero_periodos = numero_periodos;
        this.fecha_inicio_periodo1 = fecha_inicio_periodo1;
        this.fecha_fin_periodo1 = fecha_fin_periodo1;
        this.fecha_inicio_periodo2 = fecha_inicio_periodo2;
        this.fecha_fin_periodo2 = fecha_fin_periodo2;
        this.estado_vacaciones = estado_vacaciones;
    }

    public String getId_trabajador() {
        return id_trabajador;
    }

    public void setId_trabajador(String id_trabajador) {
        this.id_trabajador = id_trabajador;
    }

    public String getAnio_vacaciones() {
        return anio_vacaciones;
    }

    public void setAnio_vacaciones(String anio_vacaciones) {
        this.anio_vacaciones = anio_vacaciones;
    }

    public int getNumero_periodos() {
        return numero_periodos;
    }

    public void setNumero_periodos(int numero_periodos) {
        this.numero_periodos = numero_periodos;
    }

    public String getFecha_inicio_periodo1() {
        return fecha_inicio_periodo1;
    }

    public void setFecha_inicio_periodo1(String fecha_inicio_periodo1) {
        this.fecha_inicio_periodo1 = fecha_inicio_periodo1;
    }

    public String getFecha_fin_periodo1() {
        return fecha_fin_periodo1;
    }

    public void setFecha_fin_periodo1(String fecha_fin_periodo1) {
        this.fecha_fin_periodo1 = fecha_fin_periodo1;
    }

    public String getFecha_inicio_periodo2() {
        return fecha_inicio_periodo2;
    }

    public void setFecha_inicio_periodo2(String fecha_inicio_periodo2) {
        this.fecha_inicio_periodo2 = fecha_inicio_periodo2;
    }

    public String getFecha_fin_periodo2() {
        return fecha_fin_periodo2;
    }

    public void setFecha_fin_periodo2(String fecha_fin_periodo2) {
        this.fecha_fin_periodo2 = fecha_fin_periodo2;
    }

    public String getEstado_vacaciones() {
        return estado_vacaciones;
    }

    public void setEstado_vacaciones(String estado_vacaciones) {
        this.estado_vacaciones = estado_vacaciones;
    }
}
