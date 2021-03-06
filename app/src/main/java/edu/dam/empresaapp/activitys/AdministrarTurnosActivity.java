package edu.dam.empresaapp.activitys;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.dam.empresaapp.R;
import edu.dam.empresaapp.pojos.Trabajador;

public class AdministrarTurnosActivity extends AppCompatActivity {

    private TextView tvNombreTrabajador;
    private CardView cardAsignarTurnos, cardVerTurnos;


    // declaramos un objeto "trabajador"
    private Trabajador mJimenez;

    // referenciamos la BBDD
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar_turnos);

        // referenciamos vistas
        tvNombreTrabajador  = findViewById(R.id.tvNombreTrabajador_AdministrarTurnos);
        cardAsignarTurnos   = findViewById(R.id.cardAsignarTurnos);
        cardVerTurnos       = findViewById(R.id.cardVerTurnos);

        // creamos un objeto "Trabajador"
        mJimenez = new Trabajador();
        mJimenez = getIntent().getParcelableExtra("parametro");

        //Mostramos el nombre del trabajador en un TextView
        tvNombreTrabajador.setText(mJimenez.getNombre() + " " + mJimenez.getApellido1());

        // referenciamos la BBDD
        db = FirebaseDatabase.getInstance().getReference();

        // listener del cardView Asignar Turnos
        cardAsignarTurnos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), AsignarTurnosActivity.class);
                intent.putExtra("parametro", mJimenez); //pasamos el objeto mJimenez
                startActivity(intent);
            }
        });

        // listener del cardView Ver turnos
        cardVerTurnos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), VerTurnosActivity.class);
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
