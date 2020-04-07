package edu.dam.empresaapp.fragments;


import android.app.DatePickerDialog;
import android.content.Intent;
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
import java.util.concurrent.TimeUnit;

import edu.dam.empresaapp.R;
import edu.dam.empresaapp.activitys.VacacionesActivity;
import edu.dam.empresaapp.pojos.Vacaciones;

/**
 * A simple {@link Fragment} subclass.
 */
public class SolicitarVacacionesFragment extends Fragment {

    public SolicitarVacacionesFragment() {
        // Required empty public constructor
    }

    // declaramos vistas
    private TextView tvFechaInicioP1, tvFechaFinP1, tvFechaInicioP2, tvFechaFinP2, txtPeriodo2, tvDiasP1, tvDiasP2;
    private Button btnEnviar;
    private RadioButton rbtnSi, rbtnNo;

    private DatabaseReference db, db2;

    private Vacaciones vacaciones;

    private String periodos, fechaInicioP1, fechaFinP1, fechaInicioP2, fechaFinP2, idTrabajador, anioVacaciones;
    private DatePickerDialog datePickerDialog;
    private int year, month, dayOfMonth, resultadoP1, resultadoP2, resultadoTotal;
    private Calendar calendar;
    private Date startDateP1, endDateP1, startDateP2, endDateP2;
    private long startTimeP1, endTimeP1, startTimeP2, endTimeP2, diffTimeP1, diffTimeP2;


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
        tvDiasP1        = view.findViewById(R.id.tvDiasP1);
        tvDiasP2        = view.findViewById(R.id.tvDiasP2);

        // creamos la referencia a la BBDD
        db = FirebaseDatabase.getInstance().getReference().child("Vacaciones");

        //obtenemos los datos del Bundle que le pasamos a este Fragment
        //con "getArguments"
        Bundle bundle = getArguments();

        idTrabajador = bundle.getString("ID");

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

                                fechaInicioP1 = day + "/" + (month + 1) + "/" + year;
                                tvFechaInicioP1.setText(fechaInicioP1);
                            }
                        }, year, month, dayOfMonth);

                // si no se ha seleccionado ningún radioButton
                // mostramos un mensaje
                if (!rbtnSi.isChecked() && !rbtnNo.isChecked())
                {
                    Toast.makeText(getContext(), "No ha seleccionado si va a " +
                            "disfrutar de sus vacaciones en dos periodos", Toast.LENGTH_LONG).show();
                }
                else
                    { // si se ha seleccionado uno de los radioButton mostramos el DatePickerDialog

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

                                fechaFinP1 = day + "/" + (month + 1) + "/" + year;
                                tvFechaFinP1.setText(fechaFinP1);
                            }
                        }, year, month, dayOfMonth);

                // si no se ha seleccionado ningún radioButton
                // mostramos un mensaje
                if (!rbtnSi.isChecked() && !rbtnNo.isChecked())
                {
                    Toast.makeText(getContext(), "No ha seleccionado si va a " +
                            "disfrutar de sus vacaciones en dos periodos", Toast.LENGTH_LONG).show();

                }
                else
                    { // si se ha seleccionado uno de los radioButton mostramos el DatePickerDialog

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

                                fechaInicioP2 = day + "/" + (month + 1) + "/" + year;
                                tvFechaInicioP2.setText(fechaInicioP2);
                            }
                        }, year, month, dayOfMonth);

                // si no se ha seleccionado ningún radioButton
                // mostramos un mensaje
                if (!rbtnSi.isChecked() && !rbtnNo.isChecked())
                {
                    Toast.makeText(getContext(), "No ha seleccionado si va a " +
                            "disfrutar de sus vacaciones en dos periodos", Toast.LENGTH_LONG).show();

                }
                else
                    { // si se ha seleccionado uno de los radioButton mostramos el DatePickerDialog

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

                                fechaFinP2 = day + "/" + (month + 1) + "/" + year;
                                tvFechaFinP2.setText(fechaFinP2);
                            }
                        }, year, month, dayOfMonth);

                // si no se ha seleccionado ningún radioButton
                // mostramos un mensaje
                if (!rbtnSi.isChecked() && !rbtnNo.isChecked())
                {
                    Toast.makeText(getContext(), "No ha seleccionado si va a " +
                            "disfrutar de sus vacaciones en dos periodos", Toast.LENGTH_LONG).show();

                }
                else
                    { // si se ha seleccionado uno de los radioButton mostramos el DatePickerDialog

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

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

                if (!rbtnSi.isChecked() && !rbtnNo.isChecked())
                {
                    Toast.makeText(getContext(), "No ha seleccionado si va a " +
                            "disfrutar de sus vacaciones en dos periodos", Toast.LENGTH_LONG).show();
                }
                else
                    {

                    if (periodos.equals("1")) {

                    //introducimos una validación para que
                    //las fechas de inicio y fin no estén vacías
                    if (TextUtils.isEmpty(fechaInicioP1))
                    {
                        Toast.makeText(getContext(),
                                "introduzca una fecha de inicio del primer periodo",
                                Toast.LENGTH_LONG).show();
                    } else {

                        try {
                            startDateP1 = formatter.parse(fechaInicioP1);
                            startTimeP1 = startDateP1.getTime();

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }

                    if (TextUtils.isEmpty(fechaFinP1))
                    {
                        Toast.makeText(getContext(),
                                "introduzca una fecha de fin del primer periodo",
                                Toast.LENGTH_LONG).show();
                    }
                    else {

                        try {
                            endDateP1 = formatter.parse(fechaFinP1);
                            endTimeP1 = endDateP1.getTime();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        // Comprobamos que la fecha de fin del periodo 1 tiene que ser superior
                        // a la fecha de inicio del periodo 1. Hacemos esta validación para que
                        // el usuario no seleccione una fecha de fin de vacaciones anterior a la
                        // fecha de inicio de las vacaciones
                        if (endTimeP1 <= startTimeP1)
                        {
                            Toast.makeText(getContext(), "la fecha de fin del" +
                                    " periodo 1 tiene que ser superior a la fecha " +
                                    "de inicio del periodo 1", Toast.LENGTH_LONG).show();
                        }

                    }

                        diffTimeP1 = endTimeP1 - startTimeP1;
                        resultadoP1 = (int) TimeUnit.DAYS.convert(diffTimeP1, TimeUnit.MILLISECONDS) + 1;
                        tvDiasP1.setVisibility(View.VISIBLE);
                        tvDiasP1.setText(resultadoP1 + " días");

                        if (resultadoP1 != 30)
                        {

                            Toast.makeText(getContext(), "Ha escogido " + resultadoP1 +
                                    " días. " + "Debe escoger un total de 30 días.",
                                    Toast.LENGTH_LONG).show();

                        } else
                            {
                            // cuando el periodo seleccionado es de 30 días, añadimos
                            // la información a Firebase
                            anioVacaciones = String.valueOf(calendar.get(Calendar.YEAR));

                            db.child(idTrabajador).child(anioVacaciones)
                                    .child("numero_periodos").setValue(periodos);

                            db2 = FirebaseDatabase.getInstance().
                                    getReference().child("Vacaciones")
                                    .child(idTrabajador).child(anioVacaciones);

                            db2.child("fecha_inicio_periodo1").setValue(fechaInicioP1);
                            db2.child("fecha_fin_periodo1").setValue(fechaFinP1);
                            db2.child("fecha_inicio_periodo2").setValue("");
                            db2.child("fecha_fin_periodo2").setValue("");
                            db2.child("estado_vacaciones").setValue("pendiente_confirmacion");

                            Toast.makeText(getContext(),
                                    "Propuesta enviada. En breve recibirá una respuesta.",
                                    Toast.LENGTH_LONG).show();


                            //Creamos un objeto "Vacaciones"
                              vacaciones = new Vacaciones(
                                      idTrabajador,
                                      anioVacaciones,
                                      periodos,
                                      fechaInicioP1,
                                      fechaFinP1,
                                      "",
                                      "",
                                      "pendiente");

                            Intent intent = new Intent(getContext(), VacacionesActivity.class);
                            intent.putExtra("VACACIONES", vacaciones);

                            Objects.requireNonNull(getActivity()).onBackPressed(); //cerramos el fragment

                        }
                    }

                    if (periodos.equals("2"))
                    {
                        //introducimos una validación para que
                        //las fechas de inicio y fin no estén vacías
                        if (TextUtils.isEmpty(fechaInicioP1))
                        {
                            Toast.makeText(getContext(),
                                    "introduzca una fecha de inicio del primer periodo",
                                    Toast.LENGTH_LONG).show();
                        } else {

                            try {
                                startDateP1 = formatter.parse(fechaInicioP1);
                                startTimeP1 = startDateP1.getTime();

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }

                        if (TextUtils.isEmpty(fechaFinP1))
                        {
                            Toast.makeText(getContext(),
                                    "introduzca una fecha de fin del primer periodo",
                                    Toast.LENGTH_LONG).show();
                        } else
                            {

                            try {
                                endDateP1 = formatter.parse(fechaFinP1);
                                endTimeP1 = endDateP1.getTime();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            // Comprobamos que la fecha de fin del periodo 1 tiene que ser superior
                            // a la fecha de inicio del periodo 1. Hacemos esta validación para que
                            // el usuario no seleccione una fecha de fin de vacaciones anterior a la
                            // fecha de inicio de las vacaciones
                            if (endTimeP1 <= startTimeP1)
                            {
                                Toast.makeText(getContext(), "la fecha de fin del" +
                                        " periodo 1 tiene que ser superior a la fecha " +
                                        "de inicio del periodo 1", Toast.LENGTH_LONG).show();
                            }

                        }
                        if (TextUtils.isEmpty(fechaInicioP2))
                        {
                            Toast.makeText(getContext(),
                                    "introduzca una fecha de inicio del segundo periodo",
                                    Toast.LENGTH_LONG).show();
                        }
                        else {
                            try {
                                startDateP2 = formatter.parse(fechaInicioP2);
                                startTimeP2 = startDateP2.getTime();
                                }
                                catch (ParseException e)
                                    {
                                    e.printStackTrace();
                                    }
                             }

                        if (TextUtils.isEmpty(fechaFinP2))
                        {
                            Toast.makeText(getContext(),
                                    "introduzca una fecha de fin del segundo periodo",
                                    Toast.LENGTH_LONG).show();
                        }
                        else {

                            try {
                                endDateP2 = formatter.parse(fechaFinP2);
                                endTimeP2 = endDateP2.getTime();
                                } catch (ParseException e)
                                    {
                                    e.printStackTrace();
                                    }

                            // Hacemos más comprobaciones con las fechas, para evitar que el
                            // usuario escoja una fecha de fin del periodo 2 inferior a la
                            // fecha de inicio del periodo 2 y que las fechas del periodo 2
                            // sean anteriores a las fechas del periodo 1
                            if (endTimeP2 <= startTimeP2) {

                                Toast.makeText(getContext(), "la fecha de fin del" +
                                        " periodo 2 tiene que ser superior a la fecha " +
                                        "de inicio del periodo 2", Toast.LENGTH_LONG).show();
                            } else if (startTimeP2 <= startTimeP1) {

                                Toast.makeText(getContext(), "la fecha de inicio del" +
                                        " periodo 2 tiene que ser superior a la fecha " +
                                        "de inicio del periodo 1", Toast.LENGTH_LONG).show();
                            } else if (startTimeP2 <= endTimeP1) {

                                Toast.makeText(getContext(), "la fecha de inicio del" +
                                        " periodo 2 tiene que ser superior a la fecha " +
                                        "de fin del periodo 1", Toast.LENGTH_LONG).show();
                            } else if (endTimeP2 <= startTimeP1) {

                                Toast.makeText(getContext(), "la fecha de fin del" +
                                        " periodo 2 tiene que ser superior a la fecha " +
                                        "de inicio del periodo 1", Toast.LENGTH_LONG).show();
                            } else if (endTimeP2 <= endTimeP1) {
                                Toast.makeText(getContext(), "la fecha de fin del" +
                                        " periodo 2 tiene que ser superior a la fecha " +
                                        "de fin del periodo 1", Toast.LENGTH_LONG).show();
                            }
                            diffTimeP1 = endTimeP1 - startTimeP1;
                            diffTimeP2 = endTimeP2 - startTimeP2;
                            resultadoP1 = (int) TimeUnit.DAYS.convert(diffTimeP1, TimeUnit.MILLISECONDS) + 1;
                            resultadoP2 = (int) TimeUnit.DAYS.convert(diffTimeP2, TimeUnit.MILLISECONDS) + 1;
                            resultadoTotal = resultadoP1 + resultadoP2;
                            tvDiasP1.setVisibility(View.VISIBLE);
                            tvDiasP2.setVisibility(View.VISIBLE);
                            tvDiasP1.setText(resultadoP1 + " días");
                            tvDiasP2.setText(resultadoP2 + " días");


                            if (resultadoTotal != 30)
                            {
                                Toast.makeText(getContext(), "Ha escogido " + resultadoP1 +
                                        " días en el periodo 1 y " + resultadoP2 + " días en el " +
                                        "periodo 2, en total " + resultadoTotal + " días. Debe " +
                                        "escoger un total de 30 días.", Toast.LENGTH_LONG).show();

                            }
                            else
                                {
                                // cuando el periodo seleccionado es de 30 días, añadimos
                                // la información a Firebase
                                anioVacaciones = String.valueOf(calendar.get(Calendar.YEAR));

                                db.child(idTrabajador).child(anioVacaciones)
                                        .child("numero_periodos").setValue(periodos);

                                db2 = FirebaseDatabase.getInstance().
                                        getReference().child("Vacaciones")
                                        .child(idTrabajador).child(anioVacaciones);

                                db2.child("fecha_inicio_periodo1").setValue(fechaInicioP1);
                                db2.child("fecha_fin_periodo1").setValue(fechaFinP1);
                                db2.child("fecha_inicio_periodo2").setValue(fechaInicioP2);
                                db2.child("fecha_fin_periodo2").setValue(fechaFinP2);
                                db2.child("estado_vacaciones").setValue("pendiente_confirmacion");

                                Toast.makeText(getContext(),
                                        "Propuesta enviada. En breve recibirá una respuesta.",
                                        Toast.LENGTH_LONG).show();

                                //Creamos un objeto "Vacaciones"
                                vacaciones = new Vacaciones(
                                        idTrabajador,
                                        anioVacaciones,
                                        periodos,
                                        fechaInicioP1,
                                        fechaFinP1,
                                        fechaInicioP2,
                                        fechaFinP2,
                                        "pendiente");

                                Intent intent = new Intent(getContext(), VacacionesActivity.class);
                                intent.putExtra("VACACIONES", vacaciones);

                                Objects.requireNonNull(getActivity()).onBackPressed(); // cerramos el fragment

                            }
                        }

                    }

                }

            } // onclick del botón "enviar"

        });


        return view;


    }
}
