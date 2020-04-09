package edu.dam.empresaapp.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.dam.empresaapp.R;
import edu.dam.empresaapp.pojos.Trabajador;

public class InfoActivity extends AppCompatActivity {

    private TextView tvNombreTrabajador_Info;

    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //referenciamos vistas
        tvNombreTrabajador_Info = findViewById(R.id.tvNombreTrabajador_Info);

        // creamos la referencia a la BBDD
        db = FirebaseDatabase.getInstance().getReference().child("Vacaciones");


        // creamos un objeto "Trabajador", cuyo contenido será el objeto
        // "Trabajador" que creamos en la Activity "PrincipalActivity"
        // y que pasamos a esta Activity mediante putExtra
        Trabajador trabajador = new Trabajador();
        trabajador = getIntent().getParcelableExtra("parametro");

        //Mostramos el nombre del trabajador en un TextView
        tvNombreTrabajador_Info.setText(trabajador.getNombre() + " " + trabajador.getApellido1());


    }

    //método que al pulsar en el ImageView (flecha) de la cabecera
    // y el cardview de volver nos lleva a la pantalla anterior
    public void volver(View view) {
        Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
        startActivity(intent);
    }
}
