package edu.dam.empresaapp.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import edu.dam.empresaapp.R;
import edu.dam.empresaapp.adaptadores.AdaptadorDias;
import edu.dam.empresaapp.adaptadores.AdaptadorTrabajadores;
import edu.dam.empresaapp.pojos.Trabajador;
import edu.dam.empresaapp.pojos.Turnos;

public class VerTurnosActivity extends AppCompatActivity {

    private TextView tvNombreTrabajadorVerTurnos, tvAnio, tvMes;
    private Spinner spTrabajadores;
    private AdaptadorTrabajadores adapterTrabajadores;
    private AdaptadorDias adapterDia;
    private ListView lvDias;
    private String anio, mes, dia, mesLetra, idTrabajador;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private Button btnVerDias;

    // declaramos un objeto "Trabajador"
    private Trabajador mJimenez;

    // referenciamos la BBDD
    DatabaseReference db;

    // declaramos el arrayList
    ArrayList<Trabajador> listadoTrabajadores = new ArrayList<>();
    ArrayList<Turnos> listadoTurnos = new ArrayList<>();

    Context contexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_turnos);

        // referenciamos vistas
        tvNombreTrabajadorVerTurnos = findViewById(R.id.tvTrabajador_VerTurnos);
        spTrabajadores              = findViewById(R.id.spTrabajadores);
        lvDias                      = findViewById(R.id.lvDias);
        tvAnio                      = findViewById(R.id.tvAnio);
        tvMes                       = findViewById(R.id.tvMes);
        btnVerDias                  = findViewById(R.id.btnVerDias);

        // creamos un objeto "Trabajador"
        mJimenez = new Trabajador();
        mJimenez = getIntent().getParcelableExtra("parametro");

        //Mostramos el nombre del trabajador en un TextView
        tvNombreTrabajadorVerTurnos.setText(mJimenez.getNombre() + " " + mJimenez.getApellido1());

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


        // evento click del Spinner
        spTrabajadores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final Trabajador trabajador = listadoTrabajadores.get(position);

                idTrabajador = trabajador.getId();

                listadoTurnos.clear();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Calendar calendar = Calendar.getInstance();
        int yearNow = calendar.get(Calendar.YEAR);
        int monthNow = calendar.get(Calendar.MONTH);
        int dateNow = calendar.get(Calendar.DAY_OF_MONTH);

        Locale spanish = new Locale("es");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd//mm/yyyy", spanish);

        DatePickerDialog datePickerDialog = new DatePickerDialog(VerTurnosActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            }
        }, yearNow, monthNow, dateNow);

        final Calendar today = Calendar.getInstance();

        tvAnio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(VerTurnosActivity.this,
                        new MonthPickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(int selectedMonth, int selectedYear) {

                                anio = String.valueOf(selectedYear);
                                tvAnio.setText(anio);
                            }
                        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

                builder.setActivatedYear(2020)
                        .setMaxYear(2030)
                        .setTitle("Seleccione el año")
                        .showYearOnly()
                        .build()
                        .show();
            }

        });

        tvMes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(VerTurnosActivity.this,
                        new MonthPickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(int selectedMonth, int selectedYear) {

                                nombreMes(selectedMonth+1);
                                tvMes.setText(mesLetra);
                            }
                        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

                builder.setTitle("Seleccione el mes")
                        .showMonthOnly()
                        .build()
                        .show();
            }
        });

        btnVerDias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //verificamos que el usuario haya rellenado
                //los campos de mes y año
                if(TextUtils.isEmpty(anio))
                {
                    Toast.makeText(VerTurnosActivity.this, "Introduzca el año",
                            Toast.LENGTH_SHORT).show();
                }

                else if(TextUtils.isEmpty(mesLetra))
                {
                    Toast.makeText(VerTurnosActivity.this, "Introduzca el mes",
                            Toast.LENGTH_SHORT).show();
                }
               else
                {
                lvDias.setAdapter(null);
                listadoTurnos.clear();

                db.child("Turnos").child(idTrabajador).child(anio).child(mesLetra).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists())
                        {
                            Toast.makeText(VerTurnosActivity.this, "Datos no encontrados para el periodo solicitado", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            for(final DataSnapshot query : dataSnapshot.getChildren())
                            {
                                dia = query.getValue().toString();
                                final Turnos turnos = new Turnos(idTrabajador, anio, mesLetra, dia);
                                listadoTurnos.add(turnos);
                            }

                            adapterDia = new AdaptadorDias(contexto, listadoTurnos);

                            // ordenamos los días antes de mostrarlos en el ListView
                            Collections.sort(listadoTurnos, new Comparator<Turnos>() {
                                @Override
                                public int compare(Turnos t1, Turnos t2) {
                                    return new Integer(t1.getDiaTurno()).compareTo(new Integer(t2.getDiaTurno()));
                                }
                            });
                            lvDias.setAdapter(adapterDia);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                lvDias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        final Turnos turnos = listadoTurnos.get(position);
                        final String dia = turnos.getDiaTurno();

                        Button btnSi, btnNo;
                        // creamos la ventana de diálogo
                        AlertDialog.Builder ventana = new AlertDialog.Builder(contexto);
                        LayoutInflater inflater = (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        View vista = inflater.inflate(R.layout.alert_dialog_borrar_dia, null);

                        ventana.setView(vista);
                        final AlertDialog alert = ventana.create();
                        alert.show();

                        btnSi = vista.findViewById(R.id.btnSi);
                        btnNo = vista.findViewById(R.id.btnNo);

                        btnNo.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                alert.dismiss();
                            }
                        });

                        btnSi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                final List<String> lista = new ArrayList<>();

                                db.child("Turnos").child(idTrabajador).child(anio).child(mesLetra)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                for (DataSnapshot objeto : dataSnapshot.getChildren())
                                                {
                                                    lista.add(objeto.getValue().toString());

                                                }

                                                Boolean estado;
                                                estado = buscarFecha(lista, dia);

                                                if(estado)
                                                {
                                                    lista.remove(dia);

                                                    db.child("Turnos").child(idTrabajador).child(anio)
                                                            .child(mesLetra).setValue(lista);

                                                    listadoTurnos.clear();
                                                    //adapterDia.notifyDataSetChanged();

                                                    Toast.makeText(VerTurnosActivity.this, "Día borrado correctamente",
                                                            Toast.LENGTH_SHORT).show();

                                                    alert.dismiss();
                                                }
                                                adapterDia.notifyDataSetChanged();

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                            }
                        });

                    }
                });
            }
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

    public Boolean buscarFecha(List<String> lista, String dia)
    {
        Boolean estado = false;
        int indice = 0;

        while (indice < lista.size() && (!estado))
        {
            if (dia.equals(lista.get(indice)))
                estado = true;
            else
                indice++;
        }

        return estado;
    }
}
