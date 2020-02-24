package edu.dam.empresaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PrincipalActivity extends AppCompatActivity {

    //declaramos vistas
    TextView tvUser;

    //declaramos objeto Firebase y referencia BBDD
    FirebaseAuth mAuth;
    DatabaseReference dbTrabajadores;
    String nombre, formateado;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        //referenciamos vistas
        tvUser = findViewById(R.id.tvUser);


        //obtenemos la instancia de FirebaseAuth para pasarla como parámetro a la instancia de la BBDD y
        //obtener la referencia del "hijo" de "trabajadores" que esté logueado en ese momento
        mAuth = FirebaseAuth.getInstance();
        dbTrabajadores = FirebaseDatabase.getInstance().getReference().child("Trabajadores").child(mAuth.getUid());



        dbTrabajadores.addListenerForSingleValueEvent(new ValueEventListener() {
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
        });

    }
}
