package edu.dam.empresaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PrincipalActivity extends AppCompatActivity {

    String nombre, apellido1, apellido2, nif, email, telefono, id, formateado;
    Boolean esResponsable;
    Trabajador trabajador;

    //declaramos vistas
    TextView tvNombreTrabajador;
    Button btnVacaciones, btnTurnos, btnFichajes;


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


        //obtenemos la instancia de FirebaseAuth para pasarla como parámetro a la instancia de la BBDD y
        //obtener la referencia del "hijo" de "trabajadores" que esté logueado en ese momento
        mAuth          = FirebaseAuth.getInstance();
        dbTrabajadores = FirebaseDatabase.getInstance().getReference().child("Trabajadores").child(mAuth.getUid());


        // consultamos la BBDD y lo hacemos en un hilo. Con el resultado de la consulta
        // instanciamos un objeto de la clase Trabajador que podremos pasar a otras activitys.
        // Además, mostraremos en un TextView el nombre del usuario que ha iniciado sesión
        dbTrabajadores.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

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

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvNombreTrabajador.setText(formateado);
                            }
                        });

                    }
                }).start();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*dbTrabajadores.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String nombre = dataSnapshot.child("nombre").getValue().toString();
                //String apellido1 = dataSnapshot.child("apellido1").getValue().toString();
                //String apellido2 = dataSnapshot.child("apellido2").getValue().toString();
                @SuppressLint("StringFormatMatches") String formateado = getString(R.string.Hola, nombre);

                tvUser.setText(formateado);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        // listener del botón Vacaciones que nos llevará a la Activity
        // VacacionesActivity y en la que pasaremos
        // el objeto Trabajador
        btnVacaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), VacacionesActivity.class);
                intent.putExtra("parametro", trabajador);
                startActivity(intent);
            }
        });

    }
}
