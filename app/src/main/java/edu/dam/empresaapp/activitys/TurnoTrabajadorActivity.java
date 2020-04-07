package edu.dam.empresaapp.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.ValueEventListener;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import edu.dam.empresaapp.R;
import edu.dam.empresaapp.adaptadores.AdaptadorDias;
import edu.dam.empresaapp.adaptadores.AdaptadorTrabajadores;
import edu.dam.empresaapp.pojos.Trabajador;
import edu.dam.empresaapp.pojos.Turnos;

public class TurnoTrabajadorActivity extends AppCompatActivity {

    private TextView tvTrabajadorVerTurnos, tvAnio, tvMes;
    private Spinner spTrabajadores;
    private AdaptadorTrabajadores adapterTrabajadores;
    private AdaptadorDias adapterDia;
    private ListView lvDias;
    private String anio, mes, dia, mesLetra, idTrabajador;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private Button btnVerDias;

    // declaramos un objeto "Trabajador"
    private Trabajador trabajador;

    // referenciamos la BBDD
    DatabaseReference db;

    // declaramos el arrayList
    ArrayList<Trabajador> listadoTrabajadores = new ArrayList<>();
    ArrayList<Turnos> listadoTurnos = new ArrayList<>();

    Context contexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turno_trabajador);

        // referenciamos vistas
        tvTrabajadorVerTurnos = findViewById(R.id.tvNombreTrabajador_VerTurnos);
        spTrabajadores        = findViewById(R.id.spTrabajadores);
        lvDias                = findViewById(R.id.lvDias);
        tvAnio                = findViewById(R.id.tvAnio);
        tvMes                 = findViewById(R.id.tvMes);
        btnVerDias            = findViewById(R.id.btnVerDias);

        // creamos un objeto "Trabajador"
        new Trabajador();
        trabajador = getIntent().getParcelableExtra("parametro");
        idTrabajador = trabajador.getId();

        //Mostramos el nombre del trabajador en un TextView
        tvTrabajadorVerTurnos.setText(trabajador.getNombre() + " " + trabajador.getApellido1());

        // referenciamos la BBDD
        db = FirebaseDatabase.getInstance().getReference();

        contexto = this;

        /*//consultamos la BBDD para llenar el Spinner de los empleados
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
        });*/


        Calendar calendar = Calendar.getInstance();
        int yearNow = calendar.get(Calendar.YEAR);
        int monthNow = calendar.get(Calendar.MONTH);
        int dateNow = calendar.get(Calendar.DAY_OF_MONTH);

        Locale spanish = new Locale("es");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd//mm/yyyy", spanish);

        DatePickerDialog datePickerDialog = new DatePickerDialog(TurnoTrabajadorActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            }
        }, yearNow, monthNow, dateNow);

        final Calendar today = Calendar.getInstance();

        tvAnio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(TurnoTrabajadorActivity.this,
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
                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(TurnoTrabajadorActivity.this,
                        new MonthPickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(int selectedMonth, int selectedYear) {

                                nombreMes(selectedMonth + 1);
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
                if (TextUtils.isEmpty(anio))
                {
                    Toast.makeText(TurnoTrabajadorActivity.this, "Introduzca el año",
                            Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(mesLetra))
                {
                    Toast.makeText(TurnoTrabajadorActivity.this, "Introduzca el mes",
                            Toast.LENGTH_SHORT).show();
                }
                else
                    {

                    db.child("Turnos").child(idTrabajador).child(anio).child(mesLetra).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists())
                            {
                                Toast.makeText(TurnoTrabajadorActivity.this, "Datos no encontrados para el periodo solicitado", Toast.LENGTH_SHORT).show();
                            }
                            else
                                {
                                for (final DataSnapshot query : dataSnapshot.getChildren())
                                {
                                    dia = query.getValue().toString();
                                    final Turnos turnos = new Turnos(idTrabajador, anio, mesLetra, dia);
                                    listadoTurnos.add(turnos);
                                }

                                adapterDia = new AdaptadorDias(contexto, listadoTurnos);
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
                            LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                            View vista = inflater.inflate(R.layout.alert_dialog_cambiar_turno, null);

                            ventana.setView(vista);
                            final AlertDialog alert = ventana.create();
                            alert.show();

                            btnSi = vista.findViewById(R.id.btnSi);
                            btnNo = vista.findViewById(R.id.btnNo);

                            btnNo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alert.dismiss();
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
        Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
        intent.putExtra("parametro", trabajador); //pasamos el objeto trabajador
        startActivity(intent);
    }

    // método que nos permite pasar el mes de int a letras
    public void nombreMes(int mes) {

        switch (mes) {
            case 1:
                mesLetra = "enero";
                break;
            case 2:
                mesLetra = "febrero";
                break;
            case 3:
                mesLetra = "marzo";
                break;
            case 4:
                mesLetra = "abril";
                break;
            case 5:
                mesLetra = "mayo";
                break;
            case 6:
                mesLetra = "junio";
                break;
            case 7:
                mesLetra = "julio";
                break;
            case 8:
                mesLetra = "agosto";
                break;
            case 9:
                mesLetra = "septiembre";
                break;
            case 10:
                mesLetra = "octubre";
                break;
            case 11:
                mesLetra = "noviembre";
                break;
            case 12:
                mesLetra = "diciembre";
                break;
            default:
                mesLetra = "mes no válido";
                break;
        }
    }
}
