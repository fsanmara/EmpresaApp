package edu.dam.empresaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FichajesActivity extends AppCompatActivity {

    private TextView tvNombreTrabajadorFichajes;
    private CardView cardLeerQR ,cardListarFichajes;

    // declaramos un objeto "trabajador"
    private Trabajador trabajador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fichajes);

        //referenciamos vistas
        tvNombreTrabajadorFichajes = findViewById(R.id.tvNombreTrabajador_Fichajes);
        cardLeerQR = findViewById(R.id.cardLeerQR);
        cardListarFichajes = findViewById(R.id.cardListarFichajes);

        // creamos un objeto "Trabajador", cuyo contenido será el objeto
        // "Trabajador" que creamos en la Activity "PrincipalActivity"
        // y que pasamos a esta Activity mediante putExtra
        new Trabajador();
        trabajador = getIntent().getParcelableExtra("parametro");

        //Mostramos el nombre del trabajador en un TextView
        tvNombreTrabajadorFichajes.setText(trabajador.getNombre() + " " + trabajador.getApellido1());

        // botón que lee el código QR
        cardLeerQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), LeerQRActivity.class);
                intent.putExtra("parametro", trabajador); //pasamos el objeto trabajador
                startActivity(intent);

            }
        });

        // botón que muestra los últimos registros de entrada y salida del trabajador
        cardListarFichajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ListarFichajesUsuarioActivity.class);
                intent.putExtra("parametro", trabajador); //pasamos el objeto trabajador
                startActivity(intent);

            }
        });

    }

    public void volver(View view) {
        Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
        intent.putExtra("parametro", trabajador); //pasamos el objeto trabajador
        startActivity(intent);
    }

}
