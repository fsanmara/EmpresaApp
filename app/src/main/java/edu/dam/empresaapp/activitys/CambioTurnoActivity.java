package edu.dam.empresaapp.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.dam.empresaapp.R;
import edu.dam.empresaapp.adaptadores.AdaptadorTrabajadores;
import edu.dam.empresaapp.pojos.Trabajador;

public class CambioTurnoActivity extends AppCompatActivity {

    private Spinner spTrabajadores;
    private AdaptadorTrabajadores adapterTrabajadores;
    private Button btnEnviarEmail;
    private TextView tvTrabajadorCambioTurno;
    private String anio, mes, dia, idTrabajador, nombreusuario;

    // declaramos un objeto "Trabajador"
    private Trabajador trabajador;

    // referenciamos la BBDD
    DatabaseReference db;

    // declaramos el arrayList
    ArrayList<Trabajador> listadoTrabajadores = new ArrayList<>();

    Context contexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambio_turno);

        // referenciamos vistas
        spTrabajadores          = findViewById(R.id.spTrabajadores);
        tvTrabajadorCambioTurno = findViewById(R.id.tvTrabajador_CambioTurno);
        btnEnviarEmail          = findViewById(R.id.btnEnviarEmail);

        // creamos un objeto "Trabajador"
        new Trabajador();
        trabajador = getIntent().getParcelableExtra("parametro");
        idTrabajador = trabajador.getId();

        //recuperamos las variables que enviamos a esta activity
        anio = getIntent().getStringExtra("ANIO");
        mes = getIntent().getStringExtra("MES");
        dia = getIntent().getStringExtra("DIA");


        //Mostramos el nombre del trabajador en un TextView
        nombreusuario = trabajador.getNombre() + " " + trabajador.getApellido1();
        tvTrabajadorCambioTurno.setText(nombreusuario);

        // referenciamos la BBDD
        db = FirebaseDatabase.getInstance().getReference();

        contexto = this;

         //consultamos la BBDD para llenar el Spinner de los empleados
        db.child("Trabajadores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot objeto : dataSnapshot.getChildren()){

                    final Trabajador trabajador = objeto.getValue(Trabajador.class);

                    listadoTrabajadores.add(trabajador);
                    adapterTrabajadores = new AdaptadorTrabajadores(contexto, listadoTrabajadores);
                    spTrabajadores.setAdapter(adapterTrabajadores);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        spTrabajadores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                final Trabajador trabajador = listadoTrabajadores.get(position);

                // guardamos variables que nos servirán a la hora
                // de enviar el email
                final String nombre    = trabajador.getNombre();
                final String apellido1 = trabajador.getApellido1();
                final String email     = trabajador.getEmail();

                //listener del botón "Enviar email"
                btnEnviarEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // mostramos un ventana de diálogo
                        AlertDialog.Builder ventana = new AlertDialog.Builder(CambioTurnoActivity.this);

                        ventana.setTitle("Mensaje");
                        ventana.setMessage("¿ Desea enviar un email a " +
                                nombre + " " + apellido1 + " ?");
                        ventana.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                String asunto = "Cambio de turno";
                                String cuerpo = "Hola, " + nombre + ".\n\n" +
                                                "Me han asignado turno el " +
                                                dia + " de " + mes + " de " + anio +
                                                ".\n\n" + "¿ Me lo puedes cambiar ? \n\n" +
                                                "Saludos \n\n" + nombreusuario;

                                String emailUrl = "mailto:" + email + "?subject=" + asunto + "&body=" + cuerpo;
                                Intent request = new Intent(Intent.ACTION_VIEW);
                                request.setData(Uri.parse(emailUrl));
                                try
                                {
                                    startActivity(request);
                                }
                                catch (android.content.ActivityNotFoundException ex)
                                {
                                    Toast.makeText(getApplicationContext(), "No hay clientes de email instalados...",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        ventana.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alert = ventana.create();
                        alert.show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    //método que al pulsar en el ImageView (flecha) de la cabecera
    // nos lleva a la pantalla anterior
    public void volver(View view) {
        Intent intent = new Intent(getApplicationContext(), TurnoTrabajadorActivity.class);
        intent.putExtra("parametro", trabajador); //pasamos el objeto trabajador
        startActivity(intent);
    }
}
