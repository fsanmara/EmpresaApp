package edu.dam.empresaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PrincipalActivity extends AppCompatActivity {

    private String nombre, apellido1, apellido2, nif, email, telefono, id, formateado;
    private Boolean esResponsable;
    private Trabajador trabajador;

    private ProgressBar progressBar;

    //declaramos vistas
    private TextView tvNombreTrabajador;
    private Button btnVacaciones, btnTurnos, btnFichajes, btnInfo, btnAdministrar;


    //declaramos objeto Firebase y referencia BBDD
    FirebaseAuth mAuth;
    DatabaseReference dbTrabajadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        //referenciamos vistas
        tvNombreTrabajador = findViewById(R.id.tvNombreTrabajador);
        btnVacaciones      = findViewById(R.id.btnVacaciones);
        btnTurnos          = findViewById(R.id.btnTurnos);
        btnFichajes        = findViewById(R.id.btnFichajes);
        btnInfo            = findViewById(R.id.btnInfo);
        btnAdministrar     = findViewById(R.id.btnAdministrar);
        progressBar        = findViewById(R.id.progressbar);

        //obtenemos la instancia de FirebaseAuth para pasarla como parámetro a la instancia de la BBDD y
        //obtener la referencia del "hijo" de "trabajadores" que esté logueado en ese momento
        mAuth          = FirebaseAuth.getInstance();
        dbTrabajadores = FirebaseDatabase.getInstance().getReference().child("Trabajadores").child(mAuth.getUid());


        // Consultamos la BBDD. Con el resultado de la consulta
        // instanciamos un objeto de la clase Trabajador que podremos pasar a otras activitys.
        // Además, mostraremos en un TextView el nombre del usuario que ha iniciado sesión
        dbTrabajadores.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                // mostramos una barra de progreso mientras se cargan los datos
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
                //new Thread(new Runnable() {
                    //@Override
                    //public void run() {

                        nombre        = dataSnapshot.child("nombre").getValue().toString();
                        apellido1     = dataSnapshot.child("apellido1").getValue().toString();
                        apellido2     = dataSnapshot.child("apellido2").getValue().toString();
                        nif           = dataSnapshot.child("nif").getValue().toString();
                        email         = dataSnapshot.child("email").getValue().toString();
                        telefono      = dataSnapshot.child("telefono").getValue().toString();
                        esResponsable = (Boolean) dataSnapshot.child("esResponsable").getValue();
                        id            = dataSnapshot.getKey();
                        
                        //formateado = getString(R.string.Hola, nombre);
                        formateado = getString(R.string.NombreUsuario, nombre, apellido1);

                        trabajador = new Trabajador(
                                id,
                                email,
                                nif,
                                nombre,
                                apellido1,
                                apellido2,
                                telefono,
                                esResponsable);

                        //runOnUiThread(new Runnable() {
                         //   @Override
                         //   public void run() {


                                tvNombreTrabajador.setText(formateado);


                // Si el trabajador es, además, el responsable
                // hacemos visible el botón "Administrar" que
                // le permitirá gestionar sus tareas, como la
                // asignación de turnos y la gestión de las vacaciones
                if(trabajador.getEsResponsable()){

                            btnAdministrar.setVisibility(View.VISIBLE);}
                           // }
                        //});

                    //}
                //}).start();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // Listener del botón "Vacaciones" que nos llevará a la Activity
        // "VacacionesActivity" y en la que pasaremos el objeto Trabajador.
        // Usamos una estructura "if" porque como estamos pasando el objeto
        // "trabajador" mediante "putExtra", si pulsamos el botón antes de
        // hacerse la consulta y de instanciarse el objeto "trabajador",
        // nos devolvería un "nullpointerexception", porque el objeto aún no
        // existiría. Si el objeto aún no existe porque la consulta a Firebase
        // se demora y no se instancia el objeto, ponemos un Toast, y si el
        // objeto ya existe, al pulsar el botón nos lleva a la Activity
        // "VacacionesActivity". Pondremos también el "if" en el resto de botones.
        btnVacaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(trabajador == null){

                    Toast.makeText(PrincipalActivity.this,
                            "Cargando datos...", Toast.LENGTH_SHORT).show();

                } else {

                    Intent intent = new Intent(getApplicationContext(), VacacionesActivity.class);
                    intent.putExtra("parametro", trabajador);
                    startActivity(intent);
                }
            }
        });

        // listener del botón "Turnos"
        btnTurnos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // listener del botón "Fichajes"
        btnFichajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // listener del botón "info" de la empresa
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // listener del botón del responsable
            btnAdministrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), AdministrarActivity.class);
                intent.putExtra("parametro", trabajador); //pasamos el objeto trabajador
                startActivity(intent);
                }
            });



    }
}
