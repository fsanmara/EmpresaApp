package edu.dam.empresaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AdministrarActivity extends AppCompatActivity {

    private CardView cardGestVacac,cardAdminTurnos, cardControlHorario;
    private TextView tvNombreTrabajador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar);

        //referenciamos vistas
        tvNombreTrabajador = findViewById(R.id.tvNombreTrabajador_Administrar);
        cardGestVacac      = findViewById(R.id.cardGestVacac);
        cardAdminTurnos    = findViewById(R.id.cardAdminTurnos);
        cardControlHorario = findViewById(R.id.cardControlHorario);

        // creamos un objeto "Trabajador", cuyo contenido será el objeto
        // "Trabajador" que creamos en la Activity "PrincipalActivity"
        // y que pasamos a esta Activity mediante putExtra
        new Trabajador();
        final Trabajador mJimenez = getIntent().getParcelableExtra("parametro");

        //Mostramos el nombre del trabajador en un TextView
        tvNombreTrabajador.setText(mJimenez.getNombre() + " " + mJimenez.getApellido1());

        //listener del cardView "Gestionar vacaciones"
        cardGestVacac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), GestionarVacacionesActivity.class);
                intent.putExtra("parametro", mJimenez); //pasamos el objeto mJimenez
                startActivity(intent);
            }
        });

        //listener del cardView "Administrar turnos"
        cardAdminTurnos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), AdministrarTurnosActivity.class);
                intent.putExtra("parametro", mJimenez); //pasamos el objeto mJimenez
                startActivity(intent);

            }
        });

        //listener del cardView "Control Horario"
        cardControlHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ControlHorarioActivity.class);
                intent.putExtra("parametro", mJimenez); //pasamos el objeto mJimenez
                startActivity(intent);
            }
        });

    }

    //método que al pulsar en el ImageView (flecha) de la cabecera
    // y el cardview de volver nos lleva a la pantalla anterior
    public void volver(View view) {
        Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
        startActivity(intent);
    }
}
