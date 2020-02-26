package edu.dam.empresaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class VacacionesActivity extends AppCompatActivity {

    TextView tvNombreTrabajador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacaciones);

        tvNombreTrabajador = findViewById(R.id.tvNombreTrabajador_Vacaciones);

        Trabajador trabajador = new Trabajador();

        trabajador = getIntent().getParcelableExtra("parametro");

        tvNombreTrabajador.setText(trabajador.getNombre() + " " + trabajador.getApellido1() + " "+ trabajador.getId()+
                " " + trabajador.getEsResponsable());
    }
}
