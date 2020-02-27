package edu.dam.empresaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        tvNombreTrabajador.setText(trabajador.getNombre() + " " + trabajador.getApellido1());
    }

    //método que al pulsar en el ImageView (flecha) de la cabecera
    //nos lleva a la pantalla anterior
    public void volver(View view) {
            Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
            startActivity(intent);
    }
}
