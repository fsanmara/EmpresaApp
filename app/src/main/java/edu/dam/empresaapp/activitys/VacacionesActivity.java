package edu.dam.empresaapp.activitys;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.dam.empresaapp.fragments.ConsultarVacacionesFragment;
import edu.dam.empresaapp.R;
import edu.dam.empresaapp.fragments.SolicitarVacacionesFragment;
import edu.dam.empresaapp.pojos.Trabajador;
import edu.dam.empresaapp.pojos.Vacaciones;

public class VacacionesActivity extends AppCompatActivity {

    //declaramos vistas
    private TextView tvNombreTrabajador;
    private CardView cardSolicVacac, cardConsultVacac, cardConsultEstado;

    private String idTrabajador, anio;

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
        db = FirebaseDatabase.getInstance().getReference().child("Vacaciones");


        // creamos un objeto "Trabajador", cuyo contenido será el objeto
        // "Trabajador" que creamos en la Activity "PrincipalActivity"
        // y que pasamos a esta Activity mediante putExtra
        Trabajador trabajador = new Trabajador();
        trabajador = getIntent().getParcelableExtra("parametro");

        // guardamos el "id" del Trabajador para pasárselo al fragment
        idTrabajador = trabajador.getId();

        //Mostramos el nombre del trabajador en un TextView
        tvNombreTrabajador.setText(trabajador.getNombre() + " " + trabajador.getApellido1());

        // Como tenemos que consultar el año, lo obtenemos del sistema.
        // El trabajador consultará el estado de sus vacaciones en el
        // año natural
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        Date date = new Date();
        anio = dateFormat.format(date);
        Log.d("año", anio);

        // listener del botón "Solicitar Vacaciones"
        cardSolicVacac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //consultamos en la BBDD si la propuesta de vacaciones
                // del trabajador logueado tiene el estado "pendiente_confirmacion" o
                // el estado "aceptadas". En estos dos casos no se permitirá que el
                // usuario solicite vacaciones
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.child(idTrabajador).child(anio).exists())
                                { // si el nodo existe
                                    String estadoVacaciones = dataSnapshot.child(idTrabajador).
                                            child(anio).child("estado_vacaciones").getValue().toString();

                                    if (estadoVacaciones.equals("pendiente_confirmacion"))
                                    {

                                        Toast.makeText(VacacionesActivity.this,
                                                "Sus vacaciones están pendientes de aceptación",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                    else if (estadoVacaciones.equals("aceptadas"))
                                    {

                                        Toast.makeText(VacacionesActivity.this,
                                                "Sus vacaciones están aceptadas",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    else if(estadoVacaciones.equals("denegadas"))
                                    {
                                        // al tener las vacaciones denegadas, puede
                                        // solicitar de nuevo un periodo vacacional.
                                        // Mediante un Bundle le pasamos al fragment
                                        // "SolicitarVacacionesFragment" el "id" del usuario
                                        Bundle bundle = new Bundle();
                                        bundle.putString("ID", idTrabajador);
                                        svf.setArguments(bundle);

                                        // iniciamos la transición del Fragment, reemplazando el
                                        // layout de la Activity por el layout del Fragment
                                        // "SolicitarVacacionesFragment"
                                        FragmentManager fm = getSupportFragmentManager();
                                        FragmentTransaction ft = fm.beginTransaction();
                                        ft.replace(R.id.contenedor, svf);
                                        ft.addToBackStack(null);
                                        ft.commit();
                                    }

                                }
                                else
                                    { // si no existe el nodo y
                                    // en el caso de que las vacaciones no estén
                                    // ni aceptadas ni pendientes de aceptación,
                                    // mediante un Bundle le pasamos al fragment
                                    // "SolicitarVacacionesFragment" el "id" del usuario
                                    Bundle bundle = new Bundle();
                                    bundle.putString("ID", idTrabajador);
                                    svf.setArguments(bundle);

                                    // iniciamos la transición del Fragment, reemplazando el
                                    // layout de la Activity por el layout del Fragment
                                    // "SolicitarVacacionesFragment"
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

        // listener del botón "Consultar Solicitud"
        cardConsultEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        // Si es la primera vez que el usuario entra a esta Activity
                        // y pulsa este botón antes de solicitar las vacaciones, la
                        // aplicación devolvería un NullPointerException, ya que el
                        // "id_trabajador" del usuario logueado aún no se habría
                        //  creado en la colección "Vacaciones" de la BBDD. Así que
                        // mediante un "if" comprobamos que el nodo exista en la BBDD
                        if (dataSnapshot.child(idTrabajador).child(anio).exists())
                        {

                            // consultamos la BBDD para ver el estado de la solicitud
                            String estadoVacaciones = dataSnapshot.child(idTrabajador).child(anio).
                                    child("estado_vacaciones").getValue().toString();

                            // mostramos un ventana de diálogo
                            AlertDialog.Builder ventana = new AlertDialog.Builder(VacacionesActivity.this);

                            ventana.setTitle("Mensaje");
                            if (estadoVacaciones.equals("pendiente_confirmacion"))
                            {
                                ventana.setMessage("Su solicitud se encuentra pendiente de confirmación");
                            }

                            if (estadoVacaciones.equals("aceptadas"))
                            {
                                ventana.setMessage("Su solicitud ha sido aceptada");
                            }

                            if (estadoVacaciones.equals("denegadas"))
                            {
                                ventana.setMessage("Su solicitud ha sido denegada");
                            }

                            if ((estadoVacaciones.equals("")))
                            {
                                ventana.setMessage("No ha solicitado vacaciones para el año en curso");
                            }

                            ventana.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            AlertDialog alert = ventana.create();
                            alert.show();

                        } else { // si el nodo no existe en la BBDD

                            AlertDialog.Builder ventana = new AlertDialog.Builder(VacacionesActivity.this);
                            ventana.setTitle("Mensaje");
                            ventana.setMessage("No ha solicitado vacaciones para el año en curso");
                            ventana.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            AlertDialog alert = ventana.create();
                            alert.show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        // listener del botón "Consultar Vacaciones"
        cardConsultVacac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Mediante un Bundle le pasamos al fragment
                // "ConsultarVacacionesFragment" el "id" del usuario
                // y el "año"
                Bundle bundle = new Bundle();
                bundle.putString("ID", idTrabajador);
                bundle.putString("ANIO", anio);
                cvf.setArguments(bundle);

                // iniciamos la transición del Fragment, reemplazando el
                // layout de la Activity por el layout del Fragment
                // "ConsultarVacacionesFragment"
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.contenedor, cvf);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

    }

    //método que al pulsar en el ImageView (flecha) de la cabecera
    // y el cardview de volver nos lleva a la pantalla anterior
    public void volver(View view) {
            Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
            startActivity(intent);
    }
}
