package edu.dam.empresaapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultarVacacionesFragment extends Fragment {

    public ConsultarVacacionesFragment() {
        // Required empty public constructor
    }

    // declaramos vistas
    private TextView txtPeriodos;

    private String idTrabajador, anio, periodos, fechaInicioP1, fechaFinP1, fechaInicioP2, fechaFinP2;

    private DatabaseReference db;

    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_consultar_vacaciones, container, false);

        // referenciamos vistas

        txtPeriodos     = view.findViewById(R.id.txtPeriodos);
        progressBar     = view.findViewById(R.id.progressbar);

        // creamos la referencia a la BBDD
        db = FirebaseDatabase.getInstance().getReference().child("Vacaciones");

        //obtenemos los datos del Bundle que le pasamos a este Fragment
        //con "getArguments"
        Bundle bundle = getArguments();

        idTrabajador = bundle.getString("ID");
        anio         = bundle.getString("ANIO");

        // consultamos a la BBDD para saber los periodos seleccionados
        // y la fecha de los mismos

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // mostramos una barra de progreso mientras se cargan los datos
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }

                // Si es la primera vez que el usuario entra a la Activity
                // y pulsa este botón antes de solicitar las vacaciones, la
                // aplicación devolvería un NullPointerException, ya que el
                // "id_trabajador" del usuario logueado aún no se habría
                //  creado en la colección "Vacaciones" de la BBDD. Así que
                // mediante un "if" comprobamos que el nodo exista en la BBDD
                if (dataSnapshot.child(idTrabajador).child(anio).exists()) {

                    // si existe
                    periodos = dataSnapshot.child(idTrabajador).child(anio)
                            .child("numero_periodos").getValue().toString();
                    fechaInicioP1 = dataSnapshot.child(idTrabajador).child(anio)
                            .child("fecha_inicio_periodo1").getValue().toString();
                    fechaFinP1 = dataSnapshot.child(idTrabajador).child(anio)
                            .child("fecha_fin_periodo1").getValue().toString();
                    fechaInicioP2 = dataSnapshot.child(idTrabajador).child(anio)
                            .child("fecha_inicio_periodo2").getValue().toString();
                    fechaFinP2 = dataSnapshot.child(idTrabajador).child(anio)
                            .child("fecha_fin_periodo2").getValue().toString();

                    if (periodos.equals("1")) {

                        txtPeriodos.setText("Ha solicitado un periodo de vacaciones: \n\n");
                        txtPeriodos.append("entre el " + fechaInicioP1 + " y el " + fechaFinP1);


                    } else {

                        txtPeriodos.setText("Ha solicitado dos periodos de vacaciones: \n\n");
                        txtPeriodos.append("entre el " + fechaInicioP1 + " y el " + fechaFinP1 + "\n\n");
                        txtPeriodos.append(" y del " + fechaInicioP2 + " al " + fechaFinP2);
                    }

                } else { // si el nodo no existe en la BBDD

                    AlertDialog.Builder ventana = new AlertDialog.Builder(getContext());
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

        return view;
    }
}
