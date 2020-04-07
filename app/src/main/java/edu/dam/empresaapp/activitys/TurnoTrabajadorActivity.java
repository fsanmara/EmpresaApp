package edu.dam.empresaapp.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import edu.dam.empresaapp.R;
import edu.dam.empresaapp.adaptadores.AdaptadorDias;
import edu.dam.empresaapp.adaptadores.AdaptadorTrabajadores;
import edu.dam.empresaapp.pojos.Trabajador;
import edu.dam.empresaapp.pojos.Turnos;

public class TurnoTrabajadorActivity extends AppCompatActivity {

    private TextView tvTrabajadorVerTurnos, tvAnio, tvMes;
    private Spinner spTrabajadores;
    private AdaptadorTrabajadores adapterTrabajadores;
    private AdaptadorDias adapterDia;
    private ListView lvDias;
    private String anio, mes, dia, mesLetra, idTrabajador;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private Button btnVerDias;

    // declaramos un objeto "Trabajador"
    private Trabajador trabajador;

    // referenciamos la BBDD
    DatabaseReference db;

    // declaramos el arrayList
    ArrayList<Trabajador> listadoTrabajadores = new ArrayList<>();
    ArrayList<Turnos> listadoTurnos = new ArrayList<>();

    Context contexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turno_trabajador);

        // referenciamos vistas
        tvTrabajadorVerTurnos = findViewById(R.id.tvNombreTrabajador_VerTurnos);
        spTrabajadores              = findViewById(R.id.spTrabajadores);
        lvDias                      = findViewById(R.id.lvDias);
        tvAnio                      = findViewById(R.id.tvAnio);
        tvMes                       = findViewById(R.id.tvMes);
        btnVerDias                  = findViewById(R.id.btnVerDias);

        // creamos un objeto "Trabajador"
        new Trabajador();
        trabajador = getIntent().getParcelableExtra("parametro");

        //Mostramos el nombre del trabajador en un TextView
        tvTrabajadorVerTurnos.setText(trabajador.getNombre() + " " + trabajador.getApellido1());

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




    }

    //método que al pulsar en el ImageView (flecha) de la cabecera
    // nos lleva a la pantalla anterior
    public void volver(View view) {
        Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
        intent.putExtra("parametro", trabajador); //pasamos el objeto trabajador
        startActivity(intent);
    }
}
