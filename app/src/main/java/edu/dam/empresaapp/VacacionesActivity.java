package edu.dam.empresaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VacacionesActivity extends AppCompatActivity {

    //declaramos vistas
    TextView tvNombreTrabajador;
    CardView cardSolicVacac, cardConsultVacac, cardConsultEstado;

    String idTrabajador, estadoVacaciones, anio;

    Vacaciones vacaciones;

    DatabaseReference db;

    //Instanciamos un objeto de los fragments
    SolicitarVacacionesFragment svf = new SolicitarVacacionesFragment();
    ConsultarVacacionesFragment cvf = new ConsultarVacacionesFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacaciones);

        //referenciamos vistas
        tvNombreTrabajador = findViewById(R.id.tvNombreTrabajador_Vacaciones);
        cardSolicVacac     = findViewById(R.id.cardSolicVacac);
        cardConsultEstado  = findViewById(R.id.cardConsultEstado);
        cardConsultVacac   = findViewById(R.id.cardConsultVacac);

        // creamos la referencia a la BBDD
        db = FirebaseDatabase.getInstance().getReference();


        // creamos un objeto "Trabajador", cuyo contenido será el objeto
        // "Trabajador" que creamos en la Activity "PrincipalActivity"
        // y que pasamos a esta Activity mediante putExtra
        Trabajador trabajador = new Trabajador();
        trabajador = getIntent().getParcelableExtra("parametro");

        // guardamos el "id" del Trabajador para pasárselo al fragment
        idTrabajador = trabajador.getId();

        //Mostramos el nombre del trabajador en un TextView
        tvNombreTrabajador.setText(trabajador.getNombre() + " " + trabajador.getApellido1());

        // consultamos en la BBDD si la propuesta de vacaciones
        // tiene el estado "pendiente_confirmacion". Como tenemos
        // que consultar el año, lo obtenemos del sistema
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        Date date = new Date();
        anio = dateFormat.format(date);
        Log.d("año", anio);

        cardSolicVacac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // consultamos en la BBDD si la propuesta de vacaciones
                // tiene el estado "pendiente_confirmacion". Como tenemos
                // que consultar el año, lo obtenemos del sistema
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
                Date date = new Date();
                anio = dateFormat.format(date);
                Log.d("año", anio);

                db.child("Vacaciones").
                        addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                String id = dataSnapshot.child(idTrabajador).getKey();
                                String anios = dataSnapshot.child(idTrabajador).child(anio).getKey();
                                String periodos = dataSnapshot.child(idTrabajador).child(anio).child("numero_periodos").
                                        getValue().toString().trim();
                                String fechaInicioP1 = dataSnapshot.child(idTrabajador).child(anio).child("fecha_inicio_periodo1").
                                        getValue().toString().trim();
                                String fechaFinP1 = dataSnapshot.child(idTrabajador).child(anio).child("fecha_fin_periodo1").
                                        getValue().toString().trim();
                                String fechaInicioP2 = dataSnapshot.child(idTrabajador).child(anio).child("fecha_inicio_periodo2").
                                        getValue().toString().trim();
                                String fechaFinP2 = dataSnapshot.child(idTrabajador).child(anio).child("fecha_fin_periodo2").
                                        getValue().toString().trim();
                                String estadoVacaciones = dataSnapshot.child(idTrabajador).child(anio).child("estado_vacaciones").
                                        getValue().toString().trim();

                                //Creamos un objeto "Vacaciones"
                                vacaciones = new Vacaciones(
                                        id,
                                        anios,
                                        periodos,
                                        fechaInicioP1,
                                        fechaFinP1,
                                        fechaInicioP2,
                                        fechaFinP2,
                                        estadoVacaciones);

                                if (vacaciones.getEstadoVacaciones().equals("pendiente_confirmacion")) {

                                    Toast.makeText(VacacionesActivity.this,
                                            "Sus vacaciones están pendientes de aceptación",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    // mediante un Bundle le pasamos al fragment
                                    // el "id" del usuario
                                    Bundle bundle = new Bundle();
                                    bundle.putString("ID", idTrabajador);
                                    svf.setArguments(bundle);

                                    // al pulsar en el CardView de "solicitar vacaciones", iniciamos
                                    // la transición del Fragment, reemplazando el layout de la Activity
                                    // por el layout del Fragment "SolicitarVacacionesFragment"
                                    FragmentManager fm = getSupportFragmentManager();
                                    FragmentTransaction ft = fm.beginTransaction();
                                    ft.replace(R.id.contenedor, svf);
                                    ft.addToBackStack(null);
                                    ft.commit();
                                }
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

            }
        });

        cardConsultEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.child("Vacaciones").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Log.d("child", idTrabajador);
                        Log.d("child", anio);



                        String estado = dataSnapshot.child(idTrabajador).child(anio).child("estado_vacaciones").getValue().toString();

                        /*Toast.makeText(VacacionesActivity.this, estado,
                                Toast.LENGTH_SHORT).show();*/

                        AlertDialog.Builder ventana = new AlertDialog.Builder(VacacionesActivity.this);

                        ventana.setTitle("Mensaje");
                        if(estado.equals("pendiente_confirmacion")){
                        ventana.setMessage("Su solicitud se encuentra pendiente de confirmación");}

                        if(estado.equals("aceptadas")){
                            ventana.setMessage("Su solicitud ha sido aceptada");}

                        if(estado.equals("rechazadas")){
                            ventana.setMessage("Su solicitud ha sido rechazada");}


                        // solo mostramos el botón "Aceptar"
                        ventana.setPositiveButton("Continuar", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.cancel();
                            }
                        });

                        AlertDialog alert = ventana.create();
                        alert.show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    //método que al pulsar en el ImageView (flecha) de la cabecera
    //nos lleva a la pantalla anterior
    public void volver(View view) {
            Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
            startActivity(intent);
    }
}
