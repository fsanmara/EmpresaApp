package edu.dam.empresaapp;

import androidx.annotation.NonNull;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import edu.dam.empresaapp.adaptadores.AdaptadorListVacaciones;

public class ListarSolicitudesVacacionesActivity extends AppCompatActivity {

    private TextView tvNombreTrabajador;
    private ListView lvTrabajadores;
    private String anio;
    Context contexto;
    private int posicion; // contiene la posición del ítem pulsado del listview

    // declaramos un objeto "trabajador"
    private Trabajador mJimenez;
    private Trabajador trabajador;

    // declaramos los arraylist
    final ArrayList<Trabajador> listTrabajador = new ArrayList<>();
    final ArrayList<Vacaciones> listVacaciones = new ArrayList<>();

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
                listTrabajador.clear();
                listVacaciones.clear();

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
                                    if (vacaciones.getEstadoVacaciones().equals("aceptadas"))
                                    {
                                        listTrabajador.add(trabajador);
                                        listVacaciones.add(vacaciones);
                                    }
                                }
                            }

                            // iniciamos el adaptador para mostrar la información en el ListView
                            adaptador = new AdaptadorListVacaciones(contexto, listTrabajador);
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

        lvTrabajadores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                final String idEmpleado;

                TextView nombreEmpleado, fechaInicio1, fechaFin1, fechaInicio2, fechaFin2, tvNA;
                Button aceptar;

                posicion = i;

                Trabajador trabajador = listTrabajador.get(i);
                Vacaciones vacaciones = listVacaciones.get(i);

                // creamos la ventana de diálogo
                AlertDialog.Builder ventana = new AlertDialog.Builder(contexto);
                LayoutInflater inflater = (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View vista = inflater.inflate(R.layout.alert_dialog_listar_sol, null);

                ventana.setView(vista);
                final AlertDialog alert = ventana.create();

                nombreEmpleado = vista.findViewById(R.id.nombreEmpleado);
                fechaInicio1   = vista.findViewById(R.id.periodo1Inicio);
                fechaFin1      = vista.findViewById(R.id.periodo1Fin);
                fechaInicio2   = vista.findViewById(R.id.periodo2Inicio);
                fechaFin2      = vista.findViewById(R.id.periodo2Fin);
                tvNA           = vista.findViewById(R.id.tvNA);
                aceptar        = vista.findViewById(R.id.botonAceptar);


                nombreEmpleado.setText(trabajador.getNombre() + " " + trabajador.getApellido1() + " " + trabajador.getApellido2());

                if (vacaciones.getNumeroPeriodos().equals("1"))
                {
                    fechaInicio1.setText(vacaciones.getFechaInicioPeriodo1());
                    fechaFin1.setText(vacaciones.getFechaFinPeriodo1());
                    tvNA.setVisibility(View.VISIBLE);
                    tvNA.setText("No solicitado");
                }
                else
                if (vacaciones.getNumeroPeriodos().equals("2"))
                {
                    fechaInicio1.setText(vacaciones.getFechaInicioPeriodo1());
                    fechaFin1.setText(vacaciones.getFechaFinPeriodo1());

                    fechaInicio2.setText(vacaciones.getFechaInicioPeriodo2());
                    fechaFin2.setText(vacaciones.getFechaFinPeriodo2());
                    tvNA.setVisibility(View.INVISIBLE);
                }

                aceptar.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        alert.cancel();
                    }
                });

                alert.show();

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
