package edu.dam.empresaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.dam.empresaapp.adaptadores.AdaptadorAnio;
import edu.dam.empresaapp.adaptadores.AdaptadorDias;
import edu.dam.empresaapp.adaptadores.AdaptadorMes;
import edu.dam.empresaapp.adaptadores.AdaptadorTrabajadores;

public class VerTurnosActivity extends AppCompatActivity {

    private TextView tvNombreTrabajadorVerTurnos;
    private Spinner spTrabajadores, spAnio, spMes;
    private AdaptadorTrabajadores adapterTrabajadores;
    private AdaptadorAnio adapterAnio;
    private AdaptadorMes adapterMes;
    private AdaptadorDias adapterDia;
    private ListView lvDias;
    private String idTrabajador, anio, mes, dia;

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
        spTrabajadores              = findViewById(R.id.spTrabajadores);
        spAnio                      = findViewById(R.id.spAnio);
        spMes                       = findViewById(R.id.spMes);
        lvDias                      = findViewById(R.id.lvDias);

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
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                for(DataSnapshot query : dataSnapshot.getChildren())
                {
                    //final Turnos turnos = query.getValue(Turnos.class);
                    idTrabajador = query.getKey();
                }

                db.child("Turnos").child(idTrabajador).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                        for(DataSnapshot query : dataSnapshot.getChildren())
                        {
                            anio = query.getKey();
                            //Toast.makeText(VerTurnosActivity.this, anio , Toast.LENGTH_SHORT).show();
                        }

                        db.child("Turnos").child(idTrabajador).child(anio).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                                for(DataSnapshot query : dataSnapshot.getChildren())
                                {
                                    mes = query.getKey();
                                    Toast.makeText(VerTurnosActivity.this, mes , Toast.LENGTH_SHORT).show();
                                }

                                db.child("Turnos").child(idTrabajador).child(anio).child(mes).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                                        for(DataSnapshot query : dataSnapshot.getChildren())
                                        {
                                            //dia = query.getKey();
                                            dia = query.getValue().toString();
                                            //Toast.makeText(VerTurnosActivity.this, dia , Toast.LENGTH_SHORT).show();
                                        }
                                        final Turnos turnos = new Turnos(idTrabajador, anio, mes, dia);
                                        listadoTurnos.add(turnos);
                                        /*adapterAnio = new AdaptadorAnio(contexto, listadoTurnos);
                                        spAnio.setAdapter(adapterAnio);
                                        adapterMes = new AdaptadorMes(contexto, listadoTurnos);
                                        spMes.setAdapter(adapterMes);
                                        adapterDia = new AdaptadorDias(contexto, listadoTurnos);
                                        lvDias.setAdapter(adapterDia);*/

                                        Toast.makeText(VerTurnosActivity.this, turnos.getIdTrabajador() + " " +
                                                turnos.getAnioTurno() + " " +
                                                turnos.getMesTurno() + " " +
                                                turnos.getDiaTurno(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                /*final Turnos turnos = new Turnos(idTrabajador, anio, mes, dia);
                listadoTurnos.add(turnos);
                adapterMes = new AdaptadorMes(contexto, listadoTurnos);
                spMes.setAdapter(adapterMes);

                Toast.makeText(VerTurnosActivity.this, turnos.getMesTurno(), Toast.LENGTH_SHORT).show();*/

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
