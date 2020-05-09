package edu.dam.empresaapp.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.dam.empresaapp.R;
import edu.dam.empresaapp.activitys.AdministrarActivity;
import edu.dam.empresaapp.adaptadores.AdaptadorFichajes;
import edu.dam.empresaapp.adaptadores.AdaptadorTrabajadores;
import edu.dam.empresaapp.pojos.Fichajes;
import edu.dam.empresaapp.pojos.Trabajador;

public class ControlHorarioActivity extends AppCompatActivity {

    private TextView tvNombreTrabajadorControlFichajes;
    private ListView lvFichajesTrabajadores;
    private Spinner spTrabajadores;
    private AdaptadorTrabajadores adapterTrabajadores;
    private AdaptadorFichajes adapterFichajes;

    private String idTrabajador, fecha, horaEntrada, horaSalida, textoEntrada, textoSalida;

    // declaramos la referencia a la BBDD
    DatabaseReference db;

    // declaramos un objeto "trabajador"
    private Trabajador mJimenez;

    // declaramos los arrayList
    ArrayList<Trabajador> listadoTrabajadores = new ArrayList<>();
    ArrayList<Fichajes>   listadoFichajes     = new ArrayList<>();

    Context contexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_horario);

        //referenciamos vistas
        tvNombreTrabajadorControlFichajes = findViewById(R.id.tvNombreTrabajador_ControlFichajes);
        spTrabajadores = findViewById(R.id.spTrabajadores);
        lvFichajesTrabajadores = findViewById(R.id.lvFichajesTrabajadores);

        // creamos un objeto "Trabajador"
        new Trabajador();
        mJimenez = getIntent().getParcelableExtra("parametro");

        //Mostramos el nombre del trabajador en un TextView
        tvNombreTrabajadorControlFichajes.setText(mJimenez.getNombre() + " " + mJimenez.getApellido1());

        // referenciamos la BBDD
        db = FirebaseDatabase.getInstance().getReference();

        contexto = this;

        adapterTrabajadores = new AdaptadorTrabajadores(contexto, listadoTrabajadores);
        //adapterFichajes     = new AdaptadorFichajes(contexto, listadoFichajes);

        //consultamos la BBDD
        db.child("Trabajadores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot objeto : dataSnapshot.getChildren()){

                    final Trabajador trabajador = objeto.getValue(Trabajador.class);

                    listadoTrabajadores.add(trabajador);
                    spTrabajadores.setAdapter(adapterTrabajadores);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // evento click del Spinner
        spTrabajadores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                final Trabajador trabajador = listadoTrabajadores.get(position);

                final String idTrabajador = trabajador.getId();

                listadoFichajes.clear();

                // consultamos la BBDD para crear un objeto "Fichajes" y añadirlo a su
                // ArrayList. Limitamos la búsqueda a los últimos 30 registros
                Query query = db.child("Fichajes").child(idTrabajador).limitToLast(30);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(!dataSnapshot.exists())
                        {
                            Toast.makeText(getApplicationContext(),
                                    "No hay resultados que mostrar para el trabajador seleccionado",
                                    Toast.LENGTH_SHORT).show();
                        }

                        for (DataSnapshot query : dataSnapshot.getChildren())
                        {
                            fecha = query.getKey();

                            horaEntrada = query.child("hora_entrada").getValue().toString();

                            // cuando el usuario lee el QR, se crean los nodos "hora_entrada" y
                            // texto_entrada, pero los nodos "hora_salida" y "texto_salida" no van
                            // a existir hasta que el usuario vuelva a leer el QR por segunda vez
                            // el mismo día. Si el usuario lista los fichajes antes de la segunda
                            // lectura, los nodos "hora_salida y "texto_salida" no existirán y se
                            // producirá un nullpointerexception. Por eso creamos estructuras if en
                            // dichos nodos
                            if(!query.child("hora_salida").exists()){
                                horaSalida = "sin registrar";
                            } else {
                                horaSalida = query.child("hora_salida").getValue().toString();
                            }

                            textoEntrada = query.child("texto_entrada").getValue().toString();

                            if(!query.child("texto_salida").exists()){
                                textoSalida = "";
                            } else {
                                textoSalida = query.child("texto_salida").getValue().toString();
                            }


                            Fichajes fichajes = new Fichajes(idTrabajador, fecha, horaEntrada, horaSalida, textoEntrada, textoSalida);

                            listadoFichajes.add(fichajes);

                        }

                        adapterFichajes     = new AdaptadorFichajes(contexto, listadoFichajes);
                        lvFichajesTrabajadores.setAdapter(adapterFichajes);

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                // con este código obtendríamos todos las fechas y horas, sin limitar
                // la búsqueda a los últimos 30 registros
                /*db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot query : dataSnapshot.child("Fichajes").child(idTrabajador).getChildren())
                        {
                            fecha = query.getKey();

                            horaEntrada = query.child("hora_entrada").getValue().toString();

                            // cuando el usuario lee el QR, se crean los nodos "hora_entrada" y
                            // texto_entrada, pero los nodos "hora_salida" y "texto_salida" no van
                            // a existir hasta que el usuario vuelva a leer el QR por segunda vez
                            // el mismo día. Si el usuario lista los fichajes antes de la segunda
                            // lectura, los nodos "hora_salida y "texto_salida" no existirán y se
                            // producirá un nullpointerexception. Por eso creamos estructuras if en
                            // dichos nodos
                            if(!query.child("hora_salida").exists()){
                                horaSalida = "sin registrar";
                            } else {
                                horaSalida = query.child("hora_salida").getValue().toString();
                            }

                            textoEntrada = query.child("texto_entrada").getValue().toString();

                            if(!query.child("texto_salida").exists()){
                                textoSalida = "";
                            } else {
                                textoSalida = query.child("texto_salida").getValue().toString();
                            }


                            Fichajes fichajes = new Fichajes(idTrabajador, fecha, horaEntrada, horaSalida, textoEntrada, textoSalida);

                            listadoFichajes.add(fichajes);

                        }

                        adapterFichajes     = new AdaptadorFichajes(contexto, listadoFichajes);
                        lvFichajesTrabajadores.setAdapter(adapterFichajes);

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    //método que al pulsar en el ImageView (flecha) de la cabecera
    // nos lleva a la pantalla anterior
    public void volver(View view) {
        Intent intent = new Intent(getApplicationContext(), AdministrarActivity.class);
        intent.putExtra("parametro", mJimenez); //pasamos el objeto mJimenez
        startActivity(intent);
    }


}
