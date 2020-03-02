package edu.dam.empresaapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SolicitarVacacionesFragment extends Fragment {

    public SolicitarVacacionesFragment() {
        // Required empty public constructor
    }

    String periodos;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_solicitar_vacaciones, container, false);

        View view = inflater.inflate(R.layout.fragment_solicitar_vacaciones, container, false);


       /* //https://developer.android.com/guide/topics/ui/dialogs?hl=es

        AlertDialog.Builder ventana = new AlertDialog.Builder(getContext());

        ventana.setTitle("Mensaje:");
        ventana.setMessage("¿Quiere disfrutar sus vacaciones en dos periodos?");

        // botón "Sí"
        ventana.setPositiveButton("Sí", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                dialog.cancel();
                periodos = "2";
            }
        });

        // botón "No"
        ventana.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                dialog.cancel();
                periodos = "1";
            }
        });

        AlertDialog alert = ventana.create();
        alert.show();*/



        return view;


    }
}
