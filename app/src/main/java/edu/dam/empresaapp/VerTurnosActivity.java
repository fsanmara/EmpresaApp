package edu.dam.empresaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.dam.empresaapp.adaptadores.AdaptadorMes;
import edu.dam.empresaapp.adaptadores.AdaptadorTrabajadores;

public class VerTurnosActivity extends AppCompatActivity {

    private TextView tvNombreTrabajadorVerTurnos;
    private Spinner spTrabajadores, spAnio, spMes;
    private AdaptadorTrabajadores adapterTrabajadores;
    private AdaptadorMes adapterMes;

    // declaramos un objeto "Trabajador"
    private Trabajador mJimenez;

    // referenciamos la BBDD
    DatabaseReference db;

    // declaramos el arrayList
    ArrayList<Trabajador> listadoTrabajadores = new ArrayList<>();
    ArrayList<Turnos> listadoTurnos = new ArrayList<>();

    Context contexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_turnos);

        // referenciamos vistas
        tvNombreTrabajadorVerTurnos = findViewById(R.id.tvNombreTrabajador_VerTurnos);
        spTrabajadores = findViewById(R.id.spTrabajadores);
        spAnio = findViewById(R.id.spAnio);
        spMes = findViewById(R.id.spMes);


        // creamos un objeto "Trabajador"
        mJimenez = new Trabajador();
        mJimenez = getIntent().getParcelableExtra("parametro");

        //Mostramos el nombre del trabajador en un TextView
        tvNombreTrabajadorVerTurnos.setText(mJimenez.getNombre() + " " + mJimenez.getApellido1());

        // referenciamos la BBDD
        db = FirebaseDatabase.getInstance().getReference();

        contexto = this;

        //consultamos la BBDD para llenar el Spinner de los empleados
        db.child("Trabajadores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot objeto : dataSnapshot.getChildren()){

                    final Trabajador trabajador = objeto.getValue(Trabajador.class);

                    listadoTrabajadores.add(trabajador);
                    adapterTrabajadores = new AdaptadorTrabajadores(contexto, listadoTrabajadores);
                    spTrabajadores.setAdapter(adapterTrabajadores);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //consultamos la BBDD para llenar el Spinner de los años
        db.child("Turnos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot query : dataSnapshot.getChildren()){

                    //final Turnos turnos = query.getValue(Turnos.class);
                    String idTrabajador = query.getKey();
                    String anio = query.getKey();
                    Long n = query.getChildrenCount();
                    Toast.makeText(VerTurnosActivity.this, "numero" + n, Toast.LENGTH_SHORT).show();

                    //listadoTurnos.add(turnos);
                    adapterMes = new AdaptadorMes(contexto, listadoTurnos);
                    spMes.setAdapter(adapterMes);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //método que al pulsar en el ImageView (flecha) de la cabecera
    // nos lleva a la pantalla anterior
    public void volver(View view) {
        Intent intent = new Intent(getApplicationContext(), AdministrarTurnosActivity.class);
        intent.putExtra("parametro", mJimenez); //pasamos el objeto mJimenez
        startActivity(intent);
    }
}
