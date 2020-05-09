package edu.dam.empresaapp.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.dam.empresaapp.R;
import edu.dam.empresaapp.activitys.FichajesActivity;
import edu.dam.empresaapp.adaptadores.AdaptadorFichajesUsuario;
import edu.dam.empresaapp.pojos.Fichajes;
import edu.dam.empresaapp.pojos.Trabajador;

public class ListarFichajesUsuarioActivity extends AppCompatActivity {

    private TextView tvNombreTrabajadorListarFichajes;
    private String idTrabajador, id, fecha, horaEntrada, horaSalida, textoEntrada, textoSalida;
    private ListView lvFichajesUsuario;
    private AdaptadorFichajesUsuario adaptador;


    // referenciamos la BBDD
    DatabaseReference db;

    // declaramos un objeto "trabajador"
    private Trabajador trabajador;

    //declaramos un objeto "Fichajes"
    private Fichajes fichajes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_fichajes_usuario);

        //referenciamos vistas
        tvNombreTrabajadorListarFichajes = findViewById(R.id.tvNombreTrabajador_ListarFichajesUsuario);
        lvFichajesUsuario                = findViewById(R.id.lvFichajesUsuario);

        // creamos un objeto "Trabajador", cuyo contenido será el objeto
        // "Trabajador" que creamos en la Activity "PrincipalActivity"
        new Trabajador();
        trabajador = getIntent().getParcelableExtra("parametro");

        //Mostramos el nombre del trabajador en un TextView
        tvNombreTrabajadorListarFichajes.setText(trabajador.getNombre() + " " + trabajador.getApellido1());

        //obtenemos el id del trabajador
        idTrabajador = trabajador.getId();

        final ArrayList<Fichajes> list = new ArrayList<>();
        adaptador = new AdaptadorFichajesUsuario(this,list);
        lvFichajesUsuario.setAdapter(adaptador);

        // obtenemos la referencia de la BBDD
        db = FirebaseDatabase.getInstance().getReference();



        // limitamos la búsqueda a los últimos 30 días
         Query query = db.child("Fichajes").child(idTrabajador).limitToLast(30);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(!dataSnapshot.exists())
                        {
                            Toast.makeText(ListarFichajesUsuarioActivity.this,
                                    "No hay resultados que mostrar",
                                    Toast.LENGTH_SHORT).show();
                        }

                        for (DataSnapshot query : dataSnapshot.getChildren())
                        {
                            fecha = query.getKey();

                            horaEntrada = query.child("hora_entrada").getValue().toString();

                            // cuando el usuario lee el QR, se crean los nodos "hora_entrada" y
                            // "texto_entrada", pero los nodos "hora_salida" y "texto_salida" no van
                            // a existir hasta que el usuario vuelva a leer el QR por segunda vez
                            // el mismo día. Si el usuario lista los fichajes antes de la segunda
                            // lectura, los nodos "hora_salida y "texto_salida" no existirán y se
                            // producirá un nullpointerexception. Por eso creamos estructuras if en
                            // dichos nodos
                            if(!query.child("hora_salida").exists()){
                                horaSalida = "sin registrar";
                            } else {
                                horaSalida = query.child("hora_salida").getValue().toString();
                            }

                            textoEntrada = query.child("texto_entrada").getValue().toString();

                            if(!query.child("texto_salida").exists()){
                                textoSalida = "";
                            } else {
                                textoSalida = query.child("texto_salida").getValue().toString();
                            }


                            fichajes = new Fichajes(idTrabajador, fecha, horaEntrada, horaSalida, textoEntrada, textoSalida);

                            list.add(fichajes);

                        }
                        adaptador.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        /*db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot query : dataSnapshot.child("Fichajes").getChildren())
                {
                    id = query.getKey();
                }

                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot query : dataSnapshot.child("Fichajes").child(id).getChildren())
                        {
                            fecha = query.getKey();

                            horaEntrada = query.child("hora_entrada").getValue().toString();

                            // cuando el usuario lee el QR, se crean los nodos "hora_entrada" y
                            // texto_entrada, pero los nodos "hora_salida" y "texto_salida" no van
                            // a existir hasta que el usuario vuelva a leer el QR por segunda vez
                            // el mismo día. Si el usuario lista los fichajes antes de la segunda
                            // lectura, los nodos "hora_salida y "texto_salida" no existirán y se
                            // producirá un nullpointerexception. Por eso creamos estructuras if en
                            // dichos nodos
                            if(!query.child("hora_salida").exists()){
                                horaSalida = "sin registrar";
                            } else {
                                horaSalida = query.child("hora_salida").getValue().toString();
                            }

                            textoEntrada = query.child("texto_entrada").getValue().toString();

                            if(!query.child("texto_salida").exists()){
                                textoSalida = "";
                            } else {
                                textoSalida = query.child("texto_salida").getValue().toString();
                            }


                            fichajes = new Fichajes(id, fecha, horaEntrada, horaSalida, textoEntrada, textoSalida);

                            list.add(fichajes);

                            adaptador.notifyDataSetChanged();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

    }

    public void volver(View view) {
        Intent intent = new Intent(getApplicationContext(), FichajesActivity.class);
        intent.putExtra("parametro", trabajador); //pasamos el objeto trabajador
        startActivity(intent);
    }
}
