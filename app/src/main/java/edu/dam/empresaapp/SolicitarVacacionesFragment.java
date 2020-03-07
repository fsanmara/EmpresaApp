package edu.dam.empresaapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class SolicitarVacacionesFragment extends Fragment {

    public SolicitarVacacionesFragment() {
        // Required empty public constructor
    }

    // declaramos vistas
    TextView tvFechaInicioP1, tvFechaFinP1, tvFechaInicioP2, tvFechaFinP2, txtPeriodo2;
    Button btnEnviar;
    RadioButton rbtnSi, rbtnNo;

    DatabaseReference db;
    String periodos, fechaInicioP1, fechaFinP1, fechaInicioP2, fechaFinP2;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    Date startDateP1, endDateP1, startDateP2, endDateP2;
    long startTimeP1, endTimeP1, startTimeP2, endTimeP2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_solicitar_vacaciones, container, false);

        View view = inflater.inflate(R.layout.fragment_solicitar_vacaciones, container, false);

        // referenciamos vistas

        tvFechaInicioP1 = view.findViewById(R.id.tvFechaInicioP1);
        tvFechaFinP1    = view.findViewById(R.id.tvFechaFinP1);
        tvFechaInicioP2 = view.findViewById(R.id.tvFechaInicioP2);
        tvFechaFinP2    = view.findViewById(R.id.tvFechaFinP2);
        rbtnSi          = view.findViewById(R.id.rbtnSi);
        rbtnNo          = view.findViewById(R.id.rbtnNo);
        txtPeriodo2     = view.findViewById(R.id.txtPeriodo2);
        btnEnviar       = view.findViewById(R.id.btnEnviar);

        // comprobamos con un listener qué RadioButton se ha
        // seleccionado y lo almacenamos en la variable "periodos".
        // Si el usuario selecciona el radiobutton "sí" la variable
        // "periodos" valdrá "2"; si selecciona "no" la variable
        // valdrá "1"
        rbtnSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                periodos = "2";
                txtPeriodo2.setVisibility(View.VISIBLE);
                tvFechaInicioP2.setVisibility(View.VISIBLE);
                tvFechaFinP2.setVisibility(View.VISIBLE);

                Log.d("periodos", "número de periodos: " + periodos);
            }
        });

        // si se selecciona "no" hacemos que lo relativo al periodo 2
        // desaparezca de la pantalla
        rbtnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                periodos = "1";
                txtPeriodo2.setVisibility(View.GONE);
                tvFechaInicioP2.setVisibility(View.GONE);
                tvFechaFinP2.setVisibility(View.GONE);

                Log.d("periodos", "número de periodos: " + periodos);
            }
        });

        tvFechaInicioP1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                                fechaInicioP1 = day + "/" +  (month+1) + "/" + year;
                                tvFechaInicioP1.setText(fechaInicioP1);
                            }
                        }, year, month, dayOfMonth);

                // si no se ha seleccionado ningún radioButton
                // mostramos un mensaje
                if (!rbtnSi.isChecked() && !rbtnNo.isChecked()) {

                    Toast.makeText(getContext(), "No ha seleccionado si va a " +
                            "disfrutar de sus vacaciones en dos periodos", Toast.LENGTH_LONG).show();

                } else { // si se ha seleccionado uno de los radioButton mostramos el DatePickerDialog

                    //esta opción permite que no se puedan seleccionar fechas anteriores
                    //a la actual
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

                    datePickerDialog.show();
                }
            }
        });

        tvFechaFinP1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                                fechaFinP1 = day + "/" +  (month+1) + "/" + year;
                                tvFechaFinP1.setText(fechaFinP1);
                            }
                        }, year, month, dayOfMonth);

                // si no se ha seleccionado ningún radioButton
                // mostramos un mensaje
                if (!rbtnSi.isChecked() && !rbtnNo.isChecked()) {

                    Toast.makeText(getContext(), "No ha seleccionado si va a " +
                            "disfrutar de sus vacaciones en dos periodos", Toast.LENGTH_LONG).show();

                } else { // si se ha seleccionado uno de los radioButton mostramos el DatePickerDialog

                    //esta opción permite que no se puedan seleccionar fechas anteriores
                    //a la actual
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

                    datePickerDialog.show();
                }
            }
        });

        tvFechaInicioP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                                fechaInicioP2 = day + "/" +  (month+1) + "/" + year;
                                tvFechaInicioP2.setText(fechaInicioP2);
                            }
                        }, year, month, dayOfMonth);

                // si no se ha seleccionado ningún radioButton
                // mostramos un mensaje
                if (!rbtnSi.isChecked() && !rbtnNo.isChecked()) {

                    Toast.makeText(getContext(), "No ha seleccionado si va a " +
                            "disfrutar de sus vacaciones en dos periodos", Toast.LENGTH_LONG).show();

                } else { // si se ha seleccionado uno de los radioButton mostramos el DatePickerDialog

                    //esta opción permite que no se puedan seleccionar fechas anteriores
                    //a la actual
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

                    datePickerDialog.show();


                }
            }
        });

        tvFechaFinP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                                fechaFinP2 = day + "/" +  (month+1) + "/" + year;
                                tvFechaFinP2.setText(fechaFinP2);
                            }
                        }, year, month, dayOfMonth);

                // si no se ha seleccionado ningún radioButton
                // mostramos un mensaje
                if (!rbtnSi.isChecked() && !rbtnNo.isChecked()) {

                    Toast.makeText(getContext(), "No ha seleccionado si va a " +
                            "disfrutar de sus vacaciones en dos periodos", Toast.LENGTH_LONG).show();

                } else { // si se ha seleccionado uno de los radioButton mostramos el DatePickerDialog

                    //esta opción permite que no se puedan seleccionar fechas anteriores
                    //a la actual
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

                    datePickerDialog.show();
                }

            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // si no se ha seleccionado ningún radioButton
                // mostramos un mensaje
                if (!rbtnSi.isChecked() && !rbtnNo.isChecked()) {

                    Toast.makeText(getContext(), "No ha seleccionado si va a " +
                            "disfrutar de sus vacaciones en dos periodos", Toast.LENGTH_LONG).show();

                }
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

                //introducimos una validación para que
                //las fechas de inicio y fin no estén vacías
                if (TextUtils.isEmpty(fechaInicioP1)) {
                    Toast.makeText(getContext(),
                            "introduzca una fecha de inicio del primer periodo",
                            Toast.LENGTH_LONG).show();
                } else {
                    try {
                        startDateP1 = formatter.parse(fechaInicioP1);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if (TextUtils.isEmpty(fechaFinP1)) {
                    Toast.makeText(getContext(),
                            "introduzca una fecha de fin del primer periodo",
                            Toast.LENGTH_LONG).show();
                } else {
                    try {
                        endDateP1 = formatter.parse(fechaFinP1);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                // si el usuario no ha seleccionado dos periodos de vacaciones
                // no se comprobaría si las fechas de inicio y fin
                // del segundo periodo están vacías
                if(periodos.equals("2")) {
                    if (TextUtils.isEmpty(fechaInicioP2)) {
                        Toast.makeText(getContext(),
                                "introduzca una fecha de inicio del segundo periodo",
                                Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            startDateP2 = formatter.parse(fechaInicioP2);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    if (TextUtils.isEmpty(fechaFinP2)) {
                        Toast.makeText(getContext(),
                                "introduzca una fecha de fin del segundo periodo",
                                Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            endDateP2 = formatter.parse(fechaFinP2);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }

                //convertimos las fechas de String a Date
                //SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

                /*try {
                    startDateP1 = formatter.parse(fechaInicioP1);
                    endDateP1 = formatter.parse(fechaFinP1);

                        startDateP2 = formatter.parse(fechaInicioP2);
                        endDateP2 = formatter.parse(fechaFinP2);

                } catch (ParseException e) {
                    e.printStackTrace();
                }*/

                startTimeP1 = startDateP1.getTime();
                endTimeP1 = endDateP1.getTime();
                startTimeP2 = startDateP2.getTime();
                endTimeP2 = endDateP2.getTime();

                // Comprobamos que la fecha de fin del periodo 1 tiene que ser superior
                // a la fecha de inicio del periodo 1. Hacemos esta validación para que
                // el usuario no seleccione una fecha de fin de vacaciones anterior a la
                // fecha de inicio de las vacaciones
                if(endTimeP1 <= startTimeP1){

                    Toast.makeText(getContext(), "la fecha de fin del" +
                            " periodo 1 tiene que ser superior a la fecha " +
                            "de inicio del periodo 1", Toast.LENGTH_LONG).show();
                }

                // comprobamos que las fechas del periodo 2 tienen que ser superiores
                // a las fechas del periodo 1
                if(periodos.equals("2")) {

                    startTimeP1 = startDateP1.getTime();
                    endTimeP1 = endDateP1.getTime();
                    startTimeP2 = startDateP2.getTime();
                    endTimeP2 = endDateP2.getTime();

                    if(startTimeP2 <= startTimeP1){

                        Toast.makeText(getContext(), "nooooor", Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });


        return view;

    }
}
