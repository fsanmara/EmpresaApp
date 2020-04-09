package edu.dam.empresaapp.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.dam.empresaapp.R;
import edu.dam.empresaapp.pojos.Empresa;
import edu.dam.empresaapp.pojos.Trabajador;

public class InfoActivity extends AppCompatActivity {

    private TextView tvNombreTrabajador_Info, tvNombreEmpresa, tvDireccion, tvEmail, tvTelefono;

    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //referenciamos vistas
        tvNombreTrabajador_Info = findViewById(R.id.tvNombreTrabajador_Info);
        tvNombreEmpresa = findViewById(R.id.tvNombreEmpresa);
        tvDireccion = findViewById(R.id.tvDireccion);
        tvEmail = findViewById(R.id.tvEmail);
        tvTelefono = findViewById(R.id.tvTelefono);

        // creamos la referencia a la BBDD
        db = FirebaseDatabase.getInstance().getReference().child("Empresa");


        // creamos un objeto "Trabajador", cuyo contenido será el objeto
        // "Trabajador" que creamos en la Activity "PrincipalActivity"
        // y que pasamos a esta Activity mediante putExtra
        Trabajador trabajador = new Trabajador();
        trabajador = getIntent().getParcelableExtra("parametro");


        //Mostramos el nombre del trabajador en un TextView
        tvNombreTrabajador_Info.setText(trabajador.getNombre() + " " + trabajador.getApellido1());

        //consultamos la BBDD para llenar el Spinner de los empleados
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                final Empresa empresa = dataSnapshot.getValue(Empresa.class);

                tvNombreEmpresa.setText(empresa.getNombre());
                tvDireccion.setText(empresa.getDireccion() + ", " + empresa.getMunicipio());
                tvEmail.setText(empresa.getEmail());
                tvTelefono.setText(empresa.getTelefono());

                // establecemos un listener al textView que muestra el
                // email. Si el usuario pincha en él, se abrirá la
                // aplicación del teléfono para enviar emails
                tvEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String emailUrl = "mailto:" + empresa.getEmail();
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

                // establecemos un listener al textView que muestra el
                // teléfono. Si el usuario pincha en él, se abrirá la
                // aplicación del teléfono para realizar la llamada
                tvTelefono.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL,
                                Uri.fromParts("tel",
                                        empresa.getTelefono(),
                                        null));
                        startActivity(intent);
                        }
                    });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
