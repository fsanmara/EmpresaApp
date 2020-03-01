package edu.dam.empresaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
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

public class VacacionesActivity extends AppCompatActivity {

    //declaramos vistas
    TextView tvNombreTrabajador;
    CardView cardSolicVacac, cardConsultVacac, cardConsultEstado;

    //Instanciamos un objeto de los fragments
    SolicitarVacacionesFragment svf = new SolicitarVacacionesFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacaciones);

        //referenciamos vistas
        tvNombreTrabajador = findViewById(R.id.tvNombreTrabajador_Vacaciones);
        cardSolicVacac     = findViewById(R.id.cardSolicVacac);
        cardConsultEstado  = findViewById(R.id.cardConsultEstado);
        cardConsultVacac   = findViewById(R.id.cardConsultVacac);


        // creamos un objeto "Trabajador", cuyo contenido será el objeto
        // "Trabajador" que creamos en la Activity "PrincipalActivity"
        // y que pasamos a esta Activity mediante putExtra
        Trabajador trabajador = new Trabajador();
        trabajador = getIntent().getParcelableExtra("parametro");

        //Mostramos el nombre del trabajador en un TextView
        tvNombreTrabajador.setText(trabajador.getNombre() + " " + trabajador.getApellido1());

        cardSolicVacac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // al pulsar en el CardView de "solicitar vacaciones", iniciamos
                // la transición del Fragment, reemplazando el layout de la Activity
                // por el layout del Fragment "SolicitarVacacionesFragment"
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.contenedor, svf);
                ft.addToBackStack(null);
                ft.commit();
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
