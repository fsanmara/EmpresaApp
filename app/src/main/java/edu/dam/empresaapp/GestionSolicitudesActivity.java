package edu.dam.empresaapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

    // referenciamos la BBDD
    DatabaseReference db;

    // declaramos un objeto "trabajador"
    private Trabajador mJimenez;
    private Trabajador trabajador;

    // declaramos el arrayList
    ArrayList<Trabajador> listadoTrabajador = new ArrayList<>();
    ArrayList<Vacaciones> listadoVacaciones = new ArrayList<>();

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
        db.child("Trabajadores").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                listadoTrabajador.clear();
                listadoVacaciones.clear();

                // leemos la información y la guardamos en un ArrayList
                for (final DataSnapshot objeto : dataSnapshot.getChildren())
                {
                    final Trabajador trabajador = objeto.getValue(Trabajador.class);

                    db.child("Vacaciones").child(trabajador.getId()).child(anio).addValueEventListener(new ValueEventListener()
                    {
                        Vacaciones vacaciones = new Vacaciones();

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {
                            int i=1;

                            for (DataSnapshot objetoVacaciones : dataSnapshot.getChildren())
                            {
                                switch (objetoVacaciones.getKey())
                                {
                                    case "estado_vacaciones":
                                        vacaciones.setEstadoVacaciones(objetoVacaciones.getValue().toString());
                                        break;

                                    case "fecha_fin_periodo1":
                                        vacaciones.setFechaFinPeriodo1(objetoVacaciones.getValue().toString());
                                        break;

                                    case "fecha_fin_periodo2":
                                        vacaciones.setFechaFinPeriodo2(objetoVacaciones.getValue().toString());
                                        break;

                                    case "fecha_inicio_periodo1":
                                        vacaciones.setFechaInicioPeriodo1(objetoVacaciones.getValue().toString());
                                        break;

                                    case "fecha_inicio_periodo2":
                                        vacaciones.setFechaInicioPeriodo2(objetoVacaciones.getValue().toString());
                                        break;

                                    case "numero_periodos":
                                        vacaciones.setNumeroPeriodos(objetoVacaciones.getValue().toString());
                                        break;
                                }

                                i++;

                                if (i == 7)
                                {
                                    if (vacaciones.getEstadoVacaciones().equals("pendiente_confirmacion"))
                                    {
                                        listadoTrabajador.add(trabajador);
                                        listadoVacaciones.add(vacaciones);
                                    }
                                }
                            }

                            // iniciamos el adaptador para mostrar la información en el ListView
                            adaptador = new AdaptadorSolicitudes(contexto, listadoTrabajador);
                            lvTrabajadores.setAdapter(adaptador);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError)
                        {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

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
    public void onItemClick(AdapterView<?> parent, View view, int i, long id)
    {
        final String idEmpleado;

        TextView nombreEmpleado, fechaInicio1, fechaFin1, fechaInicio2, fechaFin2;
        Button aceptar, denegar, salir;

        posicion=i;

        Trabajador trabajador = listadoTrabajador.get(i);
        Vacaciones vacaciones = listadoVacaciones.get(i);

        idEmpleado=trabajador.getId();

        // creamos la ventana de diálogo
        AlertDialog.Builder ventana = new AlertDialog.Builder(contexto);
        LayoutInflater inflater = (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View vista = inflater.inflate(R.layout.alert_dialog_gestionar_vacaciones, null);

        ventana.setView(vista);
        final AlertDialog alert = ventana.create();

        nombreEmpleado=(TextView) vista.findViewById(R.id.nombreEmpleado);
        fechaInicio1=(TextView) vista.findViewById(R.id.periodo1Inicio);
        fechaFin1=(TextView) vista.findViewById(R.id.periodo1Fin);
        fechaInicio2=(TextView) vista.findViewById(R.id.periodo2Inicio);
        fechaFin2=(TextView) vista.findViewById(R.id.periodo2Fin);

        aceptar=(Button) vista.findViewById(R.id.botonAceptar);
        denegar=(Button) vista.findViewById(R.id.botonDenegar);
        salir=(Button) vista.findViewById(R.id.botonSalir);


        nombreEmpleado.setText(trabajador.getNombre() + " " + trabajador.getApellido1() + " " + trabajador.getApellido2());

        if (vacaciones.getNumeroPeriodos().equals("1"))
        {
            fechaInicio1.setText(vacaciones.getFechaInicioPeriodo1());
            fechaFin1.setText(vacaciones.getFechaFinPeriodo1());
        }
        else
        if (vacaciones.getNumeroPeriodos().equals("2"))
        {
            fechaInicio1.setText(vacaciones.getFechaInicioPeriodo1());
            fechaFin1.setText(vacaciones.getFechaFinPeriodo1());

            fechaInicio2.setText(vacaciones.getFechaInicioPeriodo2());
            fechaFin2.setText(vacaciones.getFechaFinPeriodo2());
        }

        aceptar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                alert.cancel();
                modificarVacaciones(idEmpleado,"aceptadas", "Vacaciones aceptadas...");
            }
        });

        denegar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                alert.cancel();
                modificarVacaciones(idEmpleado,"denegadas", "Vacaciones rechazadas...");
            }
        });

        salir.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                alert.dismiss();
            }
        });

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
                            listadoTrabajador.remove(posicion);
                            listadoVacaciones.remove(posicion);
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

