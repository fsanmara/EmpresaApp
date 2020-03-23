package edu.dam.empresaapp.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.dam.empresaapp.R;
import edu.dam.empresaapp.Trabajador;

public class AdaptadorListVacaciones extends BaseAdapter{

    private Context contexto;
    private ArrayList<Trabajador> listItems;

    public AdaptadorListVacaciones(Context contexto, ArrayList<Trabajador> listItems) {
        this.contexto = contexto;
        this.listItems = listItems;
    }


    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String nombreCompleto;
        Trabajador item = (Trabajador) getItem(position);

        convertView = LayoutInflater.from(contexto).inflate(R.layout.item_list_solvacaciones, null);

        ImageView ivBeach = convertView.findViewById(R.id.ivBeach);
        ivBeach.setImageResource(R.drawable.beach);

        nombreCompleto = item.getNombre() + " " + item.getApellido1() + " " + item.getApellido2();
        TextView tvNombre= convertView.findViewById(R.id.tvNombre);
        tvNombre.setText(nombreCompleto);

        return convertView;

    }
}
