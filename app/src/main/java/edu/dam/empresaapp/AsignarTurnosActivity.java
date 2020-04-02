package edu.dam.empresaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import edu.dam.empresaapp.adaptadores.AdaptadorTrabajadores;

import static java.security.AccessController.getContext;

public class AsignarTurnosActivity extends AppCompatActivity {

    private TextView tvNombreTrabajadorAsignarTurnos, tvFechaTurno;
    private Button btnAsignarTurnos;
    private Spinner spTrabajadores;
    private AdaptadorTrabajadores adapterTrabajadores;
    private DatePickerDialog datePickerDialog;
    private int year, month, dayOfMonth;
    private Calendar calendar;
    private String fechaTurno, anio, mes, dia, mesLetra;

    // declaramos un objeto "Trabajador"
    private Trabajador mJimenez;

    // referenciamos la BBDD
    DatabaseReference db;

    // declaramos el arrayList
    ArrayList<Trabajador> listadoTrabajadores = new ArrayList<>();

    Context contexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar_turnos);

        // referenciamos vistas
        tvNombreTrabajadorAsignarTurnos = findViewById(R.id.tvNombreTrabajador_AsignarTurnos);
        spTrabajadores = findViewById(R.id.spTrabajadores);
        tvFechaTurno = findViewById(R.id.tvFechaTurno);
        btnAsignarTurnos = findViewById(R.id.btnAsignarTurnos);

        // creamos un objeto "Trabajador"
        mJimenez = new Trabajador();
        mJimenez = getIntent().getParcelableExtra("parametro");

        //Mostramos el nombre del trabajador en un TextView
        tvNombreTrabajadorAsignarTurnos.setText(mJimenez.getNombre() + " " + mJimenez.getApellido1());

        // referenciamos la BBDD
        db = FirebaseDatabase.getInstance().getReference();

        contexto = this;

        //consultamos la BBDD
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

        // evento click del Spinner
        spTrabajadores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                final Trabajador trabajador = listadoTrabajadores.get(position);
                final String idTrabajador = trabajador.getId();
                final String nombreTrabajador = trabajador.getNombre() + " " + trabajador.getApellido1() + " " + trabajador.getApellido2();

                tvFechaTurno.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        calendar = Calendar.getInstance();
                        year = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.MONTH);
                        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                        datePickerDialog = new DatePickerDialog(Objects.requireNonNull(AsignarTurnosActivity.this),
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                                        fechaTurno = day + "/" + (month + 1) + "/" + year;
                                        anio = String.valueOf(year);
                                        mes = String.valueOf(month+1);
                                        dia = String.valueOf(day);
                                        tvFechaTurno.setText(fechaTurno);
                                        nombreMes(month+1);
                                    }
                                }, year, month, dayOfMonth);

                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

                        datePickerDialog.show();

                    }
                });

                btnAsignarTurnos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final List<String> lista = new ArrayList<>();

                        // verificamos que el textView no esté vacío
                        if(TextUtils.isEmpty(fechaTurno))
                        {
                            Toast.makeText(AsignarTurnosActivity.this,
                                    "Seleccione una fecha para el turno", Toast.LENGTH_SHORT).show();
                        }
                        else
                            {
                                //añadimos los datos a la BBDD
                                db.child("Turnos").child(idTrabajador).child(mesLetra)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                                        int i = 1;

                                        for (DataSnapshot objeto : dataSnapshot.getChildren())
                                        {
                                            lista.add(objeto.getValue().toString());
                                            Toast.makeText(AsignarTurnosActivity.this,objeto.getValue().toString(), Toast.LENGTH_SHORT).show();
                                        }

                                        Boolean estado;
                                        estado = buscarFecha(lista, dia);

                                        if(!estado)
                                        {
                                            lista.add(dia);
                                            //grabar(lista, anio, mesLetra);

                                            db.child("Turnos").child(idTrabajador).child(anio)
                                                    .child(mesLetra).setValue(lista);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                        // mostramos un ventana de diálogo informativa
                        AlertDialog.Builder ventana = new AlertDialog.Builder(AsignarTurnosActivity.this);

                        ventana.setTitle("Mensaje");
                        ventana.setMessage("Turno asignado correctamente a " +  nombreTrabajador +
                                        " para el " + dia + " de " + mesLetra + " de " + anio);

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
        Intent intent = new Intent(getApplicationContext(), AdministrarTurnosActivity.class);
        intent.putExtra("parametro", mJimenez); //pasamos el objeto mJimenez
        startActivity(intent);
    }

    // método que nos permite pasar el mes de int a letras
    public void nombreMes(int mes){

        switch (mes) {
            case 1:  mesLetra = "enero";
                break;
            case 2:  mesLetra  = "febrero";
                break;
            case 3:  mesLetra = "marzo";
                break;
            case 4:  mesLetra = "abril";
                break;
            case 5:  mesLetra = "mayo";
                break;
            case 6:  mesLetra = "junio";
                break;
            case 7:  mesLetra = "julio";
                break;
            case 8:  mesLetra = "agosto";
                break;
            case 9:  mesLetra = "septiembre";
                break;
            case 10: mesLetra = "octubre";
                break;
            case 11: mesLetra = "noviembre";
                break;
            case 12: mesLetra = "diciembre";
                break;
            default: mesLetra = "mes no válido";
                break;
        }

    }

    //método para comprobar si el día ya existe en el ArrayList
    public Boolean buscarFecha(List<String> lista, String dia)
    {
        Boolean estado = false;
        int indice = 0;

        while (indice < lista.size() && (!estado))
        {
            if (dia.equals(lista.get(indice)))
            {
                if (dia.equals(lista.get(indice)))
                    estado = true;
                else
                    indice++;
            }
        }

        return estado;
    }
}
