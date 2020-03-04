package edu.dam.empresaapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


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

    String periodos;


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
            }
        });



        tvFechaInicioP1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // si no se ha seleccionado ningún radioButton
                // mostramos un mensaje
                if (!rbtnSi.isChecked() && !rbtnNo.isChecked())
                {
                    Toast.makeText(getContext(), "No ha seleccionado si va " +
                            "disfrutar de sus vacaciones en dos periodos", Toast.LENGTH_LONG).show();
                }
            }
        });

        tvFechaFinP1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // si no se ha seleccionado ningún radioButton
                // mostramos un mensaje
                if (!rbtnSi.isChecked() && !rbtnNo.isChecked())
                {
                    Toast.makeText(getContext(), "No ha seleccionado si va " +
                            "disfrutar de sus vacaciones en dos periodos", Toast.LENGTH_LONG).show();
                }

            }
        });

        tvFechaInicioP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // si no se ha seleccionado ningún radioButton
                // mostramos un mensaje
                if (!rbtnSi.isChecked() && !rbtnNo.isChecked())
                {
                    Toast.makeText(getContext(), "No ha seleccionado si va " +
                            "disfrutar de sus vacaciones en dos periodos", Toast.LENGTH_LONG).show();
                }
            }
        });

        tvFechaFinP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // si no se ha seleccionado ningún radioButton
                // mostramos un mensaje
                if (!rbtnSi.isChecked() && !rbtnNo.isChecked())
                {
                    Toast.makeText(getContext(), "No ha seleccionado si va " +
                            "disfrutar de sus vacaciones en dos periodos", Toast.LENGTH_LONG).show();
                }

            }
        });

        return view;

    }
}
