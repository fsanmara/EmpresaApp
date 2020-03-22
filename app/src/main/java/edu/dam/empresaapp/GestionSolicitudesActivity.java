package edu.dam.empresaapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import edu.dam.empresaapp.adaptadores.AdaptadorSolicitudes;

public class GestionSolicitudesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private TextView tvNombreTrabajador;
    private String anio, fechaInicioPeriodo1, fechaFinPeriodo1, fechaInicioPeriodo2, fechaFinPeriodo2, numeroPeriodos;
    private ListView lvTrabajadores;
    private AdaptadorSolicitudes adaptador;

    // declaramos la referencia a la BBDD
    DatabaseReference db;

    // declaramos un objeto "trabajador"
    private Trabajador mJimenez;
    private Trabajador trabajador;

    // declaramos el arrayList
    ArrayList<Trabajador> list = new ArrayList<>();

    int posicion; // contiene la posición del ítem pulsado del listview
    Context contexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_solicitudes);

        // referenciamos vistas
        tvNombreTrabajador = findViewById(R.id.tvNombreTrabajador_GestionSolicitudes);
        lvTrabajadores     = findViewById(R.id.lvTrabajadores);

        lvTrabajadores.setOnItemClickListener(this);

        // creamos un objeto "Trabajador"
        mJimenez = new Trabajador();
        mJimenez = getIntent().getParcelableExtra("parametro");

        //Mostramos el nombre del trabajador en un TextView
        tvNombreTrabajador.setText(mJimenez.getNombre() + " " + mJimenez.getApellido1());

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

        // consultamos la BBDD
        db.child("Trabajadores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot objeto : dataSnapshot.getChildren()){

                    final Trabajador trabajador = objeto.getValue(Trabajador.class);

                    db.child("Vacaciones").child(trabajador.getId()).child(anio).
                            addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for (DataSnapshot objeto1 : dataSnapshot.getChildren()){
                                        if(objeto1.getValue().toString().equals("pendiente_confirmacion")){
                                            list.add(trabajador);
                                        }
                                    }
                                    adaptador = new AdaptadorSolicitudes(contexto, list);
                                    lvTrabajadores.setAdapter(adaptador);
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

    // evento clic Listview
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
        final String idEmpleado;

        posicion=i;

        Trabajador trabajador = list.get(i);
        idEmpleado=trabajador.getId();

        // consultamos la BBDD para saber las fechas que ha solicitado el trabajador
        db.child("Vacaciones").child(idEmpleado).child(anio).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                        for (DataSnapshot vacaciones : dataSnapshot.getChildren()){

                            if(vacaciones.getKey().equals("fecha_inicio_periodo1")){
                                fechaInicioPeriodo1 = vacaciones.getValue().toString();


                               Toast.makeText(GestionSolicitudesActivity.this, fechaInicioPeriodo1, Toast.LENGTH_SHORT).show();

                            }
                            if(vacaciones.getKey().equals("fecha_inicio_periodo2")){
                                fechaInicioPeriodo2 = vacaciones.getValue().toString();

                                Toast.makeText(GestionSolicitudesActivity.this, fechaInicioPeriodo2, Toast.LENGTH_SHORT).show();

                            }
                            if(vacaciones.getKey().equals("fecha_fin_periodo1")){
                                fechaFinPeriodo1 = vacaciones.getValue().toString();

                                Toast.makeText(GestionSolicitudesActivity.this, fechaFinPeriodo1, Toast.LENGTH_SHORT).show();

                            }
                            if(vacaciones.getKey().equals("fecha_fin_periodo2")){
                                fechaFinPeriodo2 = vacaciones.getValue().toString();

                                Toast.makeText(GestionSolicitudesActivity.this, fechaFinPeriodo2, Toast.LENGTH_SHORT).show();

                            }
                            if(vacaciones.getKey().equals("numero_periodos")){
                                numeroPeriodos = vacaciones.getValue().toString();

                                Toast.makeText(GestionSolicitudesActivity.this, numeroPeriodos, Toast.LENGTH_SHORT).show();

                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        // creamos la ventana de diálogo
        AlertDialog.Builder ventana = new AlertDialog.Builder(contexto);

        ventana.setTitle("Vacaciones solicitadas");
        ventana.setMessage("Condecer/Rechazar vacaciones...");

        // botón "Conceder"
        ventana.setPositiveButton("Conceder", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
                modificarVacaciones(idEmpleado,"aceptadas", "Vacaciones aceptadas...");
            }
        });

        // botón "Denegar"
        ventana.setNegativeButton("Denegar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
                modificarVacaciones(idEmpleado, "denegadas", "Vacaciones rechazadas...");
            }
        });

        AlertDialog alert = ventana.create();
        alert.show();

    }

    public void modificarVacaciones(String id, String valor, final String estado)
    {
        Map<String, Object> registroMap = new HashMap<>();

        registroMap.put("estado_vacaciones", valor);

        db.child("Vacaciones").child(id).child(anio)
                .updateChildren(registroMap, new DatabaseReference.CompletionListener()
                {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError,
                                           @NonNull DatabaseReference databaseReference)
                    {
                        // modificación correcta
                        if (databaseError == null)
                        {
                            list.remove(posicion);
                            adaptador.notifyDataSetChanged();

                            Toast.makeText(GestionSolicitudesActivity.this, estado,
                                    Toast.LENGTH_LONG).show();

                        }
                        else
                        {
                            Toast.makeText(GestionSolicitudesActivity.this,
                                    "No fue posible la operación...", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }
}

