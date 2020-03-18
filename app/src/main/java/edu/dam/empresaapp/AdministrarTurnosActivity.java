package edu.dam.empresaapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdministrarTurnosActivity extends AppCompatActivity {

    private TextView tvNombreTrabajador;

    private ListView lvTrabajadores;

    // declaramos un objeto "trabajador"
    private Trabajador mJimenez;
    private Trabajador trabajador;

    // referenciamos la BBDD
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar_turnos);

        // referenciamos vistas
        tvNombreTrabajador = findViewById(R.id.tvNombreTrabajador_AdministrarTurnos);
        lvTrabajadores     = findViewById(R.id.lvTrabajadores_AdministrarTurnos);

        // creamos un objeto "Trabajador"
        mJimenez = new Trabajador();
        mJimenez = getIntent().getParcelableExtra("parametro");

        //Mostramos el nombre del trabajador en un TextView
        tvNombreTrabajador.setText(mJimenez.getNombre() + " " + mJimenez.getApellido1());

        // referenciamos la BBDD
        db = FirebaseDatabase.getInstance().getReference();

        // declaramos un ArrayList
        final ArrayList<Trabajador> list = new ArrayList<>();
        final ArrayAdapter<Trabajador> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, list);
        lvTrabajadores.setAdapter(adapter);

        db.child("Trabajadores").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {

                trabajador = dataSnapshot.getValue(Trabajador.class);

                /*Toast.makeText(GestionSolicitudesActivity.this,
                                   trabajador.getNombre()
                                + " " + trabajador.getApellido1()
                                + " " + trabajador.getApellido2()
                                + " " + trabajador.getEmail()
                                + " " + trabajador.getEsResponsable()
                                + " " + trabajador.getNif()
                                + " " + trabajador.getId(), Toast.LENGTH_SHORT).show();*/


                list.add(trabajador);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
