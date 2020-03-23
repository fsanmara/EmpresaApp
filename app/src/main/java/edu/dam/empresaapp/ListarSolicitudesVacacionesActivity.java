package edu.dam.empresaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import edu.dam.empresaapp.adaptadores.AdaptadorListVacaciones;

public class ListarSolicitudesVacacionesActivity extends AppCompatActivity {

    private TextView tvNombreTrabajador;
    private ListView lvTrabajadores;
    private String anio;
            //String estadoVacaciones, fechaInicioPeriodo1, fechaFinPeriodo1, fechaInicioPeriodo2, fechaFinPeriodo2, numeroPeriodos;

    Context contexto;

    // declaramos un objeto "trabajador"
    Trabajador mJimenez;

    DatabaseReference db;

    private AdaptadorListVacaciones adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_solicitudes_vacaciones);

        // referenciamos vistas
        tvNombreTrabajador = findViewById(R.id.tvNombreTrabajador_ListSolVac);
        lvTrabajadores = findViewById(R.id.lvSolVac);

        // creamos un objeto "Trabajador", cuyo contenido será el objeto
        // "Trabajador" que creamos en la Activity "AdministrarActivity"
        // y que pasamos a esta Activity mediante putExtra
        mJimenez = new Trabajador();
        mJimenez = getIntent().getParcelableExtra("parametro");

        //Mostramos el nombre del trabajador en un TextView
        tvNombreTrabajador.setText(mJimenez.getNombre() + " " + mJimenez.getApellido1());

        final ArrayList<Trabajador> listTrabajador = new ArrayList<>();
        final ArrayList<Vacaciones> listVacaciones = new ArrayList<>();

        // referenciamos la BBDD
        db = FirebaseDatabase.getInstance().getReference();

        contexto = this;

        // Como tenemos que consultar el año, lo obtenemos del sistema.
        // El responsable consultará el estado de las vacaciones en el
        // año natural
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        Date date = new Date();
        anio = dateFormat.format(date);
        Log.d("año", anio);

        // consultamos la BBDD para crear objetos trabajadores y pasarlos
        // al ArrayList
        db.child("Trabajadores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                for(DataSnapshot objeto : dataSnapshot.getChildren()){

                     final Trabajador trabajador = objeto.getValue(Trabajador.class);

                    db.child("Vacaciones").child(trabajador.getId()).child(anio).child("estado_vacaciones").
                            addValueEventListener(new ValueEventListener() {

                                Vacaciones vac = new Vacaciones();
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            //for (DataSnapshot vacaciones : dataSnapshot.getChildren()) {

                            String estadoVacaciones = dataSnapshot.getValue().toString();

                                /*if (vacaciones.getKey().equals("estado_vacaciones")) {
                                    estadoVacaciones = vacaciones.getValue().toString();
                                }

                            if(vacaciones.getKey().equals("fecha_inicio_periodo1")){
                                fechaInicioPeriodo1 = vacaciones.getValue().toString();

                            }
                            if(vacaciones.getKey().equals("fecha_inicio_periodo2")){
                                fechaInicioPeriodo2 = vacaciones.getValue().toString();

                            }
                            if(vacaciones.getKey().equals("fecha_fin_periodo1")){
                                fechaFinPeriodo1 = vacaciones.getValue().toString();


                            }
                            if(vacaciones.getKey().equals("fecha_fin_periodo2")){
                                fechaFinPeriodo2 = vacaciones.getValue().toString();

                            }
                            if(vacaciones.getKey().equals("numero_periodos")){
                                numeroPeriodos = vacaciones.getValue().toString();

                            }*/

                            if(estadoVacaciones.equals("aceptadas")){

                                listTrabajador.add(trabajador);
                                //listVacaciones.add(vac);

                                // iniciamos el adaptador para mostrar la información en el ListView
                                adaptador = new AdaptadorListVacaciones(contexto, listTrabajador);
                                lvTrabajadores.setAdapter(adaptador);

                            }

                           // }//fin del for
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
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
        Intent intent = new Intent(getApplicationContext(), GestionarVacacionesActivity.class);
        intent.putExtra("parametro", mJimenez); //pasamos el objeto mJimenez
        startActivity(intent);
    }
}
