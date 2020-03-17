package edu.dam.empresaapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class GestionSolicitudesActivity extends AppCompatActivity {

    private TextView tvNombreTrabajador;
    private String anio, id;
    private ListView lvTrabajadores;

    // referenciamos la BBDD
    DatabaseReference db;
    DatabaseReference dbTrabajadores;

    // declaramos un objeto "trabajador"
    private Trabajador mJimenez;
    private Trabajador trabajador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_solicitudes);

        // referenciamos vistas
        tvNombreTrabajador = findViewById(R.id.tvNombreTrabajador_GestionSolicitudes);
        lvTrabajadores     = findViewById(R.id.lvTrabajadores);

        // creamos un objeto "Trabajador"
        mJimenez = new Trabajador();
        mJimenez = getIntent().getParcelableExtra("parametro");

        //Mostramos el nombre del trabajador en un TextView
        tvNombreTrabajador.setText(mJimenez.getNombre() + " " + mJimenez.getApellido1());

        // referenciamos la BBDD
        db = FirebaseDatabase.getInstance().getReference();
        dbTrabajadores = FirebaseDatabase.getInstance().getReference().child("Trabajadores");

        // Como tenemos que consultar el año, lo obtenemos del sistema.
        // El responsable consultará el estado de las vacaciones en el
        // año natural
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        Date date = new Date();
        anio = dateFormat.format(date);
        Log.d("año", anio);

        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, list);
        lvTrabajadores.setAdapter(adapter);

        // consultamos la BBDD
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

                list.add(trabajador.getNombre() + " "
                        + trabajador.getApellido1() + " "
                        + trabajador.getApellido2());
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

        /*dbTrabajadores.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                id = dataSnapshot.getChildren().iterator().next().getKey();
                //Toast.makeText(GestionSolicitudesActivity.this, id, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        /*db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot query : dataSnapshot.child("Trabajadores").getChildren())
                {
                    //trabajador = query.getValue(Trabajador.class);

                    String id = query.getKey();
                    String nombre = query.child("nombre").getValue().toString();

                    Toast.makeText(GestionSolicitudesActivity.this,
                                       trabajador.getNombre()
                                    + " " + trabajador.getApellido1()
                                    + " " + trabajador.getApellido2()
                                    + " " + trabajador.getEmail()
                                    + " " + trabajador.getEsResponsable()
                                    + " " + trabajador.getNif()
                                    + " " + trabajador.getId(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(GestionSolicitudesActivity.this, id + " " + nombre, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        // este funciona, pero no devuelve el id, el resto de datos sí
        /*db.child("Trabajadores").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {

                trabajador = dataSnapshot.getValue(Trabajador.class);

                Toast.makeText(GestionSolicitudesActivity.this,
                           trabajador.getNombre()
                        + " " + trabajador.getApellido1()
                        + " " + trabajador.getApellido2()
                        + " " + trabajador.getEmail()
                        + " " + trabajador.getEsResponsable()
                        + " " + trabajador.getNif()
                        + " " + trabajador.getId(), Toast.LENGTH_SHORT).show();

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
        });*/


        /*db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {

                // colección trabajador
                id = dataSnapshot.child("Trabajadores").getChildren().iterator().next().getKey();
                Log.d("marca", id);
                Toast.makeText(GestionSolicitudesActivity.this, id, Toast.LENGTH_SHORT).show();
                *//*String nombre = dataSnapshot.child("nombre").getValue().toString();
                String apellido1 = dataSnapshot.child("apellido1").getValue().toString();
                String apellido2 = dataSnapshot.child("apellido2").getValue().toString();*//*

                // colección Vacaciones
                *//*String estadoVacaciones = dataSnapshot.child("Vacaciones").child(anio)
                        .child(id).child("estado_vacaciones").getValue().toString();
                String numeroPeriodos = dataSnapshot.child("Vacaciones").child(anio)
                        .child(id).child("numero_periodos").getValue().toString();
                String fechaInicioP1 = dataSnapshot.child("Vacaciones").child(anio)
                        .child(id).child("fecha_inicio_periodo1").getValue().toString();
                String fechafinP1 = dataSnapshot.child("Vacaciones").child(anio)
                        .child(id).child("fecha_fin_periodo1").getValue().toString();
                String fechaInicioP2 = dataSnapshot.child("Vacaciones").child(anio)
                        .child(id).child("fecha_inicio_periodo2").getValue().toString();
                String fechafinP2 = dataSnapshot.child("Vacaciones").child(anio)
                        .child(id).child("fecha_fin_periodo2").getValue().toString();*//*

                list.add(id);
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
        });*/

        /*db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                // colección trabajador
                String id = dataSnapshot.getKey();
                Log.d("id", id);
                Toast.makeText(GestionSolicitudesActivity.this, id, Toast.LENGTH_SHORT).show();
                *//*String nombre = dataSnapshot.child("Trabajadores").child(id).child("nombre").getValue().toString();
                String apellido1 = dataSnapshot.child("Trabajadores").child(id).child("apellido1").getValue().toString();
                String apellido2 = dataSnapshot.child("Trabajadores").child(id).child("apellido2").getValue().toString();

                // colección Vacaciones
                String estadoVacaciones = dataSnapshot.child("Vacaciones").child(anio)
                                        .child(id).child("estado_vacaciones").getValue().toString();
                String numeroPeriodos = dataSnapshot.child("Vacaciones").child(anio)
                        .child(id).child("numero_periodos").getValue().toString();
                String fechaInicioP1 = dataSnapshot.child("Vacaciones").child(anio)
                        .child(id).child("fecha_inicio_periodo1").getValue().toString();
                String fechafinP1 = dataSnapshot.child("Vacaciones").child(anio)
                        .child(id).child("fecha_fin_periodo1").getValue().toString();
                String fechaInicioP2 = dataSnapshot.child("Vacaciones").child(anio)
                        .child(id).child("fecha_inicio_periodo2").getValue().toString();
                String fechafinP2 = dataSnapshot.child("Vacaciones").child(anio)
                        .child(id).child("fecha_fin_periodo2").getValue().toString();

                list.add(nombre + "" + apellido1);
                adapter.notifyDataSetChanged();*//*
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


    }

        //método que al pulsar en el ImageView (flecha) de la cabecera
        // nos lleva a la pantalla anterior
        public void volver(View view) {
        Intent intent = new Intent(getApplicationContext(), GestionarVacacionesActivity.class);
        intent.putExtra("parametro", mJimenez); //pasamos el objeto mJimenez
        startActivity(intent);
    }
}
