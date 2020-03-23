package edu.dam.empresaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class GestionarVacacionesActivity extends AppCompatActivity {

    private CardView cardListarSolicitudes, cardGestSolicitudes;
    private TextView tvNombreTrabajador;

    // declaramos un objeto "trabajador"
    Trabajador mJimenez;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionar_vacaciones);

        // referenciamos vistas
        tvNombreTrabajador = findViewById(R.id.tvNombreTrabajador_Gestionar);
        cardGestSolicitudes = findViewById(R.id.cardGestSolicitudes);
        cardListarSolicitudes = findViewById(R.id.cardListarSolicitudes);

        // creamos un objeto "Trabajador", cuyo contenido será el objeto
        // "Trabajador" que creamos en la Activity "AdministrarActivity"
        // y que pasamos a esta Activity mediante putExtra
        mJimenez = new Trabajador();
        mJimenez = getIntent().getParcelableExtra("parametro");

        //Mostramos el nombre del trabajador en un TextView
        tvNombreTrabajador.setText(mJimenez.getNombre() + " " + mJimenez.getApellido1());

        //listener del botón Gestión de Solicitudes
        cardGestSolicitudes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), GestionSolicitudesActivity.class);
                intent.putExtra("parametro", mJimenez); //pasamos el objeto mJimenez
                startActivity(intent);
            }
        });

        cardListarSolicitudes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ListarSolicitudesVacacionesActivity.class);
                intent.putExtra("parametro", mJimenez); //pasamos el objeto mJimenez
                startActivity(intent);
            }
        });
    }


    //método que al pulsar en el ImageView (flecha) de la cabecera
    // y el cardview de volver nos lleva a la pantalla anterior
    public void volver(View view) {
        Intent intent = new Intent(getApplicationContext(), AdministrarActivity.class);
        intent.putExtra("parametro", mJimenez); //pasamos el objeto mJimenez
        startActivity(intent);
    }
}
